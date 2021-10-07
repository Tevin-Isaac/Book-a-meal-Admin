package com.tev.book_a_meal

import android.Manifest
import com.tev.book_a_meal.Model.Order
import com.tev.book_a_meal.Remote.APIService
import com.tev.book_a_meal.Remote.FCMRetrofitClient
import com.tev.book_a_meal.Remote.IGeoCoordinates
import com.tev.book_a_meal.Remote.RetrofitClient
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.graphics.Bitmap
import org.json.JSONObject
import org.json.JSONArray
import com.google.android.gms.maps.model.LatLng
import org.json.JSONException
import android.content.ContextWrapper
import android.app.NotificationManager
import android.annotation.TargetApi
import android.os.Build
import android.app.NotificationChannel
import com.tev.book_a_meal.Helper.NotificationHelper
import android.app.PendingIntent
import com.tev.book_a_meal.R
import retrofit2.http.POST
import com.tev.book_a_meal.Model.Sender
import com.tev.book_a_meal.Model.MyResponse
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.converter.gson.GsonConverterFactory
import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.content.Intent
import com.tev.book_a_meal.OrderStatus
import android.media.RingtoneManager
import com.tev.book_a_meal.MainActivity
import android.support.v7.app.AppCompatActivity
import android.support.design.widget.NavigationView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.tev.book_a_meal.ViewHolder.MenuViewHolder
import android.support.v7.widget.RecyclerView
import com.rengwuxian.materialedittext.MaterialEditText
import info.hoang8f.widget.FButton
import android.support.v4.widget.DrawerLayout
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import android.os.Bundle
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.content.DialogInterface
import android.support.design.widget.Snackbar
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.storage.OnProgressListener
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.squareup.picasso.Picasso
import com.tev.book_a_meal.Interface.ItemClickListener
import com.tev.book_a_meal.FoodList
import android.view.ViewGroup
import android.support.v4.view.GravityCompat
import com.tev.book_a_meal.BannerActivity
import com.tev.book_a_meal.SendMessage
import com.tev.book_a_meal.ScrollingActivity
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import android.view.View.OnCreateContextMenuListener
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import com.tev.book_a_meal.ViewHolder.MyViewHolder
import com.tev.book_a_meal.Model.Food
import com.tev.book_a_meal.ViewHolder.FoodViewHolder
import com.tev.book_a_meal.ViewHolder.OrderDetailAdapter
import com.tev.book_a_meal.ViewHolder.OrderViewHolder
import com.jaredrummler.materialspinner.MaterialSpinner
import com.tev.book_a_meal.OrderDetail
import com.tev.book_a_meal.TrackingOrder
import android.text.TextUtils
import com.tev.book_a_meal.ViewHolder.ShowCommentViewHolder
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener
import com.tev.book_a_meal.Common.NumberOfFood
import com.tev.book_a_meal.SignInAsStaff
import com.tev.book_a_meal.SignInAsAdmin
import com.tev.book_a_meal.ViewHolder.UserViewHolder
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import com.tev.book_a_meal.MainAdminActivity
import com.tev.book_a_meal.Home
import android.support.v4.app.FragmentActivity
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.common.api.GoogleApiClient
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.CameraUpdateFactory
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.tev.book_a_meal.TrackingOrder.ParserTask
import kotlin.jvm.Synchronized
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.common.ConnectionResult
import android.os.AsyncTask
import android.view.View
import android.widget.*
import com.com.tev.book_a_meal.Common.DirectionJSONParser
import com.google.android.gms.maps.model.PolylineOptions
import com.com.tev.book_a_meal.Model.Banner
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.tev.book_a_meal.ViewHolder.BannerViewHolder
import com.google.android.gms.tasks.OnCompleteListener
import com.tev.book_a_meal.ManageAccount
import com.tev.book_a_meal.ViewComment
import com.tev.book_a_meal.ShipperManagement
import com.tev.book_a_meal.AdminScrollingActivity
import com.tev.book_a_meal.Model.Shipper
import com.tev.book_a_meal.ViewHolder.ShipperViewHolder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
import java.util.HashMap

