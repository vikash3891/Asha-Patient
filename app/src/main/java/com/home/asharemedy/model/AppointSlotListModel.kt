package com.home.asharemedy.model

class AppointSlotListModel {

    var slot_status: String? = null
    var slot_date: String? = null
    var start_time: String? = null
    var end_time: String? = null
    var isSelected: Boolean? = null

    constructor()
    constructor(slot_status: String, slot_date: String, start_time: String, end_time: String) {
        this.slot_status = slot_status
        this.slot_date = slot_date
        this.start_time = start_time
        this.end_time = end_time
    }
}