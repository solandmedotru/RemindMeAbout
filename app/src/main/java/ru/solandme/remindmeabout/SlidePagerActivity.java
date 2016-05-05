package ru.solandme.remindmeabout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import ru.solandme.remindmeabout.fragments.SlidePageFragment;
import ru.solandme.remindmeabout.trasformers.ZoomOutPageTransformer;

public class SlidePagerActivity extends FragmentActivity {
    Toolbar toolbar;

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
        initToolBar();


    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolBarSlideActivity);
        if (toolbar != null) {
            toolbar.setTitle(R.string.app_name);
        }
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });
        toolbar.inflateMenu(R.menu.toolbar_slide_activity_menu);
    }

    private List<String> getTextCongratulate() {
        List<String> textCongratulate;
        CongratulateDBHelper helper = new CongratulateDBHelper(getApplicationContext());
        textCongratulate = helper.getCongratulationsByCode(getIntent().getStringExtra("code"));
        if (textCongratulate.size() == 0) {
            textCongratulate.add("Empty");
        }
        return textCongratulate;
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.about_app_menu_item:
                Toast.makeText(getApplicationContext(), item.getTitle().toString(), Toast.LENGTH_LONG).show();
                break;
            case R.id.add_new_congratulate:
                Toast.makeText(getApplicationContext(), item.getTitle().toString(), Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }
}
