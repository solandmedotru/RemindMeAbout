package ru.solandme.remindmeabout.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ru.solandme.remindmeabout.fragments.BirthdayFragment;
import ru.solandme.remindmeabout.fragments.EventFragment;
import ru.solandme.remindmeabout.fragments.HolidayFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int numOfTabs;

    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new HolidayFragment();
            case 1:
                return new BirthdayFragment();
            case 2:
                return new EventFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
