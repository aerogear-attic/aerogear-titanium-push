# push Module

## Description

This is a titanium version of the [AeroGear Push plugin](https://github.com/aerogear/aerogear-cordova-push).
More information on how to configure GCM and UPS (Unified Push Server) take a look at [aerogear.org](https://github.com/aerogear/aerogear-cordova-push)

## Accessing the push Module

To access this module from JavaScript, you would do the following:

    var push = require('org.jboss.aerogear.push');

The push variable is a reference to the Module object.

## Reference

After that one can register for push in almost the same way as the cordova API, with the exception that callbacks are part of the config object.

### push.registerPush

Registers the device with GCM (Android) and the Unified Push server.

## Usage

```js
var pushConfig = {
    pushServerURL: "<pushServerURL e.g http(s)//host:port/context >",
    alias: "<alias e.g. a username or an email address optional>",
    android: {
      senderID: "<senderID e.g Google Project ID only for android>",
      variantID: "<variantID e.g. 1234456-234320>",
      variantSecret: "<variantSecret e.g. 1234456-234320>"
    },
	success: eventSuccess,
	error: eventError,
	onNotification: onNotification
};

push.registerPush(pushConfig);
```

## License

Apache License, Version 2.0
