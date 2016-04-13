package org.jboss.aerogear.cordova.push;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;
import org.jboss.aerogear.android.core.Callback;
import org.jboss.aerogear.android.unifiedpush.PushRegistrar;
import org.jboss.aerogear.android.unifiedpush.RegistrarManager;
import org.jboss.aerogear.android.unifiedpush.gcm.AeroGearGCMPushConfiguration;

import android.os.Bundle;

@Kroll.module(name = "Push", id = "org.jboss.aerogear.push")
public class PushPlugin extends KrollModule {

   private static final String MODULE_NAME = "PushModule";

   private KrollFunction successCallback = null;
   private KrollFunction errorCallback = null;
   private KrollFunction notificationCallback = null;

   private static final String REGISTRAR = "registrar";

   public PushPlugin() {
      super(MODULE_NAME);
   }

   @Kroll.onAppCreate
   public static void onAppCreate(TiApplication app) {
      Log.d(MODULE_NAME, "inside onAppCreate");
   }

   @Kroll.method
   @SuppressWarnings("rawtypes")
   public void registerPush(Map pushConfig) {
      Map androidSettings = pushConfig.get("android") != null ? (Map) pushConfig.get("android") : pushConfig;
      successCallback = (KrollFunction) pushConfig.get("success");
      errorCallback = (KrollFunction) pushConfig.get("error");
      notificationCallback = (KrollFunction) pushConfig.get("onNotification");
      URI uri = null;
      try {
         uri = new URI((String) pushConfig.get("pushServerURL"));
      } catch (URISyntaxException e) {
         throw new RuntimeException(e);
      }

      AeroGearGCMPushConfiguration config = RegistrarManager.config(REGISTRAR, AeroGearGCMPushConfiguration.class)
            .setPushServerURI(uri)
            .setSenderIds((String) androidSettings.get("senderID"))
            .setVariantID((String) androidSettings.get("variantID"))
            .setSecret((String) androidSettings.get("variantSecret"))
            .setAlias((String) pushConfig.get("alias"));
      Object[] categories = (Object[]) pushConfig.get("categories");
      if (categories != null) {
         List<String> list = new ArrayList<String>();
         for (Object object : categories) {
            list.add((String) object);
         }
         config.setCategories(list);
      }

      PushRegistrar registrar = config.asRegistrar();

      registrar.register(TiApplication.getInstance().getApplicationContext(), new Callback<Void>() {
         public void onSuccess(Void data) {
            if (successCallback != null) {
               successCallback.callAsync(getKrollObject(), (HashMap) null);
            }
         }

         public void onFailure(Exception e) {
            Log.e(MODULE_NAME, "could not register with UPS", e);
            if (errorCallback != null) {
               HashMap<String, Object> data = new HashMap<String, Object>();
               data.put("error", e.getMessage());
               errorCallback.callAsync(getKrollObject(), data);
            }
         }
      });
   }

   public static void sendMessage(Bundle message) {
      PushPlugin module = getModule();

      message.putBoolean("foreground", isInForeground());
      module.notificationCallback.callAsync(module.getKrollObject(), convertBundleToMap(message));
   }

   private static HashMap<String, Object> convertBundleToMap(Bundle message) {
      HashMap<String, Object> json = new HashMap<String, Object>();

      Map<String, Object> jsondata = new HashMap<String, Object>();
      for (String key : message.keySet()) {
         Object value = message.get(key);

         // System data from Android
         if (key.equals("from") || key.equals("collapse_key")) {
            json.put(key, value);
         } else if (key.equals("foreground")) {
            json.put(key, message.getBoolean("foreground"));
         } else if (key.equals("coldstart")) {
            json.put(key, message.getBoolean("coldstart"));
         } else {
            // Maintain backwards compatibility
            if (key.equals("message") || key.equals("msgcnt") || key.equals("sound") || key.equals("alert")) {
               json.put(key, value);
            }

            jsondata.put(key, value);
         }
      }
      json.put("payload", jsondata);

      return json;
   }

   private static PushPlugin getModule() {
      TiApplication appContext = TiApplication.getInstance();
      PushPlugin module = (PushPlugin) appContext.getModuleByName(MODULE_NAME);

      if (module == null) {
         Log.w(MODULE_NAME, "Push module not currently loaded");
      }
      return module;
   }

   public static boolean isActive() {
      return TiApplication.getInstance().getModuleByName(MODULE_NAME) != null;
   }

   public static boolean isInForeground() {
      try {
         return new ForegroundCheckTask().execute(TiApplication.getInstance().getApplicationContext()).get();
      } catch (Exception e) {
         Log.e(MODULE_NAME, "could not determain state", e);
         return false;
      }
   }
}
