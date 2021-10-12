package com.tev.book_a_meal

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView



import com.google.android.material.floatingactionbutton.FloatingActionButton


import com.rengwuxian.materialedittext.MaterialEditText
import com.tev.book_a_meal.Common.Common
import com.tev.book_a_meal.Common.Common.convertRole
import com.tev.book_a_meal.ManageAccount
import com.tev.book_a_meal.Model.User
import com.tev.book_a_meal.ViewHolder.UserViewHolder
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class ManageAccount : AppCompatActivity() {
    var fabAddStaff: FloatingActionButton? = null

    //database
//    var db: Database? = null
//    var users: DatabaseReference? = null
//    var adapter: RecyclerAdapter<User?, UserViewHolder?>? = null
    var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
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
        setContentView(R.layout.activity_inc_account)

        //Init view
        //Init View
        fabAddStaff = findViewById<View>(R.id.fab_add_staff) as FloatingActionButton
//        fabAddStaff!!.setOnClickListener { showCreateAccountDialog() }
//        db = Database.getInstance()
//        users = db!!.getReference(Common.Staff_TABLE)
        recyclerView = findViewById<View>(R.id.recycler_account) as RecyclerView
        layoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
//        loadAccount()
    }}

    private fun loadAccount() {
        val options = RecyclerOptions.Builder<User>()
            .setQuery(users!!, User::class.java)
           .build()
        adapter = object : RecyclerAdapter<User, UserViewHolder>(options) {
            override fun onBindViewHolder(viewHolder: UserViewHolder, position: Int, model: User) {
                viewHolder.staffName.text = model.phone
                viewHolder.staffPassword.text = model.name
                viewHolder.staffRole.text = Common.convertRole(model.isstaff)

                //new event
                viewHolder.btnEditAccount.setOnClickListener {
                    showEditAccountDialog(
                        adapter!!.getRef(
                            position
                        ).key, model
                    )
                }
                viewHolder.btnDeleteAccount.setOnClickListener {
                    showDeleteAccountDialog(
                        adapter!!.getRef(
                            position
                        ).key
                    )
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
                val itemView = LayoutInflater.from(parent.context)
                   .inflate(R.layout.activity_manage_account, parent, false)
                return UserViewHolder(itemView)
            }
        }
        adapter.startListening()
        recyclerView!!.adapter = adapter
    }

    private fun showCreateAccountDialog() {
        val alertDialog =
            AlertDialog.Builder(this@ManageAccount, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
        alertDialog.setTitle("CREATE STAFF ACCOUNT")
        alertDialog.setMessage("Please fill in all information")
        val inflater = LayoutInflater.from(this)
        val layout_account = inflater.inflate(R.layout.create_account_layout, null)
        val account_phone =
            layout_account.findViewById<View>(R.id.create_account_phone) as MaterialEditText
        val account_name =
            layout_account.findViewById<View>(R.id.create_account_name) as MaterialEditText
        val account_password =
            layout_account.findViewById<View>(R.id.create_account_password) as MaterialEditText
        account_password.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        account_password.transformationMethod = PasswordTransformationMethod()
        alertDialog.setView(layout_account)
        alertDialog.setIcon(R.drawable.ic_create_black_24dp)
        alertDialog.setPositiveButton("CREATE") { dialog, which ->
            dialog.dismiss()
            //create account
           if (TextUtils.isEmpty(account_phone.text)) {
                Toast.makeText(this@ManageAccount, "Phone Number is Empty!", Toast.LENGTH_SHORT)
                    .show()
            } else if (TextUtils.isEmpty(account_name.text)) {
                Toast.makeText(this@ManageAccount, "Username is Empty!", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(account_password.text)) {
                Toast.makeText(this@ManageAccount, "Password is Empty!", Toast.LENGTH_SHORT).show()
            } else if (account_phone.text.length < 11) {
                Toast.makeText(
                    this@ManageAccount,
                    "Phone Number cannot less than 11 digts!",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (account_phone.text.length > 13) {
                Toast.makeText(
                    this@ManageAccount,
                    "Phone Number cannot exceed 13 digits!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val user = User()
                user.phone = account_phone.text.toString()
                user.name = account_name.text.toString()
                user.password = account_password.text.toString()
                user.isstaff = "true"
                user.isadmin = "false"
                user!!.child(account_phone.text.toString())
                    .setValue(user)
                    .addOnSuccessListener {
                        Toast.makeText(
                            this@ManageAccount,
                            "Staff Created Successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this@ManageAccount,
                            "Failed to Create Account!",
                            Toast.LENGTH_SHORT
                       ).show()
                    }
            }
        }
        alertDialog.setNegativeButton("CANCEL") { dialog, which -> dialog.dismiss() }
        alertDialog.show()
    }

    private fun showEditAccountDialog(key: String?, model: User) {
        val alertDialog =
            AlertDialog.Builder(this@ManageAccount, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
        alertDialog.setTitle("UPDATE ACCOUNT")
        alertDialog.setMessage("Please fill in all information")
        val inflater = LayoutInflater.from(this)
        val layout_account = inflater.inflate(R.layout.create_account_layout, null)
        val account_phone =
            layout_account.findViewById<View>(R.id.create_account_phone) as MaterialEditText
        val account_name =
            layout_account.findViewById<View>(R.id.create_account_name) as MaterialEditText
        val account_password =
            layout_account.findViewById<View>(R.id.create_account_password) as MaterialEditText
        account_password.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        account_password.transformationMethod = PasswordTransformationMethod()

            //set data
        account_name.setText(model.name)
        account_password.setText(model.password)
        account_phone.setText(model.phone)
        account_phone.isEnabled = false
        alertDialog.setView(layout_account)
        alertDialog.setIcon(R.drawable.ic_create_black_24dp)
        alertDialog.setPositiveButton("UPDATE") { dialog, which ->
            dialog.dismiss()
            //create account
            if (TextUtils.isEmpty(account_phone.text)) {
                Toast.makeText(this@ManageAccount, "Phone Number is Empty!", Toast.LENGTH_SHORT)
                    .show()
            } else if (TextUtils.isEmpty(account_name.text)) {
                Toast.makeText(this@ManageAccount, "Username is Empty!", Toast.LENGTH_SHORT).show()
            } else if (account_phone.text.length < 11) {
                Toast.makeText(
                    this@ManageAccount,
                    "Phone Number cannot less than 11 digts!",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (account_phone.text.length > 13) {
                Toast.makeText(
                    this@ManageAccount,
                    "Phone Number cannot exceed 13 digits!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val user = User()
                user.phone = account_phone.text.toString()
                user.name = account_name.text.toString()
                user.password = account_password.text.toString()
                user.isstaff = "true"
                user.isadmin = "false"
                users!!.child(account_phone.text.toString())
                    .setValue(user)
                    .addOnSuccessListener {
                        Toast.makeText(
                            this@ManageAccount,
                            "Staff Created Successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this@ManageAccount,
                            "Failed to Create Account!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
        alertDialog.setNegativeButton("CANCEL") { dialog, which -> dialog.dismiss() }
        alertDialog.show()
    }

    private fun showDeleteAccountDialog(key: String?) {
        val alertDialog =
            AlertDialog.Builder(this@ManageAccount, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
        alertDialog.setTitle("Confirm Delete?")
        val inflater = this.layoutInflater
        val confirm_delete_layout = inflater.inflate(R.layout.confirm_delete_layout, null)
        alertDialog.setView(confirm_delete_layout)
        alertDialog.setIcon(R.drawable.ic_delete_black_24dp)
        alertDialog.setPositiveButton("DELETE") { dialog, which ->
            dialog.dismiss()
            users!!.child(key!!).removeValue()
            Toast.makeText(this@ManageAccount, "Account Delete Successfully!", Toast.LENGTH_SHORT)
                .show()
        }
        alertDialog.setNegativeButton("CANCEL") { dialog, which -> dialog.dismiss() }
        alertDialog.show()
        adapter!!.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        adapter!!.startListening()
        loadAccount()
    }
}
