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
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import androidx.core.app.Person;
import androidx.core.app.RemoteInput;
import androidx.core.app.TaskStackBuilder;
import androidx.core.graphics.drawable.IconCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import sg.MAD.socially.Class.Chat;
import sg.MAD.socially.Class.User;
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
        String type = remoteMessage.getData().get("type");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if(type.equals("Message")){
                    sendOreoNotification(remoteMessage);
                }
                else if(type.equals("Reply Message")){
                    sendOreoMessage(remoteMessage);
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

    /**
     * This is to use the messaging style in notifications which will show the user's reply to his friend's message
     * and the message he replied to
     */
    private void sendOreoMessage(final RemoteMessage remoteMessage){
        final String receiver = remoteMessage.getData().get("receiver");
        final String[] URL = new String[1];
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        /**
         * Retreive the friend's profile URL to be converted into an image
         * This method is placed here as there has to have a buffer before the data is retrieved to be converted into an image
          */
        reference.child("Users").child(receiver).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                URL[0] = user.getImageURL();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final Intent[] intent = new Intent[1];
        final PendingIntent[] pendingIntent = new PendingIntent[1];
        final String sender = remoteMessage.getData().get("sender");
        final String title = remoteMessage.getData().get("title");
        final Bitmap userpic = getBitmapfromUrl(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
        final ArrayList<Chat> MESSAGES = new ArrayList<>();
        final ArrayList<Chat> last_two = new ArrayList<>();

        final Bitmap bitmaps = getBitmapfromUrl(URL[0]);

        reference.child("Chats").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MESSAGES.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    //Checks if this message is send between the 2 users
                    if (chat.getReceiver().equals(sender) && chat.getSender().equals(receiver) ||
                            chat.getReceiver().equals(receiver) && chat.getSender().equals(sender)) {
                        MESSAGES.add(chat);
                    }
                }

                last_two.add(MESSAGES.get(MESSAGES.size()-2));
                last_two.add(MESSAGES.get(MESSAGES.size()-1));

                //Removes the alphabets in the userid
                int j = Integer.parseInt(receiver.replaceAll("[\\D]", ""));

                intent[0] = new Intent(getApplicationContext(), Message.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                stackBuilder.addNextIntentWithParentStack(intent[0]);
                Bundle bundle = new Bundle();
                bundle.putString("userid", receiver);
                intent[0].putExtras(bundle);
                pendingIntent[0] = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                OreoNotification oreoNotification = new OreoNotification(getApplicationContext());
                NotificationCompat.Builder builder = oreoNotification.getOreoMessage(pendingIntent[0],
                        defaultSound, R.drawable.logo_mark);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    Person person1 = new Person.Builder().setName(title).setIcon(IconCompat.createWithBitmap(bitmaps)).build();
                    Person person2 = new Person.Builder().setName("You").setIcon(IconCompat.createWithBitmap(userpic)).build();
                    NotificationCompat.MessagingStyle messagingStyle =
                            new NotificationCompat.MessagingStyle(person1);
                    messagingStyle.setConversationTitle(title);

                    Person name;

                    for(Chat chatMessage : last_two){
                        if(chatMessage.getName().equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){
                            name = person2;
                        }
                        else{
                            name = person1;
                        }
                        NotificationCompat.MessagingStyle.Message notificationMessage =
                                new NotificationCompat.MessagingStyle.Message(
                                        chatMessage.getMessage(),
                                        chatMessage.getTimestamp(),
                                        name
                                );
                        messagingStyle.addMessage(notificationMessage);
                    }

                    builder.setStyle(messagingStyle);
                }

                oreoNotification.getManager().notify(j, builder.build());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            Log.e("awesome", "Error in getting notification image: " + e.getLocalizedMessage());
            return null;
        }
    }
}
