package ru.solandme.remindmeabout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import ru.solandme.remindmeabout.fragments.SlidePageFragment;
import ru.solandme.remindmeabout.trasformers.ZoomOutPageTransformer;

public class SlidePagerActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);

        ViewPager slidePager = (ViewPager) findViewById(R.id.slidePager);

        //Можно выбрать другую анимацию, заменив PageTransformer на
        //slidePager.setPageTransformer(true, new DepthPageTransformer());
        slidePager.setPageTransformer(true, new ZoomOutPageTransformer());
        PagerAdapter pagerAdapter = new SlidePageAdapter(getSupportFragmentManager(), getTextCongratulate());
        slidePager.setAdapter(pagerAdapter);
    }

    private List<String> getTextCongratulate() {
        List<String> data = new ArrayList<>();
        data.add(getResources().getString(R.string.lorem_ipsum));
        data.add(getResources().getString(R.string.lorem_ipsum2));
        return data;
    }

    private class SlidePageAdapter extends FragmentStatePagerAdapter {
        List<String> textData;

        public SlidePageAdapter(FragmentManager fm, List<String> textData) {
            super(fm);
            this.textData = textData;
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = new SlidePageFragment();
            Bundle args = new Bundle();
            args.putString(SlidePageFragment.ARG_TEXT, textData.get(position));
            args.putInt(SlidePageFragment.ARG_POSITION, position+1);
            args.putInt(SlidePageFragment.ARG_COUNT, getCount());
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public int getCount() {
            return textData.size();
        }
    }
}
