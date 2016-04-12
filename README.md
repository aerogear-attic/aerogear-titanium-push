# aerogear-titanium-push

Titanuim version of the AeroGear push plugin for GCM

To build, create a `build.properties` file with the following content:

```
titanium.platform=/Users/###USER###/Library/Application Support/Titanium/mobilesdk/osx/5.2.1.GA/android
android.platform=/Users/###USER###/Library/Android/sdk/platforms/android-23
```

Make sure your paths are correct for your system setup. Then run:

```bash
$ ant clean
$ ant
```

A zip file will be created in the `dist` folder.
