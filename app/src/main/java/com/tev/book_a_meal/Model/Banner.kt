package com.tev.book_a_meal.Model


class Banner {
    var id: String? = null
    var name: String? = null
    var image: String? = null

    constructor() {}
    constructor(id: String?, name: String?, image: String?) {
        this.id = id
        this.name = name
        this.image = image
    }
}

*