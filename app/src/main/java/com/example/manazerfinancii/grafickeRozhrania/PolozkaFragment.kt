package com.example.manazerfinancii.grafickeRozhrania

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.manazerfinancii.adaptery.ListAdapter
import com.example.manazerfinancii.R
import com.example.manazerfinancii.databaza.*
import com.example.manazerfinancii.databinding.FragmentPolozkaBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat

/**
 * ragment, ktorý slúži na zobrazenie položiek
 *
 * @constructor Vytvorenie Polozka fragmentu
 */
class PolozkaFragment : Fragment() {
    private var typId : Int = 0;

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
        val binding = DataBindingUtil.inflate<FragmentPolozkaBinding>(inflater, R.layout.fragment_polozka, container, false)

        // získanie hodnoty z argumentu - o aký typ položky ide, 1-príjem, 2-výdavok
        typId = arguments?.getInt("typId")!!

        //databáza
        val db = MyDatabase.getDatabase(requireContext().applicationContext);
        val kategoriaDao : KategoriaDao = db.kategoriaDao();

        // ak kategórie položky nie sú v DB, pridaj ich
        if(kategoriaDao.getNumOfCategories(typId) == 0 )
        {
            pridajKategorie(kategoriaDao, typId)
        }

        val polozkaDao : PolozkaDao = db.polozkaDao();
        var list : List<Polozka> = listOf();

        //  filter
        val filter = arguments?.getInt("filter");

        // výber filtera
        when(filter)
        {
            // podľa obdobia
            1-> {
                val obdobie = arguments?.getInt("podlaObdobia");
                when(obdobie)
                {
                    1-> list = polozkaDao.getAllPolozkyRok(typId)
                    2-> list = polozkaDao.getAllPolozkyMesiac(typId)
                    3-> list = polozkaDao.getAllPolozkyTyzden(typId)
                }
            }
            // podľa dátumu
            2->
            {
                val datumOdP = arguments?.getString("datumod");
                val datumDoP = arguments?.getString("datumdo");

                val datumOd = SimpleDateFormat("dd.MM.yyyy/HH:mm:ss").parse(datumOdP!!)
                val datumDo = SimpleDateFormat("dd.MM.yyyy/HH:mm:ss").parse(datumDoP!!)

                list = polozkaDao.getAllPolozkyByBetweenDate(typId, datumOd!!, datumDo!!)
            }
            // podľa prostriedku
            3-> list = polozkaDao.getAllPolozkyByProstriedok(typId, requireArguments().getString("prostriedok")!!)
            // podľa kategórie
            4-> list = polozkaDao.getAllPolozkyByCategory(typId, requireArguments().getInt("kategoria"))

            // inak všetky
            else ->  list = polozkaDao.getAllPolozky(typId);
        }

        // recyclerview
        val polozkaList : RecyclerView = binding.Zoznam;
        polozkaList.adapter = ListAdapter(list, activity);
        polozkaList.setHasFixedSize(true);

        // button na pridanie položky
        val actionButton: View = binding.addButton;

        // zmena farby na základe typu položky
        when(typId)
        {
            1-> actionButton.backgroundTintList = AppCompatResources.getColorStateList(requireContext().applicationContext,
                R.color.green
            );
            2-> actionButton.backgroundTintList = AppCompatResources.getColorStateList(requireContext().applicationContext,
                R.color.red
            );
        }

        // button listener
        actionButton.setOnClickListener {
            val bundle = Bundle();
            bundle.putInt("typId", typId)

            val fragment = AddFragment();
            fragment.arguments = bundle;

            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.myFragment, fragment).addToBackStack(null).commit()
        }

        // button pre filtrovanie
        val filterButton: FloatingActionButton = binding.filterButton;

        // button listener
        filterButton.setOnClickListener {
            val bundle = Bundle();
            bundle.putInt("typId", typId)

            val fragment = FilterFragment();
            fragment.arguments = bundle;

            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.myFragment, fragment).addToBackStack(null).commit()
        }

        return binding.root;
    }

    /**
     * On resume
     *
     */
    override fun onResume() {
        super.onResume()
        when(typId)
        {
            1-> activity?.title = getString(R.string.fragment_prijmy)
            2-> activity?.title = getString(R.string.fragment_vydavky)
        }
    }

    /**
     * Pridanie kategórie do DB
     *
     * @param kategoriaDao Dao
     * @param typId typ
     */
    private fun pridajKategorie(kategoriaDao : KategoriaDao, typId : Int)
    {
        when(typId)
        {
            // pre príjmy
            1->
            {
                kategoriaDao.writeKategoria(Kategoria(1, 1, R.drawable.kat_prijmy_praca, "Práca"));
                kategoriaDao.writeKategoria(Kategoria(2, 1, R.drawable.kat_prijmy_brigada, "Brigáda"));
                kategoriaDao.writeKategoria(Kategoria(3, 1, R.drawable.kat_prijmy_vyhra, "Výhra"));
                kategoriaDao.writeKategoria(Kategoria(4, 1, R.drawable.kat_prijmy_stipendium, "Štipendium"));
                kategoriaDao.writeKategoria(Kategoria(5, 1, R.drawable.kat_prijmy_other, "Ostatné"))
            }
            // pre výdavky
            2->
            {
                kategoriaDao.writeKategoria(Kategoria(1, 2, R.drawable.kat_vydavky_stravovanie, "Stravovanie"));
                kategoriaDao.writeKategoria(Kategoria(2, 2, R.drawable.kat_vydavky_oblecenie, "Oblečenie"));
                kategoriaDao.writeKategoria(Kategoria(3, 2, R.drawable.kat_vydavky_obuv, "Obuv"));
                kategoriaDao.writeKategoria(Kategoria(4, 2, R.drawable.kat_vydavky_drogeria, "Drogéria"));
                kategoriaDao.writeKategoria(Kategoria(5, 2, R.drawable.kat_vydavky_zdravie, "Zdravie"))
                kategoriaDao.writeKategoria(Kategoria(6, 2, R.drawable.kat_vydavky_nakup, "Nákup"))
                kategoriaDao.writeKategoria(Kategoria(7, 2, R.drawable.kat_vydavky_zabava, "Bar/Zábava"))
                kategoriaDao.writeKategoria(Kategoria(8, 2, R.drawable.kat_vydavky_doprava, "Doprava"))
                kategoriaDao.writeKategoria(Kategoria(9, 2, R.drawable.kat_vydavky_domacnost, "Domacnost"))
                kategoriaDao.writeKategoria(Kategoria(10, 2, R.drawable.kat_vydavky_doplnok, "Doplnky"))
                kategoriaDao.writeKategoria(Kategoria(11, 2, R.drawable.kat_vydavky_dane, "Dane"))
                kategoriaDao.writeKategoria(Kategoria(12, 2, R.drawable.kat_vydavky_auto, "Auto"))
                kategoriaDao.writeKategoria(Kategoria(13, 2, R.drawable.kat_vydavky_maznacik, "Zvieratá"))
                kategoriaDao.writeKategoria(Kategoria(14, 2, R.drawable.kat_vydavky_ostatne, "Ostatné"))
                kategoriaDao.writeKategoria(Kategoria(15, 2, R.drawable.setrenie, "Šetrenie"))
            }
        }
    }
}