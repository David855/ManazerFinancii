package com.example.manazerfinancii

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.manazerfinancii.databinding.ActivityMainBinding
import com.example.manazerfinancii.grafickeRozhrania.NastaveniaFragment
import com.example.manazerfinancii.grafickeRozhrania.PolozkaFragment
import com.example.manazerfinancii.grafickeRozhrania.PrehladFragment
import com.example.manazerfinancii.grafickeRozhrania.SetrenieFragment
import com.example.manazerfinancii.notifikacie.NotificationSetting
import com.google.android.material.navigation.NavigationView

/**
 * Main activity
 * Hlavná aktivita, ktorá vykresľuje fragmenty
 *
 * @constructor Vytvorenie Main activity
 */
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var binding: ActivityMainBinding

    /**
     * On create
     *
     * @param savedInstanceState savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // binding
        binding = DataBindingUtil.setContentView(this,
                R.layout.activity_main)

        drawerLayout = binding.drawerLayout

        // získanie a nastavenie toolbaru
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        // získanie navigácie
        val navigationView : NavigationView  = binding.navView
        navigationView.setNavigationItemSelectedListener(this)

        // pridanie hamburgeru
        val toggle: ActionBarDrawerToggle  = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //ak nie je nič uložené, zobraz fragment prehľad
        if(savedInstanceState == null)
        {
            supportFragmentManager.beginTransaction().replace(R.id.myFragment, PrehladFragment()).commit()
            navigationView.setCheckedItem(R.id.item_prehlad)
        }
        else
        {
            title = savedInstanceState.getString("title")
        }

        //notifikacia
        NotificationSetting(this).schedulePushNotifications()
    }

    /**
     * Vybraná položka v navigácií
     *
     * @param item vybratý item
     * @return true/false
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // podľa vybratej položky sa zobrazí fragment
        when(item.itemId)
        {
            R.id.item_prehlad -> {
                replaceFragment(PrehladFragment());
            }
            R.id.item_prijmy -> {
                val bundle = Bundle();
                bundle.putInt("typId", 1)

                val fragment = PolozkaFragment();
                fragment.arguments = bundle;
                replaceFragment(fragment);
            }
            R.id.item_vydavky -> {
                val bundle = Bundle();
                bundle.putInt("typId", 2)

                val fragment = PolozkaFragment();
                fragment.arguments = bundle;
                replaceFragment(fragment);
            }
            R.id.item_setrenie -> {
                replaceFragment(SetrenieFragment());
            }
            R.id.item_nastavenia ->
            {
                replaceFragment(NastaveniaFragment());
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }

    /**
     * Pri ukladaní stavu
     *
     * @param outState outState
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("title", title as String?);
    }

    /**
     * V prípade, že bolo stlačené tlačidlo späť
     *
     */
    override fun onBackPressed() {
        // ak je zobrazená navigácia
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            //zavretie navigácie
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        else
        {
            // ak mám backstacks tak ma presmeruje späť
            if(supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStackImmediate()

                // ak aktuálny fragment sa rovná niektorému z fragmentov v navigácií, nastaví sa set item checked v navigácií
                val actual : Fragment? = supportFragmentManager.findFragmentById(R.id.myFragment);
                if(actual is PrehladFragment)
                    binding.navView.setCheckedItem(R.id.item_prehlad)
                else if(actual is PolozkaFragment && actual.arguments?.getInt("typId") == 1)
                    binding.navView.setCheckedItem(R.id.item_prijmy)
                else if(actual is PolozkaFragment && actual.arguments?.getInt("typId") == 2)
                    binding.navView.setCheckedItem(R.id.item_vydavky)
                else if(actual is SetrenieFragment)
                    binding.navView.setCheckedItem(R.id.item_setrenie)
                else if(actual is NastaveniaFragment)
                    binding.navView.setCheckedItem(R.id.item_nastavenia)
            }
            // inak sa zatvorí aplikácia
            else
                closeApp();
        }
    }

    /**
     * Nahradenie fragmentu
     *
     * @param fragment fragment
     */
    private fun replaceFragment(fragment: Fragment)
    {
        val actual : Fragment? = supportFragmentManager.findFragmentById(R.id.myFragment);

        // ak sa nejedná o rovnaké fragmenty
        if(actual?.javaClass?.name != fragment.javaClass.name || actual?.arguments?.getInt("typId") != fragment.arguments?.getInt("typId"))
            supportFragmentManager.beginTransaction().replace(R.id.myFragment, fragment).addToBackStack(null).commit()
    }

    /**
     * Modalové okno pri zatvaraní aplikácie
     *
     */
    private fun closeApp()
    {
        // modálové okno s ikonou
        val builder = AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setIcon(R.drawable.ic_exit_app)
        builder.setTitle(getString(R.string.close_app))
        builder.setMessage(getString(R.string.close_app_dialog))

        //pri kliknutí na áno
        builder.setPositiveButton(getString(R.string.close_app_yes)) {
            _, _-> finish()
        }

        //pri kliknutí na nie
        builder.setNegativeButton(getString(R.string.close_app_no)) {
            _, _->
        }

        builder.setCancelable(false);

        //zobrazenie alertu
        val alert = builder.create()
        alert.show()
    }
}