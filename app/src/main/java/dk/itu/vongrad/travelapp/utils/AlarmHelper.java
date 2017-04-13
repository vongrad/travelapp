package dk.itu.vongrad.travelapp.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import dk.itu.vongrad.travelapp.services.AutoCheckoutService;

/**
 * Created by Adam Vongrej on 4/13/17.
 */

public class AlarmHelper {

    /**
     * Check if the service alarm is already up
     * @param context
     * @param intent
     * @return boolean
     */
    public static boolean isServiceAlarmUp(Context context, Intent intent) {
        return PendingIntent.getService(context, 0, new Intent(context, AutoCheckoutService.class), PendingIntent.FLAG_NO_CREATE) != null;
    }

    /**
     * Schedule repetitive service
     * @param context Context
     * @param intent Intent - Intent representing the service to be run
     * @param interval long - run every x milliseconds
     */
    public static void scheduleRepeatingService(Context context, Intent intent, long interval) {
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, interval, pendingIntent);
    }
}
