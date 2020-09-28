package com.property.activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.property.R
import com.property.adapter.GridInvestAdapter
import com.property.adapter.ListSubDistrictAdapter
import com.property.databinding.ActivityAllInvestmentBinding
import com.property.databinding.DialogSubdistrictBinding
import com.property.interfaceApp.PropertyBySubDistrict
import com.property.viewModel.AllInvestmentViewModel

class AllInvestmentActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAllInvestmentBinding
    private lateinit var gridInvestAdapter: GridInvestAdapter
    private lateinit var listSubDistrictAdapter: ListSubDistrictAdapter
    lateinit var allInvestmentViewModel: AllInvestmentViewModel
    private var isLoading: Boolean = false
    private val TAG = javaClass.simpleName
    private var subDistrict: String = "All"
    private var page: Int = 1
    private var totalPage: Int = 0
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllInvestmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
        initListener()
    }

    private fun initView() {

        binding.toolbar.tvToolbar.text = "Investasi"

        binding.rvAllInvestment.setHasFixedSize(true)
        gridInvestAdapter = GridInvestAdapter()
        gridInvestAdapter.notifyDataSetChanged()

        binding.rvAllInvestment.layoutManager = GridLayoutManager(this, 2)
        binding.rvAllInvestment.adapter = gridInvestAdapter

        //sub district
        listSubDistrictAdapter = ListSubDistrictAdapter()
        listSubDistrictAdapter.notifyDataSetChanged()

        listSubDistrictAdapter.setPropertyBySubDistrict(object : PropertyBySubDistrict {
            override fun propertyBySubDistrict(subDistrictId: String) {
                page = 1
                subDistrict = subDistrictId
                showLoading()
                gridInvestAdapter.clearData()
                allInvestmentViewModel.setInvest(subDistrictId, page)
                dialog.dismiss()
            }

        })

    }

    private fun initViewModel() {
        allInvestmentViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(AllInvestmentViewModel::class.java)

        allInvestmentViewModel.getInvest().observe(this, Observer { property ->
            if (property.isNotEmpty()) {
                gridInvestAdapter.setData(property)
                hideLoading()
            } else {
                binding.rvAllInvestment.visibility = View.GONE
                binding.layoutNoData.rlNoData.visibility = View.VISIBLE
                hideLoading()
            }
        })

        allInvestmentViewModel.getSubDistrict().observe(this, Observer { subdistrict ->
            if (subdistrict.isNotEmpty()) {
                listSubDistrictAdapter.setData(subdistrict)
            }
        })

        allInvestmentViewModel.getTotalPage().observe(this, Observer { total ->
            if (total != null) {
                totalPage = total
            }
        })

    }

    private fun initListener() {
        binding.swLayout.post {
            showLoading()
            allInvestmentViewModel.setSubDistrict()
            allInvestmentViewModel.setInvest(subDistrict, page)

        }

        binding.swLayout.setOnRefreshListener {
            page = 1
            subDistrict = "All"
            showLoading()
            gridInvestAdapter.clearData()
            allInvestmentViewModel.setSubDistrict()
            allInvestmentViewModel.setInvest(subDistrict, page)
        }

        binding.rvAllInvestment.addOnScrollListener(object :
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
                    allInvestmentViewModel.setInvest(subDistrict, page)
                }
            }
        })

        binding.toolbar.ibBack.setOnClickListener(this)
        binding.toolbar.ibFilter.setOnClickListener(this)

    }

    private fun showLoading() {
        isLoading = true
        binding.shimmerProperty.visibility = View.VISIBLE
        binding.shimmerProperty.startShimmerAnimation()
        binding.layoutNoData.rlNoData.visibility = View.GONE
    }

    private fun hideLoading() {
        isLoading = false
        binding.swLayout.isRefreshing = false
        binding.progressBar.visibility = View.GONE
        binding.shimmerProperty.stopShimmerAnimation()
        binding.shimmerProperty.visibility = View.GONE
        binding.rvAllInvestment.visibility = View.VISIBLE
    }

    private fun dialogSubDistrict() {

        dialog = Dialog(this, R.style.DialogSlideAnim)
        val views: View =
            LayoutInflater.from(this).inflate(R.layout.dialog_subdistrict, null)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.setContentView(views)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()


        val bindingDialog = DialogSubdistrictBinding.bind(views)

        bindingDialog.rvSubdistrict.setHasFixedSize(true)
        bindingDialog.rvSubdistrict.layoutManager = LinearLayoutManager(this)
        bindingDialog.rvSubdistrict.adapter = listSubDistrictAdapter

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ib_back -> {
                onBackPressed()
            }

            R.id.ib_filter -> {
                dialogSubDistrict()
            }
        }
    }
}