package ru.solandme.remindmeabout;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class CongratulateDBHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "congratulations.db";
    private static final int DATABASE_VERSION = 1;


    public static final String TABLE = "congratulation";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_TEXT = "text";


    public CongratulateDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public List<String> getCongratulationsByCode(String code) {
        List<String> congratulations = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + TABLE + " where " +
                COLUMN_CODE + "=? ", new String[]{code});

        while (cursor.moveToNext()) {
            congratulations.add(cursor.getString(1));
        }
        cursor.close();
        db.close();
        return congratulations;
    }

//    public void addCongratulationsToDB(Holiday holiday) {
//        SQLiteDatabase db = getWritableDatabase();
//
//        ContentValues cv = new ContentValues();
//        cv.put(HolidayDBHelper.COLUMN_NAME, holiday.getName());
//        cv.put(HolidayDBHelper.COLUMN_DESCRIPTION, holiday.getDescription());
//        cv.put(HolidayDBHelper.COLUMN_IMAGE_URI, holiday.getImageUri());
//        cv.put(HolidayDBHelper.COLUMN_CATEGORY, holiday.getCategory());
//        cv.put(HolidayDBHelper.COLUMN_DATA, holiday.getDate());
//
//        db.insert(TABLE, null, cv);
//        db.close();
//        Log.e("DB_add", cv.toString());
//    }

//    public void replaceHolidayOnDB(Holiday holiday) {
//        SQLiteDatabase db = getWritableDatabase();
//
//        ContentValues cv = new ContentValues();
//        cv.put(HolidayDBHelper.COLUMN_NAME, holiday.getName());
//        cv.put(HolidayDBHelper.COLUMN_DESCRIPTION, holiday.getDescription());
//        cv.put(HolidayDBHelper.COLUMN_IMAGE_URI, holiday.getImageUri());
//        cv.put(HolidayDBHelper.COLUMN_CATEGORY, holiday.getCategory());
//        cv.put(HolidayDBHelper.COLUMN_DATA, holiday.getDate());
//
//        db.update(TABLE, cv, COLUMN_ID + "=" + holiday.getId(), null);
//        db.close();
//        Log.e("DB_edit", holiday.getId() + "  " + cv.toString());
//    }

//    public boolean deleteHolidayFromDB(Holiday holiday) {
//        SQLiteDatabase db = getWritableDatabase();
//        return db.delete(TABLE, COLUMN_ID + "=" + holiday.getId(), null) > 0;
//    }

}
