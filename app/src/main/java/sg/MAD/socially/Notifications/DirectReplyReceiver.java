package sg.MAD.socially.Notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sg.MAD.socially.Message;
import sg.MAD.socially.R;


public class DirectReplyReceiver extends BroadcastReceiver {

    String receiver;
    FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle remoteInput = null;
        receiver = intent.getStringExtra("receiver");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            remoteInput = RemoteInput.getResultsFromIntent(intent);
        }
        if (remoteInput != null) {
            CharSequence replyText = remoteInput.getCharSequence("key_text_reply");
            Message.sendMessagesss(fuser.getUid(),receiver,replyText,context);
        }
    }

}
