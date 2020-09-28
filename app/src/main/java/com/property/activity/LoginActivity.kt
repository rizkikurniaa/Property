package com.property.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.property.R
import com.property.databinding.ActivityLoginBinding
import com.property.utils.SharedPrefManager
import com.property.viewModel.HomeViewModel
import com.property.viewModel.LoginViewModel
import com.tapadoo.alerter.Alerter

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
        initListener()
    }

    private fun initView() {
        sharedPrefManager = SharedPrefManager(this)
    }

    private fun initViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(LoginViewModel::class.java)


        loginViewModel.getUserLogin().observe(this, Observer { loginData ->
            if (loginData.success == 1) {
                sharedPrefManager.saveSPString(sharedPrefManager.SP_TOKEN, loginData.token)
                sharedPrefManager.saveSPBoolean(sharedPrefManager.SP_SUDAH_LOGIN, true)
                startActivity(
                    Intent(this@LoginActivity, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                finish()
            }else{
                Alerter.create(this)
                    .setTitle("Gagal login...")
                    .setText(loginData.message.toString())
                    .setIcon(R.drawable.ic_wrong)
                    .setBackgroundColorRes(R.color.yellow)
                    .show()
            }

            hideLoading()

        })
    }

    private fun initListener() {
        binding.tvRegister.setOnClickListener(this)
        binding.rvLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rv_login -> {
                showLoading()
                loginViewModel.requestLogin(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            }
            R.id.tv_register -> {
                val i = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(i)
                finish()
            }
        }
    }

    private fun showLoading() {
        binding.progressLogin.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressLogin.visibility = View.GONE
    }
}