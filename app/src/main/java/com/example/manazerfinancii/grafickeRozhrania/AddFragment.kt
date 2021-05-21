package com.example.manazerfinancii.grafickeRozhrania

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.manazerfinancii.R
import com.example.manazerfinancii.aktualizujWidgetPrehlad
import com.example.manazerfinancii.aktualizujWidgetSetrenie
import com.example.manazerfinancii.databaza.KategoriaDao
import com.example.manazerfinancii.databaza.MyDatabase
import com.example.manazerfinancii.databaza.Polozka
import com.example.manazerfinancii.databaza.PolozkaDao
import com.example.manazerfinancii.databinding.FragmentAddBinding
import java.util.*


/**
 * Fragment, ktorý slúži na pridanie položky do databázy
 *
 * @constructor Vytvorenie Add fragmentu
 */
class AddFragment : Fragment() {

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

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentAddBinding>(inflater, R.layout.fragment_add, container, false)

        // získanie hodnoty z argumentu - o aký typ položky ide, 1-príjem, 2-výdavok
        val typId = arguments?.getInt("typId")

        // databáza
        val db = MyDatabase.getDatabase(requireContext().applicationContext);
        val kategoriaDao : KategoriaDao = db.kategoriaDao();

        //získanie kategórií pre danú položku
        val arrayCategories = kategoriaDao.getAllCategoriesName(typId!!);

        // vloženie kategórií do spinnera
        val spinnerKategoria = binding.pridanieKategoria

        val myAdapter : ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, arrayCategories);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerKategoria.adapter = myAdapter

        // tlačidlo na pridanie položky
        val button = binding.pridanieTlacidlo

        // button listener
        button.setOnClickListener {

            // získanie vyplnených hodnôt
            val editTextNazov = binding.pridanieNazov;
            val editTextPopis = binding.pridaniePopis;
            val editTextSuma = binding.pridanieSuma;
            val spinnerProstriedok = binding.pridanieProstriedok;
            val datum = Date()

            // ak boli vyplnené potrebné položky
            if(editTextNazov.text.isNotEmpty() && editTextPopis.text.isNotEmpty() && editTextSuma.text.isNotEmpty()) {
                val polozkaDao: PolozkaDao = db.polozkaDao();
                val sumaPenaziProstriedok : Float

                // získanie prostriedku
                when(spinnerProstriedok.selectedItem.toString())
                {
                    "Peňaženka" -> {
                        sumaPenaziProstriedok = polozkaDao.getSumPenazenkaPrijmy() - polozkaDao.getSumPenazenkaVydavky();
                    }
                    else ->
                    {
                        sumaPenaziProstriedok = polozkaDao.getSumBankUcetPrijmy() - polozkaDao.getSumBankUcetVydavky();
                    }
                }

                // ak ide o výdavok a má dosť peňazí alebo ak ide o príjem, tak pridá položku
                if((typId == 2 && sumaPenaziProstriedok >= editTextSuma.text.toString().toFloat()) || typId == 1) {

                    polozkaDao.writePolozka(Polozka(0, typId, editTextNazov.text.toString(), editTextPopis.text.toString(), spinnerKategoria.selectedItemId.toInt() + 1, editTextSuma.text.toString().toFloat(), spinnerProstriedok.selectedItem.toString(), datum));

                    //aktualizácia widgetov, ak existujú
                    aktualizujWidgetPrehlad(requireContext())
                    aktualizujWidgetSetrenie(requireContext())

                    // presmerovanie naspäť podľa typu položky
                    val bundle = Bundle();
                    bundle.putInt("typId", typId)

                    val fragment = PolozkaFragment();
                    fragment.arguments = bundle;
                    requireActivity().supportFragmentManager.beginTransaction().replace(R.id.myFragment, fragment).commit()
                }
                else
                {
                    Toast.makeText(requireContext().applicationContext, getString(R.string.nedostatokPenazi), Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                Toast.makeText(requireContext().applicationContext, getString(R.string.nevyplnene), Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root;
    }

    /**
     * On resume
     *
     */
    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.fragment_pridat)
    }
}