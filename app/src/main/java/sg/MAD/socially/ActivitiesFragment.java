package sg.MAD.socially;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActivitiesFragment extends Fragment {

    RecyclerView rv;

    public ActivitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rv = container.findViewById(R.id.rv_activities);
        return inflater.inflate(R.layout.fragment_activities, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
