package ru.solandme.remindmeabout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AddDialog extends AppCompatActivity {

    JSONObject jsonObject;
    JSONObject newJsonObject;
    String holidayName;
    String holidayDescription;
    EditText add_holidayName;
    EditText add_holidayDescription;
    public static final int RESULT_SAVE = 100;

    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_form);
        Holiday holiday = (Holiday) getIntent().getSerializableExtra("holiday");
        setTitle(holiday.getName());
        initView();

        initAdView();

        initAdInterstitial();
    }

    private void initAdInterstitial() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.fullscreen_ad_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                saveHoliday();
            }
        });

        requestNewInterstitial();
    }

    private void saveHoliday() {
        try {
            holidayName = add_holidayName.getText().toString();
            holidayDescription = add_holidayDescription.getText().toString();
            jsonObject = new JSONObject(MyJSON.getData(getApplicationContext(), "holidays.json"));
            JSONArray jsonArray = jsonObject.getJSONArray("holidays");

            newJsonObject = new JSONObject();

            newJsonObject
                    .put("id", "0")
                    .put("name", holidayName)
                    .put("description", holidayDescription)
                    .put("day", "0")
                    .put("month", "0")
                    .put("imageUri", "june")
                    .put("category", "holidays");

            jsonArray.put(newJsonObject);
            JSONObject newJsonObject2 = new JSONObject();
            newJsonObject2.put("holidays", jsonArray);

            String text = newJsonObject2.toString();
            MyJSON.saveData(getApplicationContext(), text, "holidays.json");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void initAdView() {
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().
                addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        if (adView != null) {
            adView.loadAd(adRequest);
        }
    }

    private void initView() {
        add_holidayName = (EditText) findViewById(R.id.add_holiday_name);
        add_holidayDescription = (EditText) findViewById(R.id.add_holiday_description);
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }


    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_save:
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    saveHoliday();
                }
                setResult(RESULT_SAVE);
                finish();
                break;
            case R.id.btn_cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
            default:
                break;
        }

    }
}
