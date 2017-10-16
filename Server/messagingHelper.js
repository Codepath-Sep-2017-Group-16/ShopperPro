var admin = require("firebase-admin");

var serviceAccount = require("./serviceAccountKey.json")

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://socialshopper-a8997.firebaseio.com"
});

  

function sendMessage(regToken) {
	console.log("sending gcm");
	// This registration token comes from the client FCM SDKs.
	var registrationToken = regToken;

	// See the "Defining the message payload" section below for details
	// on how to define a message payload.
	var payload = {
	  data: {
	  	payload : "test"
	  }
	};

	// Send a message to the device corresponding to the provided
	// registration token.
	admin.messaging().sendToDevice(registrationToken, payload)
	  .then(function(response) {
	    // See the MessagingDevicesResponse reference documentation for
	    // the contents of response.
	    console.log("Successfully sent message:", response);
	  })
	  .catch(function(error) {
	    console.log("Error sending message:", error);
	  });
} 

module.exports.SendMessage = sendMessage;
