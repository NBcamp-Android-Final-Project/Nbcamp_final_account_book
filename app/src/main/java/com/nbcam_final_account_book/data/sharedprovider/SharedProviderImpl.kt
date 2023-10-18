package com.nbcam_final_account_book.data.sharedprovider

import android.content.Context
import android.content.SharedPreferences

class SharedProviderImpl(private val context: Context) : SharedProvider {
    override fun setSharedPrefUserToken(name: String): SharedPreferences {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }


}