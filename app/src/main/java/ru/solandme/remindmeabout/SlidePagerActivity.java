package ru.solandme.remindmeabout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import ru.solandme.remindmeabout.database.CongratulateDBHelper;
import ru.solandme.remindmeabout.fragments.SlidePageFragment;
import ru.solandme.remindmeabout.trasformers.ZoomOutPageTransformer;

public class SlidePagerActivity extends AppCompatActivity {
    Toolbar toolbar;
    RadioGroup rg_sex;
    RadioButton rb_for_her;
    RadioButton rb_for_him;

    CheckBox checkBox_sms;
    CheckBox checkBox_verse;
    CheckBox checkBox_favorite;


    PagerAdapter pagerAdapter;
    ViewPager slidePager;

    public static final String OFF = "0";
    public static final String ON = "1";
    public static final String FORHIM = "1";
    public static final String FORHER = "2";

    String filterFlag = OFF; // forHim - 1, forHer - 2, forAll - 0
    String smsFlag = OFF; // on - 1, off - 0
    String verseFlag = ON;
    String favoriteFlag = OFF;

    CongratulateDBHelper helper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);
        initToolBar();
        initView();
//        initAdView();
    }

    private void initAdView() {
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("C79BD6D360D092383E26BB030B13893D")
                .addTestDevice("E38C2A53C7B24FE9163CDCE72FFA277B")
                .build();
        if (adView != null) {
            adView.loadAd(adRequest);
        }
    }

    private void setMyAdapter(ViewPager slidePager) {

        pagerAdapter = new SlidePageAdapter(getSupportFragmentManager());
        if (slidePager != null) {
            //Можно выбрать другую анимацию, заменив PageTransformer на
            //slidePager.setPageTransformer(true, new DepthPageTransformer());
            slidePager.setPageTransformer(true, new ZoomOutPageTransformer());
            slidePager.setAdapter(pagerAdapter);
        }
    }

    @Override
    public void onBackPressed() {
        if (slidePager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            slidePager.setCurrentItem(0);
        }
    }

    private void initView() {
        slidePager = (ViewPager) findViewById(R.id.slidePager);
        setMyAdapter(slidePager);

        rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
        checkBox_sms = (CheckBox) findViewById(R.id.chb_sms);
        checkBox_verse = (CheckBox) findViewById(R.id.chb_verse);
        checkBox_favorite = (CheckBox) findViewById(R.id.chb_favorite);
        rb_for_her = (RadioButton) findViewById(R.id.rb_for_her);
        rb_for_him = (RadioButton) findViewById(R.id.rb_for_him);
        checkBox_verse = (CheckBox) findViewById(R.id.chb_verse);


        if (getIntent().getStringExtra("code").equals(Holiday.CODE_WOMANSDAY) ||
                getIntent().getStringExtra("code").equals(Holiday.CODE_MANSDAY)) {
            rb_for_her.setEnabled(false);
            rb_for_him.setEnabled(false);
        }

        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_for_all:
                        filterFlag = OFF;
                        setMyAdapter(slidePager);
                        break;
                    case R.id.rb_for_him:
                        filterFlag = FORHIM;
                        setMyAdapter(slidePager);
                        break;
                    case R.id.rb_for_her:
                        filterFlag = FORHER;
                        setMyAdapter(slidePager);
                        break;
                    default:
                        filterFlag = OFF;
                        setMyAdapter(slidePager);
                }
            }
        });
        checkBox_sms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    smsFlag = ON;
                    setMyAdapter(slidePager);
                } else {
                    smsFlag = OFF;
                    setMyAdapter(slidePager);
                }
            }
        });
        checkBox_verse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    verseFlag = ON;
                    setMyAdapter(slidePager);
                } else {
                    verseFlag = OFF;
                    setMyAdapter(slidePager);
                }
            }
        });
        checkBox_favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    favoriteFlag = ON;
                    setMyAdapter(slidePager);
                } else {
                    favoriteFlag = OFF;
                    setMyAdapter(slidePager);
                }
            }
        });


    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolBarSlideActivity);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getIntent().getStringExtra("holidayName"));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private List<Congratulation> getTextCongratulate() {
//        List<String> textCongratulate = new ArrayList<>();

        helper = new CongratulateDBHelper(getApplicationContext());

//        for (int i = 0; i < congratulations.size(); i++) {
//            textCongratulate.add(congratulations.get(i).getText());
//        }
//
//        if (textCongratulate.size() == 0) {
//            textCongratulate.add(getString(R.string.empty));
//        }
        return helper.getCongratulationsByCode(getIntent().getStringExtra("code"), smsFlag, verseFlag, filterFlag, favoriteFlag);
    }

    private class SlidePageAdapter extends FragmentStatePagerAdapter {
        List<Congratulation> congratulations = getTextCongratulate();

        public SlidePageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = new SlidePageFragment();
            Bundle args = new Bundle();
            args.putString(SlidePageFragment.ARG_TEXT, congratulations.get(position).getText());
            args.putInt(SlidePageFragment.ARG_POSITION, position + 1);
            args.putInt(SlidePageFragment.ARG_COUNT, getCount());
            args.putString(SlidePageFragment.ARG_VERSE, congratulations.get(position).getVerse());
            args.putString(SlidePageFragment.ARG_ID, congratulations.get(position).get_id());
            args.putString(SlidePageFragment.ARG_FAVORITE, congratulations.get(position).getFavorite());
            args.putString(SlidePageFragment.ARG_SEX, congratulations.get(position).getFilter());
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public int getCount() {
            return congratulations.size();
        }
    }
}
