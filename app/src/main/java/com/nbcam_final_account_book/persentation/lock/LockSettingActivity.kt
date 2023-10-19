package com.nbcam_final_account_book.persentation.lock

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.LockSettingActivityBinding
import com.nbcam_final_account_book.persentation.lock.pin.PinFragment

class LockSettingActivity : AppCompatActivity() {

    private lateinit var binding: LockSettingActivityBinding
    private lateinit var pinFragment: PinFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LockSettingActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pinFragment = PinFragment()
        initView()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        finish()
    }

    fun initView() = with(binding) {
        locksettingRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.locksetting_btn_none -> {
                    locksettingDivider1.visibility = View.GONE
                    locksettingSwitchFingerprint.visibility = View.GONE
                    locksettingTvPinEdit.visibility = View.GONE
                }
                R.id.locksetting_btn_pin -> {
                    locksettingDivider1.visibility = View.VISIBLE
                    locksettingSwitchFingerprint.visibility = View.VISIBLE
                    locksettingTvPinEdit.visibility = View.VISIBLE

                    loadFragment(pinFragment)
                }
            }
        }

        locksettingBtnPin.setOnClickListener {
            if (pinFragment.isAdded) {
                loadFragment(pinFragment)
            } else {
                pinFragment = PinFragment()
                loadFragment(pinFragment)
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.locksetting_fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}