package ru.solandme.remindmeabout;

import android.content.Intent;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.solandme.remindmeabout.adapters.PagerAdapter;

public class MainActivity extends AppCompatActivity {

    public static final String HOLIDAY = "holiday";
    private static final int HOLIDAY_REQUEST = 1;
    Toolbar toolbar;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext())
                .getBoolean("installed", false)) {
            PreferenceManager.getDefaultSharedPreferences(
                    getApplicationContext())
                    .edit().putBoolean("installed", true).commit();
            copyAssetFolder(getAssets(), "images", getApplicationContext().getFilesDir().getPath() + "/images");
        }

        initToolBar();
        initTabs();
        initPager();
    }

    private static boolean copyAssetFolder(AssetManager assetManager,
                                           String fromAssetPath, String toPath) {
        try {
            String[] files = assetManager.list(fromAssetPath);
            new File(toPath).mkdirs();
            boolean res = true;
            for (String file : files)
                if (file.contains("."))
                    res &= copyAsset(assetManager,
                            fromAssetPath + "/" + file,
                            toPath + "/" + file);
                else
                    res &= copyAssetFolder(assetManager,
                            fromAssetPath + "/" + file,
                            toPath + "/" + file);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean copyAsset(AssetManager assetManager,
                                     String fromAssetPath, String toPath) {
        InputStream in;
        OutputStream out;
        try {
            in = assetManager.open(fromAssetPath);
            new File(toPath).createNewFile();
            out = new FileOutputStream(toPath);
            copyFile(in, out);
            in.close();
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }


    private void initPager() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        final PagerAdapter pagerAdapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        if (viewPager != null) {
            viewPager.setAdapter(pagerAdapter);
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
        if (toolbar != null) {
            toolbar.setTitle(R.string.app_name);
        }

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });
        toolbar.inflateMenu(R.menu.toolbar_main_menu);
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
                    Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
                    break;
                case AddEditDialog.RESULT_SAVE:
                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.about_app_menu_item:
                Toast.makeText(getApplicationContext(), item.getTitle().toString(), Toast.LENGTH_LONG).show();
                break;
            case R.id.add_new_holiday:
                Holiday holiday = new Holiday();
                holiday.setName(getString(R.string.new_holiday));
                Intent intent = new Intent(getApplicationContext(), AddEditDialog.class);
                intent.putExtra(HOLIDAY, holiday);
                intent.putExtra("Editing", false);
                startActivityForResult(intent, HOLIDAY_REQUEST);
                break;
        }
        return true;
    }
}
