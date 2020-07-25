package sg.MAD.socially.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import sg.MAD.socially.R;

public class NotificationsFragment extends Fragment {
    NotificationsAdapter notificationsAdapter;
    ViewPager2 viewPager;
    TabLayout tabLayout;
    //get tab titles from strings.xml
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        notificationsAdapter = new NotificationsAdapter(this.getContext(), this);
        //viewpager allows the user to swipe left/right for different fragments
        viewPager = view.findViewById(R.id.notifications_viewpager);
        viewPager.setAdapter(notificationsAdapter);

        //tablayout allows the user to click on desired fragment
        tabLayout = view.findViewById(R.id.notification_tabs);
        //set the text for the tabs
        for (int i : TAB_TITLES) {
            tabLayout.addTab(tabLayout.newTab().setText(i));
        }

        //on swipe, the tab in tablayout changes selected position
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        //on clicking of selected tab, the viewpager will
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}

