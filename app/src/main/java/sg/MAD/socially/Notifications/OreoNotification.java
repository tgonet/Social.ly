package sg.MAD.socially.Notifications;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class OreoNotification extends ContextWrapper {

    private static final String CHANNEL_ID = "1";
    private static final String CHANNEL_NAME = "Messages";
    private static final String CHANNEL_ID_1 = "2";
    private static final String CHANNEL_NAME_1 = "Friend Request";

    private NotificationManager notificationManager;

    public OreoNotification(Context base) {
        super(base);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
        }
    }

    //Creates the channel
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Messages");
        channel.enableLights(false);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        getManager().createNotificationChannel(channel);

        NotificationChannel channel_1 = new NotificationChannel(CHANNEL_ID_1,
                CHANNEL_NAME_1,
                NotificationManager.IMPORTANCE_DEFAULT);
        channel_1.setDescription("Friend Request");
        channel_1.enableLights(false);
        channel_1.enableVibration(false);
        channel_1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel_1);
    }

    public NotificationManager getManager(){
        if (notificationManager == null){
            notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return  notificationManager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public NotificationCompat.Builder getOreoNotification(String title, String body,
                                                          PendingIntent pendingIntent, Uri soundUri, int icon,Bitmap bitmap){

        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(icon)
                .setLargeIcon(bitmap)
                .setSound(soundUri)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);

    }

    @TargetApi(Build.VERSION_CODES.O)
    public  NotificationCompat.Builder getOreoNotificationFriends(String title, String body,
            PendingIntent pendingIntent, Uri soundUri, int icon){

        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_1)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(icon)
                .setSound(soundUri)
                .setAutoCancel(true);

    }

    @TargetApi(Build.VERSION_CODES.O)
    public  NotificationCompat.Builder getOreoMessage(PendingIntent pendingIntent, Uri soundUri, int icon){

        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(icon)
                .setSound(soundUri)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);

    }
}