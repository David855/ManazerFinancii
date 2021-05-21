package com.example.manazerfinancii

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.example.manazerfinancii.widgety.PrehladWidget
import com.example.manazerfinancii.widgety.SetrenieWidget
import java.text.SimpleDateFormat
import java.util.*

/**
 * Rozšírujúca funkcia triedy Date na vrátenie reťazca, ktorý bol naformátovaný z dátumu
 *
 * @return﻿ naformatovaný reťazec
 */
fun Date.toMyDate() : String
{
    return SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(this);
}

/**
 * Pridá k reťazcu znak eura
 *
 * @param retazec reťazec
 * @return﻿ vyskladaný reťazec
 */
fun pridajEuro(retazec : String) :String
{
    return "${retazec}€"
}

/**
 * Rozširujúca funkcia pre triedu Float, ktorá vráti čislo s 2 desatinnými miestami v tvare reťazca
 *
 * @return﻿ výsledný reťazec
 */
fun Float.formatFloatToString() : String
{
    return "%.2f".format(this)
}

/**
 * Vráti dátum zo zadaných parametrov
 *
 * @param rok rok
 * @param mesiac mesiac
 * @param den den
 * @return datum
 */
fun urobDatum(rok : Int, mesiac : Int, den : Int) : Date
{
    val kalendar = Calendar.getInstance();

    kalendar[Calendar.YEAR] = rok;
    kalendar[Calendar.MONTH] = mesiac;
    kalendar[Calendar.DAY_OF_MONTH] = den;

    return kalendar.time;
}

/**
 * Aktualizuj widget prehlad
 *
 * @param context context
 */
fun aktualizujWidgetPrehlad(context : Context)
{
    // Prehľad Widget

    val intent = Intent(context.applicationContext, PrehladWidget::class.java)
    intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE

    val widgetManager = AppWidgetManager.getInstance(context)
    val ids = widgetManager.getAppWidgetIds(ComponentName(context, PrehladWidget::class.java))

    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
    context.sendBroadcast(intent);
}

/**
 * Aktualizuj widget setrenie
 *
 * @param context context
 */
fun aktualizujWidgetSetrenie(context : Context)
{
    //Setrenie Widget

    val intent = Intent(context.applicationContext, SetrenieWidget::class.java)
    intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE

    val widgetManager = AppWidgetManager.getInstance(context)
    val ids = widgetManager.getAppWidgetIds(ComponentName(context, SetrenieWidget::class.java))

    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
    context.sendBroadcast(intent);
}

