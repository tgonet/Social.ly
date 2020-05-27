package sg.MAD.socially;

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

import java.util.List;

public class GridFriendAdapter extends RecyclerView.Adapter<GridFriendAdapter.MyViewHolder> {

    List<User> FriendsList;
    Context mContext;

    public GridFriendAdapter(List<User> friendslist, Context mContext) {
        FriendsList = friendslist;
        this.mContext = mContext;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView friendname;
        public ImageView friendpic;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            friendname = itemView.findViewById(R.id.Friend_name);
            friendpic = itemView.findViewById(R.id.Friend_pic);
        }
    }

    @NonNull
    @Override
    public GridFriendAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridfrienditem, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final User user = FriendsList.get(position);
        holder.friendname.setText(user.getName());
        if(user.getImageURL().equals("default")){
            holder.friendpic.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            Glide.with(mContext).load(user.getImageURL()).into(holder.friendpic);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Message.class);
                intent.putExtra("userid",user.getId());
                mContext.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return FriendsList.size();
    }
}
