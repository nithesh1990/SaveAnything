package napps.saveanything.view.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import napps.saveanything.view.Fragments.Fraggment;

/**
 * Created by nithesh on 5/6/2016.
 */
public class PageAdapter extends FragmentStatePagerAdapter {

    List<Fraggment> fragmentList;
    public PageAdapter(FragmentManager fm, List<Fraggment> fragmentList) {
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