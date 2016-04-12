var push = require('org.jboss.aerogear.push');

function onNotification(e) {
	Ti.API.info('  Message: ' + e.alert);
	Ti.API.info('  Payload: ' + e.payload);

	Ti.UI.createAlertDialog({
		title : 'Info',
		message : e.alert
	}).show();
}

function eventSuccess() {
	Ti.API.info('Success');
}

function eventError(e) {
	Ti.API.info('Error:' + e.error);
	var alert = Ti.UI.createAlertDialog({
		title : 'Error',
		message : e.error
	});
	alert.show();
}

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
