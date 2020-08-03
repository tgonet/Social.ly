package sg.MAD.socially.Notifications;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
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
import java.util.List;

import sg.MAD.socially.MainActivity;
import sg.MAD.socially.Message;
import sg.MAD.socially.R;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    @SuppressLint("WrongThread")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String type = remoteMessage.getData().get("type");
        String receiver = remoteMessage.getData().get("receiver");
        String profilepicurl = remoteMessage.getData().get("PicUrl");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(!isAppForeground(this)){
            if (firebaseUser != null && receiver.equals(firebaseUser.getUid())){
                if(!type.equals("Message")){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        sendOreoFriendNotification(remoteMessage);
                    }
                    else{
                        sendFriendNotification(remoteMessage);
                    }
                }
                else{
                    new Tested(remoteMessage).execute(profilepicurl);
                }
            }
        }

    }

    //sends a "Got new friend" notification to devices with android version < 8.0
    private void sendFriendNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("sender");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        //Removes the alphabets in the userid
        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));

        //Creating an intent to the app when notification is clicked
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);

        //Sets the default ringtone
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Creates the notification
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

    private void sendOreoFriendNotification(RemoteMessage remoteMessage) {
    //sends a "Got new friend" notification to devices with android version > 8.0
        String sender = remoteMessage.getData().get("sender");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        //Removes the alphabets in the userid
        int j = Integer.parseInt(sender.replaceAll("[\\D]", ""));

        //Creating an intent to the app when notification is clicked
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);

        //Sets the default ringtone
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Creates the notification
        OreoNotification oreoNotification = new OreoNotification(this);
        NotificationCompat.Builder builder = oreoNotification.getOreoNotificationFriends(title, body, pendingIntent,
                defaultSound, R.drawable.logo_mark);

        oreoNotification.getManager().notify(j, builder.build());
    }

    //sends a chat notification to devices with android version > 8.0
    private void sendOreoNotification(RemoteMessage remoteMessage,Bitmap bitmap){
        Intent intent;
        PendingIntent pendingIntent;
        Intent replyIntent;
        PendingIntent replyPendingIntent = null;
        String sender = remoteMessage.getData().get("sender");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        //final Bitmap bitmap = getBitmapfromUrl(remoteMessage.getData().get("PicUrl"));

        //Removes the alphabets in the userid
        int j = Integer.parseInt(sender.replaceAll("[\\D]", ""));

        //Creating an intent to Message.java when notification is clicked
        intent = new Intent(this, Message.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        Bundle bundle = new Bundle();
        bundle.putString("userid", sender);
        intent.putExtras(bundle);
        pendingIntent = stackBuilder.getPendingIntent(j, PendingIntent.FLAG_UPDATE_CURRENT);

        //Creates a notification that is made for messages using messaging style
        Person person = new Person.Builder().setName(title).setIcon(IconCompat.createWithBitmap(bitmap)).build();
        NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle(person);
        messagingStyle.setConversationTitle(title);
        messagingStyle.addMessage(body,0,person);

        //Sets the default ringtone
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Creates the notification
        OreoNotification oreoNotification = new OreoNotification(this);
        NotificationCompat.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent,
                defaultSound, R.drawable.logo_mark,bitmap);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            Bundle bundles = new Bundle();
            bundles.putString("receiver",remoteMessage.getData().get("sender"));
            bundles.putString("Name",remoteMessage.getData().get("title"));
            bundles.putString("Pic", remoteMessage.getData().get("PicUrl"));

            //Creates the action
            RemoteInput remoteInput = new RemoteInput.Builder("Notification reply")
                    .setLabel("Reply")
                    .build();

            //Reply is received in the DirectReplyReceiver.class
            replyIntent = new Intent(this, DirectReplyReceiver.class);
            replyIntent.putExtras(bundles);
            replyPendingIntent = PendingIntent.getBroadcast(this,
                    j, replyIntent,  PendingIntent.FLAG_UPDATE_CURRENT);


            //Adds the action to the notification
            NotificationCompat.Action action =
                    new NotificationCompat.Action.Builder(R.drawable.ic_baseline_reply_24,"Reply", replyPendingIntent)
                            .addRemoteInput(remoteInput).build();
            builder.addAction(action);
            builder.setStyle(messagingStyle);
        }

        oreoNotification.getManager().notify(j, builder.build());
    }

    //sends a chat notification to devices with android version < 8.0
    private void sendNotification(RemoteMessage remoteMessage,Bitmap bitmap) {
        Intent intent;
        PendingIntent pendingIntent;
        Notification builder;
        String sender = remoteMessage.getData().get("sender");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        //final Bitmap bitmap = getBitmapfromUrl(remoteMessage.getData().get("PicUrl"));

        //Removes the alphabets in the userid
        int j = Integer.parseInt(sender.replaceAll("[\\D]", ""));

        //Creating an intent to Message.java when notification is clicked
        intent = new Intent(this, Message.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        Bundle bundle = new Bundle();
        bundle.putString("userid", sender);
        intent.putExtras(bundle);
        pendingIntent = stackBuilder.getPendingIntent(j, PendingIntent.FLAG_UPDATE_CURRENT);

        //Sets the default ringtone
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Creates a notification that is made for messages using messaging style
        Person person = new Person.Builder().setName(title).setIcon(IconCompat.createWithBitmap(bitmap)).build();
        NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle(person);
        messagingStyle.setConversationTitle(title);
        messagingStyle.addMessage(body,0,person);

        //Builds the notification
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

    //Used for getbitmapfromurl method
    public class Tested  extends AsyncTask<String , Long, Bitmap> {
        RemoteMessage RemoteMessage;
        public Tested( RemoteMessage remoteMessage){
            RemoteMessage =  remoteMessage;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = getBitmapfromUrl(params[0]);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sendOreoNotification(RemoteMessage,bitmap);
            }
            else{
                sendNotification(RemoteMessage,bitmap);
            }
        }

    }

    public static boolean isAppForeground(Context context) {

        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> l = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : l) {
            if (info.uid == context.getApplicationInfo().uid && info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

}


