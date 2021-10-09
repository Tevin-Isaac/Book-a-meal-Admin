package com.tev.book_a_meal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.tev.book_a_meal.MainActivity
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper




class MainActivity : AppCompatActivity() {
    var btnSignInAsAdmin: Button? = null
    var btnSignInAsStaff: Button? = null
    var lottieAnimationView: LottieAnimationView? = null
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lottieAnimationView = findViewById(R.id.animationView);


        //add calligraphy
        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/restaurant_font.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
        setContentView(R.layout.activity_main)
        btnSignInAsAdmin = findViewById<View>(R.id.btnSignInAsAdmin) as Button
        btnSignInAsStaff = findViewById<View>(R.id.btnSignInAsStaff) as Button
        btnSignInAsStaff!!.setOnClickListener {
            val signInAsStaff = Intent(this@MainActivity, SignInAsStaff::class.java)
            startActivity(signInAsStaff)
        }
        btnSignInAsAdmin!!.setOnClickListener {
            val signInAsAdmin = Intent(this@MainActivity, SignInAsAdmin::class.java)
            startActivity(signInAsAdmin)
        }
    }
}