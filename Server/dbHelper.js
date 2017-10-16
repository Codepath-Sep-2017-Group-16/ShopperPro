var firebase = require('firebase');

function set(key, value, callback) {
    initializeDb();
    firebase.database().ref(key).set(value)
        .then(function() {
            console.log("DATABASE: success")
            callback(null, null);
        })
        .catch(function(err) {
            console.log("DATABASE: error - " + err)
            callback(err, null)
        });
}


function checkIfKeyExists(key, callback) {
    initializeDb();
    firebase.database().ref(key).once('value', function(snapshot) {
        var exists = (snapshot.val() != null);
        callback(null, exists);
    });
}

function initializeDb() {

    if (firebase.apps.length === 0) {
        // Initialize Firebase
        var config = {
            apiKey: "AIzaSyB61cnlUVDmH_ABOpoMacaQP3MIvFNyYQA",
            authDomain: "shgfirebaseproject.firebaseapp.com",
            databaseURL: "https://shgfirebaseproject.firebaseio.com",
            storageBucket: "shgfirebaseproject.appspot.com",
            messagingSenderId: "311008079682"
          };

        firebase.initializeApp(config);
    }
}


module.exports.Set = set;
module.exports.CheckIfKeyExists = checkIfKeyExists