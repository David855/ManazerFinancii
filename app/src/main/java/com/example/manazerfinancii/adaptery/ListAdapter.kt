package com.example.manazerfinancii.adaptery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.manazerfinancii.R
import com.example.manazerfinancii.databaza.KategoriaDao
import com.example.manazerfinancii.databaza.MyDatabase
import com.example.manazerfinancii.databaza.Polozka
import com.example.manazerfinancii.formatFloatToString
import com.example.manazerfinancii.grafickeRozhrania.DetailFragment
import com.example.manazerfinancii.pridajEuro
import com.example.manazerfinancii.toMyDate

/**
 * List adaptér
 *
 * @property data data
 * @property activity aktivita
 * @constructor Vytvorenie List Adaptéra
 */
class ListAdapter(val data: List<Polozka>, val activity: FragmentActivity?) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    /**
     * View holder
     *
     * @constructor konštruktor
     *
     * @param view view
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // získanie views
        val itemNazov : TextView = view.findViewById(R.id.item_title)
        val itemIcon : ImageView = view.findViewById(R.id.item_icon)
        val itemSuma : TextView = view.findViewById(R.id.item_suma)
        val itemProstriedok : TextView = view.findViewById(R.id.item_prostriedok)
        val itemDatum: TextView = view.findViewById(R.id.item_datum);
        val itemObdlznik: ImageView = view.findViewById(R.id.item_obdlznik);
    }

    /**
     * On create view holder
     *
     * @param viewGroup viewGroup
     * @param viewType viewType
     * @return viewHolder
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.layout_item, viewGroup, false)

        return ViewHolder(view)
    }

    /**
     * On bind view holder
     *
     * @param viewHolder viewHolder
     * @param position pozícia
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        //bindovanie a zobrazenie položky na danej pozícií
        val polozka = data.get(position);
        val typId = polozka.typ;

        //databáza
        val db = MyDatabase.getDatabase(viewHolder.itemView.context);
        val kategoriaDao : KategoriaDao = db.kategoriaDao();

        //získanie kategórie
        val kategoria = kategoriaDao.getCategoryById(polozka.kategoria, polozka.typ);

        //vloženie informácií do views
        viewHolder.itemNazov.text = polozka.nazov
        viewHolder.itemIcon.setImageResource(kategoria.icon);
        viewHolder.itemSuma.text = pridajEuro(polozka.suma.formatFloatToString())
        viewHolder.itemProstriedok.text = polozka.prostriedok
        viewHolder.itemDatum.text = polozka.datum.toMyDate();

        // podľa typu nastaví obrázok a farbu textu
        when(typId)
        {
            1 ->
            {
                viewHolder.itemObdlznik.setImageResource(R.drawable.prijmysquare)
                viewHolder.itemSuma.setTextColor(ContextCompat.getColor(activity!!.applicationContext,
                    R.color.green
                ))
            }
            2->
            {
                viewHolder.itemObdlznik.setImageResource(R.drawable.vydavkysquare)
                viewHolder.itemSuma.setTextColor(ContextCompat.getColor(activity!!.applicationContext,
                    R.color.red
                ))
            }
        }

        // pri kliknutí na položku
        viewHolder.itemView.setOnClickListener {
            val bundle = Bundle();
            bundle.putString("nazov", polozka.nazov)
            bundle.putString("popis", polozka.popis)
            bundle.putInt("icon", kategoria.icon)
            bundle.putString("nazovKat", kategoria.nazov)
            bundle.putFloat("suma", polozka.suma)
            bundle.putString("prostriedok", polozka.prostriedok)
            bundle.putString("datum", polozka.datum.toMyDate())

            // presmerovanie do detail fragmentu s argumentami
            val fragment = DetailFragment();
            fragment.arguments = bundle;

            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.myFragment, fragment)?.addToBackStack(null)?.commit()
        }
    }

    /**
     * Get item count
     *
     */
    override fun getItemCount() = data.size

}