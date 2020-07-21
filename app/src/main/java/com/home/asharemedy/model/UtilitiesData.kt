package com.home.asharemedy.model


object UtilitiesData {
    var utilityArrayList: ArrayList<String>? = null

    init {
        if (utilityArrayList == null)
            utilityArrayList = ArrayList<String>()
    }

    @Synchronized
    fun getCount(): Int {
        var count = 0
        count = utilityArrayList!!.size
        return count
    }

    @Synchronized
    fun addArrayList(modelList: ArrayList<String>) {
        utilityArrayList = modelList
    }

    @Synchronized
    fun getArrayList(): ArrayList<String>? {
        return utilityArrayList
    }

    @Synchronized
    fun getArrayItem(position: Int): String {
        return utilityArrayList!!.get(position)
    }

    @Synchronized
    fun clearListItem(index: Int) {
        utilityArrayList!!.removeAt(index)
    }

    @Synchronized
    fun clearArrayList() {
        utilityArrayList!!.removeAll(utilityArrayList!!)

    }


}


