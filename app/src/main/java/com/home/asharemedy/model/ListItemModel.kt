package com.home.asharemedy.model

class ListItemModel {

    var date: String? = null
    var time: String? = null
    var facility: String? = null
    var clinician: String? = null
    var spec: String? = null
    var complaints: String? = null
    var remarks: String? = null
    var diagnosis: String? = null
    var tests: String? = null
    var recordPdf: String? = null
    var status: String? = null

    constructor(
        date: String,
        time: String,
        facility: String,
        clinician: String,
        spec: String,
        complaints: String,
        remarks: String,
        diagnosis: String,
        tests: String,
        recordPdf: String,
        status: String
    ) {
        this.date = date
        this.time = time
        this.facility = facility
        this.clinician = clinician
        this.spec = spec
        this.complaints = complaints
        this.remarks = remarks
        this.diagnosis = diagnosis
        this.tests = tests
        this.recordPdf = recordPdf
        this.status = status
    }
}