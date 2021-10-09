package com.tev.book_a_meal.Common

import java.util.*

class DirectionJSONParser {
    var message: List<List<HashMap<String, String>>>? = null

    /**
     * Receives a JSONObject and returns a list of lists containing latitude and longitude
     */
//    fun parse(jObject: JSONObject): List<List<HashMap<String, String>>> {
//        val routes: MutableList<List<HashMap<String, String>>> = ArrayList()
//        var jRoutes: JSONArray? = null
//        var jLegs: JSONArray? = null
//        var jSteps: JSONArray? = null
//        var jDistance: JSONObject? = null
//        var jDuration: JSONObject? = null
//        var totalDistance: Long = 0
//        var totalSeconds = 0
//        try {
//            jRoutes = jObject.getJSONArray("routes")
//            /** Traversing all routes  */
//            for (i in 0 until jRoutes.length()) {
//                jLegs = (jRoutes[i] as JSONObject).getJSONArray("legs")
//                val path: MutableList<*> = ArrayList<HashMap<String, String>>()
//                /** Traversing all legs  */
//                for (j in 0 until jLegs.length()) {
//                    jSteps = (jLegs[j] as JSONObject).getJSONArray("steps")
//                    /** Getting distance from the json data  */
//                    jDistance = (jLegs[j] as JSONObject).getJSONObject("distance")
//                    totalDistance = totalDistance + jDistance.getString("value").toLong()
//                    /** Getting duration from the json data  */
//                    jDuration = (jLegs[j] as JSONObject).getJSONObject("duration")
//                    totalSeconds = totalSeconds + jDuration.getString("value").toInt()
//                    /** Traversing all steps  */
//                    for (k in 0 until jSteps.length()) {
//                        var polyline = ""
//                        polyline =
//                            ((jSteps[k] as JSONObject)["polyline"] as JSONObject)["points"] as String
//                        val list = decodePoly(polyline)
//                        /** Traversing all points  */
//                        for (l in list.indices) {
//                            val hm = HashMap<String, String>()
////                            hm["lat"] = java.lang.Double.toString((list[l] as LatLng).latitude)
////                            hm["lng"] = java.lang.Double.toString((list[l] as LatLng).longitude)
//                            path.add(hm)
//                        }
//                    }
//                    routes.add(path)
//                    val dist = totalDistance / 1000.0
//                    val days = totalSeconds / 86400
//                    val hours = (totalSeconds - days * 86400) / 3600
//                    val minutes = (totalSeconds - days * 86400 - hours * 3600) / 60
//                    val seconds = totalSeconds - days * 86400 - hours * 3600 - minutes * 60
//                    Common.DISTANCE = "$dist km "
//                    Common.DURATION = "$hours hours $minutes mins $seconds seconds "
//                    val DBFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
//                    val currentDateandTime = DBFormat.format(Date())
//                    var date: Date? = null
//                    try {
//                        date = DBFormat.parse(currentDateandTime)
//                    } catch (e: ParseException) {
//                        e.printStackTrace()
//                    }
//                    val calendar = Calendar.getInstance()
//                    calendar.time = formatDate
//                    calendar.add(Calendar.HOUR, hours)
//                    calendar.add(Calendar.MINUTE, minutes)
//                    calendar.add(Calendar.SECOND, seconds)
//                    Common.ESTIMATED_TIME = DBFormat.format(calendar.time).toString()
//                }
//            }
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        } catch (e: Exception) {
//        }
//        return routes
//    }
//
//    private val formatDate: Date
//        private get() = Date()
//
//    /**
//     * Method to decode polyline points
//     * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
//     */
//    private fun decodePoly(encoded: String): List<*> {
//        val poly: MutableList<*> = ArrayList<Any?>()
//        var index = 0
//        val len = encoded.length
//        var lat = 0
//        var lng = 0
//        while (index < len) {
//            var b: Int
//            var shift = 0
//            var result = 0
//            do {
//                b = encoded[index++].toInt() - 63
//                result = result or (b and 0x1f shl shift)
//                shift += 5
//            } while (b >= 0x20)
//            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
//            lat += dlat
//            shift = 0
//            result = 0
//            do {
//                b = encoded[index++].toInt() - 63
//                result = result or (b and 0x1f shl shift)
//                shift += 5
//            } while (b >= 0x20)
//            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
//            lng += dlng
//            val p = LatLng(
//                lat.toDouble() / 1E5,
//                lng.toDouble() / 1E5
//            )
//            poly.add(p)
//        }
//        return poly
//    }
//
//    companion object {
//        var DBFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
//    }
}