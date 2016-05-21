package ru.solandme.remindmeabout.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import ru.solandme.remindmeabout.Congratulation;

public class CongratulateDBHelper extends SQLiteAssetHelper {

    public static final String DATABASE_NAME = "congratulations.db";
    private static final int DATABASE_VERSION = 1;


    public static final String TABLE = "congratulation";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_SMS = "sms";
    public static final String COLUMN_VERSE = "verse";
    public static final String COLUMN_FILTER = "filter";
    public static final String COLUMN_FAVORITE = "favorite";

    public CongratulateDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade(DATABASE_VERSION);
    }

    public List<Congratulation> getCongratulationsByCode(String code,
                                                         String sms,
                                                         String verse,
                                                         String favorite,
                                                         String forHim,
                                                         String forHer,
                                                         String forAll) {
        List<Congratulation> congratulations = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor;

        // 1 - 0 - 0
        if(forHim.equals("1") & forHer.equals("0") & forAll.equals("0")){
            String filter = "1";
            cursor = db.rawQuery("select * from " +
                    TABLE + " where " +
                    COLUMN_CODE + "=?" + " and " +
                    COLUMN_SMS + "=?" + " and " +
                    COLUMN_VERSE + "=?" + " and " +
                    COLUMN_FAVORITE + "=?" + " and " +
                    COLUMN_FILTER + "=?", new String[]{code, sms, verse, favorite, filter});
        }
        // 0 - 1 - 0
        else if(forHim.equals("0") & forHer.equals("1") & forAll.equals("0")){
            String filter = "2";
            cursor = db.rawQuery("select * from " +
                    TABLE + " where " +
                    COLUMN_CODE + "=?" + " and " +
                    COLUMN_SMS + "=?" + " and " +
                    COLUMN_VERSE + "=?" + " and " +
                    COLUMN_FAVORITE + "=?" + " and " +
                    COLUMN_FILTER + "=?", new String[]{code, sms, verse, favorite, filter});
        }
        // 1 - 1 - 0
        else if(forHim.equals("1") & forHer.equals("1") & forAll.equals("0")){
            String filter1 = "1";
            String filter2 = "2";
            cursor = db.rawQuery("select * from " +
                    TABLE + " where " +
                    COLUMN_CODE + "=?" + " and " +
                    COLUMN_SMS + "=?" + " and " +
                    COLUMN_VERSE + "=?" + " and " +
                    COLUMN_FAVORITE + "=?" + " and (" +
                    COLUMN_FILTER + "=?" + " or " +
                    COLUMN_FILTER + "=?)", new String[]{code, sms, verse, favorite, filter1, filter2});
        }
        // 1 - 1 - 1
        else if(forHim.equals("1") & forHer.equals("1") & forAll.equals("1")){
            cursor = db.rawQuery("select * from " +
                    TABLE + " where " +
                    COLUMN_CODE + "=?" + " and " +
                    COLUMN_SMS + "=?" + " and " +
                    COLUMN_VERSE + "=?" + " and " +
                    COLUMN_FAVORITE + "=?", new String[]{code, sms, verse, favorite});
        }
        // 0 - 0 - 0
        else if(forHim.equals("0") & forHer.equals("0") & forAll.equals("0")){
            cursor = db.rawQuery("select * from " +
                    TABLE + " where " +
                    COLUMN_CODE + "=?" + " and " +
                    COLUMN_SMS + "=?" + " and " +
                    COLUMN_VERSE + "=?" + " and " +
                    COLUMN_FAVORITE + "=?", new String[]{code, sms, verse, favorite});
        }
        // 0 - 0 - 1
        else if(forHim.equals("0") & forHer.equals("0") & forAll.equals("1")){
            String filter = "0";
            cursor = db.rawQuery("select * from " +
                    TABLE + " where " +
                    COLUMN_CODE + "=?" + " and " +
                    COLUMN_SMS + "=?" + " and " +
                    COLUMN_VERSE + "=?" + " and " +
                    COLUMN_FAVORITE + "=?" + " and " +
                    COLUMN_FILTER + "=?", new String[]{code, sms, verse, favorite, filter});

        }
        // 1 - 0 - 1
        else if(forHim.equals("1") & forHer.equals("0") & forAll.equals("1")){
            String filter1 = "0";
            String filter2 = "1";
            cursor = db.rawQuery("select * from " +
                    TABLE + " where " +
                    COLUMN_CODE + "=?" + " and " +
                    COLUMN_SMS + "=?" + " and " +
                    COLUMN_VERSE + "=?" + " and " +
                    COLUMN_FAVORITE + "=?" + " and (" +
                    COLUMN_FILTER + "=?" + " or " +
                    COLUMN_FILTER + "=?)", new String[]{code, sms, verse, favorite, filter1, filter2});
        }
        // 0 - 1 - 1
        else if(forHim.equals("0") & forHer.equals("1") & forAll.equals("1")) {
            String filter1 = "0";
            String filter2 = "2";
            cursor = db.rawQuery("select * from " +
                    TABLE + " where " +
                    COLUMN_CODE + "=?" + " and " +
                    COLUMN_SMS + "=?" + " and " +
                    COLUMN_VERSE + "=?" + " and " +
                    COLUMN_FAVORITE + "=?" + " and (" +
                    COLUMN_FILTER + "=?" + " or " +
                    COLUMN_FILTER + "=?)", new String[]{code, sms, verse, favorite, filter1, filter2});
        } else {
            cursor = db.rawQuery("select * from " +
                    TABLE + " where " +
                    COLUMN_CODE + "=?" + " and " +
                    COLUMN_SMS + "=?" + " and " +
                    COLUMN_VERSE + "=?" + " and " +
                    COLUMN_FAVORITE + "=?", new String[]{code, sms, verse, favorite});
        }


        while (cursor.moveToNext()) {
            Congratulation congratulation = new Congratulation();
            congratulation.set_id(cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
            congratulation.setText(cursor.getString(cursor.getColumnIndex(COLUMN_TEXT)));
            congratulation.setCode(cursor.getString(cursor.getColumnIndex(COLUMN_CODE)));
            congratulation.setSms(cursor.getString(cursor.getColumnIndex(COLUMN_SMS)));
            congratulation.setVerse(cursor.getString(cursor.getColumnIndex(COLUMN_VERSE)));
            congratulation.setFilter(cursor.getString(cursor.getColumnIndex(COLUMN_FILTER)));
            congratulation.setFavorite(cursor.getString(cursor.getColumnIndex(COLUMN_FAVORITE)));

            congratulations.add(congratulation);
        }
        cursor.close();
        db.close();
        return congratulations;
    }

    public String getCountAll() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("select * from " + TABLE, null);
        String col = Integer.toString(cursor.getCount());
        cursor.close();
        return col;
    }

    public void setFavorite(String _id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FAVORITE, "1");

        db.update(TABLE, cv, "_id=" + _id, null);
        cv.clear();
        db.close();
    }

    public void clearFavorite(String _id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FAVORITE, "0");

        db.update(TABLE, cv, "_id=" + _id, null);
        cv.clear();
        db.close();
    }

