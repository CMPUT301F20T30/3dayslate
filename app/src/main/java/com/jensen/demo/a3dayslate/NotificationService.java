package com.jensen.demo.a3dayslate;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

/* NotificationService

   Version 1.0.0

   November 5 2020

   Copyright [2020] [Jensen Khemchandani]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

/**
 * This class is the notification service for the app
 * Extends from the FirebaseMessagingService to handle messages received by the device through Firebase
 * Creates and listens for notifications on a single channel -> used for requesting books
 * @author Jensen Khemchandani
 * @version 1.0.0
 */

/* FireBase Notifications by Code sphere (Rajjan Sharma)
   Youtube Link: https://www.youtube.com/watch?v=lL_HaaPbMF4

   Used video as a reference for setting up notifications
 */

public class NotificationService extends FirebaseMessagingService {
    private String channel_id = "my_channel";
    /**
     * Handles a message when one is recieved
     * @param remoteMessage
     * Contains the message received from the server along with the data associated with it
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //Log.w("TESTINGNOTFIC", "GOT HERE!!!!");
        //Intent intent = new Intent(this, MainActivity.class);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Random random = new Random();
        int notificatonID = random.nextInt();
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Map<String, String> messageData = remoteMessage.getData();
        Notification notification = new NotificationCompat.Builder(this, channel_id)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setSmallIcon(R.drawable.ic_android_black_24dp)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(notificatonID, notification);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager);
            Log.w("TESTINGNOTFIC", "GOT HERE!!!!");
        }
    }

    /**
     * Creates a notification channel to route messages for retrieval
     * @param notificationManager
     * Contains a NotificationManager instance to handle and create the channel
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationManager notificationManager) {
        String channelName = "ChannelName";
        NotificationChannel channel = new NotificationChannel(channel_id, channelName, NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);
    }

}
