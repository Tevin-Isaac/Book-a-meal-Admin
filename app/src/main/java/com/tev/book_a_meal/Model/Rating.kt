package com.tev.book_a_meal.Model


class Rating {
    //key & value
    var userPhone: String? = null
    var foodId: String? = null
    var rateValue: String? = null
    var comment: String? = null
    var image: String? = null

    constructor() {}
    constructor(
        userPhone: String?,
        foodId: String?,
        rateValue: String?,
        comment: String?,
        image: String?
    ) {
        this.userPhone = userPhone
        this.foodId = foodId
        this.rateValue = rateValue
        this.comment = comment
        this.image = image
    }
}