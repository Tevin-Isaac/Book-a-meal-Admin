package com.tev.book_a_meal.Model


class Food {
    var name: String? = null
    var image: String? = null
    var description: String? = null
    var price: String? = null
    var menuId: String? = null

    constructor() {}
    constructor(
        name: String?,
        image: String?,
        description: String?,
        price: String?,
        menuId: String?
    ) {
        this.name = name
        this.image = image
        this.description = description
        this.price = price
        this.menuId = menuId
    }
}