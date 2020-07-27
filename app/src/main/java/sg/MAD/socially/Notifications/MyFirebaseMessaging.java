package sg.MAD.socially.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import androidx.core.app.Person;
import androidx.core.app.RemoteInput;
import androidx.core.app.TaskStackBuilder;
import androidx.core.graphics.drawable.IconCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import sg.MAD.socially.MainActivity;
import sg.MAD.socially.Message;
import sg.MAD.socially.R;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String type = remoteMessage.getData().get("type");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if(type.equals("Message")){
                    sendOreoNotification(remoteMessage);
                }
                else{
                    sendOreoFriendNotification(remoteMessage);
                }
            }
            else {
                if(type.equals("Message")){
                    sendNotification(remoteMessage);
                }
                else{
                    sendFriendNotification(remoteMessage);
                }
            }
        }
    }

    //sends a "Got new friend" notification to devices with android version < 8.0
    private void sendFriendNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("sender");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));

        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification builder = new Notification.Builder(this)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.logo_mark)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setShowWhen(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(j, builder);
    }

    //sends a "Got new friend" notification to devices with android version > 8.0
    private void sendOreoFriendNotification(RemoteMessage remoteMessage) {
        String sender = remoteMessage.getData().get("sender");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        //Removes the alphabets in the userid
        int j = Integer.parseInt(sender.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, MainActivity.class);


        PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoNotification oreoNotification = new OreoNotification(this);
        NotificationCompat.Builder builder = oreoNotification.getOreoNotificationFriends(title, body, pendingIntent,
                defaultSound, R.drawable.logo_mark);

        oreoNotification.getManager().notify(j, builder.build());
    }

    //sends a chat notification to devices with android version > 8.0
    private void sendOreoNotification(RemoteMessage remoteMessage){
        Intent intent;
        PendingIntent pendingIntent;
        Intent replyIntent;
        PendingIntent replyPendingIntent = null;
        String sender = remoteMessage.getData().get("sender");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        final Bitmap bitmap = getBitmapfromUrl(remoteMessage.getData().get("PicUrl"));

        //Removes the alphabets in the userid
        int j = Integer.parseInt(sender.replaceAll("[\\D]", ""));

        intent = new Intent(this, Message.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        Bundle bundle = new Bundle();
        bundle.putString("userid", sender);
        intent.putExtras(bundle);
        pendingIntent = stackBuilder.getPendingIntent(j, PendingIntent.FLAG_UPDATE_CURRENT);

        Person person = new Person.Builder().setName(title).setIcon(IconCompat.createWithBitmap(bitmap)).build();
        NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle(person);
        messagingStyle.setConversationTitle(title);
        messagingStyle.addMessage(body,0,person);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        OreoNotification oreoNotification = new OreoNotification(this);
        NotificationCompat.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent,
                defaultSound, R.drawable.logo_mark,bitmap);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            Bundle bundles = new Bundle();
            bundles.putString("receiver",remoteMessage.getData().get("sender"));
            bundles.putString("Name",remoteMessage.getData().get("title"));
            bundles.putString("Pic", remoteMessage.getData().get("PicUrl"));
            RemoteInput remoteInput = new RemoteInput.Builder("Notification reply")
                    .setLabel("Reply")
                    .build();

            replyIntent = new Intent(this, DirectReplyReceiver.class);
            replyIntent.putExtras(bundles);
            replyPendingIntent = PendingIntent.getBroadcast(this,
                    j, replyIntent,  PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Action action =
                    new NotificationCompat.Action.Builder(R.drawable.ic_baseline_reply_24,"Reply", replyPendingIntent)
                            .addRemoteInput(remoteInput).build();
            builder.addAction(action);
            builder.setStyle(messagingStyle);
        }

        oreoNotification.getManager().notify(j, builder.build());
    }

    //sends a chat notification to devices with android version < 8.0
    private void sendNotification(RemoteMessage remoteMessage) {
        Intent intent;
        PendingIntent pendingIntent;
        Notification builder;
        String sender = remoteMessage.getData().get("sender");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        final Bitmap bitmap = getBitmapfromUrl(remoteMessage.getData().get("PicUrl"));

        //Removes the alphabets in the userid
        int j = Integer.parseInt(sender.replaceAll("[\\D]", ""));

        intent = new Intent(this, Message.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        Bundle bundle = new Bundle();
        bundle.putString("userid", sender);
        intent.putExtras(bundle);
        pendingIntent = stackBuilder.getPendingIntent(j, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Person person = new Person.Builder().setName(title).setIcon(IconCompat.createWithBitmap(bitmap)).build();

        NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle(person);
        messagingStyle.setConversationTitle(title);
        messagingStyle.addMessage(body,0,person);

        builder = new NotificationCompat.Builder(this)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.logo_mark)
                .setLargeIcon(bitmap)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(messagingStyle)
                .setAutoCancel(true)
                .setShowWhen(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(j, builder);
    }

    //Converts an image URL into an image
    public static Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            Log.e("awesome", "Error in getting notification image: " + e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
    }

}


