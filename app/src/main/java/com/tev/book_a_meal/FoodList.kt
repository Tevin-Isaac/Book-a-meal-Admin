package com.tev.book_a_meal

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle


import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*


import com.rengwuxian.materialedittext.MaterialEditText
import com.squareup.picasso.Picasso
import com.tev.book_a_meal.FoodList
import com.tev.book_a_meal.Model.Food
import com.tev.book_a_meal.ViewHolder.FoodViewHolder
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*

class FoodList : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var fab: FloatingActionButton? = null
    var rootLayout: RelativeLayout? = null

    //Firebase
    var db: FirebaseDatabase? = null
    var foodList: DatabaseReference? = null
//    var storage: datbaseStorage? = null
//    var storageReference: StorageReference? = null
    var categoryId: String? = ""
//    var adapter: RecyclerAdapter<Food?, FoodViewHolder?>? = null

    //add new food
    var edtName: MaterialEditText? = null
    var edtDescription: MaterialEditText? = null
    var edtPrice: MaterialEditText? = null
    var btnSelect: FButton? = null
    var btnUpload: FButton? = null
    var newFood: Food? = null
    var saveUri: Uri? = null
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
        setContentView(R.layout.activity_food_list)}}

//        //Firebase
//        db = FirebaseDatabase.getInstance()
//        foodList = db!!.getReference("Foods")
//        storage = FirebaseStorage.getInstance()
//        storageReference = storage!!.reference
//
//        //Init
//        recyclerView = findViewById<View>(R.id.recycler_food) as RecyclerView
//        recyclerView!!.setHasFixedSize(true)
//        layoutManager = LinearLayoutManager(this)
//        recyclerView!!.layoutManager = layoutManager
//
//        //set layout of foodlist
//        rootLayout = findViewById<View>(R.id.rootLayout) as RelativeLayout
//        fab = findViewById<View>(R.id.fab) as FloatingActionButton
//        fab!!.setOnClickListener { showAddFoodDialog() }
//        if (intent != null) categoryId = intent.getStringExtra("CategoryId")
//        if (!categoryId!!.isEmpty()) loadListFood(categoryId)
//    }
//
//    private fun showAddFoodDialog() {
//
//        //add food menu
//        val alertDialog =
//            AlertDialog.Builder(this@FoodList, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
//        alertDialog.setTitle("Add New Food")
//        alertDialog.setMessage("Please fill full formation")
//        val inflater = this.layoutInflater
//        val add_menu_layout = inflater.inflate(R.layout.add_new_food_layout, null)
//        edtName = add_menu_layout.findViewById(R.id.edtName)
//        edtDescription = add_menu_layout.findViewById(R.id.edtDescription)
//        edtPrice = add_menu_layout.findViewById(R.id.edtPrice)
//        btnSelect = add_menu_layout.findViewById(R.id.btnSelect)
//        btnUpload = add_menu_layout.findViewById(R.id.btnUpload)
//
//        //Event for button
//        btnSelect.setOnClickListener(View.OnClickListener { //let users select image from gallery and save URL of this image
//            chooseImage()
//        })
//        btnUpload.setOnClickListener(View.OnClickListener { //upload image
//            uploadImage()
//        })
//        alertDialog.setView(add_menu_layout)
//        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp)
//
//        //set Button
//        alertDialog.setPositiveButton("YES") { dialog, which ->
//            dialog.dismiss()
//
//            //create new category
//            if (newFood != null) {
//                foodList!!.push().setValue(newFood)
//                Snackbar.make(
//                    rootLayout!!, " New Food " + newFood!!.name + " was added ",
//                    Snackbar.LENGTH_SHORT
//                ).show()
//            }
//        }
//        alertDialog.setNegativeButton("NO") { dialog, which -> dialog.dismiss() }
//        alertDialog.show()
//    }
//
//    private fun uploadImage() {
//        if (saveUri != null) {
//            val mDialog = ProgressDialog(this)
//            mDialog.setMessage("Uploading...")
//            mDialog.show()
//            val imageName = UUID.randomUUID().toString()
//            val imageFolder = storageReference!!.child("images/$imageName")
//            imageFolder.putFile(saveUri!!).addOnSuccessListener {
//                mDialog.dismiss()
//                Toast.makeText(this@FoodList, "Uploaded!!!", Toast.LENGTH_SHORT).show()
//                imageFolder.downloadUrl.addOnSuccessListener { uri -> //set value for newCategory if image upload and we can get download link
//                    newFood = Food()
//                    newFood!!.name = edtName!!.text.toString()
//                    newFood!!.description = edtDescription!!.text.toString()
//                    newFood!!.price = edtPrice!!.text.toString()
//                    newFood!!.menuId = categoryId
//                    newFood!!.image = uri.toString()
//                }
//            }
//                .addOnFailureListener { e ->
//                    mDialog.dismiss()
//                    Toast.makeText(this@FoodList, "" + e.message, Toast.LENGTH_SHORT).show()
//                }
//                .addOnProgressListener { taskSnapshot ->
//                    val progress =
//                        (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toDouble()
//                    mDialog.setMessage("Uploading$progress % ")
//                }
//        }
//    }
//
//    private fun chooseImage() {
//        val intent = Intent()
//        intent.type = "image/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(
//            Intent.createChooser(intent, "Select Image"),
//            Common.PICK_IMAGE_REQUEST
//        )
//    }
//
//    private fun loadListFood(categoryId: String?) {
//        val listFoodByCategoryId = foodList!!.orderByChild("menuId").equalTo(categoryId)
//        val options = FirebaseRecyclerOptions.Builder<Food>()
//            .setQuery(listFoodByCategoryId, Food::class.java)
//            .build()
//        adapter = object : FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
//            override fun onBindViewHolder(viewHolder: FoodViewHolder, position: Int, model: Food) {
//                viewHolder.food_name.text = model.name
//                Picasso.with(baseContext).load(model.image).into(viewHolder.food_image)
//                viewHolder.setItemClickListener { view, position, isLongClick -> }
//            }
//
//            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
//                val itemView = LayoutInflater.from(parent.context)
//                    .inflate(R.layout.food_item, parent, false)
//                return FoodViewHolder(itemView)
//            }
//        }
//        adapter.startListening()
//        adapter.notifyDataSetChanged()
//        recyclerView!!.adapter = adapter
//    }
//
//    override fun onStop() {
//        super.onStop()
//        adapter!!.stopListening()
//    }
//
//    override fun onStart() {
//        super.onStart()
//        adapter!!.startListening()
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
//            saveUri = data.data
//            btnSelect!!.text = "Image Selected!"
//        }
//    }
//
//    override fun onContextItemSelected(item: MenuItem): Boolean {
//        if (item.title == Common.UPDATE) {
//            showUpdateFoodDialog(adapter!!.getRef(item.order).key, adapter!!.getItem(item.order))
//        } else if (item.title == Common.DELETE) {
//            ConfirmDeleteDialog(item)
//        }
//        return super.onContextItemSelected(item)
//    }
//
//    private fun ConfirmDeleteDialog(item: MenuItem) {
//        val alertDialog =
//            AlertDialog.Builder(this@FoodList, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
//        alertDialog.setTitle("Confirm Delete?")
//        val inflater = this.layoutInflater
//        val confirm_delete_layout = inflater.inflate(R.layout.confirm_delete_layout, null)
//        alertDialog.setView(confirm_delete_layout)
//        alertDialog.setIcon(R.drawable.ic_delete_black_24dp)
//        alertDialog.setPositiveButton("DELETE") { dialog, which ->
//            dialog.dismiss()
//            deleteFood(adapter!!.getRef(item.order).key)
//        }
//        alertDialog.setNegativeButton("CANCEL") { dialog, which -> dialog.dismiss() }
//        alertDialog.show()
//    }
//
//    private fun showUpdateFoodDialog(key: String?, item: Food) {
//        val alertDialog =
//            AlertDialog.Builder(this@FoodList, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
//        alertDialog.setTitle("Edit Food")
//        alertDialog.setMessage("Please fill full formation")
//        val inflater = this.layoutInflater
//        val add_menu_layout = inflater.inflate(R.layout.add_new_food_layout, null)
//        edtName = add_menu_layout.findViewById(R.id.edtName)
//        edtDescription = add_menu_layout.findViewById(R.id.edtDescription)
//        edtPrice = add_menu_layout.findViewById(R.id.edtPrice)
//        btnSelect = add_menu_layout.findViewById(R.id.btnSelect)
//        btnUpload = add_menu_layout.findViewById(R.id.btnUpload)
//
//        //set default value for view
//        edtName.setText(item.name)
//        edtDescription.setText(item.name)
//        edtPrice.setText(item.name)
//
//        //Event for button
//        btnSelect.setOnClickListener(View.OnClickListener { //let users select image from gallery and save URL of this image
//            chooseImage()
//        })
//        btnUpload.setOnClickListener(View.OnClickListener { //upload image
//            changeImage(item)
//        })
//        alertDialog.setView(add_menu_layout)
//        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp)
//
//        //set Button
//        alertDialog.setPositiveButton("YES") { dialog, which ->
//            dialog.dismiss()
//
//
//            //update information
//            item.name = edtName.getText().toString()
//            item.description = edtDescription.getText().toString()
//            item.price = edtPrice.getText().toString()
//            foodList!!.child(key!!).setValue(item)
//            Snackbar.make(
//                rootLayout!!, " Food " + item.name + " was edited ",
//                Snackbar.LENGTH_SHORT
//            ).show()
//        }
//        alertDialog.setNegativeButton("NO") { dialog, which -> dialog.dismiss() }
//        alertDialog.show()
//    }
//
//    private fun changeImage(item: Food) {
//        if (saveUri != null) {
//            val mDialog = ProgressDialog(this)
//            mDialog.setMessage("Uploading...")
//            mDialog.show()
//            val imageName = UUID.randomUUID().toString()
//            val imageFolder = storageReference!!.child("images/$imageName")
//            imageFolder.putFile(saveUri!!).addOnSuccessListener {
//                mDialog.dismiss()
//                Toast.makeText(this@FoodList, "Uploaded!!!", Toast.LENGTH_SHORT).show()
//                imageFolder.downloadUrl.addOnSuccessListener { uri -> //set value for newCategory if image upload and we can get download link
//                    item.image = uri.toString()
//                    Toast.makeText(this@FoodList, "Image Changed Successfully!", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            }
//                .addOnFailureListener { e ->
//                    mDialog.dismiss()
//                    Toast.makeText(this@FoodList, "" + e.message, Toast.LENGTH_SHORT).show()
//                }
//                .addOnProgressListener { taskSnapshot ->
//                    val progress =
//                        (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toDouble()
//                    mDialog.setMessage("Uploading$progress % ")
//                }
//        }
//    }
//
//    private fun deleteFood(key: String?) {
//
//        //get all food in category
//        val foods = db!!.getReference("Foods")
//        val foodInCategory = foods.orderByChild("menuId").equalTo(key)
//        foodInCategory.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                for (postSnapShot in dataSnapshot.children) {
//                    postSnapShot.ref.removeValue()
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {}
//        })
//        foodList!!.child(key!!).removeValue()
//        Toast.makeText(this@FoodList, "Food Deleted Successfully!", Toast.LENGTH_SHORT).show()
//    }
//}