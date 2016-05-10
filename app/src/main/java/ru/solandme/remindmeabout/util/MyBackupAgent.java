package ru.solandme.remindmeabout.util;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupManager;
import android.app.backup.FileBackupHelper;
import android.content.Context;

import ru.solandme.remindmeabout.database.CongratulateDBHelper;
import ru.solandme.remindmeabout.database.HolidayDBHelper;

public class MyBackupAgent extends BackupAgentHelper {

    @Override
    public void onCreate() {

        FileBackupHelper fileBackupHelper = new FileBackupHelper(this, "../databases/" + HolidayDBHelper.DATABASE_NAME);
        addHelper(HolidayDBHelper.DATABASE_NAME, fileBackupHelper);

        FileBackupHelper fileBackupHelper2 = new FileBackupHelper(this, "../databases/" + CongratulateDBHelper.DATABASE_NAME);
        addHelper(CongratulateDBHelper.DATABASE_NAME, fileBackupHelper2);
    }

    //метод для запроса бэкапа. Согласно документации следует вызывать этот метод всякий раз, когда данные изменились.
    public static void requestBackup(Context context) {
        BackupManager bm = new BackupManager(context);
        bm.dataChanged();
    }
}
