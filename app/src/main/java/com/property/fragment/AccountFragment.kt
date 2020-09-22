package com.property.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.property.databinding.FragmentAccountBinding
import com.property.utils.SharedPrefManager

class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPrefManager = SharedPrefManager(activity!!)
        token = sharedPrefManager.getSpToken().toString()

        initView()
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
}