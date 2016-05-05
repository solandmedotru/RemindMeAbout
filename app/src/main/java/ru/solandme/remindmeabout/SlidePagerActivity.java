package ru.solandme.remindmeabout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import ru.solandme.remindmeabout.fragments.SlidePageFragment;
import ru.solandme.remindmeabout.trasformers.ZoomOutPageTransformer;

public class SlidePagerActivity extends FragmentActivity {

    private ViewPager slidePager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);

        slidePager = (ViewPager) findViewById(R.id.slidePager);

        //Можно выбрать другую анимацию, заменив PageTransformer на
        //slidePager.setPageTransformer(true, new DepthPageTransformer());
        slidePager.setPageTransformer(true, new ZoomOutPageTransformer());
        pagerAdapter = new SlidePageAdapter(getSupportFragmentManager());
        slidePager.setAdapter(pagerAdapter);
    }

    private class SlidePageAdapter extends FragmentStatePagerAdapter {
        public SlidePageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new SlidePageFragment();
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
