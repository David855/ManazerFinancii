package com.example.manazerfinancii

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

/**
 * Start screen activity
 *
 * @constructor Vytvorenie Start screen activity
 */
class StartScreenActivity : AppCompatActivity() {
    /**
     * On create
     *
     * @param savedInstanceState savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_screen)

        // získanie obrázka
        val startScreenLogo: ImageView = findViewById(R.id.startScreenLogo)

        // animácia a spustenie novej aktivity
        startScreenLogo.alpha = 0f;
        startScreenLogo.animate().setDuration(1000).alpha(1f).withEndAction {
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}