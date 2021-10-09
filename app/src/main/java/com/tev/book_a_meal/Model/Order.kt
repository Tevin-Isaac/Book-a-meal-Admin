package com.tev.book_a_meal.Model


class Order {
    var productId: String? = null
    var productName: String? = null
    var quantity: String? = null
    var price: String? = null

    constructor() {}
    constructor(
        productId: String?,
        productName: String?,
        quantity: String?,
        price: String?,
        image: String?
    ) {
        this.productId = productId
        this.productName = productName
        this.quantity = quantity
        this.price = price
    }
}