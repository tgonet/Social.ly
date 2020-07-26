package sg.MAD.socially.Notifications;

import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import sg.MAD.socially.Message;

public class DirectReplyReceiver extends BroadcastReceiver {

    String receivers,name,Pic;
    FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle remoteInput = null;
        receivers = intent.getStringExtra("receiver");
        name = intent.getStringExtra("Name");
        Pic = intent.getStringExtra("Pic");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            remoteInput = RemoteInput.getResultsFromIntent(intent);
        }
        if (remoteInput != null) {
            CharSequence replyText = remoteInput.getCharSequence("Notification reply");
            //Sends the message to fireabase and a notification to the receiver
            Message.sendMessagesss(fuser.getUid(),receivers,replyText,context,"Message",fuser.getPhotoUrl().toString());
            //Updates the user's notification that he replied to
            Message.sendNotifications(fuser.getUid(),name,replyText,context,"Reply Message",receivers);
        }
    }


}
