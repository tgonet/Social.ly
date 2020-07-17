package sg.MAD.socially.ui.notifications;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import sg.MAD.socially.R;
import sg.MAD.socially.ui.notifications.activities.NotificationsActivityFragment;
import sg.MAD.socially.ui.notifications.friends.NotificationsFriendsFragment;

public class NotificationsAdapter extends FragmentStateAdapter {
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    public NotificationsAdapter(Context context, Fragment fragment) {
        super(fragment);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                return new NotificationsFriendsFragment();
            case 1:
                return new NotificationsActivityFragment();
            default:
                return null;
        }
    }


    @Nullable
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}

/*
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    public NotificationsAdapter(Context context, Fragment fragment) {
        super(fragment);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new NotificationsFriendsFragment();
            case 1:
                fragment = new NotificationsActivityFragment();
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}
 */