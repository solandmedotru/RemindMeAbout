package ru.solandme.remindmeabout;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.solandme.remindmeabout.adapters.MyPagerAdapter;

public class MainActivity extends AppCompatActivity {

    static final String HOLIDAY = "holiday";
    private static final int HOLIDAY_REQUEST = 1;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private InterstitialAd interstitialAd;
    private boolean shownAdvert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-8994936165518589~1639164557");
        initAdInterstitial();

        initToolBar();
        initTabs();
        initPager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                onBackPressed();
                break;
//            case R.id.about_app_menu_item:
//                Toast.makeText(getApplicationContext(), item.getTitle().toString(), Toast.LENGTH_LONG).show();
//                break;
            case R.id.add_new_holiday:
                Holiday holiday = new Holiday();
                holiday.setName(getString(R.string.new_holiday));
                Intent intent = new Intent(getApplicationContext(), AddEditDialog.class);
                intent.putExtra(HOLIDAY, holiday);
                intent.putExtra("isActionEdit", false);
                startActivityForResult(intent, HOLIDAY_REQUEST);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    private void initPager() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        final MyPagerAdapter myPagerAdapter = new MyPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        if (viewPager != null) {
            viewPager.setAdapter(myPagerAdapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        }
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (viewPager != null) {
                    viewPager.setCurrentItem(tab.getPosition());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }


    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolBarMainActivity);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void initTabs() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (tabLayout != null) {
            tabLayout.addTab(tabLayout.newTab().setText(R.string.Holidays));
            tabLayout.addTab(tabLayout.newTab().setText(R.string.Birthdays));
            tabLayout.addTab(tabLayout.newTab().setText(R.string.Events));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == HOLIDAY_REQUEST) {
            switch (resultCode) {
                case RESULT_CANCELED:
                    Toast.makeText(this, R.string.canceled, Toast.LENGTH_SHORT).show();
                    break;
                case AddEditDialog.RESULT_SAVE:
                    Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (shownAdvert) {
            super.onBackPressed();
        } else {
            if (interstitialAd.isLoaded()) {
                interstitialAd.show();
            } else super.onBackPressed();
        }
    }

    private void initAdInterstitial() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.fullscreen_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                shownAdvert = true;
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

//    private void updateSubtitle(){
//
//        CongratulateDBHelper congratulateDBHelper = new CongratulateDBHelper(getApplicationContext());
//        String subtitle = "В базе "+ congratulateDBHelper.getCountAll()+" поздравлений";
//        if (getSupportActionBar() != null){
//            getSupportActionBar().setSubtitle(subtitle);
//        }
//    }


}
