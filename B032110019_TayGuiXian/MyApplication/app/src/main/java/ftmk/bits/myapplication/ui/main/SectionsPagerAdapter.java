package ftmk.bits.myapplication.ui.main;

import android.content.Context;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import ftmk.bits.myapplication.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private final Context mContext;
    private int totalTabs;

    public SectionsPagerAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        mContext = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                GetJokeActivityFrag jokeActivityFrag = new GetJokeActivityFrag();
                return  jokeActivityFrag;
            case 1:
                GetUniversityFrag getUniversityFrag = new GetUniversityFrag();
                return getUniversityFrag;
            default: return null;
        }
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return totalTabs;
    }
}