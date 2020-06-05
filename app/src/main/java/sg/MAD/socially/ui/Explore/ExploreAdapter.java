package sg.MAD.socially.ui.Explore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.interest.setText(interest.get(position));
        holder.interestImage.setImageResource(images.get(position));

    }

    @Override
    public int getItemCount() {

        return interest.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView interest;
        ImageView interestImage;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            interest = itemView.findViewById(R.id.interest_item);
            interestImage = itemView.findViewById(R.id.interest_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //need to bring to activity page for specific interest
                }
            });

        }
    }

}
