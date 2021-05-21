package com.example.manazerfinancii.grafickeRozhrania

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.manazerfinancii.adaptery.ListAdapter
import com.example.manazerfinancii.R
import com.example.manazerfinancii.aktualizujWidgetSetrenie
import com.example.manazerfinancii.databaza.*
import com.example.manazerfinancii.databinding.FragmentSetrenieBinding
import com.example.manazerfinancii.databinding.PridanieSetreniaBinding
import com.example.manazerfinancii.formatFloatToString
import com.example.manazerfinancii.pridajEuro
import java.util.*

/**
 * Fragment ktorý slúži na pridanie a prácu so šetrením
 *
 * @constructor Vytvorenie Setrenie fragmentu
 */
class SetrenieFragment : Fragment() {

    /**
     * On create view
     *
     * @param inflater inflater
     * @param container container
     * @param savedInstanceState savedInstanceState
     * @return view
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //databáza
        val db = MyDatabase.getDatabase(requireContext().applicationContext);
        val setrenieDao: SetrenieDao = db.setrenieDao();

        //získanie šetrenia
        val setrenie : Setrenie = setrenieDao.getSetrenie();

        // ak nemám šetrenie, zobrazí sa formulár na vytvorenie
        @Suppress("SENSELESS_COMPARISON")
        if(setrenie == null ) {
            val binding = DataBindingUtil.inflate<PridanieSetreniaBinding>(inflater, R.layout.pridanie_setrenia, container, false)

            // tlačidlo na uloženie šetrenia
            val button = binding.pridaniesTlacidlo

            // button listener
            button.setOnClickListener {
                // získanie view
                val editTextNazov = binding.pridaniesNazov;
                val editTextPopis = binding.pridaniesPopis;
                val editTextSuma = binding.pridaniesSuma;
                val datum = Date()

                // ak som všetko vyplnil, pridá sa šetrenie a zobrazí sa dané šetrenie
                if (editTextNazov.text.isNotEmpty() && editTextPopis.text.isNotEmpty() && editTextSuma.text.isNotEmpty()) {
                    setrenieDao.writeSetrenie(
                        Setrenie(
                            0,
                            editTextNazov.text.toString(),
                            editTextPopis.text.toString(),
                            editTextSuma.text.toString().toFloat(),
                            datum
                        )
                    )
                    //aktualizácia widgetu
                    aktualizujWidgetSetrenie(requireContext())

                    //presmerovanie
                    requireActivity().supportFragmentManager.beginTransaction().replace(R.id.myFragment, SetrenieFragment()).addToBackStack(null).commit()
                    // nevyplnené všetko
                } else {
                    Toast.makeText(
                        requireContext().applicationContext,
                        getString(R.string.nevyplnene),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            return binding.root;
        }
        else
        {
            val binding = DataBindingUtil.inflate<FragmentSetrenieBinding>(inflater, R.layout.fragment_setrenie, container, false)

            val polozkaDao : PolozkaDao = db.polozkaDao();

            // získanie views na výpis informácií
            val nazov : TextView = binding.setrenieNazov;
            val popis : TextView = binding.setreniePopis;
            val suma : TextView = binding.setrenieSumaSporenia;
            val stavNaSetreni : TextView = binding.setrenieStavSuma;

            // výpis informácií
            nazov.text = setrenie.nazov
            suma.text = pridajEuro(setrenie.suma.formatFloatToString());
            popis.text = setrenie.popis

            // získanie stavu, koľko peňazí je už nasporených
            val stav : Float = polozkaDao.getSumSetrenia(setrenie.datum)

            // vypísanie informácie
            stavNaSetreni.text = "${pridajEuro(stav.formatFloatToString())} / ${pridajEuro(setrenie.suma.formatFloatToString())}"

            // získanie príspevkov, ktoré patria k šetreniu
            val prispevky : List<Polozka> = polozkaDao.getAllSetrenia(setrenie.datum)

            // pridanie položiek do recyclerview
            val zoznamPrispevkov : RecyclerView = binding.setreniePrispevkyList
            zoznamPrispevkov.adapter = ListAdapter(prispevky, activity)

            // tlačidlo vymazania
            val actionButton: View = binding.setrenieDelete;

            // button listener
            actionButton.setOnClickListener {
                vymazanieDialog(requireContext(), requireActivity(), setrenie, setrenieDao)
            }
            return binding.root;
        }

    }

    /**
     * On resume
     *
     */
    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.fragment_setrenie)
    }
}


/**
 * Dialog pred vymazaním položky
 *
 * @param context context
 * @param activity activita
 * @param setrenie setrenie
 * @param setrenieDao Dao
 */
fun vymazanieDialog(context : Context, activity : FragmentActivity, setrenie : Setrenie, setrenieDao : SetrenieDao)
{
    // modalové okno
    val builder = AlertDialog.Builder(context, R.style.DialogTheme);
    builder.setIcon(R.drawable.ic_trash)
    builder.setTitle(R.string.vymazanie_polozky)
    builder.setMessage(R.string.vymazanie_otazka)

    // po kliknutí na áno
    builder.setPositiveButton(R.string.close_app_yes) {
            _, _->
        run {
            setrenieDao.deleteSetrenie(setrenie)

            aktualizujWidgetSetrenie(context)

            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.myFragment, SetrenieFragment()).addToBackStack(null).commit()
        }
    }

    // po kliknutí na nie
    builder.setNegativeButton(R.string.close_app_no) {
            _, _->
    }

    builder.setCancelable(false);

    //zobrazenie alertu
    val alert = builder.create()
    alert.show()
}