package com.property.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.property.R
import com.property.activity.AllHousePropertyActivity
import com.property.activity.AllInvestmentActivity
import com.property.adapter.GridInvestAdapter
import com.property.adapter.GridPropertyAdapter
import com.property.adapter.SliderHomeAdapter
import com.property.databinding.FragmentHomeBinding
import com.property.method.CirclePagerIndicatorDecoration
import com.property.viewModel.HomeViewModel
import java.util.*

class HomeFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var sliderHomeAdapter: SliderHomeAdapter
    private lateinit var gridPropertyAdapter: GridPropertyAdapter
    private lateinit var gridInvestAdapter: GridInvestAdapter
    lateinit var homeViewModel: HomeViewModel
    val time = 3000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
        initListener()

    }

    private fun initView() {
        //slider
        binding.rvSlider.setHasFixedSize(true)
        sliderHomeAdapter = SliderHomeAdapter()
        sliderHomeAdapter.notifyDataSetChanged()

        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvSlider.layoutManager = linearLayoutManager
        binding.rvSlider.adapter = sliderHomeAdapter
        binding.rvSlider.addItemDecoration(CirclePagerIndicatorDecoration())

        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (linearLayoutManager.findFirstVisibleItemPosition() != -1) {
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() < sliderHomeAdapter.itemCount - 1) {
                        linearLayoutManager.smoothScrollToPosition(
                            binding.rvSlider,
                            RecyclerView.State(),
                            linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1
                        )
                    } else if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == sliderHomeAdapter.itemCount - 1) {
                        linearLayoutManager.smoothScrollToPosition(
                            binding.rvSlider,
                            RecyclerView.State(),
                            0
                        )
                    }
                }
            }
        }, 0, time.toLong())

        //recycler property
        binding.rvProperty.setHasFixedSize(true)
        gridPropertyAdapter = GridPropertyAdapter()
        gridPropertyAdapter.notifyDataSetChanged()

        binding.rvProperty.layoutManager = GridLayoutManager(context, 2)
        binding.rvProperty.adapter = gridPropertyAdapter

        //recycler invest
        binding.rvInvest.setHasFixedSize(true)
        gridInvestAdapter = GridInvestAdapter()
        gridInvestAdapter.notifyDataSetChanged()

        binding.rvInvest.layoutManager = GridLayoutManager(context, 2)
        binding.rvInvest.adapter = gridInvestAdapter


    }

    private fun initViewModel() {
        homeViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(HomeViewModel::class.java)

        homeViewModel.getSlider().observe(viewLifecycleOwner, Observer { slider ->
            if (slider.isNotEmpty()) {
                sliderHomeAdapter.setData(slider)
                showLoading(false)
            }
        })

        homeViewModel.getProperty().observe(viewLifecycleOwner, Observer { property ->
            if (property.isNotEmpty()){
                gridPropertyAdapter.setData(property)
                showLoading(false)
            }
        })

        homeViewModel.getInvest().observe(viewLifecycleOwner, Observer { invest ->
            if (invest.isNotEmpty()){
                gridInvestAdapter.setData(invest)
                showLoading(false)
            }
        })
    }

    private fun initListener() {
        binding.tvLihatRumah.setOnClickListener(this)
        binding.tvSeeInvest.setOnClickListener(this)
        binding.swLayout.post {
            showLoading(true)
            homeViewModel.setSlider()
            homeViewModel.setProperty()
            homeViewModel.setInvest()

        }

        binding.swLayout.setOnRefreshListener {
            showLoading(true)
            gridPropertyAdapter.clearData()
            gridInvestAdapter.clearData()
            homeViewModel.setSlider()
            homeViewModel.setProperty()
            homeViewModel.setInvest()
        }
    }

    private fun showLoading(state: Boolean) {
        binding.swLayout.isRefreshing = state

        if (state){

            binding.shimmerSlider.startShimmerAnimation()
            binding.shimmerProperty.startShimmerAnimation()
            binding.shimmerInvest.startShimmerAnimation()

        }else{

            binding.shimmerSlider.stopShimmerAnimation()
            binding.shimmerProperty.stopShimmerAnimation()
            binding.shimmerInvest.stopShimmerAnimation()
            binding.shimmerSlider.visibility = View.GONE
            binding.shimmerProperty.visibility = View.GONE
            binding.shimmerInvest.visibility = View.GONE

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_lihat_rumah -> {
                val intent = Intent(context, AllHousePropertyActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context!!.startActivity(intent)
            }
            R.id.tv_see_invest -> {
                val intent = Intent(context, AllInvestmentActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context!!.startActivity(intent)
            }
        }
    }

}