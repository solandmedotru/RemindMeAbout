package ru.solandme.remindmeabout;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.solandme.remindmeabout.database.CongratulateDBHelper;
import ru.solandme.remindmeabout.database.HolidayDBHelper;

public class AddEditHolidayDialog extends AppCompatActivity implements View.OnClickListener {

    static final int RESULT_SAVE = 100;
    public static final int REQUEST_READ_PERMISION_CODE = 2;
    private static int LOAD_IMAGE_RESULTS = 1;
    private static final int THUMBSIZE = 80;
    private static final String TAG = "Holidays Log:";
    private Holiday holiday;
    private HolidayDBHelper holidayDbHelper;
    private EditText addHolidayName;
    private EditText addHolidayDescription;
    private RadioGroup radioGroup;
    private ImageView editImageHoliday;
    private Button btn_date;
    private Calendar calendar;
    private Button btn_save;
    private Button btn_cancel;
    private Button btn_delete;

    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_form);
        holiday = (Holiday) getIntent().getSerializableExtra(MainActivity.HOLIDAY_OBJECT_KEY);
        holidayDbHelper = new HolidayDBHelper(getApplicationContext());
        initToolBar();
        initView();

        initAdInterstitial();

    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBarAddEditActivity);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            if (getIntent().getBooleanExtra("isActionEdit", true)) {
                getSupportActionBar().setTitle(holiday.getName());
            } else {
                getSupportActionBar().setTitle(R.string.add_new_holiday);
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void initAdInterstitial() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.fullscreen_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                saveHoliday(holiday);
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

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            holiday.setDate(calendar.getTimeInMillis());
            btn_date.setText(SimpleDateFormat.getDateInstance().format(holiday.getDate()));

            Log.i(TAG, holiday.getDate().toString());
        }
    };


    private void saveHoliday(Holiday holiday) {
        holiday.setName(addHolidayName.getText().toString());
        holiday.setDescription(addHolidayDescription.getText().toString());

        if (holiday.getImageUri() == null) {
            holiday.setImageUri("ic_h.png"); //устанавливаем иконку по умолчанию если не задана
        }
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radio_button_holidays:
                holiday.setCategory(Holiday.CATEGORY_HOLIDAY);
                break;
            case R.id.radio_button_birthdays:
                holiday.setCategory(Holiday.CATEGORY_BIRTHDAY);
                break;
            case R.id.radio_button_events:
                holiday.setCategory(Holiday.CATEGORY_EVENT);
                break;
        }

        if (getIntent().getBooleanExtra("isActionEdit", true)) {
            holidayDbHelper.replaceHolidayOnDB(holiday);
        } else {
            holiday.setCode(generateCode());
            holidayDbHelper.addHolidayToDB(holiday);
        }
        holidayDbHelper.close();
    }

    private String generateCode() {
        if (holiday.getCategory().equals(Holiday.CATEGORY_HOLIDAY) || holiday.getCategory().equals(Holiday.CATEGORY_EVENT)) {
            return holiday.getCategory() + holiday.getDate();
        } else {
            return Holiday.CATEGORY_BIRTHDAY;
        }
    }

    private void initView() {
        addHolidayName = (EditText) findViewById(R.id.add_holiday_name);
        addHolidayDescription = (EditText) findViewById(R.id.add_holiday_description);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        RadioButton radio_button_holidays = (RadioButton) findViewById(R.id.radio_button_holidays);
        RadioButton radio_button_birthdays = (RadioButton) findViewById(R.id.radio_button_birthdays);
        RadioButton radio_button_events = (RadioButton) findViewById(R.id.radio_button_events);
        editImageHoliday = (ImageView) findViewById(R.id.edit_image_holiday);

        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_date = (Button) findViewById(R.id.btn_data);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_date.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        checkFieldForEmptyValue();

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkFieldForEmptyValue();
            }
        };

        addHolidayName.addTextChangedListener(textWatcher);
        addHolidayDescription.addTextChangedListener(textWatcher);

        if (holiday.getDate() == null) {
            calendar = Calendar.getInstance();
            holiday.setDate(calendar.getTimeInMillis());
        }
        btn_date.setText(SimpleDateFormat.getDateInstance().format(holiday.getDate()));


        if (getIntent().getBooleanExtra("isActionEdit", true)) {
            addHolidayName.setText(holiday.getName());
            addHolidayDescription.setText(holiday.getDescription());
            Bitmap bmp = BitmapFactory.decodeFile(getApplicationContext().getFilesDir().getPath() + "/images/" + holiday.getImageUri());
            editImageHoliday.setImageBitmap(bmp);

            switch (holiday.getCategory()) {
                case Holiday.CATEGORY_HOLIDAY:
                    if (radio_button_holidays != null) {
                        radio_button_holidays.setChecked(true);
                    }
                    break;
                case Holiday.CATEGORY_BIRTHDAY:
                    if (radio_button_birthdays != null) {
                        radio_button_birthdays.setChecked(true);
                    }
                    break;
                case Holiday.CATEGORY_EVENT:
                    if (radio_button_events != null) {
                        radio_button_events.setChecked(true);
                    }
                    break;
            }
        } else {
            if (btn_delete != null) {
                btn_delete.setVisibility(View.GONE);
            }
        }
    }

    private void checkFieldForEmptyValue() {
        if (addHolidayName.getText().toString().length() > 0 && (addHolidayDescription.getText().toString().length() > 0)) {
            btn_save.setClickable(true);
            btn_save.setEnabled(true);
            btn_save.setBackground(getResources().getDrawable(R.drawable.bg_button_enabled));
        } else {
            btn_save.setClickable(false);
            btn_save.setEnabled(false);
            btn_save.setBackground(getResources().getDrawable(R.drawable.bg_button_desabled));
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_save:
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
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
                CongratulateDBHelper congratulateDBHelper = new CongratulateDBHelper(getApplicationContext());


                List<Congratulation> congratulations;
                congratulations = congratulateDBHelper.getAllCongratulationsByCode(holiday.getCode());

                for (Congratulation c : congratulations) {
                    congratulateDBHelper.deleteCongratulationFromDB(c);
                }

                holidayDbHelper.deleteHolidayFromDB(holiday);
                setResult(RESULT_SAVE);
                finish();
                break;
            case R.id.edit_image_holiday:

                int hasReadPermission = ContextCompat.checkSelfPermission(AddEditHolidayDialog.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE);
                boolean shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(AddEditHolidayDialog.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE);

                if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
                    if (shouldShow) {
                        showCustomDialog(getString(R.string.permission_text),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts("package", getPackageName(), null));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                });

                    } else {
                        ActivityCompat.requestPermissions(AddEditHolidayDialog.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_READ_PERMISION_CODE);
                    }
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, LOAD_IMAGE_RESULTS);
                }

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

    private void showCustomDialog(String message, DialogInterface.OnClickListener listner) {
        new AlertDialog.Builder(AddEditHolidayDialog.this)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, listner)
                .setNegativeButton(android.R.string.cancel, null)
                .create()
                .show();
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
                editImageHoliday.setImageBitmap(thumbImage);
                saveImage(thumbImage);
            }
        }
    }

    private void saveImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG, "Error creating media file, check storage permissions: ");
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

    @Nullable
    private File getOutputMediaFile() {
        File mediaStorageDir = new File(getApplicationContext().getFilesDir().getPath()
                + "/images");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.ROOT).format(new Date());
        String mImageName = "thumbImage_" + timeStamp + ".png";
        holiday.setImageUri(mImageName);
        return new File(mediaStorageDir.getPath() + File.separator + mImageName);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_READ_PERMISION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, LOAD_IMAGE_RESULTS);
                } else {
                    Toast.makeText(this, R.string.permission_text, Toast.LENGTH_LONG).show();
                }
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }
}
