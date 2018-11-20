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
  admin.database()
    .ref('/events/' + eventId + '/users/admin/')
    .once('value', snapshot => {
      // TODO: find uid from email, insert into event
      return snapshot.val().contains(currentUserUid);
    })
})
