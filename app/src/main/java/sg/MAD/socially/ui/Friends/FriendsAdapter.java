package sg.MAD.socially.ui.Friends;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import sg.MAD.socially.Class.NotificationFriend;
import sg.MAD.socially.R;
import sg.MAD.socially.Class.User;
import sg.MAD.socially.ui.Friends.Interests.FriendsInterestAdapter;

public class FriendsAdapter extends ArrayAdapter<User> {

    public FriendsAdapter(Context context, int resourceId, ArrayList<User> User){
        super(context, resourceId, User);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        //position of user in the swipe cards
        User user = getItem(position);

        Log.d("Adapter", "getView " + position + " ");
        //this is the view for each user card
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home_addfriend, parent, false);
        }

        //Instantiating the contents in each card by referencing to the resource id
        TextView nickname = (TextView) convertView.findViewById(R.id.addfriend_nickname);
        ImageView image = (ImageView) convertView.findViewById(R.id.addfriend_profilepic);
        TextView friendCount = (TextView) convertView.findViewById(R.id.addfriend_friendcount);
        RecyclerView rv_interests = (RecyclerView) convertView.findViewById(R.id.rv_addfriends_interest);
        TextView desc = (TextView) convertView.findViewById(R.id.addfriend_shortdesc);

        //setting the user details to each content
        nickname.setText(user.getNickName());
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

        ArrayList<String> interest_list = FriendsFragment.removeComma(user.getInterest());
        FriendsInterestAdapter mAdapter = new FriendsInterestAdapter(interest_list);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);

        rv_interests.setLayoutManager(mLayoutManager);
        rv_interests.setAdapter(mAdapter);

        desc.setText(user.getShortDesc());

        //return the details in a card view
        return convertView;
    }
}
