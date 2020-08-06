package com.home.asharemedy.model

class PaymentItemModel {

    var paymentDate: String? = null
    var paymentMonth: String? = null
    var paymentAmount: String? = null
    var paymentStatus: String? = null
    var paymentID: String? = null

    constructor()
    constructor(
        paymentDate: String,
        paymentMonth: String,
        paymentAmount: String,
        paymentStatus: String,
        paymentID: String
    ) {
        this.paymentDate = paymentDate
        this.paymentMonth = paymentMonth
        this.paymentAmount = paymentAmount
        this.paymentStatus = paymentStatus
        this.paymentID = paymentID
    }
}