package com.tev.book_a_meal

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

import com.rengwuxian.materialedittext.MaterialEditText
import com.tev.book_a_meal.Common.Common
import com.tev.book_a_meal.Model.Banner
import com.tev.book_a_meal.ViewHolder.BannerViewHolder
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*

class BannerActivity : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var fab: FloatingActionButton? = null
    var rootLayout: RelativeLayout? = null

    //base
//    var db: Database? = null
//    var banners: DatabaseReference? = null
//    var storage: databaseStorage? = null
//    var storageReference: StorageReference? = null
//    var adapter: databaseRecyclerAdapter<Banner?, BannerViewHolder?>? = null

    //Add new banner
    var edtName: MaterialEditText? = null
    var edtFoodId: MaterialEditText? = null
    var btnUpload: FButton? = null
    var btnSelect: FButton? = null
    var newBanner: Banner? = null
    var filePath: Uri? = null
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
        setContentView(R.layout.activity_banner)

        //Init database
//        db = Database.getInstance()
//        banners = db!!.getReference("Banner")
//        storage = Storage.getInstance()
//        storageReference = storage!!.reference

        //Init View
        recyclerView = findViewById<View>(R.id.recycler_banner) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        rootLayout = findViewById<View>(R.id.rootLayout) as RelativeLayout}}

        //fab
       fab = findViewById<View>(R.id.fab) as FloatingActionButton
       fab!!.setOnClickListener { showAddBanner() }
       loadListBanner()
       }

     private fun loadListBanner() {
        val allBanner = RecyclerOptions.Builder<Banner>()
            .setQuery(banners!!, Banner::class.java)
            .build()
            adapter = object : RecyclerAdapter<Banner, BannerViewHolder>(allBanner) {
            override fun onBindViewHolder(holder: BannerViewHolder, position: Int, model: Banner) {
                 holder.banner_name.text = model.name
                Picasso.with(baseContext)
                   .load(model.image)
                     .into(holder.banner_image)
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
                val itemView = LayoutInflater.from(parent.context)
                     .inflate(R.layout.banner_layout, parent, false)
                 return BannerViewHolder(itemView)
             }
         }
         adapter.startListening()

         //set adapter
         adapter.notifyDataSetChanged()
         recyclerView!!.adapter = adapter
     }

     override fun onStop() {
         super.onStop()
         adapter!!.stopListening()
     }

    private fun showAddBanner() {
        val alertDialog =
            AlertDialog.Builder(this@BannerActivity, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
       alertDialog.setTitle("Add New Banner")
        alertDialog.setMessage("Please fill full formation")
        val inflater = this.layoutInflater
         val add_menu_layout = inflater.inflate(R.layout.add_new_banner, null)
        edtFoodId = add_menu_layout.findViewById(R.id.edtFoodId)
        edtName = add_menu_layout.findViewById(R.id.edtFoodName)
        btnSelect = add_menu_layout.findViewById(R.id.btnSelect)
        btnUpload = add_menu_layout.findViewById(R.id.btnUpload)

        //set event for select picture from phone
        btnSelect.setOnClickListener(View.OnClickListener { chooseImage() })

        //set event for upload picture from phone
       btnUpload.setOnClickListener(View.OnClickListener { uploadImage() })
        alertDialog.setView(add_menu_layout)
        alertDialog.setIcon(R.drawable.ic_laptop_black_24dp)

       //set button for dialog
       alertDialog.setPositiveButton("CREATE") { dialog, which ->
           dialog.dismiss()
            if (newBanner != null) banners!!.push().setValue(newBanner) else Toast.makeText(
               this@BannerActivity,
                "Failed to Create Banner",
                Toast.LENGTH_SHORT
            ).show()
            loadListBanner()
        }
        alertDialog.setNegativeButton("CANCEL") { dialog, which -> dialog.dismiss() }
        alertDialog.show()
    }

    private fun uploadImage() {
        if (filePath != null) {
            val mDialog = ProgressDialog(this)
            mDialog.setMessage("Uploading...")
            mDialog.show()
            val imageName = UUID.randomUUID().toString()
            val imageFolder = storageReference!!.child("images/$imageName")
            imageFolder.putFile(filePath!!).addOnSuccessListener {
                mDialog.dismiss()
                Toast.makeText(this@BannerActivity, "Uploaded!!!", Toast.LENGTH_SHORT).show()
                imageFolder.downloadUrl.addOnSuccessListener { uri -> //set value for newCategory if image upload and we can get download link
                   newBanner = Banner()
                    newBanner!!.id = edtFoodId!!.text.toString()
                    newBanner!!.name = edtName!!.text.toString()
                    newBanner!!.image = uri.toString()
                }
            }
                .addOnFailureListener { e ->
                    mDialog.dismiss()
                    Toast.makeText(this@BannerActivity, "" + e.message, Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener { taskSnapshot ->
                   val progress =
                        (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toDouble()
                    mDialog.setMessage("Uploading$progress % ")
                }
        }
    }

    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Image"),
            Common.PICK_IMAGE_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            btnSelect!!.text = "Image Selected!"
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.title == Common.UPDATE) {
            showUpdateBannerDialog(adapter!!.getRef(item.order).key, adapter!!.getItem(item.order))
        } else if (item.title == Common.DELETE) {
            deleteBanner(adapter!!.getRef(item.order).key)
        }
        return super.onContextItemSelected(item)
    }

    private fun deleteBanner(key: String?) {
        banners!!.child(key!!).removeValue()
    }

    private fun showUpdateBannerDialog(key: String?, item: Banner) {
        val alertDialog = AlertDialog.Builder(this@BannerActivity)
        alertDialog.setTitle("Edit Banner")
        alertDialog.setMessage("Please fill full formation")
        val inflater = this.layoutInflater
        val edit_banner = inflater.inflate(R.layout.add_new_banner, null)
        edtName = edit_banner.findViewById(R.id.edtFoodName)
        edtFoodId = edit_banner.findViewById(R.id.edtFoodId)
        btnSelect = edit_banner.findViewById(R.id.btnSelect)
        btnUpload = edit_banner.findViewById(R.id.btnUpload)

        //set default value for view
        edtName.setText(item.name)
       edtFoodId.setText(item.id)

        //Event for button
        btnSelect.setOnClickListener(View.OnClickListener { //let users select image from gallery and save URL of this image
            chooseImage()
        })
       btnUpload.setOnClickListener(View.OnClickListener { //upload image
            changeImage(item)
        })
        alertDialog.setView(edit_banner)
        alertDialog.setIcon(R.drawable.ic_laptop_black_24dp)

        //set Button
        alertDialog.setPositiveButton("UPDATE") { dialog, which ->
            dialog.dismiss()
            item.name = edtName.getText().toString()
            item.id = edtFoodId.getText().toString()

            //make update
            val update: MutableMap<String, Any> = HashMap()
            update["name"] = item.name
            update["id"] = item.id
            update["image"] = item.image
            banners!!.child(key!!).updateChildren(update)
                .addOnCompleteListener {
                    Snackbar.make(rootLayout!!, "Updated", Snackbar.LENGTH_SHORT).show()
                    loadListBanner()
                }
            Snackbar.make(
                rootLayout!!, " Food " + item.name + " was edited ",
               Snackbar.LENGTH_SHORT
            ).show()
            loadListBanner()
        }
       alertDialog.setNegativeButton("NO") { dialog, which ->
            dialog.dismiss()
           loadListBanner()
        }
        alertDialog.show()
    }

    private fun changeImage(item: Banner) {
        if (filePath != null) {
            val mDialog = ProgressDialog(this)
            mDialog.setMessage("Uploading...")
            mDialog.show()
            val imageName = UUID.randomUUID().toString()
            val imageFolder = storageReference!!.child("images/$imageName")
            imageFolder.putFile(filePath!!).addOnSuccessListener {
               mDialog.dismiss()
                Toast.makeText(this@BannerActivity, "Uploaded!!!", Toast.LENGTH_SHORT).show()
                imageFolder.downloadUrl.addOnSuccessListener { uri -> //set value for newCategory if image upload and we can get download link
                    item.image = uri.toString()
                    Toast.makeText(
                        this@BannerActivity,
                        "Image Changed Successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
                .addOnFailureListener { e ->
                    mDialog.dismiss()
                    Toast.makeText(this@BannerActivity, "" + e.message, Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener { taskSnapshot ->
                    val progress =
                        (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toDouble()
                    mDialog.setMessage("Uploading$progress % ")
                }
        }
    }
}