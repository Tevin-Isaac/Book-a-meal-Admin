package com.tev.book_a_meal.Model

class MyResponse {
    var multicast_id: Long = 0
    var success = 0
    var failure = 0
    var canonical_ids = 0
    var results: List<Result>? = null
}