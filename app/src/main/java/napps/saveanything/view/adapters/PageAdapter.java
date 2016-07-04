package napps.saveanything.view.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import napps.saveanything.view.Fragments.CustomFragment;

/**
 * Created by nithesh on 5/6/2016.
 */
/*
    Reason for choosing FragmentStatePagerAdapter over fragmentPagerAdapter
    1. FragmentPagerAdapter is best for use when there are a handful of typically more static fragments to be paged through, such as a set of tabs.
        The fragment of each page the user visits will be kept in memory, though its view hierarchy may be destroyed when not visible.
    2. FragmentStatePagerAdapter is more useful when there are a large number of pages, working more like a list view. When pages are not visible to the user, their entire fragment may be destroyed, only keeping the saved state of that fragment.
        This allows the pager to hold on to much less memory associated with each visited page as compared to FragmentPagerAdapter at the cost of potentially more overhead when switching between pages.
    3. In our case we have image fragment which holds lot of memory but currently we have only 2 fragments
    4. FSPA anyway loads fragments next to visible fragments. So in this case it won't make much difference.
    5. In terms of futuristic design, FSPA is best.

 */
 /*
    For example ClipFragment is 2 level subclass of Fragment.
    In getItem of adapter we should have returned corresponding fragment instance but we are returning superclass instance
    This is because there are no independent subclass methods that need to be called for Fragment lifecycle.
    All the subclass methods are used by Fragment class lifecycle methods. So the getItem only takes Fragment's instance
 */
public class PageAdapter extends FragmentStatePagerAdapter {

    List<CustomFragment> fragmentList;
    public PageAdapter(FragmentManager fm, List<CustomFragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentList.get(position).getTitle();
    }
}
