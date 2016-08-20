package ru.solandme.remindmeabout.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import ru.solandme.remindmeabout.Holiday;

public class HolidayDBHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "holidays.db";
    private static final int DATABASE_VERSION = 3; //для обновления базы названить assets/databases/holidays.db_upgrade_1-2.sql
    private static final String TABLE = "holidays";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_IMAGE_URI = "imageUri";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_DATA = "date";
    private static final String COLUMN_CODE = "code";


    public HolidayDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade(DATABASE_VERSION);
    }

    public List<Holiday> getHolidaysByCategory(String category) {
        List<Holiday> holidays = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + HolidayDBHelper.TABLE + " where " +
                HolidayDBHelper.COLUMN_CATEGORY + "=? ", new String[]{category});

        while (cursor.moveToNext()) {
            Holiday holiday = new Holiday();
            holiday.setId(cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
            holiday.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            holiday.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
            holiday.setImageUri(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_URI)));
            holiday.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));
            holiday.setDate(cursor.getLong(cursor.getColumnIndex(COLUMN_DATA)));
            holiday.setCode(cursor.getString(cursor.getColumnIndex(COLUMN_CODE)));
            holiday.setHoursLeft(getHoursForHolidayDate(holiday.getDate()));
            holidays.add(holiday);
        }
        cursor.close();
        Collections.sort(holidays, Holiday.daysOrdered);
        db.close();
        return holidays;
    }

    public void addHolidayToDB(Holiday holiday) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(HolidayDBHelper.COLUMN_ID, holiday.getId());
        cv.put(HolidayDBHelper.COLUMN_NAME, holiday.getName());
        cv.put(HolidayDBHelper.COLUMN_DESCRIPTION, holiday.getDescription());
        cv.put(HolidayDBHelper.COLUMN_IMAGE_URI, holiday.getImageUri());
        cv.put(HolidayDBHelper.COLUMN_CATEGORY, holiday.getCategory());
        cv.put(HolidayDBHelper.COLUMN_DATA, holiday.getDate());
        cv.put(HolidayDBHelper.COLUMN_CODE, holiday.getCode());

        db.insert(TABLE, null, cv);
        db.close();
        Log.e("DB_add", cv.toString());
    }

    public void replaceHolidayOnDB(Holiday holiday) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(HolidayDBHelper.COLUMN_ID, holiday.getId());
        cv.put(HolidayDBHelper.COLUMN_NAME, holiday.getName());
        cv.put(HolidayDBHelper.COLUMN_DESCRIPTION, holiday.getDescription());
        cv.put(HolidayDBHelper.COLUMN_IMAGE_URI, holiday.getImageUri());
        cv.put(HolidayDBHelper.COLUMN_CATEGORY, holiday.getCategory());
        cv.put(HolidayDBHelper.COLUMN_DATA, holiday.getDate());
        cv.put(HolidayDBHelper.COLUMN_CODE, holiday.getCode());

        db.update(TABLE, cv, COLUMN_ID + "=" + holiday.getId(), null);
        db.close();
        Log.e("DB_edit", holiday.getId() + "  " + cv.toString());
    }

    public boolean deleteHolidayFromDB(Holiday holiday) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE, COLUMN_ID + "=" + holiday.getId(), null) > 0;
    }

    private int getHoursForHolidayDate(Long holidaysDate) {
        Calendar todayCalendar = Calendar.getInstance();
        Calendar holidayCalendar = Calendar.getInstance();
        holidayCalendar.setTimeInMillis(holidaysDate);

        int todayYear = todayCalendar.get(Calendar.YEAR);
        int month = holidayCalendar.get(Calendar.MONTH);
        int day = holidayCalendar.get(Calendar.DAY_OF_MONTH);

        int daysInYear = todayCalendar.getActualMaximum(Calendar.DAY_OF_YEAR); //максимум дней в этом году
        holidayCalendar.set(todayYear, month, day);
        int hours = (int) ((holidayCalendar.getTimeInMillis() - todayCalendar.getTimeInMillis()) / 1000) / 3600;
        if ((hours < 0) && (hours > -48)){
            return hours;
        } else if (hours < 0){
            return daysInYear * 24 + hours;
        }
        return hours;
    }

}
