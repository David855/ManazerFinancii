package com.example.manazerfinancii.grafickeRozhrania

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.manazerfinancii.R
import com.example.manazerfinancii.databinding.FragmentNastaveniaBinding

/**
 * Nastavenia fragment
 *
 * @constructor Vytvorenie Nastavenia fragmentu
 */
class NastaveniaFragment : Fragment() {

    /**
     * On create view
     *
     * @param inflater inflater
     * @param container container
     * @param savedInstanceState saveInstanceState
     * @return
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentNastaveniaBinding>(inflater, R.layout.fragment_nastavenia, container, false)

        //získanie switchu
        val hodnotaNotif = binding.nastavNotifOnof

        // získanie hodnoty zo sharedPrefences
        val sharedPref = requireActivity().getSharedPreferences(getString(R.string.pref_name), Context.MODE_PRIVATE)
        val hodnota = sharedPref.getBoolean(getString(R.string.notifikaciaOnOFF), false)

        //nastavenie swichu podľa hodnoty
        hodnotaNotif.isChecked = hodnota

        //tlačidlo uloženia
        val button = binding.nastaveniaUloz

        // button listener
        button.setOnClickListener {
            //uloženie hodnoty
            with (sharedPref.edit()) {
                putBoolean(getString(R.string.notifikaciaOnOFF), hodnotaNotif.isChecked )
                apply()
            }
            Toast.makeText(requireContext().applicationContext, requireContext().getString(R.string.nastavenia_saved), Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.myFragment, NastaveniaFragment()).commit()
        }

        return binding.root
    }

    /**
     * On resume
     *
     */
    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.fragment_nastavenia)
    }
}