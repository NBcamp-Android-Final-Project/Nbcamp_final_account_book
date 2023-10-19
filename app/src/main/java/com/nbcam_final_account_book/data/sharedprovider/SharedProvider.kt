package com.nbcam_final_account_book.data.sharedprovider

import android.content.SharedPreferences

interface SharedProvider {

    fun setSharedPref(name: String): SharedPreferences

}