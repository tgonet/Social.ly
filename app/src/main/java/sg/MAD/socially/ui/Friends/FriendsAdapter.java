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
        ImageView image = (ImageView) convertView.findViewById(R.id.addfriend_profilepic);
        TextView friendCount = (TextView) convertView.findViewById(R.id.addfriend_friendcount);
        TextView dob = (TextView) convertView.findViewById(R.id.addfriend_dob);
        TextView desc = (TextView) convertView.findViewById(R.id.addfriend_shortdesc);

        name.setText(user.getName());
        Glide.with(convertView.getContext()).load(user.getImageURL()).into(image);
        String userFriends = user.getFriends();
        int friends = 0;
        String[] friendList = userFriends.split(",");
        friends =friendList.length;
        if (friends > 1){
            friendCount.setText(friends + " friends");
        }
        else{
            friendCount.setText(friends + " friend");
        }

        dob.setText("Date of Birth: \t" + user.getDOB());
        desc.setText(user.getShortDesc());

        return convertView;

    }
}
