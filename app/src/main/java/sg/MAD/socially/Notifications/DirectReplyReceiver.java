package sg.MAD.socially.Notifications;

import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.Person;
import androidx.core.app.TaskStackBuilder;
import androidx.core.graphics.drawable.IconCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.MAD.socially.Class.Chat;
import sg.MAD.socially.Message;
import sg.MAD.socially.R;


public class DirectReplyReceiver extends BroadcastReceiver  {

    Bitmap FriendPic,UserPic;
    String receivers,name,pic,sender,title;
    FirebaseUser fuser;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle remoteInput = null;
        receivers = intent.getStringExtra("receiver");
        name = intent.getStringExtra("Name");
        pic = intent.getStringExtra("Pic");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        this.context = context;
        title = name;
        sender = fuser.getUid();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            remoteInput = RemoteInput.getResultsFromIntent(intent);
        }
        if (remoteInput != null) {
            CharSequence replyText = remoteInput.getCharSequence("Notification reply");
            //Sends the message to fireabase and a notification to the receiver
            Message.sendMessagesss(fuser.getUid(),receivers,replyText,context,"Message",fuser.getPhotoUrl().toString());
            //Updates the user's notification that he replied to
            sendOreoMessage(pic);
        }
    }

    private void sendOreoMessage(String Pic){
        String[] bitmapstring = {Pic,FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString()};
        new Testing(bitmapstring,this).execute(bitmapstring);
    }

    public void callback()
    {
        final ArrayList<Chat> MESSAGES = new ArrayList<>();
        final ArrayList<Chat> last_two = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final Intent[] intent = new Intent[1];
        final PendingIntent[] pendingIntent = new PendingIntent[1];
        final Person[] name = new Person[1];

        reference.child("Chats").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MESSAGES.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    //Checks if this message is send between the 2 users
                    if (chat.getReceiver().equals(sender) && chat.getSender().equals(receivers) ||
                            chat.getReceiver().equals(receivers) && chat.getSender().equals(sender)) {
                        MESSAGES.add(chat);
                    }
                }

                last_two.add(MESSAGES.get(MESSAGES.size()-2));
                last_two.add(MESSAGES.get(MESSAGES.size()-1));

                //Removes the alphabets in the userid
                int j = Integer.parseInt(receivers.replaceAll("[\\D]", ""));

                //rebuild notification
                intent[0] = new Intent(context, Message.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addNextIntentWithParentStack(intent[0]);
                Bundle bundle = new Bundle();
                bundle.putString("userid", receivers);
                intent[0].putExtras(bundle);
                pendingIntent[0] = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                OreoNotification oreoNotification = new OreoNotification(context);
                NotificationCompat.Builder builder = oreoNotification.getOreoMessage(pendingIntent[0],
                        defaultSound, R.drawable.logo_mark);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    Person person2 = new Person.Builder().setName("You").setIcon(IconCompat.createWithBitmap(UserPic)).build();
                    Person person1 = new Person.Builder().setName(title).setIcon(IconCompat.createWithBitmap(FriendPic)).build();
                    NotificationCompat.MessagingStyle messagingStyle =
                            new NotificationCompat.MessagingStyle(person1);
                    messagingStyle.setConversationTitle(title);

                    for(Chat chatMessage : last_two){
                        if(chatMessage.getName().equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){
                            name[0] = person2;
                        }
                        else{
                            name[0] = person1;
                        }
                        NotificationCompat.MessagingStyle.Message notificationMessage =
                                new NotificationCompat.MessagingStyle.Message(
                                        chatMessage.getMessage(),
                                        chatMessage.getTimestamp(),
                                        name[0]
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

    public class Testing extends AsyncTask<String, Long, Bitmap[]>{
        DirectReplyReceiver directReply;
        String[] Bitmap;
        public Testing(String[] bitmap, DirectReplyReceiver drr){
            Bitmap = bitmap;
            directReply = drr;
        }

        @Override
        protected Bitmap[] doInBackground(String... params) {
            Bitmap[] bitmap = {MyFirebaseMessaging.getBitmapfromUrl(params[0]), MyFirebaseMessaging.getBitmapfromUrl(params[1])};
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap[] bitmap) {
            super.onPostExecute(bitmap);

            FriendPic = bitmap[0];
            UserPic = bitmap[1];

            directReply.callback();
        }
    }
}
