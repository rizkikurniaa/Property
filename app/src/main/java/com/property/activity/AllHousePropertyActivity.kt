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
import com.property.adapter.GridPropertyAdapter
import com.property.adapter.ListSubDistrictAdapter
import com.property.databinding.ActivityAllHousePropertyBinding
import com.property.databinding.DialogSubdistrictBinding
import com.property.interfaceApp.PropertyBySubDistrict
import com.property.viewModel.AllHouseViewModel

class AllHousePropertyActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAllHousePropertyBinding
    private lateinit var gridPropertyAdapter: GridPropertyAdapter
    private lateinit var listSubDistrictAdapter: ListSubDistrictAdapter
    lateinit var allHouseViewModel: AllHouseViewModel
    private var isLoading: Boolean = false
    private val TAG = javaClass.simpleName
    private var subDistrict: String = "All"
    private var page: Int = 1
    private var totalPage: Int = 0
    private lateinit var dialog: Dialog

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
        binding.layoutNoData.rlNoData.visibility = View.GONE

        binding.rvAllHouse.setHasFixedSize(true)
        gridPropertyAdapter = GridPropertyAdapter()
        gridPropertyAdapter.notifyDataSetChanged()

        binding.rvAllHouse.layoutManager = GridLayoutManager(this, 2)
        binding.rvAllHouse.adapter = gridPropertyAdapter

        //sub district
        listSubDistrictAdapter = ListSubDistrictAdapter()
        listSubDistrictAdapter.notifyDataSetChanged()

        listSubDistrictAdapter.setPropertyBySubDistrict(object : PropertyBySubDistrict {
            override fun propertyBySubDistrict(subDistrictId: String) {
                page = 1
                subDistrict = subDistrictId
                showLoading()
                gridPropertyAdapter.clearData()
                allHouseViewModel.setProperty(subDistrictId, page)
                dialog.dismiss()
            }

        })

    }

    private fun initViewModel() {
        allHouseViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(AllHouseViewModel::class.java)

        allHouseViewModel.getProperty().observe(this, Observer { property ->
            if (property.isNotEmpty()) {
                gridPropertyAdapter.setData(property)
                binding.layoutNoData.rlNoData.visibility = View.GONE
                hideLoading()
            } else {
                binding.rvAllHouse.visibility = View.GONE
                binding.layoutNoData.rlNoData.visibility = View.VISIBLE
                hideLoading()
            }
        })

        allHouseViewModel.getSubDistrict().observe(this, Observer { subdistrict ->
            if (subdistrict.isNotEmpty()) {
                listSubDistrictAdapter.setData(subdistrict)
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
            showLoading()
            allHouseViewModel.setProperty(subDistrict, page)
            allHouseViewModel.setSubDistrict()

        }

        binding.swLayout.setOnRefreshListener {
            page = 1
            subDistrict = "All"
            showLoading()
            gridPropertyAdapter.clearData()
            allHouseViewModel.setProperty(subDistrict, page)
            allHouseViewModel.setSubDistrict()
        }

        binding.rvAllHouse.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val countItem = linearLayoutManager.itemCount
                val lastVisiblePosition =
                    linearLayoutManager.findLastCompletelyVisibleItemPosition()
                val isLastPosition = countItem.minus(1) == lastVisiblePosition

                Log.d(TAG, "Sub district: $subDistrict")
                Log.d(TAG, "countItem: $countItem")
                Log.d(TAG, "lastVisiblePosition: $lastVisiblePosition")
                Log.d(TAG, "isLastPosition: $isLastPosition")

                if (!isLoading && isLastPosition && page < totalPage) {

                    binding.progressBar.visibility = View.VISIBLE
                    page = page.let { it.plus(1) }
                    allHouseViewModel.setProperty(subDistrict, page)
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
        binding.rvAllHouse.visibility = View.VISIBLE
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