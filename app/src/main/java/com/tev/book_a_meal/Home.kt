package com.tev.book_a_meal

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.icu.number.NumberFormatter.with
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar


import com.rengwuxian.materialedittext.MaterialEditText
import com.squareup.picasso.Picasso
import com.tev.book_a_meal.Common.Common
import com.tev.book_a_meal.Home
import com.tev.book_a_meal.Model.*
import com.tev.book_a_meal.ViewHolder.MenuViewHolder
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*

class Home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var txtFullName: TextView? = null

    //database
//    var database: Database? = null
//    var categories: DatabaseReference? = null
//    var storage: Storage? = null
//    var storageReference: StorageReference? = null
//    var adapter: RecyclerAdapter<Category?, MenuViewHolder?>? = null

    //View
   var recycler_menu: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null

    //Add new menu layout
    var edtName: MaterialEditText? = null
    var btnUpload: FButton? = null
    var btnSelect: FButton? = null
    var newCategory: Category? = null
    var saveUri: Uri? = null
    var drawer: DrawerLayout? = null
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
        setContentView(R.layout.activity_home)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setTitleTextColor(resources.getColor(R.color.colorWhite))
        toolbar.title = "Menu Management"
       setSupportActionBar(toolbar)

////        //Init database
////        database = Database.getInstance()
////        categories = database!!.getReference("Category")
////        storage = databaseStorage.getInstance()
////        storageReference = storage!!.reference
        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { showDialog() }
        drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        //set name for user
        val headerView = navigationView.getHeaderView(0)
        txtFullName = headerView.findViewById<View>(R.id.txtFullName) as TextView
        txtFullName!!.text = Common.currentUser!!.name

        //init view
        recycler_menu = findViewById<View>(R.id.recycler_menu) as RecyclerView
        recycler_menu!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recycler_menu!!.layoutManager = layoutManager
        loadMenu()

        //send token
        updateToken(InstanceId.getInstance().token)
    }
