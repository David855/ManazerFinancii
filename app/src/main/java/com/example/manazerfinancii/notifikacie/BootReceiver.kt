package com.example.manazerfinancii.notifikacie

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Trieda, ktorá po reštartovaní zariadenia spustí pripomienku notifikácie
 *
 * @constructor Vytvorenie Boot receivera
 */
class BootReceiver : BroadcastReceiver() {

    /**
     * On receive
     *
     * @param context context
     * @param intent intent
     */
    override fun onReceive(context: Context, intent: Intent) {
        if(Intent.ACTION_BOOT_COMPLETED == intent.action) {
            NotificationSetting(context).schedulePushNotifications()
        }
    }
}