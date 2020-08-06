package com.home.asharemedy.model

import com.home.asharemedy.api.ResponseModelClasses


object AilmentArrayData {
    private var ailmentArrayList: ArrayList<ResponseModelClasses.GetAilmentResponseModel>? = null
    var serviceArrayList: ArrayList<ResponseModelClasses.GetServiceResponseModel>? = null

    init {
        if (ailmentArrayList == null)
            ailmentArrayList = ArrayList()
        if (serviceArrayList == null)
            serviceArrayList = ArrayList()
    }

    /*Ailment*/
    @Synchronized
    fun getCount(): Int {
        var count = 0
        count = ailmentArrayList!!.size
        return count
    }

    @Synchronized
    fun addArrayList(modelList: ArrayList<ResponseModelClasses.GetAilmentResponseModel>) {
        ailmentArrayList = modelList
    }

    @Synchronized
    fun getArrayList(): ArrayList<ResponseModelClasses.GetAilmentResponseModel>? {
        return ailmentArrayList
    }

    @Synchronized
    fun getArrayItem(position: Int): ResponseModelClasses.GetAilmentResponseModel {
        return ailmentArrayList!!.get(position)
    }

    @Synchronized
    fun clearListItem(index: Int) {
        ailmentArrayList!!.removeAt(index)
    }

    @Synchronized
    fun clearArrayList() {
        ailmentArrayList!!.removeAll(ailmentArrayList!!)

    }


    /*Services*/
    @Synchronized
    fun getServicesCount(): Int {
        var count = 0
        count = ailmentArrayList!!.size
        return count
    }

    @Synchronized
    fun addServicesArrayList(modelList: ArrayList<ResponseModelClasses.GetServiceResponseModel>) {
        serviceArrayList = modelList
    }

    @Synchronized
    fun getServicesArrayList(): ArrayList<ResponseModelClasses.GetServiceResponseModel>? {
        return serviceArrayList
    }

    @Synchronized
    fun getServicesArrayItem(position: Int): ResponseModelClasses.GetServiceResponseModel {
        return serviceArrayList!!.get(position)
    }

    @Synchronized
    fun clearServicesListItem(index: Int) {
        serviceArrayList!!.removeAt(index)
    }

    @Synchronized
    fun clearServicesArrayList() {
        serviceArrayList!!.removeAll(serviceArrayList!!)

    }


}