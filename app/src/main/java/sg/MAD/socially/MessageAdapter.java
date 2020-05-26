package sg.MAD.socially;

import android.content.Context;
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

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    private List<Chat> mChat;
    private Context mContext;
    private String imageurl;
    FirebaseUser fuser;

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    public MessageAdapter(List<Chat> chat, Context mContext,String imageurl) {
        this.mChat = chat;
        this.mContext = mContext;
        this.imageurl = imageurl;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profilepic;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profilepic = itemView.findViewById(R.id.profile_image_chatitem);
        }
    }


    @NonNull
    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_right_item, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_left_item, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, int position) {
        Chat chat = mChat.get(position);
        holder.show_message.setText(chat.getMessage());
        if(imageurl.equals("default")){
            holder.profilepic.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            Glide.with(mContext).load(imageurl).into(holder.profilepic);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    @Override
    public int getItemViewType(int position) {    //Check who is sender who is receiver to display the message at the correct position
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }
    }
}