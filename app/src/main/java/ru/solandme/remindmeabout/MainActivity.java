package ru.solandme.remindmeabout;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import ru.solandme.remindmeabout.adapters.MyPagerAdapter;

public class MainActivity extends AppCompatActivity {

    static final String HOLIDAY_OBJECT_KEY = "holiday";
    private static final int HOLIDAY_REQUEST = 1;
    public static final String TAG_ABOUT = "about";
    public static final String ADS_APP_ID = "ca-app-pub-8994936165518589~1639164557";
    public static final String IS_ACTION_EDIT_KEY = "isActionEdit";
    private TabLayout tabLayout;
    private InterstitialAd interstitialAd;
    private boolean shownAdvert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(getApplicationContext(), ADS_APP_ID);
        initAdInterstitial();

        initToolBar();
        initTabs();
        initPager();
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBarMainActivity);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main_menu, menu);
        MenuItem toggleItem = menu.findItem(R.id.menu_item_toggle_polling);
        if (RemindService.isServiceAlarmOn(getApplicationContext())) {
            toggleItem.setTitle(R.string.alarm_on);
            toggleItem.setIcon(R.drawable.ic_notifications);
        } else {
            toggleItem.setTitle(R.string.alarm_off);
            toggleItem.setIcon(R.drawable.ic_notifications_off);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                onBackPressed();
                break;
            case R.id.about_app_menu_item:
                new AboutAppDialog().show(getSupportFragmentManager(), TAG_ABOUT);
                break;
            case R.id.add_new_holiday:
                startActivityForResult(getNewHolidayIntent(), HOLIDAY_REQUEST);
                break;
            case R.id.menu_item_toggle_polling:
                RemindService.setServiceAlarm(getApplicationContext(), isShouldStartAlarm());
                Toast.makeText(getApplicationContext(), Boolean.toString(RemindService.isServiceAlarmOn(getApplicationContext())), Toast.LENGTH_SHORT).show();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().invalidateOptionsMenu();
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @NonNull
    private Intent getNewHolidayIntent() {
        return new Intent(getApplicationContext(), AddEditHolidayDialog.class)
                .putExtra(HOLIDAY_OBJECT_KEY, new Holiday(getString(R.string.new_holiday)))
                .putExtra(IS_ACTION_EDIT_KEY, false);
    }

    private boolean isShouldStartAlarm() {
        return !RemindService.isServiceAlarmOn(getApplicationContext());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == HOLIDAY_REQUEST) {
            switch (resultCode) {
                case RESULT_CANCELED:
                    Toast.makeText(this, R.string.canceled, Toast.LENGTH_SHORT).show();
                    break;
                case AddEditHolidayDialog.RESULT_SAVE:
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
                .addTestDevice("2C3C035C0493E83D9C1F0E40421EC41B")
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
