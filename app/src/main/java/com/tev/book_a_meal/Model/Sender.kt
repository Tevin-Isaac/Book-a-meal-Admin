package com.tev.book_a_meal.Model


class Sender {
    var to: String? = null
    var notification: Notification? = null

    constructor() {}
    constructor(to: String?, notification: Notification?) {
        this.to = to
        this.notification = notification
    }
}