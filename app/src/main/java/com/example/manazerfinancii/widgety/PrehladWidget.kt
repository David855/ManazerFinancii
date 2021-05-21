package com.example.manazerfinancii.widgety

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import com.example.manazerfinancii.R
import com.example.manazerfinancii.databaza.MyDatabase
import com.example.manazerfinancii.databaza.PolozkaDao
import com.example.manazerfinancii.formatFloatToString
import com.example.manazerfinancii.pridajEuro
import java.util.*

/**
 * Prehlad widget
 *
 * @constructor Vytvorenie Prehlad widgetu
 */
class PrehladWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
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
 * Update app widget
 *
 * @param context context
 * @param appWidgetManager appWidgetManager
 * @param appWidgetId appWidgetId
 */
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.prehlad_widget)

    //databáza
    val db = MyDatabase.getDatabase(context.applicationContext);
    val polozkaDao : PolozkaDao = db.polozkaDao();

    val cisloMesiaca = Calendar.getInstance().get(Calendar.MONTH);
    val cisloRoku = Calendar.getInstance().get(Calendar.YEAR);

    val nazovMesiaca: String = context.resources.getStringArray(R.array.mesiace)[cisloMesiaca]

    var prijmyMesiac = polozkaDao.getSumPrijmyMonth();
    var vydavkyMesiac = polozkaDao.getSumVydavkyMonth();
    val sumVysledok = prijmyMesiac - vydavkyMesiac;

    views.setTextViewText(R.id.widget_mesiac, "${nazovMesiaca} ${cisloRoku}")
    views.setTextViewText(R.id.widget_prijmy, pridajEuro(prijmyMesiac.formatFloatToString()))
    views.setTextViewText(R.id.widget_vydavky, pridajEuro(vydavkyMesiac.formatFloatToString()))
    views.setTextViewText(R.id.widget_spolu, pridajEuro(sumVysledok.formatFloatToString()))

    if(sumVysledok < 0)
        views.setTextColor(R.id.widget_spolu, ContextCompat.getColor(context, R.color.red))
    else
        views.setTextColor(R.id.widget_spolu, ContextCompat.getColor(context, R.color.green))

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}