package sg.MAD.socially.ui.Friends.Interests;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import sg.MAD.socially.Class.NotificationFriend;
import sg.MAD.socially.R;

public class FriendsInterestAdapter extends RecyclerView.Adapter<FriendsInterestViewHolder>{
    ArrayList<String> data;

    public FriendsInterestAdapter(ArrayList<String> input) {
        data = input;
    }

    @NonNull
    @Override
    public FriendsInterestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.fragment_home_addfriend_interest, parent, false);
        return new FriendsInterestViewHolder(item);
    }

    @Override
    public void onBindViewHolder(FriendsInterestViewHolder holder, int position) {
        String interest = data.get(position); //pass the list into objects
        holder.interest.setText(interest); //display individual interests
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

