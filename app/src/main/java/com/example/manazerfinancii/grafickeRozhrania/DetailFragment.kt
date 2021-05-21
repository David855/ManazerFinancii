package com.example.manazerfinancii.grafickeRozhrania

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.manazerfinancii.R
import com.example.manazerfinancii.databinding.FragmentDetailBinding
import com.example.manazerfinancii.formatFloatToString
import com.example.manazerfinancii.pridajEuro

/**
 * Fragment slúžiaci na detailné zobrazenie položky
 *
 * @constructor Vytvorenie Detail fragmentu
 */
class DetailFragment() : Fragment() {

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
        val binding = DataBindingUtil.inflate<FragmentDetailBinding>(inflater, R.layout.fragment_detail, container, false)

        // získanie argumentov
        val nazov = arguments?.getString("nazov")
        val popis = arguments?.getString("popis")
        val icon = arguments?.getInt("icon")
        val suma = arguments?.getFloat("suma")
        val prostriedok = arguments?.getString("prostriedok")
        val datum = arguments?.getString("datum")
        val nazovKat = arguments?.getString("nazovKat")

        // získanie pohľadov do ktorých budú vypísané údaje
        val detailNazov : TextView = binding.detailTitle
        val detailPopis : TextView = binding.detailPopis
        val detailIcon : ImageView = binding.detailIcon
        val detailNazovKat : TextView = binding.detailKategoria
        val detailSuma : TextView = binding.detailSuma
        val detailProstriedok : TextView = binding.detailProstriedok
        val detailDatum: TextView = binding.detailDatum

        // vypísanie informácií o položke
        detailNazov.text = nazov
        detailPopis.text = popis
        detailIcon.setImageResource(icon!!)
        detailSuma.text = pridajEuro(suma!!.formatFloatToString())
        detailDatum.text = datum
        detailProstriedok.text = prostriedok
        detailNazovKat.text = nazovKat

        return binding.root
    }

    /**
     * On resume
     *
     */
    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.fragment_detail)
    }
}