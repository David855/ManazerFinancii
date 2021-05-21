package com.example.manazerfinancii.widgety

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.example.manazerfinancii.R
import com.example.manazerfinancii.databaza.MyDatabase
import com.example.manazerfinancii.databaza.PolozkaDao
import com.example.manazerfinancii.databaza.Setrenie
import com.example.manazerfinancii.databaza.SetrenieDao

/**
 * Setrenie widget
 *
 * @constructor Vytvorenie Setrenie widgetu
 */
class SetrenieWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget1(context, appWidgetManager, appWidgetId)
        }
    }

    /**
     * On enabled
     *
     * @param context context
     */
    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    /**
     * On disabled
     *
     * @param context context
     */
    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

/**
 * Update app widget1
 *
 * @param context context
 * @param appWidgetManager appWidgetManager
 * @param appWidgetId appWidgetId
 */
internal fun updateAppWidget1(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.setrenie_widget)

    //databáza
    val db = MyDatabase.getDatabase(context.applicationContext);
    val setrenieDao : SetrenieDao = db.setrenieDao();
    val polozkaDao : PolozkaDao = db.polozkaDao();

    val setrenie : Setrenie = setrenieDao.getSetrenie();


    @Suppress("SENSELESS_COMPARISON")
    if(setrenie != null)
    {
        val stavNaSetreni : Float = polozkaDao.getSumSetrenia(setrenie.datum);
        val percent = ((stavNaSetreni / setrenie.suma)*100).toInt().toString();

        views.setTextViewText(R.id.widget_setrenieNazov, setrenie.nazov)
        views.setInt(R.id.widget_BarSetrenia, "setMax", setrenie.suma.toInt())
        views.setInt(R.id.widget_BarSetrenia, "setProgress", stavNaSetreni.toInt())
        views.setTextViewText(R.id.widget_setrenie_percent, "${percent}%")
    }
    else
    {
        views.setTextViewText(R.id.widget_setrenieNazov, context.getString(R.string.ziadnesetrenie))
        views.setInt(R.id.widget_BarSetrenia, "setMax", 0)
        views.setInt(R.id.widget_BarSetrenia, "setProgress", 0)
        views.setTextViewText(R.id.widget_setrenie_percent, "0%")
    }

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}