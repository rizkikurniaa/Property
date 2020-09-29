package com.property.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.property.R
import com.property.activity.LoginActivity
import com.property.activity.RegisterActivity
import com.property.adapter.GridPropertyAdapter
import com.property.adapter.ListNotifAdapter
import com.property.databinding.FragmentNotificationBinding
import com.property.utils.SharedPrefManager
import com.property.viewModel.HomeViewModel
import com.property.viewModel.NotificationViewModel

class NotificationFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var token: String
    private lateinit var listNotifAdapter: ListNotifAdapter
    private lateinit var notificationViewModel: NotificationViewModel

    private var page: Int = 1
    private var totalPage: Int = 0
    private val TAG = javaClass.simpleName
    private var isLoading: Boolean = false

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
        initViewModel()
        initListener()
    }

    private fun initView() {

        binding.toolbarTitle.tvToolbar.text = "Notifikasi"

        if (sharedPrefManager.getSpSudahLogin() == false) {
            binding.layoutNotLogin.rlNotLogin.visibility = View.VISIBLE
            binding.rvNotification.visibility = View.GONE
        } else {
            binding.layoutNotLogin.rlNotLogin.visibility = View.GONE
            binding.rvNotification.visibility = View.VISIBLE

            //recycler notif
            binding.rvNotification.setHasFixedSize(true)
            listNotifAdapter = ListNotifAdapter()
            listNotifAdapter.notifyDataSetChanged()

            binding.rvNotification.layoutManager = LinearLayoutManager(context)
            binding.rvNotification.adapter = listNotifAdapter
        }
    }

    private fun initViewModel() {
        notificationViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(NotificationViewModel::class.java)

        notificationViewModel.getNotification().observe(viewLifecycleOwner, Observer { notif ->
            if (notif.isNotEmpty()) {
                listNotifAdapter.setData(notif)
                binding.layoutNoData.rlNoData.visibility = View.GONE
                showLoading(false)
            }else{
                binding.layoutNoData.rlNoData.visibility = View.VISIBLE
                binding.layoutNoData.ivNoData.setImageResource(R.drawable.ic_undraw_mailbox)
                binding.layoutNoData.tvNoData.text = getString(R.string.no_notification)
                showLoading(false)
            }
        })

        notificationViewModel.getTotalPage().observe(viewLifecycleOwner, Observer { total ->
            if (total != null) {
                totalPage = total
            }
        })


    }

    private fun initListener() {
        binding.swLayout.post {
            showLoading(true)
            notificationViewModel.setNotification(token, page)

        }

        binding.swLayout.setOnRefreshListener {
            page = 1
            showLoading(true)
            notificationViewModel.setNotification(token, page)
        }

        binding.rvNotification.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val countItem = linearLayoutManager.itemCount
                val lastVisiblePosition =
                    linearLayoutManager.findLastCompletelyVisibleItemPosition()
                val isLastPosition = countItem.minus(1) == lastVisiblePosition

                Log.d(TAG, "countItem: $countItem")
                Log.d(TAG, "lastVisiblePosition: $lastVisiblePosition")
                Log.d(TAG, "isLastPosition: $isLastPosition")

                if (!isLoading && isLastPosition && page < totalPage) {

                    binding.progressBar.visibility = View.VISIBLE
                    page = page.let { it.plus(1) }
                    notificationViewModel.setNotification(token, page)
                }
            }
        })

        binding.layoutNotLogin.btnLogin.setOnClickListener(this)
        binding.layoutNotLogin.btnRegister.setOnClickListener(this)
    }

    private fun showLoading(state: Boolean) {
        binding.swLayout.isRefreshing = state
        isLoading = state

        if (state){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
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