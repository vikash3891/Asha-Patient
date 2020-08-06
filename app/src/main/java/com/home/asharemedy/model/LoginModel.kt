package com.home.asharemedy.model

class LoginModel {

    var user_email: String? = null
    var user_id: String? = null
    var user_name: String? = null
    var user_password: String? = null
    var user_type: String? = null

    constructor()
    constructor(
        user_email: String,
        user_id: String,
        user_name: String,
        user_password: String,
        user_type: String
    ) {
        this.user_email = user_email
        this.user_id = user_id
        this.user_name = user_name
        this.user_password = user_password
        this.user_type = user_type
    }
}