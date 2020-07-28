package sg.MAD.socially.ui.Friends.Interests;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.MAD.socially.R;

public class FriendsInterestViewHolder extends RecyclerView.ViewHolder {
    TextView interest;

    public FriendsInterestViewHolder(View itemView){
        super(itemView);
        interest = itemView.findViewById(R.id.addfriend_interest);
    }
}

