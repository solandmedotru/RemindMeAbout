package ru.solandme.remindmeabout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AddEditDialog extends AppCompatActivity{

    public static final int RESULT_SAVE = 100;
    private static int LOAD_IMAGE_RESULTS = 1;
    private static final int THUMBSIZE = 80;
    private static final String TAG = "Holidays Log:";
    private Holiday holiday;
    private DBHelper dbHelper;
    private EditText add_holidayName;
    private EditText add_holidayDescription;
    private RadioGroup radioGroup;
    private RadioButton radio_button_holidays;
    private RadioButton radio_button_birthdays;
    private RadioButton radio_button_events;
    private ImageView edit_image_holiday;
    private Button btn_delete;
    private Button btn_data;
    private Calendar calendar;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private InterstitialAd mInterstitialAd;

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar = Calendar.getInstance();
            calendar.set(year,monthOfYear,dayOfMonth);
            holiday.setDate(calendar.getTimeInMillis());
            btn_data.setText(dateFormat.format(holiday.getDate()));
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_form);
        holiday = (Holiday) getIntent().getSerializableExtra(MainActivity.HOLIDAY);
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
                saveHoliday(holiday);
            }
        });

        requestNewInterstitial();
    }

    private void initAdView() {
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("C79BD6D360D092383E26BB030B13893D")
                .build();
        if (adView != null) {
            adView.loadAd(adRequest);
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("C79BD6D360D092383E26BB030B13893D")
                .build();
        mInterstitialAd.loadAd(adRequest);
    }


    private void saveHoliday(Holiday holiday) {
        holiday.setName(add_holidayName.getText().toString());
        holiday.setDescription(add_holidayDescription.getText().toString());

        if (holiday.getImageUri() == null) {
            holiday.setImageUri("ic_h.png"); //устанавливаем иконку по умолчанию если не задана
        }
        //задаем категорию праздника
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radio_button_holidays:
                holiday.setCategory("holidays");
                break;
            case R.id.radio_button_birthdays:
                holiday.setCategory("birthdays");
                break;
            case R.id.radio_button_events:
                holiday.setCategory("events");
                break;
        }

        if (getIntent().getBooleanExtra("Editing", true)) {
            dbHelper.replaceHolidayOnDB(holiday);
        } else {
            dbHelper.addHolidayToDB(holiday);
        }
        dbHelper.close();
    }

    private void initView() {
        add_holidayName = (EditText) findViewById(R.id.add_holiday_name);
        add_holidayDescription = (EditText) findViewById(R.id.add_holiday_description);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radio_button_holidays = (RadioButton) findViewById(R.id.radio_button_holidays);
        radio_button_birthdays = (RadioButton) findViewById(R.id.radio_button_birthdays);
        radio_button_events = (RadioButton) findViewById(R.id.radio_button_events);
        edit_image_holiday = (ImageView) findViewById(R.id.edit_image_holiday);

        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_data = (Button) findViewById(R.id.btn_data);

        if(holiday.getDate() == null){
            calendar = Calendar.getInstance();
            holiday.setDate(calendar.getTimeInMillis());
        }

        btn_data.setText(dateFormat.format(holiday.getDate()));

        if (getIntent().getBooleanExtra("Editing", true)) {
            add_holidayName.setText(holiday.getName());
            add_holidayDescription.setText(holiday.getDescription());
            Bitmap bmp = BitmapFactory.decodeFile(getApplicationContext().getFilesDir().getPath() + "/images/" + holiday.getImageUri());
            edit_image_holiday.setImageBitmap(bmp);

            switch (holiday.getCategory()) {
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
        } else {
            btn_delete.setVisibility(View.GONE);
        }
    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_save:
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    saveHoliday(holiday);
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
            case R.id.edit_image_holiday:
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, LOAD_IMAGE_RESULTS);
                break;
            case R.id.btn_data:
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(holiday.getDate());
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(picturePath), THUMBSIZE, THUMBSIZE);
                edit_image_holiday.setImageBitmap(thumbImage);
                saveImage(thumbImage);
            }
        }
    }

    private void saveImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,"Error creating media file, check storage permissions: ");
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(getApplicationContext().getFilesDir().getPath()
                + "/images");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.ROOT).format(new Date());
        String mImageName = "thumbImage_" + timeStamp + ".png";
        holiday.setImageUri(mImageName);
        return new File(mediaStorageDir.getPath() + File.separator + mImageName);
    }

}
