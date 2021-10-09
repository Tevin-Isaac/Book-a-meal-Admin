package com.tev.book_a_meal.Model


class Notification {
    var body: String? = null
    var title: String? = null

    constructor() {}
    constructor(body: String?, title: String?) {
        this.body = body
        this.title = title
    }
}