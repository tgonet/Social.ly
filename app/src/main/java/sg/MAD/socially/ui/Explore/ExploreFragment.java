package sg.MAD.socially.ui.Explore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sg.MAD.socially.R;

public class ExploreFragment extends Fragment {

    RecyclerView recyclerView;
    List<String> interest;
    List<Integer> images;
    ExploreAdapter adapter;

    private ExploreViewModel exploreViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        exploreViewModel =
                ViewModelProviders.of(this).get(ExploreViewModel.class);
        View root = inflater.inflate(R.layout.fragment_explore, container, false);

        recyclerView = root.findViewById(R.id.recyclerview_id);

        //create arraylist to store interests and images
        interest = new ArrayList<>();
        images = new ArrayList<>();

        interest.add("Gaming"); //add items into interest list
        interest.add("Sports");
        interest.add("Eating");
        interest.add("Music");
        interest.add("Art & Design");
        interest.add("Reading");
        interest.add("Cooking");
        interest.add("Dancing");
        interest.add("Photography");
        interest.add("Volunteering");

        images.add(R.drawable.gaming); //add images to images list
        images.add(R.drawable.sports);
        images.add(R.drawable.eating);
        images.add(R.drawable.music);
        images.add(R.drawable.artdesign);
        images.add(R.drawable.reading);
        images.add(R.drawable.cooking);
        images.add(R.drawable.dancing);
        images.add(R.drawable.photography);
        images.add(R.drawable.volunteer);

        adapter = new ExploreAdapter(getActivity(), interest, images);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        recyclerView.setLayoutManager(gridLayoutManager); //set layout of content as grid + recycler view
        recyclerView.setAdapter(adapter);

        return root;
    }


}
