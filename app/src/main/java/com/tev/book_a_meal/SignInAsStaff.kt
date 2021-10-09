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
import com.tev.book_a_meal.SignInAsStaff
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.lang.Boolean

class SignInAsStaff : AppCompatActivity() {
    var edtPhone: EditText? = null
    var edtPassword: EditText? = null
    var btnSignInAsStaff: Button? = null
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
        setContentView(R.layout.activity_sign_in)
        edtPhone = findViewById<View>(R.id.edtPhone) as MaterialEditText
        edtPassword = findViewById<View>(R.id.edtPassword) as MaterialEditText
        edtPassword!!.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        edtPassword!!.transformationMethod = PasswordTransformationMethod()}}
//        btnSignInAsStaff = findViewById<View>(R.id.btnSignInAsStaff) as FButton

        //Init database
//        db = Database.getInstance()
//        users = db!!.getReference("User")
//        btnSignInAsStaff!!.setOnClickListener {
//            signInUser(
//                edtPhone!!.text.toString(),
//                edtPassword!!.text.toString()
//            )
//        }
//    }

//    private fun signInUser(phone: String, password: String) {
//        val mDialog = ProgressDialog(this@SignInAsStaff)
//        mDialog.setMessage("Please waiting...")
//        mDialog.show()
//        users!!.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dataSnapshot.child(phone).exists()) {
//                    mDialog.dismiss()
//                    val user = dataSnapshot.child(phone)
//                        .getValue(
////                            User::class.java
//                        )
////                    user!!.phone = phone
//                    if (Boolean.parseBoolean(user.isstaff)) {
//
//                        //If isStaff = true
////                        if (user.password == password) {
//                            val login = Intent(this@SignInAsStaff, Home::class.java)
//                            Common.currentUser = user
//                            startActivity(login)
//                            finish()
//                        } else Toast.makeText(
//                            this@SignInAsStaff,
//                            "Wrong password!",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    } else Toast.makeText(
//                        this@SignInAsStaff,
//                        "Please login with Staff account",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else {
//                    mDialog.dismiss()
//                    Toast.makeText(
//                        this@SignInAsStaff,
//                        "User not exist in Database!",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {}
//        })
//    }
//}