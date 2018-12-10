// The Cloud Functions for Firebase SDK to create Cloud Functions and setup
// triggers.
const functions = require('firebase-functions');
const request = require('request');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();

exports.syncDatabaseOnUserCreation = functions.auth.user().onCreate((user) => {
  return admin.database().ref('/users/' + user.uid + '/email').set(user.email);
});

exports.syncDatabaseOnUserDeletion = functions.auth.user().onDelete((user) => {
  return admin.database().ref('/users/' + user.uid).remove();
});

exports.addUserToEvent = functions.https.onCall((data, context) => {
  // Extract parameters from client
  const eventId = data.eventId;
  const userEmail = data.userEmail;
  const role = data.role;

  // Auhtenticated user
  const currentUserUid = context.auth.uid;
  //const currentUserUid = "u0YmYQasWpNaNYZt4iXngV0aTxF3"; // Used locally for debug

  // FIXME: improve robustness
  admin.database()
    .ref('/events/' + eventId + '/users/admin/')
    .once('value', snapshot => {
      // Object.values() is not supported on NodeJS 6, which is used by Firebase
      var eventAdmins = snapshot.val();
      var allowedUids = Object.keys(eventAdmins).map((k) => eventAdmins[k]);
      if (allowedUids.includes(currentUserUid)) {
        var users = admin.database().ref('/users/');
        users.orderByChild('email').equalTo(userEmail).limitToFirst(1).once('value', snapshot => {
          var match = snapshot.val();
          if (match === null) {
            console.log('Could not find user linked to email ' + userEmail);
            return false;
          } else {
            var targetUid = Object.keys(match)[0];
            console.log('Adding role ' + role + ' for UID ' + targetUid + ' on event ' + eventId);
            return admin.database()
              .ref('/events/' + eventId + '/users/' + role)
              .push()
              .set(targetUid);
          }
        })
      } else {
        console.log('Current user ' + currentUserUid + ' is not allowed to write event ' + eventId);
        return false;
      }
    })
})

const API_KEY = "AAAAlIAvtxI:APA91bHnmNkZWIQzzWcxypS45bpVKBXkLNwtxM-gU6UCfZt2TI-jd02Typ8ACtLpGbHCASrWlwKHDT9EsRpqrUj7hAH8GdhvKp3_UaF_Vx4k3yqgXLqMQv2py-FiUODmG2hy2QuTGdUI"; // Firebase Cloud Messaging Server API key

function listenForNotificationRequests() {
    var requests = ref.child('notificationRequest');
    requests.on('child_added', function(requestSnapshot) {
        var request = requestSnapshot.val();
        sendNotificationToUsers(
            request.username,
            request.message,
            function() {
                requestSnapshot.ref.remove();
            }
        );
    }, function(error) {
        console.error(error);
    });
}

function sendNotificationToUsers(title, body, eventId, eventName) {
    request({
        url: 'https://fcm.googleapis.com/fcm/send',
        method: 'POST',
        headers: {
            'Content-Type' :' application/json',
            'Authorization': 'key='+API_KEY
        },
        body: JSON.stringify({
            notification: {
                title: title,
                body: body
            },
            to : '/topics/' + eventName + '_' + eventId
        })
    }, function(error, response, body) {
        if (error) { console.error(error); }
        else if (response.statusCode >= 400) {
            console.error('HTTP Error: '+response.statusCode+' - '+response.statusMessage);
        }
    });
}

// start listening
listenForNotificationRequests();

