/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
'use strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

/**
 * Triggers when a user location is updated
 */
exports.sendFollowerNotification = functions.database.ref('/users/{userId}/{location}').onWrite(event => {
  const userId = event.params.userId;
  const location = event.data.val();


  // If un-follow we exit the function.
  if (!event.data.val()) {
    return console.log('User ', userId, 'location', location);
  }
  console.log('User :', userId, ' updated location:', location);


  // Get the notification token for the user
  const getDeviceTokensPromise = admin.database().ref(`/users/${userId}/gcmid`).once('value');

  // Get the lists associated to the current user location
  const getStoreListsPromise = admin.database().ref(`/stores/${location}`).once('value');

  // Get the user's friend list
  const getUserFriendsPromise = admin.database().ref(`/users/${userId}/friends`).once('value');

  return Promise.all([getDeviceTokensPromise, getStoreListsPromise, getUserFriendsPromise]).then(results => {

	 /*
      LOGIC:
      1. For each open list in the store:
          - Get User Id
          - Check if userId exists in FriendList
          - If exists, add friend name and list id to the payload
    */

    const tokensSnapshot = results[0];
    const listsSnapshot = results[1];
    const friendsSnapshot = results[2];

    console.log(tokensSnapshot.val());
    console.log(listsSnapshot.val());
    console.log(friendsSnapshot.val());

	  // Check if there are any lists attached to the store
    console.log('There are', listsSnapshot.numChildren(), 'lists available.');


    // Check if userIds belong to the current user's friend list
    // Entry Format=> KEY: ListId format: listId, VALUE: userId-status
    var matchedListIds = [];
    var listMap = listsSnapshot.val();
    var friends = friendsSnapshot.val();
    var originalSenders = [];

    for(var key in listMap) {
        var listId = key;
        var userId = listMap[key];

        if (friends.indexOf(userId) > -1) {
          originalSenders.push(listId+":" + userId);
          matchedListIds.push(listId);
        }
    }

    console.log("originalSenders: " + originalSenders.toString());
    console.log("Matched Lists: " + matchedListIds.toString());
    console.log("Matched Lists Count: " + matchedListIds.length);

    if (matchedListIds.length == 0) {
      console.log("Exiting: No List Match");
      return;
    }

    // Notification details
    const payload = {
      data: {
        payload: 'Some of your friends need a few things from ' + location + '. Would you like to pick those up?',
        listid: matchedListIds.toString(),
	    userId: originalSenders.toString()
      }
    };

    // Listing all tokens.
    const token = tokensSnapshot.val();

    // Send notifications to all tokens.
    return admin.messaging().sendToDevice(token, payload).then(response => {
      response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          console.error('Failure sending notification to', token, error);
        }
      });
      return 1;
    });
  });
});

/**
 * Triggers when a Shopper accepts an order
 */
exports.sendListStatusUpdateNotification = functions.database.ref('/lists/{listId}').onWrite(event => {
  const listId = event.params.listId;
  const listData = event.data.val();
  const recipient = "REQUESTER";


  if (!event.data.val()) {
    return console.log('List data not available');
  }
  console.log('List Data :', listData.toString());

  var shopperName = "shopper_name" in listData ? listData["shopper_name"] : "";
  var storeName = listData["store"];

  var status = listData["status"];
  var notificationMessage = "";

  switch(status) {
    case 'PICKED_UP':
      notificationMessage = shopperName + " has picked up your items from " + storeName;
      break;
    case 'COMPLETED':
      notificationMessage = shopperName + " has paid for your items from " + storeName;
      break;
    case 'OUT_FOR_DELIVERY':
      notificationMessage = shopperName + " is on the way with your items from " + storeName;
      break;
    default:
      console.log ("No action needed. Status: " + status);
      return 1;
  }

  // Get the notification token for the user
  var userId = listData["userId"];
  const getDeviceTokensPromise = admin.database().ref(`/users/${userId}/gcmid`).once('value');


  return Promise.all([getDeviceTokensPromise]).then(results => {

    const tokensSnapshot = results[0];
    console.log(tokensSnapshot.val());

    // Notification details
    const payload = {
      data: {
        payload: notificationMessage,
        listid: listId,
        recipient : recipient,
        status : status
      }
    };

    // Listing all tokens.
    const token = tokensSnapshot.val();

    // Send notifications to all tokens.
    return admin.messaging().sendToDevice(token, payload).then(response => {
      response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          console.error('Failure sending notification to', token, error);
        }
      });
      return 1;
    });
  });
});
