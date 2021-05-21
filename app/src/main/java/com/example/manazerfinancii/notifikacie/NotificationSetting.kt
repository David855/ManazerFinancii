package com.example.manazerfinancii.notifikacie

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import java.util.*

/**
 * Trieda, ktorá slúži na nastavenie notifikácie
 *
 * @constructor konštruktor
 *
 * @param context context
 */
class NotificationSetting(context : Context) {
    private val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
    private val alarmPendingIntent by lazy{
        val intent = Intent(context, AlarmReceiver::class.java)
        PendingIntent.getBroadcast(context, 0, intent, 0)
    }
    private val hodina = 15

    /**
     * Naplánovanie upozornenia
     *
     */
    fun schedulePushNotifications() {
        val calendar = GregorianCalendar.getInstance().apply {
            if (get(Calendar.HOUR_OF_DAY) >= hodina) {
                add(Calendar.DAY_OF_MONTH, 1)
            }

            set(Calendar.HOUR_OF_DAY, hodina)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                alarmPendingIntent
        )
    }
}