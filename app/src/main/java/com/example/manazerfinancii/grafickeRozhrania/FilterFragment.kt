package com.example.manazerfinancii.grafickeRozhrania

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.manazerfinancii.R
import com.example.manazerfinancii.databaza.KategoriaDao
import com.example.manazerfinancii.databaza.MyDatabase
import com.example.manazerfinancii.databinding.FragmentFilterBinding
import com.example.manazerfinancii.urobDatum
import java.text.SimpleDateFormat
import java.util.*

/**
 * Fragment slúžiaci na filtrovanie položiek
 *
 * @constructor Vytvorenie Filter fragmentu
 */
class FilterFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private var year : Int = 0
    private var month : Int = 0
    private var day : Int = 0
    private lateinit var kliknutyViewDatum : EditText;

    /**
     * On create view
     *
     * @param inflater inflater
     * @param container container
     * @param savedInstanceState savedInstanceState
     * @return view
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentFilterBinding>(inflater, R.layout.fragment_filter, container, false)

        // získanie hodnoty z argumentu - o aký typ položky ide, 1-príjem, 2-výdavok
        val typId = arguments?.getInt("typId")

        // databáza
        val db = MyDatabase.getDatabase(requireContext().applicationContext);
        val kategoriaDao : KategoriaDao = db.kategoriaDao();

        //získanie kategórií pre danú položku
        val arrayCategories = kategoriaDao.getAllCategoriesName(typId!!);

        // vloženie kategórií do spinnera
        val spinnerKategoria = binding.filterKategoria;

        val myAdapter : ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, arrayCategories);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerKategoria.adapter = myAdapter

        // kontrolovanie switchov, či nie sú zapnuté 2 a viac naraz
        checkSwitch(binding)

        // získanie view
        val datumOdView : EditText  = binding.filterZaciatokDatum
        val datumDoView : EditText  = binding.filterUkoncenieDatum

        datumOdView.setOnClickListener{
            kliknutyViewDatum = it as EditText
            nastavKalendar()
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }
        datumDoView.setOnClickListener{
            kliknutyViewDatum = it as EditText
            nastavKalendar()
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }


        // tlačidlo na filtrovanie
        val button : Button = binding.filterTlacidlo

        // button listener
        button.setOnClickListener {
            var error = false;

            // získanie switchov
            val switch1 : Switch = binding.filterPodObdOnof
            val switch2 : Switch = binding.filterPodDatOnof
            val switch3 : Switch = binding.filterPodProsOnof
            val switch4 : Switch = binding.filterPodKatOnof

            val bundle = Bundle();

            // prvý switch zapnutý - podľa obdobia
            if(switch1.isChecked)
            {
                bundle.putInt("filter", 1)

                // zistenie, ktorý radioButton bol vybratý
                val group : RadioGroup = binding.filterObdobieGroup
                when(group.checkedRadioButtonId)
                {
                    R.id.filter_rok ->  bundle.putInt("podlaObdobia", 1)
                    R.id.filter_mesiac -> bundle.putInt("podlaObdobia", 2)
                    R.id.filter_tyzden -> bundle.putInt("podlaObdobia", 3)
                }

            }
            // druhý switch zapnutý - podľa dátumu
            else if(switch2.isChecked)
            {
                val datumOd : String = datumOdView.text.toString().plus("/00:00:00")
                val datumDo : String = datumDoView.text.toString().plus("/23:59:59")

                bundle.putInt("filter", 2)
                bundle.putString("datumod", datumOd)
                bundle.putString("datumdo", datumDo)

            }
            // tretí switch zapnutý - podľa prostriedku
            else if(switch3.isChecked)
            {
                bundle.putInt("filter", 3)

                // ziskanie Spinneru pre zistenie vybratej hodnoty prostriedku
                val spinnerProstriedok : Spinner = binding.filterProstriedok

                bundle.putString("prostriedok", spinnerProstriedok.selectedItem.toString())
            }
            // štvrtý switch zapnutý -
            else if(switch4.isChecked)
            {
                bundle.putInt("filter", 4)

                // ziskanie Spinneru pre zistenie vybratej hodnoty kategórie
                val kategory : Spinner = binding.filterKategoria

                bundle.putInt("kategoria", (kategory.selectedItemId.toInt() + 1))
            }
            // ak nebolo nič vybraté
            else
            {
                error = true;
                Toast.makeText(requireContext().applicationContext, getString(R.string.filter_chyba), Toast.LENGTH_SHORT).show()
            }

            // v prípade že všetko prebehlo úspešne, pošlú sa parametre fragmentu a zobrazia sa položky podľa filtra
            if(!error)
            {
                val fragment = PolozkaFragment();
                bundle.putInt("typId", typId);
                fragment.arguments = bundle;
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.myFragment, fragment).addToBackStack(null).commit()
            }
        }

        return binding.root;
    }

    /**
     * Nastavenie kalendára na dnešný deň
     *
     */
    private fun nastavKalendar() {
        val kalendar = Calendar.getInstance();

        year = kalendar.get(Calendar.YEAR)
        month = kalendar.get(Calendar.MONTH)
        day = kalendar.get(Calendar.DAY_OF_MONTH)
    }

    /**
     * Kontrola switchov, musí byť zapnutý len jeden
     *
     * @param binding binding
     */
    private fun checkSwitch(binding : FragmentFilterBinding) {

        val switch1 : Switch = binding.filterPodObdOnof
        val switch2 : Switch = binding.filterPodDatOnof
        val switch3 : Switch = binding.filterPodProsOnof
        val switch4 : Switch = binding.filterPodKatOnof

        // ak je prvý switch zapnutý, ostatné vypni
        switch1.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            if(isChecked)
            {
                switch2.isChecked = false
                switch3.isChecked = false
                switch4.isChecked = false
            }
        })

        // ak je druhý switch zapnutý, ostatné vypni
        switch2.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                switch1.isChecked = false
                switch3.isChecked = false
                switch4.isChecked = false
            }
        })

        // ak je tretí switch zapnutý, ostatné vypni
        switch3.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                switch1.isChecked = false
                switch2.isChecked = false
                switch4.isChecked = false
            }
        })

        // ak je štvrtý switch zapnutý, ostatné vypni
        switch4.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                switch1.isChecked = false
                switch2.isChecked = false
                switch3.isChecked = false
            }
        })
    }

    /**
     * On resume
     *
     */
    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.fragment_filter)
    }

    /**
     * On date set
     *
     * @param view view
     * @param year rok
     * @param month mesiac
     * @param dayOfMonth den
     */
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        kliknutyViewDatum.setText(SimpleDateFormat("dd.MM.yyyy").format(urobDatum(year, month, dayOfMonth)))
    }
}