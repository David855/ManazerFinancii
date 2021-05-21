package com.example.manazerfinancii.notifikacie

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import com.example.manazerfinancii.MainActivity
import com.example.manazerfinancii.R
import com.example.manazerfinancii.databaza.MyDatabase
import com.example.manazerfinancii.databaza.PolozkaDao

/**
 * Trieda, ktorá zobrazuje notifikáciu
 *
 * @constructor Vytvorenie Alarm receiveru
 */
class AlarmReceiver : BroadcastReceiver() {

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "prehlad_notifikacia"
    private val description = "Prehlad notifikacia"

    /**
     * On receive
     *
     * @param context context
     * @param intent intent
     */
    override fun onReceive(context: Context, intent: Intent) {
        //zobraz notifikáciu
        showPushNotification(context)
    }

    /**
     * Zobrazenie notifikácie
     *
     * @param context context
     */
    private fun showPushNotification(context : Context) {
        //získanie nastavenia
        val pref = context.getSharedPreferences(context.getString(R.string.pref_name), Context.MODE_PRIVATE)
        val hodnota = pref.getBoolean(context.getString(R.string.notifikaciaOnOFF), false)

        //ak sú notifikácie vypnuté
        if(!hodnota) return

        val intent = Intent(context, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val db = MyDatabase.getDatabase(context.applicationContext);
        val polozkaDao : PolozkaDao = db.polozkaDao();

        var prijmyMesiac = polozkaDao.getSumPrijmyMonth();
        var vydavkyMesiac = polozkaDao.getSumVydavkyMonth();
        val sumVysledok = prijmyMesiac - vydavkyMesiac;

        //text pre notifikáciu
        val nadpisText : String = context.getString(R.string.text_prehlad)
        var text: String

        if(sumVysledok < 0)
        {
            text = context.getString(R.string.notifikacia_prehlad_zle)
        }
        else
        {
            text = context.getString(R.string.notifikacia_prehlad_dobre)
        }

        //zobrazenie notifikácie

        //či je oreo alebo vyššie
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(context, channelId)
                    .setContentTitle(nadpisText)
                    .setContentText(text)
                    .setSmallIcon(R.drawable.app_logo)
                    .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.app_logo))
                    .setContentIntent(pendingIntent)
        } else {
            @Suppress("DEPRECATION")
            builder = Notification.Builder(context)
                    .setContentTitle(nadpisText)
                    .setContentText(text)
                    .setSmallIcon(R.drawable.app_logo)
                    .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.app_logo))
                    .setContentIntent(pendingIntent)
        }
        notificationManager.notify(12345, builder.build())
    }
}