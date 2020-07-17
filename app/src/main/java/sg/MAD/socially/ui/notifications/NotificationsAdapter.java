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

    @Override
    public int getItemCount() {
        return 2;
    }

}
