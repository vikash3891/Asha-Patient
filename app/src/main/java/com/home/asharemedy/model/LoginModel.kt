package com.home.asharemedy.model

class LoginModel {

    var id: String? = null
    var name: String? = null
    var mobile: String? = null
    var password: String? = null
    var email: String? = null
    var address: String? = null
    var zip: String? = null
    var state: String? = null
    var city: String? = null
    var create_date: String? = null
    var landmark: String? = null
    var pin: String? = null
    constructor()
    constructor(
        id: String,
        name: String,
        mobile: String,
        password: String,
        email: String,
        address: String,
        zip: String,
        state: String,
        city: String,
        create_date: String,
        landmark: String,
        pin: String
    ) {
        this.id = id
        this.name = name
        this.mobile = mobile
        this.password = password
        this.email = email
        this.address = address
        this.zip = zip
        this.state = state
        this.city = city
        this.create_date = create_date
        this.landmark = landmark
        this.pin = pin
    }
}