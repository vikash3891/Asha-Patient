package com.home.asharemedy.view

import android.os.Bundle
import com.home.asharemedy.R
import com.home.asharemedy.base.BaseActivity
import com.home.asharemedy.databinding.AcitivityProfileBinding
import kotlinx.android.synthetic.main.activity_profile_editable.*

class MyProfile : BaseActivity() {

    private lateinit var viewDataBinding: AcitivityProfileBinding
    var isEditable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_editable)

        try {
            /*topBarLayout.screenName.text = "My Profile"
            topBarLayout.imageBack.setOnClickListener {
                finish()
            }*/
            edit.setOnClickListener {
                if (!isEditable) {
                    isEditable = true
                    edit.text = "Save"
                    nationalityValue.isEnabled = true
                    religionValue.isEnabled = true
                    membershipValue.isEnabled = true
                    addressValue.isEnabled = true
                    communicationValue.isEnabled = true
                    insuranceValue.isEnabled = true
                    emiratesIDValue.isEnabled = true
                } else {
                    isEditable = false
                    nationalityValue.isEnabled = false
                    religionValue.isEnabled = false
                    membershipValue.isEnabled = false
                    addressValue.isEnabled = false
                    communicationValue.isEnabled = false
                    insuranceValue.isEnabled = false
                    emiratesIDValue.isEnabled = false
                }
            }
            //viewProfile.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}