//
//    private fun updateToken(token: String?) {
//        val db = databaseDatabase.getInstance()
//        val tokens = db.getReference("Tokens")
//        val data = Token(token, true)
//        // false because token send from client app
//        tokens.child(Common.currentUser!!.phone).setValue(data)
//    }
//
    private fun showDialog() {
        val alertDialog =
            AlertDialog.Builder(this@Home, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
        alertDialog.setTitle("Add New Category")
        alertDialog.setMessage("Please fill full formation")
        val inflater = this.layoutInflater
        val add_menu_layout = inflater.inflate(R.layout.add_new_menu_layout, null)
        edtName = add_menu_layout.findViewById(R.id.edtName)
        btnSelect = add_menu_layout.findViewById(R.id.btnSelect)
        btnUpload = add_menu_layout.findViewById(R.id.btnUpload)

        //Event for button
        btnSelect.setOnClickListener(View.OnClickListener { //let users select image from gallery and save URL of this image
            chooseImage()
        })
        btnUpload.setOnClickListener(View.OnClickListener { //upload image
            uploadImage()
        })
        alertDialog.setView(add_menu_layout)
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp)

        //set Button
        alertDialog.setPositiveButton("YES") { dialog, which ->
            dialog.dismiss()

            //create new category
            if (newCategory != null) {
                categories!!.push().setValue(newCategory)
                Snackbar.make(
                   drawer!!, " New Category " + newCategory!!.name + " was added ",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        alertDialog.setNegativeButton("NO") { dialog, which -> dialog.dismiss() }
        alertDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            saveUri = data.data
            btnSelect!!.text = "Image Selected!"
        }
    }
//
    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Image"),
            Common.PICK_IMAGE_REQUEST
        )
    }

    private fun uploadImage() {
        if (saveUri != null) {
            val mDialog = ProgressDialog(this)
            mDialog.setMessage("Uploading...")
            mDialog.show()
            val imageName = UUID.randomUUID().toString()
            val imageFolder = storageReference!!.child("images/$imageName")
            imageFolder.putFile(saveUri!!).addOnSuccessListener {
                mDialog.dismiss()
                Toast.makeText(this@Home, "Uploaded!!!", Toast.LENGTH_SHORT).show()
                imageFolder.downloadUrl.addOnSuccessListener { uri -> //set value for newCategory if image upload and we can get download link newCategory = Category(edtName!!.text.toString(), uri.toString())
                }
            }
                .addOnFailureListener { e ->
                    mDialog.dismiss()
                    Toast.makeText(this@Home, "" + e.message, Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener { taskSnapshot ->
                    val progress =
                        (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toDouble()
                    mDialog.setMessage("Uploading$progress % ")
                }
        }
    }

    private fun loadMenu() {
        val options = RecyclerOptions.Builder<Category>()
            .setQuery(categories!!, Category::class.java)
            .build()
        adapter = object : RecyclerAdapter<Category, MenuViewHolder>(options) {
            override fun onBindViewHolder(
                viewHolder: MenuViewHolder,
                position: Int,
                model: Category
            ) {
                viewHolder.txtMenuName.text = model.name
                Picasso.with(this@Home).load(model.image).into(viewHolder.imageView)
                viewHolder.setItemClickListener { view, position, isLongClick -> //send category Id and start new activity
                    val foodList = Intent(this@Home, FoodList::class.java)
                    foodList.putExtra("CategoryId", adapter!!.getRef(position).key)
                    startActivity(foodList)
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.menu_item, parent, false)
                return MenuViewHolder(itemView)
            }
        }

        //refresh data if have data changed
        adapter.startListening()
        adapter.notifyDataSetChanged()
        recycler_menu!!.adapter = adapter
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
           drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        if (id == R.id.nav_orders) {
            val orders = Intent(this@Home, OrderStatus::class.java)
            startActivity(orders)
       } else if (id == R.id.nav_banner) {
            val banner = Intent(this@Home, BannerActivity::class.java)
            startActivity(banner)
        } else if (id == R.id.nav_message) {
            val message = Intent(this@Home, SendMessage::class.java)
            startActivity(message)
        } else if (id == R.id.nav_sign_out) {
            ConfirmSignOutDialog()
        } else if (id == R.id.nav_about) {
            val about = Intent(this@Home, ScrollingActivity::class.java)
            startActivity(about)
        }
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    //Update and delete
    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.title == Common.UPDATE) {
            showUpdateDialog(adapter!!.getRef(item.order).key, adapter!!.getItem(item.order))
        } else if (item.title == Common.DELETE) {
            ConfirmDeleteDialog(item)
        }
        return super.onContextItemSelected(item)
    }

    private fun ConfirmSignOutDialog() { val alertDialog =
            AlertDialog.Builder(this@Home, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
        alertDialog.setTitle("Confirm Sign Out?")
        val inflater = LayoutInflater.from(this)
        val layout_signout = inflater.inflate(R.layout.confirm_signout_layout, null)
        alertDialog.setView(layout_signout)
        alertDialog.setIcon(R.drawable.ic_exit_to_app_black_24dp)
        alertDialog.setPositiveButton("SIGN OUT") { dialog, which ->
            dialog.dismiss()
            val signout = Intent(this@Home, MainActivity::class.java)
            startActivity(signout)
        }
        alertDialog.setNegativeButton("CANCEL") { dialog, which -> dialog.dismiss() }
        alertDialog.show()
    }

    private fun ConfirmDeleteDialog(item: MenuItem) {
        val alertDialog =
            AlertDialog.Builder(this@Home, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
        alertDialog.setTitle("Confirm Delete?")
        val inflater = this.layoutInflater
        val confirm_delete_layout = inflater.inflate(R.layout.confirm_delete_layout, null)
        alertDialog.setView(confirm_delete_layout)
        alertDialog.setIcon(R.drawable.ic_delete_black_24dp)
        alertDialog.setPositiveButton("DELETE") { dialog, which ->
            dialog.dismiss()
            deleteCategory(adapter!!.getRef(item.order).key)
        }
        alertDialog.setNegativeButton("CANCEL") { dialog, which -> dialog.dismiss() }
        alertDialog.show()
    }

    private fun showUpdateDialog(key: String?, item: Category) {
        val alertDialog =
            AlertDialog.Builder(this@Home, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
        alertDialog.setTitle("Update Category")
        alertDialog.setMessage("Please fill full formation")
        val inflater = this.layoutInflater
        val add_menu_layout = inflater.inflate(R.layout.add_new_menu_layout, null)
        edtName = add_menu_layout.findViewById(R.id.edtName)
        btnSelect = add_menu_layout.findViewById(R.id.btnSelect)
        btnUpload = add_menu_layout.findViewById(R.id.btnUpload)

        //set default name
        edtName.setText(item.name)

        //Event for button
        btnSelect.setOnClickListener(View.OnClickListener { //let users select image from gallery and save URL of this image
            chooseImage()
        })
        btnUpload.setOnClickListener(View.OnClickListener { //upload image
            changeImage(item)
        })
        alertDialog.setView(add_menu_layout)
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp)

        //set Button
        alertDialog.setPositiveButton("YES") { dialog, which ->
            dialog.dismiss()

            //update information
            item.name = edtName.getText().toString()
            categories!!.child(key!!).setValue(item)
            Toast.makeText(this@Home, "Category Name Updated Successfully!", Toast.LENGTH_SHORT)
                .show()
        }
        alertDialog.setNegativeButton("NO") { dialog, which -> dialog.dismiss() }
        alertDialog.show()
    }

    private fun deleteCategory(key: String?) {

        //get all food in category
//        val foods = database!!.getReference("Foods")
//        val foodInCategory = foods.orderByChild("menuId").equalTo(key)
//        foodInCategory.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                for (postSnapShot in dataSnapshot.children) {
//                    postSnapShot.ref.removeValue()
//                }
//            }

//            override fun onCancelled(databaseError: DatabaseError) {}
//        })
//        categories!!.child(key!!).removeValue()
//        Toast.makeText(this@Home, "Category Deleted Successfully!", Toast.LENGTH_SHORT).show()
//    }

    private fun changeImage(item: Category) {
        if (saveUri != null) {
            val mDialog = ProgressDialog(this)
            mDialog.setMessage("Uploading...")
            mDialog.show()
            val imageName = UUID.randomUUID().toString()
            val imageFolder = storageReference!!.child("images/$imageName")
            imageFolder.putFile(saveUri!!).addOnSuccessListener {
                mDialog.dismiss()
                Toast.makeText(this@Home, "Uploaded!!!", Toast.LENGTH_SHORT).show()
                imageFolder.downloadUrl.addOnSuccessListener { uri -> //set value for newCategory if image upload and we can get download link
                    item.image = uri.toString()
                    Toast.makeText(this@Home, "Image Changed Successfully!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
                .addOnFailureListener { e ->
                    mDialog.dismiss()
                    Toast.makeText(this@Home, "" + e.message, Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener { taskSnapshot ->
                    val progress =
                        (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toDouble()
                    mDialog.setMessage("Uploading$progress % ")
                }
        }
    }
 }