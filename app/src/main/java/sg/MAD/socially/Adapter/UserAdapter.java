package sg.MAD.socially.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import sg.MAD.socially.Class.Chat;
import sg.MAD.socially.Message;
import sg.MAD.socially.R;
import sg.MAD.socially.Class.User;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class UserAdapter  extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{
    List<User> Users;
    Context mContext;

    String theLastMessage;

    public UserAdapter(List<User> users, Context mContext) {
        this.Users = users;
        this.mContext = mContext;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profilepic;
        public  TextView message;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profilepic = itemView.findViewById(R.id.profilepic);
            message = itemView.findViewById(R.id.message);
        }
    }

    @NonNull
    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.useritem, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.MyViewHolder holder, int position) {
        final User user = Users.get(position);
        lastMessage(user.getId(),holder.message);
        holder.username.setText(user.getName());
        if(user.getImageURL().equals("default")){
            holder.profilepic.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            Glide.with(mContext).load(user.getImageURL()).into(holder.profilepic);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Message.class);
                intent.putExtra("userid",user.getId());
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return Users.size();
    }

    //check for last message
    private void lastMessage(final String userid, final TextView message){
        theLastMessage = "Empty";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (firebaseUser != null && chat != null) {
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                            theLastMessage = chat.getMessage();
                        }
                    }
                }

                if(theLastMessage.equals("Empty")){
                    message.setText("");

                }
                else {
                    message.setText(theLastMessage);
                }

                theLastMessage = "Empty";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
