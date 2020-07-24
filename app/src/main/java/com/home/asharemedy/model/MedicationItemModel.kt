package com.home.asharemedy.model

class MedicationItemModel {

    var medicineName: String? = null
    var daysCount: String? = null
    var medicineCourse: String? = null
    var medicinePrice: String? = null
    var details: String? = null

    constructor()
    constructor(
        medicineName: String,
        daysCount: String,
        medicineCourse: String,
        medicinePrice: String,
        details: String

    ) {
        this.medicineName = medicineName
        this.daysCount = daysCount
        this.medicineCourse = medicineCourse
        this.medicinePrice = medicinePrice
        this.details = details

    }
}