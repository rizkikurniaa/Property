package com.property.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.property.R
import com.property.databinding.ActivityMainBinding
import com.property.fragment.AccountFragment
import com.property.fragment.HistoryFragment
import com.property.fragment.HomeFragment
import com.property.fragment.NotificationFragment

class MainActivity : AppCompatActivity(), ChipNavigationBar.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // beri listener pada saat item/menu bottomnavigation terpilih
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }
        binding.bnMain.setItemSelected(R.id.home)
        binding.bnMain.setOnItemSelectedListener(this)
    }

    // method untuk load fragment yang sesuai
    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, fragment)
                .commit()
            return true
        }
        return false
    }

    override fun onItemSelected(id: Int) {
        when (id) {
            R.id.home -> loadFragment(HomeFragment())
            R.id.history -> loadFragment(HistoryFragment())
            R.id.notification -> loadFragment(NotificationFragment())
            R.id.account -> loadFragment(AccountFragment())
        }
    }
}