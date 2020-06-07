package sg.MAD.socially.ui.Friends;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;

import sg.MAD.socially.R;
import sg.MAD.socially.User;

public class FriendsAdapter extends ArrayAdapter<User> {

    public FriendsAdapter(Context context, int resourceId, ArrayList<User> User){
        super(context, resourceId, User);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        User user = getItem(position);

        Log.d("Adapter", "getView " + position + " ");
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home_addfriend, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.addfriend_name);
        TextView nickname = (TextView) convertView.findViewById(R.id.addfriend_nickname);
        ImageView image = (ImageView) convertView.findViewById(R.id.addfriend_profilepic);
        TextView friendCount = (TextView) convertView.findViewById(R.id.addfriend_friendcount);
        TextView interests = (TextView) convertView.findViewById(R.id.addfriend_interests);
        TextView desc = (TextView) convertView.findViewById(R.id.addfriend_shortdesc);

        name.setText(user.getName());
        nickname.setText("aka " + user.getNickName());
        Glide.with(convertView.getContext()).load(user.getImageURL()).into(image);
        String userFriends = user.getFriends();

        int friends = 0;
        if (!userFriends.isEmpty()) {
            //More than 1 friend
            if (userFriends.contains(",")) {
                String[] friendList = userFriends.split(",");
                friends = friendList.length;
            }
            //1 friend
            else {
                friends = 1;
            }
        }
        friendCount.setText(friends + " friends");
        interests.setText(user.getInterest());
        desc.setText(user.getShortDesc());


        return convertView;
    }
}
