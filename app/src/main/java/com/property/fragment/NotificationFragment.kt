package com.property.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.property.R
import com.property.activity.LoginActivity
import com.property.activity.RegisterActivity
import com.property.databinding.FragmentNotificationBinding
import com.property.utils.SharedPrefManager

class NotificationFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPrefManager = SharedPrefManager(activity!!)
        token = sharedPrefManager.getSpToken().toString()

        initView()
        initListener()
    }

    private fun initView() {
        if (sharedPrefManager.getSpSudahLogin() == false) {
            binding.layoutNotLogin.rlNotLogin.visibility = View.VISIBLE
            binding.nsvAccount.visibility = View.GONE
        } else {
            binding.layoutNotLogin.rlNotLogin.visibility = View.GONE
            binding.nsvAccount.visibility = View.VISIBLE
        }
    }

    private fun initListener() {
        binding.layoutNotLogin.btnLogin.setOnClickListener(this)
        binding.layoutNotLogin.btnRegister.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                val i = Intent(context, LoginActivity::class.java)
                startActivity(i)
            }
            R.id.btn_register -> {
                val i = Intent(context, RegisterActivity::class.java)
                startActivity(i)
            }
        }
    }

}