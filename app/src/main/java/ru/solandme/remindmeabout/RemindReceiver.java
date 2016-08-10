package ru.solandme.remindmeabout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RemindReceiver extends BroadcastReceiver {
    Context mContext;
    private final String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        String action = intent.getAction();
        if (action.equalsIgnoreCase(BOOT_ACTION)) {
            RemindService.setServiceAlarm(context, true);
        }
    }
}
