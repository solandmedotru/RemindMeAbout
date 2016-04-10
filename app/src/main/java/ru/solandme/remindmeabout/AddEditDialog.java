package ru.solandme.remindmeabout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


public class AddEditDialog extends AppCompatActivity {

    Holiday holiday;
    DBHelper dbHelper;

    JSONObject jsonObject;
    JSONObject newJsonObject;

    String holidayName;
    String holidayDescription;

    EditText add_holidayName;
    EditText add_holidayDescription;

    private DatePicker pickerDate;

    RadioGroup radioGroup;
    RadioButton radio_button_holidays;
    RadioButton radio_button_birthdays;
    RadioButton radio_button_events;

    Button btn_delete;

    public static final int RESULT_SAVE = 100;

    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_form);
        holiday = (Holiday) getIntent().getSerializableExtra("holiday");
        dbHelper = new DBHelper(getApplicationContext());
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

    private void initAdView() {
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        if (adView != null) {
            adView.loadAd(adRequest);
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }


    private void saveHoliday() {
        holiday.setName(add_holidayName.getText().toString());
        holiday.setDescription(add_holidayDescription.getText().toString());
        holiday.setDay(pickerDate.getDayOfMonth() + 1);
        holiday.setMonth(pickerDate.getMonth() + 1);


        if (getIntent().getBooleanExtra("Editing", true)) {
            dbHelper.replaceHolidayOnDB(holiday);
        }else {
            if (holiday.getCategory() == null) {
                holiday.setCategory("events");
            }
            holiday.setImageUri("june");
            dbHelper.addHolidayToDB(holiday);
        }
        dbHelper.close();

//        try {
//            holiday.setName(add_holidayName.getText().toString());
//            holiday.setDescription(add_holidayDescription.getText().toString());
//
//            jsonObject = new JSONObject(MyJSON.getData(getApplicationContext(), "holidays.json"));
//            JSONArray jsonArray = jsonObject.getJSONArray("holidays");
//
//            newJsonObject = new JSONObject();
//
//            newJsonObject
//                    .put("id", "0")
//                    .put("name", holidayName)
//                    .put("description", holidayDescription)
//                    .put("day", "0")
//                    .put("month", "0")
//                    .put("imageUri", "june")
//                    .put("category", "holidays");
//
//            jsonArray.put(newJsonObject);
//            JSONObject newJsonObject2 = new JSONObject();
//            newJsonObject2.put("holidays", jsonArray);
//
//            String text = newJsonObject2.toString();
//            MyJSON.saveData(getApplicationContext(), text, "holidays.json");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    private void initView() {
        add_holidayName = (EditText) findViewById(R.id.add_holiday_name);
        add_holidayDescription = (EditText) findViewById(R.id.add_holiday_description);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radio_button_holidays = (RadioButton) findViewById(R.id.radio_button_holidays);
        radio_button_birthdays = (RadioButton) findViewById(R.id.radio_button_birthdays);
        radio_button_events = (RadioButton) findViewById(R.id.radio_button_events);
        pickerDate = (DatePicker) findViewById(R.id.pickerdate);

        btn_delete = (Button) findViewById(R.id.btn_delete);

        if (pickerDate != null) {
            pickerDate.setCalendarViewShown(false);
        }
        Calendar today = Calendar.getInstance();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_holidays:
                        holiday.setCategory("holidays");
                        break;
                    case R.id.radio_button_birthdays:
                        holiday.setCategory("birthdays");
                        break;
                    case R.id.radio_button_events:
                        holiday.setCategory("events");
                        break;
                    default:
                        holiday.setCategory("events");
                        break;
                }
            }
        });

        if (getIntent().getBooleanExtra("Editing", true)) {
            add_holidayName.setText(holiday.getName());
            add_holidayDescription.setText(holiday.getDescription());
            switch (holiday.getCategory()){
                case "holidays":
                    radio_button_holidays.setChecked(true);
                    break;
                case "birthdays":
                    radio_button_birthdays.setChecked(true);
                    break;
                case "events":
                    radio_button_events.setChecked(true);
                    break;
            }

            pickerDate.init(today.get(Calendar.YEAR),holiday.getMonth()-1, holiday.getDay(), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                }
            });
        } else {
            btn_delete.setVisibility(View.GONE);
            pickerDate.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                    today.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                        }
                    });
        }
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
            case R.id.btn_delete:
                dbHelper.deleteHolidayFromDB(holiday);
                setResult(RESULT_SAVE);
                finish();
                break;
            default:
                break;
        }

    }
}