class TrackingOrder : FragmentActivity(), OnMapReadyCallback, ConnectionCallbacks,
    OnConnectionFailedListener, LocationListener {
    private var mMap: GoogleMap? = null
    private var mLastLocation: Location? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mService: IGeoCoordinates? = null
    var distance: TextView? = null
    var duration: TextView? = null
    var time: TextView? = null
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
        setContentView(R.layout.activity_tracking_order)
        mService = Common.getGeoCodeService()
        distance = findViewById<View>(R.id.display_distance) as TextView
        duration = findViewById<View>(R.id.display_duration) as TextView
        time = findViewById<View>(R.id.display_expected_hour) as TextView
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestRuntimePermission()
        } else {
            if (checkPlayServices()) {
                buildGoogleApiClient()
                createLocationRequest()
            }
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
        displayLocation()


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun requestRuntimePermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (checkPlayServices()) {
                    buildGoogleApiClient()
                    createLocationRequest()
                    displayLocation()
                }
            }
        }
    }

    private fun displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestRuntimePermission()
        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
            if (mLastLocation != null) {
                val latitude = mLastLocation!!.latitude
                val longitude = mLastLocation!!.longitude

                //Add Marker in location and move the camera
                val yourLocation = LatLng(latitude, longitude)
                mMap!!.addMarker(MarkerOptions().position(yourLocation).title("Your Location"))
                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(yourLocation))
                mMap!!.animateCamera(CameraUpdateFactory.zoomTo(17.0f))

                //After add Marker for location, Add Marker for Order and draw route
                drawRoute(yourLocation, Common.currentRequest!!.address)
            } else {
                //Toast.makeText(this, "Couldn't get the location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private fun drawRoute(yourLocation: LatLng, address: String) {
        mService!!.getGeoCode(address)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    try {
                        val jsonObject = JSONObject(response.body().toString())
                        val lat = (jsonObject["results"] as JSONArray)
                            .getJSONObject(0)
                            .getJSONObject("geometry")
                            .getJSONObject("location")["lat"].toString()
                        val lng = (jsonObject["results"] as JSONArray)
                            .getJSONObject(0)
                            .getJSONObject("geometry")
                            .getJSONObject("location")["lng"].toString()
                        val orderLocation = LatLng(lat.toDouble(), lng.toDouble())
                        var bitmap = BitmapFactory.decodeResource(resources, R.drawable.deliverybox)
                        bitmap = Common.scaleBitmap(bitmap, 70, 70)
                        val marker =
                            MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                                .title("Order of " + Common.currentRequest!!.phone)
                                .position(orderLocation)
                        mMap!!.addMarker(marker)

                        //draw route
                        mService!!.getDirections(
                            yourLocation.latitude.toString() + "," + yourLocation.longitude,
                            orderLocation.latitude.toString() + "," + orderLocation.longitude
                        )
                            .enqueue(object : Callback<String> {
                                override fun onResponse(
                                    call: Call<String>,
                                    response: Response<String>
                                ) {
                                    ParserTask().execute(response.body().toString())
                                }

                                override fun onFailure(call: Call<String>, t: Throwable) {}
                            })
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {}
            })
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = UPDATE_INTERVAL.toLong()
        mLocationRequest!!.fastestInterval = FATEST_INTERVAL.toLong()
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest!!.smallestDisplacement = DISPLACEMENT.toFloat()
    }

    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
        mGoogleApiClient.connect()
    }

    private fun checkPlayServices(): Boolean {
        val resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(
                    resultCode,
                    this,
                    PLAY_SERVICE_RESOLUTION_REQUEST
                ).show()
            } else {
                Toast.makeText(this, "This device is not support", Toast.LENGTH_SHORT).show()
                finish()
            }
            return false
        }
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    override fun onLocationChanged(location: Location) {
        mLastLocation = location
        displayLocation()
    }

    override fun onConnected(bundle: Bundle?) {
        displayLocation()
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
            mGoogleApiClient,
            mLocationRequest,
            this as LocationListener
        )
    }

    override fun onConnectionSuspended(i: Int) {
        mGoogleApiClient!!.connect()
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}
    override fun onResume() {
        super.onResume()
        checkPlayServices()
    }

    override fun onStart() {
        super.onStart()
        if (mGoogleApiClient != null) mGoogleApiClient!!.connect()
    }

    private inner class ParserTask :
        AsyncTask<String?, Int?, List<List<HashMap<String?, String?>?>?>?>() {
        var mDialog = ProgressDialog(this@TrackingOrder)
        override fun onPreExecute() {
            super.onPreExecute()
            mDialog.setMessage("Please waiting...")
            mDialog.show()
        }

        protected override fun doInBackground(vararg strings: String): List<List<HashMap<String?, String?>?>?>? {
            val jsonObject: JSONObject
            var routes: List<List<HashMap<String?, String?>?>?>? = null
            try {
                jsonObject = JSONObject(strings[0])
                val parser = DirectionJSONParser()
                routes = parser.parse(jsonObject)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return routes
        }

        override fun onPostExecute(lists: List<List<HashMap<String?, String?>?>?>?) {
            mDialog.dismiss()
            distance!!.text = Common.DISTANCE
            duration!!.text = Common.DURATION
            time!!.text = Common.ESTIMATED_TIME
            var points: ArrayList<*>? = null
            var lineOptions: PolylineOptions? = null
            for (i in lists!!.indices) {
                points = ArrayList<Any?>()
                lineOptions = PolylineOptions()
                val path = lists[i]
                for (j in path!!.indices) {
                    val point = path[j]
                    val lat = point!!["lat"]!!.toDouble()
                    val lng = point["lng"]!!.toDouble()
                    val position = LatLng(lat, lng)
                    points.add(position)
                }
                lineOptions.addAll(points)
                lineOptions.width(8f)
                lineOptions.color(Color.RED)
                lineOptions.geodesic(true)
            }
            mMap!!.addPolyline(lineOptions)
        }
    }

    companion object {
        private const val PLAY_SERVICE_RESOLUTION_REQUEST = 1000
        private const val LOCATION_PERMISSION_REQUEST = 1001
        private const val UPDATE_INTERVAL = 1000
        private const val FATEST_INTERVAL = 5000
        private const val DISPLACEMENT = 10
    }
}