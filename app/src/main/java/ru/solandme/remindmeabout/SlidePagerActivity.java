package ru.solandme.remindmeabout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.List;

import ru.solandme.remindmeabout.adapters.SmartFragmentStatePagerAdapter;
import ru.solandme.remindmeabout.database.CongratulateDBHelper;
import ru.solandme.remindmeabout.fragments.SlidePageFragment;
import ru.solandme.remindmeabout.trasformers.ZoomOutPageTransformer;

public class SlidePagerActivity extends AppCompatActivity {
    Toolbar toolbar;

    CheckBox checkBoxSms;
    CheckBox checkBoxVerse;
    CheckBox checkBoxFavorite;

    CheckBox checkBoxForHim;
    CheckBox checkBoxForHer;
    CheckBox checkBoxForAll;

    private InterstitialAd interstitialAd;

    View fragmentFilter;


    SmartFragmentStatePagerAdapter pagerAdapter;
    ViewPager slidePager;

    public List<Congratulation> congratulations;


    public static final String OFF = "0";
    public static final String ON = "1";
    public static final String FORHIM = "1";
    public static final String FORHER = "2";
    public static final String FORALL = "0";



    String smsFlag = OFF; // on - 1, off - 0
    String verseFlag = ON; // on - 1, off - 0
    String favoriteFlag = OFF; // on - 1, off - 0
    String forHimFlag = OFF;
    String forHerFlag = OFF;
    String forAllFlag = ON;

    CongratulateDBHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);
        initAdInterstitial();

        initToolBar();
        initView();
//        initAdView();

    }

//    private void initAdView() {
//        AdView adView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .addTestDevice("C79BD6D360D092383E26BB030B13893D")
//                .addTestDevice("E38C2A53C7B24FE9163CDCE72FFA277B")
//                .build();
//        if (adView != null) {
//            adView.loadAd(adRequest);
//        }
//    }

    private void initAdInterstitial() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.fullscreen_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                share_text();
            }
        });

        requestNewInterstitial();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("E38C2A53C7B24FE9163CDCE72FFA277B")
                .addTestDevice("C79BD6D360D092383E26BB030B13893D")
                .build();
        interstitialAd.loadAd(adRequest);
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

        fragmentFilter = findViewById(R.id.fragment_filter);

        slidePager = (ViewPager) findViewById(R.id.slidePager);
        setMyAdapter(slidePager);

        checkBoxSms = (CheckBox) findViewById(R.id.chb_sms);
        checkBoxVerse = (CheckBox) findViewById(R.id.chb_verse);
        checkBoxFavorite = (CheckBox) findViewById(R.id.chb_favorite);

        checkBoxForHim = (CheckBox) findViewById(R.id.chb_for_him);
        checkBoxForHer = (CheckBox) findViewById(R.id.chb_for_her);
        checkBoxForAll = (CheckBox) findViewById(R.id.chb_for_all);


        if (getIntent().getStringExtra("code").equals(Holiday.CODE_WOMANSDAY) ||
                getIntent().getStringExtra("code").equals(Holiday.CODE_MANSDAY)) {
            checkBoxForHer.setEnabled(false);
            checkBoxForHim.setEnabled(false);
            checkBoxForAll.setEnabled(false);
        }

        checkBoxForHim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    forHimFlag = ON;
                    setMyAdapter(slidePager);
                } else {
                    forHimFlag = OFF;
                    setMyAdapter(slidePager);
                }
            }
        });

        checkBoxForHer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    forHerFlag = ON;
                    setMyAdapter(slidePager);
                } else {
                    forHerFlag = OFF;
                    setMyAdapter(slidePager);
                }
            }
        });

        checkBoxForAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    forAllFlag = ON;
                    setMyAdapter(slidePager);
                } else {
                    forAllFlag = OFF;
                    setMyAdapter(slidePager);
                }
            }
        });
        checkBoxSms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        checkBoxVerse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        checkBoxFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

    private void setMyAdapter(ViewPager slidePager) {

        pagerAdapter = new SlidePageAdapter(getSupportFragmentManager());
        if (slidePager != null) {
            //Можно выбрать другую анимацию, заменив PageTransformer на
            //slidePager.setPageTransformer(true, new DepthPageTransformer());
            slidePager.setPageTransformer(true, new ZoomOutPageTransformer());
            slidePager.setAdapter(pagerAdapter);
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
        return helper.getCongratulationsByCode(
                getIntent().getStringExtra("code"),
                smsFlag,
                verseFlag,
                favoriteFlag,
                forHimFlag,
                forHerFlag,
                forAllFlag);
    }

    private class SlidePageAdapter extends SmartFragmentStatePagerAdapter {


        public SlidePageAdapter(FragmentManager fm) {
            super(fm);
            congratulations = getTextCongratulate();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_slide_activity_menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_share:
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                } else {
                    share_text();
                }
                break;
            case R.id.action_filter:
                if (fragmentFilter.getVisibility() == View.GONE) {
                    fragmentFilter.setVisibility(View.VISIBLE);
                } else {
                    fragmentFilter.setVisibility(View.GONE);
                    setMyAdapter(slidePager);
                }
                break;
            case R.id.about_app_menu_item:
                Toast.makeText(getApplicationContext(), item.getTitle().toString(), Toast.LENGTH_LONG).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    public void share_text() {
        TextView currentText = (TextView) pagerAdapter
                .getRegisteredFragment(slidePager.getCurrentItem())
                .getView().findViewById(R.id.text_container);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, currentText.getText().toString());
        String chooserTitle = getString(R.string.chooser_title);
        Intent chooserIntent = Intent.createChooser(intent, chooserTitle);
        startActivity(chooserIntent);
    }
}
