package com.property.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.property.R
import com.property.adapter.ListPropertyAdapter
import com.property.databinding.ActivityAllHousePropertyBinding
import com.property.viewModel.AllHouseViewModel

class AllHousePropertyActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAllHousePropertyBinding
    private lateinit var listPropertyAdapter: ListPropertyAdapter
    lateinit var allHouseViewModel: AllHouseViewModel
    private var isLoading: Boolean = false
    private val TAG = javaClass.simpleName
    private var page: Int = 1
    private var totalPage: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllHousePropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
        initListener()
    }

    private fun initView() {

        binding.toolbar.tvToolbar.text = "Perumahan"

        binding.rvAllHouse.setHasFixedSize(true)
        listPropertyAdapter = ListPropertyAdapter()
        listPropertyAdapter.notifyDataSetChanged()

        binding.rvAllHouse.layoutManager = LinearLayoutManager(this)
        binding.rvAllHouse.adapter = listPropertyAdapter

    }

    private fun initViewModel() {
        allHouseViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(AllHouseViewModel::class.java)

        allHouseViewModel.getProperty().observe(this, Observer { property ->
            if (property.isNotEmpty()) {
                listPropertyAdapter.setData(property)
                hideLoading()
            }
        })

        allHouseViewModel.getTotalPage().observe(this, Observer { total ->
            if (total != null) {
                totalPage = total
            }
        })

    }

    private fun initListener() {
        binding.swLayout.post {
            showLoading(true)
            allHouseViewModel.setProperty(page)

        }

        binding.swLayout.setOnRefreshListener {
            page = 1
            showLoading(true)
            listPropertyAdapter.clearData()
            allHouseViewModel.setProperty(page)
        }

        binding.rvAllHouse.addOnScrollListener(object :
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
                    allHouseViewModel.setProperty(page)
                }
            }
        })

        binding.toolbar.ibBack.setOnClickListener(this)
        binding.toolbar.ibFilter.setOnClickListener(this)

    }

    private fun showLoading(isRefresh: Boolean) {
        isLoading = true
        binding.shimmerProperty.startShimmerAnimation()
    }

    private fun hideLoading() {
        isLoading = false
        binding.swLayout.isRefreshing = false
        binding.progressBar.visibility = View.GONE
        binding.shimmerProperty.stopShimmerAnimation()
        binding.shimmerProperty.visibility = View.GONE
        binding.rvAllHouse.visibility = View.VISIBLE
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ib_back -> {
                onBackPressed()
            }

            R.id.ib_filter -> {

            }
        }
    }
}