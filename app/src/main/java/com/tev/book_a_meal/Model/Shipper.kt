package com.tev.book_a_meal.Model


class Shipper {
    var name: String? = null
    var password: String? = null
    var phone: String? = null
    var isstaff: String? = null
    var isadmin: String? = null

    constructor() {}
    constructor(Pname: String?, Ppassword: String?) {
        name = Pname
        password = Ppassword
    }
}