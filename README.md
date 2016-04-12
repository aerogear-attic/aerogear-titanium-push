# aerogear-titanium-push

Titanium version of the AeroGear push plugin for GCM

To build, create a `build.properties` file with the following content:

```
titanium.platform=/Users/###USER###/Library/Application Support/Titanium/mobilesdk/osx/5.2.1.GA/android
android.platform=/Users/###USER###/Library/Android/sdk/platforms/android-23
android.ndk=/Users/###USER###/Library/Android/ndk
```

Make sure your paths are correct for your system setup. Then run:

```bash
$ ant clean
$ ant
```

A zip file will be created in the `dist` folder. 
