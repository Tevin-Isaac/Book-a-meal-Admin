package com.tev.book_a_meal.Model


class Token {
    var token: String? = null
    var isServerToken = false

    constructor() {}
    constructor(token: String?, isServerToken: Boolean) {
        this.token = token
        this.isServerToken = isServerToken
    }
}