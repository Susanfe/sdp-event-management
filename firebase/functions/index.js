// The Cloud Functions for Firebase SDK to create Cloud Functions and setup
// triggers.
const functions = require('firebase-functions');

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

  var ref = admin.database().ref('/events/' + eventId + '/users/');
  ref.orderByValue().equalTo('admin').once("value", adminUidMap => {
    var adminUids = Object.keys(adminUidMap.val());
    if (adminUids.includes(currentUserUid)) { // current user is admin
      var users = admin.database().ref('/users/');
      users.orderByChild('email').equalTo(userEmail).limitToFirst(1).once('value', userSnapshot => {
        var match = userSnapshot.val();
        if (match === null) {
          console.log('Could not find user linked to email ' + userEmail);
          return false;
        } else {
          var targetUid = Object.keys(match)[0];
          return ref.child(targetUid).set(role);
        }
      });
    } else {
      console.log('Current user ' + currentUserUid + ' is not allowed to write event ' + eventId);
      return false;
    }
  });
});

exports.removeUserFromEvent = functions.https.onCall((data, context) => {
  // Extract parameters from client
  const eventId = data.eventId;
  const targetUserUid = data.uid;
  const role = data.role;

  // Auhtenticated user
  const currentUserUid = context.auth.uid;
  //const currentUserUid = "u0YmYQasWpNaNYZt4iXngV0aTxF3"; // Used locally for debug

  var ref = admin.database().ref('/events/' + eventId + '/users/');
  ref.orderByValue().equalTo('admin').once("value", adminUidMap => {
    var adminUids = Object.keys(adminUidMap.val());
    if (adminUids.includes(currentUserUid)) { // current user is admin
        return ref.child(targetUserUid).set(null);
    } else {
      console.log('Current user ' + currentUserUid + ' is not allowed to write event ' + eventId);
      return false;
    }
  });
});

exports.importTickets = functions.https.onCall((data, context) => {
  const tickets = data.tickets;
  const eventId = data.eventId;

  var ref = admin.database().ref('/events/' + eventId + '/users/');
  ref.orderByValue().equalTo('admin').once("value", adminUidMap => {
    var adminUids = Object.keys(adminUidMap.val());
    if (adminUids.includes(currentUserUid)) { // current user is admin
      var ticketsRef = admin.database().ref('/events/' + eventId).child('tickets');
      return ticketsRef.set(tickets);
    } else {
      console.log('Current user ' + currentUserUid + ' is not allowed to write event ' + eventId);
      return false;
    }
  });
});