//    public void addCongratulationsToDB(List<String> congratulations) {
//        SQLiteDatabase db = getWritableDatabase();
//
//        ContentValues cv = new ContentValues();
//        cv.put(CongratulateDBHelper.COLUMN_NAME, holiday.getName());
//        cv.put(CongratulateDBHelper.COLUMN_DESCRIPTION, holiday.getDescription());
//        cv.put(CongratulateDBHelper.COLUMN_IMAGE_URI, holiday.getImageUri());
//        cv.put(CongratulateDBHelper.COLUMN_CATEGORY, holiday.getCategory());
//        cv.put(CongratulateDBHelper.COLUMN_DATA, holiday.getDate());
//
//        db.insert(TABLE, null, cv);
//        db.close();
//        Log.e("DB_add", cv.toString());
//    }

//    public void replaceCongratulationOnDB(List<Congratulation> congratulations) {
//        SQLiteDatabase db = getWritableDatabase();
//
//        ContentValues cv = new ContentValues();
//        cv.put(CongratulateDBHelper.COLUMN_NAME, congratulations.getName());
//        cv.put(CongratulateDBHelper.COLUMN_DESCRIPTION, congratulations.getDescription());
//        cv.put(CongratulateDBHelper.COLUMN_IMAGE_URI, congratulations.getImageUri());
//        cv.put(CongratulateDBHelper.COLUMN_CATEGORY, congratulations.getCategory());
//        cv.put(CongratulateDBHelper.COLUMN_DATA, congratulations.getDate());
//
//        db.update(TABLE, cv, COLUMN_ID + "=" + congratulations.getId(), null);
//        db.close();
//        Log.e("DB_edit", congratulations.getId() + "  " + cv.toString());
//    }

//    public boolean deleteHolidayFromDB(Holiday holiday) {
//        SQLiteDatabase db = getWritableDatabase();
//        return db.delete(TABLE, COLUMN_ID + "=" + holiday.getId(), null) > 0;
//    }

}
