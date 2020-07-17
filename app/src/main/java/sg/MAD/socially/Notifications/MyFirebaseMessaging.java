package sg.MAD.socially.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import androidx.core.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import androidx.core.app.TaskStackBuilder;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import sg.MAD.socially.Class.Chat;
import sg.MAD.socially.MainActivity;
import sg.MAD.socially.Message;
import sg.MAD.socially.R;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    /**
     *
     * @param remoteMessage
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String receiver = remoteMessage.getData().get("receiver");
        String type = remoteMessage.getData().get("type");
        Log.d("TYPESSSS", String.valueOf(remoteMessage.getData()));

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null && receiver.equals(firebaseUser.getUid())){
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

    private void sendFriendNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        RemoteMessage.Notification notification  = remoteMessage.getNotification();

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

        int i=0;
        if (j>0) {
            i=j;
        }

        notificationManager.notify(i, builder);
    }

    private void sendOreoFriendNotification(RemoteMessage remoteMessage) {
        String sender = remoteMessage.getData().get("sender");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(sender.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, MainActivity.class);


        PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoNotification oreoNotification = new OreoNotification(this);
        NotificationCompat.Builder builder = oreoNotification.getOreoNotificationFriends(title, body, pendingIntent,
                defaultSound, R.drawable.logo_mark);

        int i = 0;
        if (j > 0){
            i = j;
        }
        oreoNotification.getManager().notify(i, builder.build());
    }

    private void sendOreoNotification(RemoteMessage remoteMessage){
        Intent intent;
        PendingIntent pendingIntent;
        Intent replyIntent;
        PendingIntent replyPendingIntent = null;
        String sender = remoteMessage.getData().get("sender");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(sender.replaceAll("[\\D]", ""));
        int i = 0;
        if (j > 0){
            i = j;
        }

        intent = new Intent(this, Message.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        Bundle bundle = new Bundle();
        bundle.putString("userid", sender);
        intent.putExtras(bundle);
        pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        OreoNotification oreoNotification = new OreoNotification(this);
        NotificationCompat.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent,
                defaultSound, R.drawable.logo_mark);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            Bundle bundles = new Bundle();
            bundles.putString("receiver",remoteMessage.getData().get("sender"));
            RemoteInput remoteInput = new RemoteInput.Builder("key_text_reply")
                    .setLabel("Your answer...")
                    .build();

            replyIntent = new Intent(this, DirectReplyReceiver.class);
            replyIntent.putExtras(bundles);
            replyPendingIntent = PendingIntent.getBroadcast(this,
                    i, replyIntent,  PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Action action =
                    new NotificationCompat.Action.Builder(R.drawable.ic_baseline_reply_24,"Reply", replyPendingIntent)
                            .addRemoteInput(remoteInput).build();
            builder.addAction(action);

           /* NotificationCompat.MessagingStyle messagingStyle =
                    new NotificationCompat.MessagingStyle("Me");
            messagingStyle.setConversationTitle(title);
            for(Chat chatMessage : MESSAGES){
                NotificationCompat.MessagingStyle.Message notificationMessage =
                        new NotificationCompat.MessagingStyle.Message(
                                chatMessage.getMessage(),
                                chatMessage.getTimestamp(),
                                chatMessage.getSender()
                        );
                messagingStyle.addMessage(notificationMessage);
            }


            builder.setStyle(messagingStyle);*/
        }


        oreoNotification.getManager().notify(i, builder.build());
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        Intent intent;
        PendingIntent pendingIntent;
        Notification builder;
        String sender = remoteMessage.getData().get("sender");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        RemoteMessage.Notification notification  = remoteMessage.getNotification();

        int j = Integer.parseInt(sender.replaceAll("[\\D]", ""));

        intent = new Intent(this, Message.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        Bundle bundle = new Bundle();
        bundle.putString("userid", sender);
        intent.putExtras(bundle);
        pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if(body.length() > 35){
            builder = new Notification.Builder(this)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setSmallIcon(R.drawable.logo_mark)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setStyle(new Notification.BigTextStyle()
                            .bigText(body)
                            .setBigContentTitle(title))
                    .setAutoCancel(true)
                    .setShowWhen(true)
                    .setSound(defaultSound)
                    .setContentIntent(pendingIntent).build();
        }
        else{
            builder = new Notification.Builder(this)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setSmallIcon(R.drawable.logo_mark)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setShowWhen(true)
                    .setSound(defaultSound)
                    .setContentIntent(pendingIntent).build();
        }

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);


        int i=0;
        if (j>0) {
            i=j;
        }

        notificationManager.notify(i, builder);
    }
}
