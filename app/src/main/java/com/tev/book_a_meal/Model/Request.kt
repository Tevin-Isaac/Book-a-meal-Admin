package com.tev.book_a_meal.Model


class Request {
    var phone: String? = null
    var name: String? = null
    var address: String? = null
    var total: String? = null
    var status: String? = null
    var comment: String? = null
    var paymentMethod: String? = null
    var latLng: String? = null
    private var foods: List<Order>? = null

    constructor() {}
    constructor(
        phone: String?,
        name: String?,
        address: String?,
        total: String?,
        status: String?,
        comment: String?,
        paymentMethod: String?,
        latLng: String?,
        foods: List<Order>?
    ) {
        this.phone = phone
        this.name = name
        this.address = address
        this.total = total
        this.status = status
        this.comment = comment
        this.paymentMethod = paymentMethod
        this.latLng = latLng
        this.foods = foods
    }

    fun getFoods(): List<Order>? {
        return foods
    }

    fun setFoods(foods: List<Order>?) {
        this.foods = foods
    }
}