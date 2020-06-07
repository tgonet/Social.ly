package sg.MAD.socially.ui.Explore;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sg.MAD.socially.Activity;
import sg.MAD.socially.DisplayActivities;
import sg.MAD.socially.MainActivity;
import sg.MAD.socially.R;

public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.ViewHolder> {

    List<String> interest;
    List<Integer> images;
    LayoutInflater inflater;


    public ExploreAdapter(Context ctx, List<String> interest, List<Integer> images) {
        this.interest = interest;
        this.images = images;
        this.inflater = LayoutInflater.from(ctx);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.interestitem,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) { //display data at their specified position
        holder.interests.setText(interest.get(position));
        holder.interestImage.setImageResource(images.get(position));

    }

    @Override
    public int getItemCount() { //shows data for the number of interests

        return interest.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView interests;
        ImageView interestImage;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            interests = itemView.findViewById(R.id.interest_item);
            interestImage = itemView.findViewById(R.id.interest_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //need to bring to activity page for specific interest
                    Intent intent = new Intent(v.getContext(), DisplayActivities.class);
                    intent.putExtra("activity",interest.get(getAdapterPosition()));
                    v.getContext().startActivity(intent);
                }
            });

        }
    }

}
