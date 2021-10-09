package com.tev.book_a_meal

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.rengwuxian.materialedittext.MaterialEditText
import com.tev.book_a_meal.Model.User
import com.tev.book_a_meal.SignInAsAdmin
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.lang.Boolean

class SignInAsAdmin : AppCompatActivity() {
    var edtPhone: EditText? = null
    var edtPassword: EditText? = null
    var btnSignInAsAdmin: Button? = null
    var db: FirebaseDatabase? = null
    var users: DatabaseReference? = null
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //add calligraphy
        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/restaurant_font.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
        setContentView(R.layout.activity_sign_in_admin)
        edtPhone = findViewById<View>(R.id.edtPhone) as MaterialEditText
        edtPassword = findViewById<View>(R.id.edtPassword) as MaterialEditText
        edtPassword!!.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        edtPassword!!.transformationMethod = PasswordTransformationMethod()
        btnSignInAsAdmin = findViewById<View>(R.id.btnSignInAsAdmin) as FButton

        //Init firebase
        db = FirebaseDatabase.getInstance()
        users = db!!.getReference("User")
        btnSignInAsAdmin!!.setOnClickListener {
            signInUser(
                edtPhone!!.text.toString(),
                edtPassword!!.text.toString()
            )
        }
    }

    private fun signInUser(phone: String, password: String) {
        val mDialog = ProgressDialog(this@SignInAsAdmin)
        mDialog.setMessage("Please waiting...")
        mDialog.show()
        users!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child(phone).exists()) {
                    mDialog.dismiss()
                    val user = dataSnapshot.child(phone)
                        .getValue(
                            User::class.java
                        )
                    user!!.phone = phone
                    if (Boolean.parseBoolean(user.isadmin)) {

                        //If isAdmin = true
                        if (user.password == password) {
                            val login = Intent(this@SignInAsAdmin, MainAdminActivity::class.java)
                            Common.currentUser = user
                            startActivity(login)
                            finish()
                        } else Toast.makeText(
                            this@SignInAsAdmin,
                            "Wrong password!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else Toast.makeText(
                        this@SignInAsAdmin,
                        "Please login with Staff account",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    mDialog.dismiss()
                    Toast.makeText(
                        this@SignInAsAdmin,
                        "User not exist in Database",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}