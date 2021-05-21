package com.example.manazerfinancii.grafickeRozhrania

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.androidplot.pie.PieChart
import com.androidplot.pie.Segment
import com.androidplot.pie.SegmentFormatter
import com.example.manazerfinancii.R
import com.example.manazerfinancii.databaza.MyDatabase
import com.example.manazerfinancii.databaza.PolozkaDao
import com.example.manazerfinancii.databaza.Setrenie
import com.example.manazerfinancii.databaza.SetrenieDao
import com.example.manazerfinancii.databinding.FragmentPrehladBinding
import com.example.manazerfinancii.formatFloatToString
import com.example.manazerfinancii.pridajEuro
import com.example.manazerfinancii.toMyDate
import java.util.*

/**
 * Fragment ktorý slúži na prehľad o príjmoch a výdavkoch
 *
 * @constructor Vytvorenie Prehlad fragmentu
 */
class PrehladFragment : Fragment() {

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
        val binding = DataBindingUtil.inflate<FragmentPrehladBinding>(inflater, R.layout.fragment_prehlad, container, false)

        //databáza
        val db = MyDatabase.getDatabase(requireContext().applicationContext);
        val polozkaDao : PolozkaDao = db.polozkaDao();
        val setrenieDao : SetrenieDao = db.setrenieDao();

        //Zhrnutie

        val cisloMesiaca = Calendar.getInstance().get(Calendar.MONTH);
        val cisloRoku = Calendar.getInstance().get(Calendar.YEAR);

        val nazovMesiaca: String = resources.getStringArray(R.array.mesiace)[cisloMesiaca]

        val zhrnutieMesiac : TextView = binding.zhrnutieMesiac

        var prijmyMesiac = polozkaDao.getSumPrijmyMonth();
        var vydavkyMesiac = polozkaDao.getSumVydavkyMonth();

        val prijmyMesiacView : TextView = binding.zhrnutiePrijmyHodnota
        prijmyMesiacView.text = pridajEuro(prijmyMesiac.formatFloatToString());

        val vydavkyMesiacView : TextView = binding.zhrnutieVydavkyHodnota
        vydavkyMesiacView.text = pridajEuro(vydavkyMesiac.formatFloatToString());

        val sumVysledok = prijmyMesiac - vydavkyMesiac;

        val sumMesiacView : TextView = binding.zhrnutieCelkomHodnota;
        sumMesiacView.text = pridajEuro(sumVysledok.formatFloatToString());

        if(sumVysledok < 0)
            sumMesiacView.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
        else
            sumMesiacView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))

        zhrnutieMesiac.text = "${nazovMesiaca} ${cisloRoku}"

        //graf
        val s1 = Segment(getString(R.string.text_prijmy), if(prijmyMesiac > 0) prijmyMesiac else 0.01)
        val s2 = Segment(getString(R.string.text_vydavky), if(vydavkyMesiac > 0) vydavkyMesiac else 0.01)

        val sf1 = SegmentFormatter(ContextCompat.getColor(requireContext(), R.color.green))
        val sf2 = SegmentFormatter(ContextCompat.getColor(requireContext(), R.color.red))

        val graf :PieChart = binding.zhrnutieGraf;

        graf.addSegment(s1, sf1)
        graf.addSegment(s2, sf2)
        graf.borderPaint.setColor(Color.TRANSPARENT)
        graf.backgroundPaint.setColor(Color.TRANSPARENT)


        //prostriedky
        val penazenkaSum : Float = polozkaDao.getSumPenazenkaPrijmy() - polozkaDao.getSumPenazenkaVydavky();
        val penazenkaLast : Date = polozkaDao.getLastUsePenazenka();
        val bankUcetSum : Float = polozkaDao.getSumBankUcetPrijmy() - polozkaDao.getSumBankUcetVydavky();
        val bankUcetLast : Date = polozkaDao.getLastUseBankUcet();

        val penazenkaHodnota : TextView = binding.hotovostHodnota;
        val penazenkaNaposledy : TextView = binding.hotovostNaposledyDatum;

        val bankUcetHodnota : TextView = binding.bankaHodnota;
        val bankUcetNaposledy : TextView = binding.bankaNaposledyDatum;

        penazenkaHodnota.text = pridajEuro(penazenkaSum.formatFloatToString());
        penazenkaNaposledy.text = penazenkaLast?.toMyDate()

        bankUcetHodnota.text = pridajEuro(bankUcetSum.formatFloatToString());
        bankUcetNaposledy.text = bankUcetLast?.toMyDate();

        //setrenie
        val nazovSetrenia : TextView = binding.setreniePolozka
        val sumaSetrenia : TextView = binding.setrenieSuma
        val progresSetrenie : ProgressBar = binding.setrenieProgress
        val sumaNaSetreni : TextView = binding.setrenieSumanasetreni

        val setrenie : Setrenie = setrenieDao.getSetrenie();
        val stavNaSetreni : Float = polozkaDao.getSumSetrenia(setrenie?.datum);

        // ak máme šetrenie
        @Suppress("SENSELESS_COMPARISON")
        if(setrenie != null)
        {
            nazovSetrenia.text = setrenie.nazov
            sumaSetrenia.text = pridajEuro(setrenie.suma.formatFloatToString())
            progresSetrenie.max = setrenie.suma.toInt()
            progresSetrenie.progress = stavNaSetreni.toInt()
            sumaNaSetreni.text = pridajEuro(stavNaSetreni.formatFloatToString());
        }
        // ak nemáme
        else
        {
            nazovSetrenia.text = getString(R.string.ziadnesetrenie)
            sumaSetrenia.text = pridajEuro("0.00")
            progresSetrenie.max = 0
            progresSetrenie.progress = 0
            sumaNaSetreni.text = pridajEuro("0.00");
        }

        return binding.root;
    }

    /**
     * On resume
     *
     */
    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.text_prehlad)
    }
}