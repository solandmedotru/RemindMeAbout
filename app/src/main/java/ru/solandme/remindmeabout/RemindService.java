package ru.solandme.remindmeabout;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;

import ru.solandme.remindmeabout.database.HolidayDBHelper;

import static ru.solandme.remindmeabout.R.plurals.hours;

public class RemindService extends IntentService {
    private static final String TAG = "RemindService";
    private static final int POLL_INTERVAL = 1000 * 60;

    private static final int NOTIFY_ID = 101;
    List<Holiday> allHolidays = new ArrayList<>();
    ArrayList<String> notifies = new ArrayList<>();

    public static Intent newIntent(Context context) {
        return new Intent(context, RemindService.class);
    }

    public RemindService() {
        super(TAG);
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = RemindService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(),
                    AlarmManager.INTERVAL_HALF_DAY, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent i = RemindService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        HolidayDBHelper holidayDbHelper = new HolidayDBHelper(getApplicationContext());
        allHolidays.addAll(holidayDbHelper.getHolidaysByCategory(Holiday.CATEGORY_HOLIDAY));
        allHolidays.addAll(holidayDbHelper.getHolidaysByCategory(Holiday.CATEGORY_BIRTHDAY));
        allHolidays.addAll(holidayDbHelper.getHolidaysByCategory(Holiday.CATEGORY_EVENT));
        for (int i = 0; i < allHolidays.size(); i++) {
            int hoursLeft;
            hoursLeft = allHolidays.get(i).getHoursLeft();

            if ((hoursLeft <= 48) & (hoursLeft > 0)) {
                notifies.add(allHolidays.get(i).getName() + " - " + getString(R.string.left) + getApplicationContext().getResources().getQuantityString(hours, hoursLeft, hoursLeft));
            }
        }
        if (notifies.size() > 0) {
            sendNotif();
        }
    }

    void sendNotif() {
        Context context = getApplicationContext();

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentTitle(getApplicationContext().getResources().getString(R.string.app_name))
                .setWhen(System.currentTimeMillis());

        NotificationCompat.InboxStyle notification = new NotificationCompat.InboxStyle(builder);
        notification.setBigContentTitle(getApplicationContext().getResources().getString(R.string.app_name));
        notification.setSummaryText(""); //Bug with InboxStyle fixed
        String[] arr = notifies.toArray(new String[notifies.size()]);
        for (int i = 0; i < notifies.size(); i++) {
            notification.addLine(arr[i]);
        }

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification.build());
    }


}
