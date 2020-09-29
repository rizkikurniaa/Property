package com.property.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.property.R
import com.property.databinding.ActivityRegisterBinding
import com.property.viewModel.LoginViewModel
import com.property.viewModel.RegisterViewModel
import com.tapadoo.alerter.Alerter

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        initListener()
    }

    private fun initViewModel() {
        registerViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(RegisterViewModel::class.java)


        registerViewModel.getResponseRegister().observe(this, Observer { response ->
            if (response.success == 1) {
                Alerter.create(this)
                    .setTitle("Sukses...")
                    .setText(response.message.toString())
                    .setIcon(R.drawable.ic_success)
                    .setBackgroundColorRes(R.color.yellow)
                    .show()

                val i = Intent(this@RegisterActivity, LoginActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                val timer: Thread = object : Thread() {
                    override fun run() {
                        try {
                            sleep(1000)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        } finally {
                            startActivity(i)
                            finish()
                        }
                    }
                }
                timer.start()

            }else{
                Alerter.create(this)
                    .setTitle("Gagal...")
                    .setText(response.message.toString())
                    .setIcon(R.drawable.ic_wrong)
                    .setBackgroundColorRes(R.color.yellow)
                    .show()
            }

            hideLoading()

        })
    }

    private fun initListener() {
        binding.rvRegister.setOnClickListener(this)
        binding.tvLogin.setOnClickListener(this)
    }

    private fun showLoading() {
        binding.progressRegister.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressRegister.visibility = View.GONE
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rv_register -> {
                showLoading()
                val inputEmail = binding.etEmail.text.toString().trim()
                val inputPhone = binding.etPhone.text.toString().trim()
                val inputPassword = binding.etPassword.text.toString().trim()

                var isEmptyFields = false

                when {
                    inputEmail.isEmpty() -> {
                        isEmptyFields = true
                        binding.etEmail.error = "Field ini tidak boleh kosong"
                    }

                    inputPhone.isEmpty() -> {
                        isEmptyFields = true
                        binding.etPhone.error = "Field ini tidak boleh kosong"
                    }

                    inputPassword.isEmpty() -> {
                        isEmptyFields = true
                        binding.etPassword.error = "Field ini tidak boleh kosong"
                    }
                }

                if (!isEmptyFields) {
                    if (!isEmailValid(inputEmail)) {
                        Toast.makeText(
                            this@RegisterActivity,"Masukkan email dengan benar", Toast.LENGTH_LONG
                        ).show()
                        hideLoading()
                    } else {
                       registerViewModel.requestLogin(inputEmail, inputPhone, inputPassword)
                    }
                }else{
                    hideLoading()
                }

            }
            R.id.tv_login -> {
                val i = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(i)
                finish()
            }
        }
    }
}