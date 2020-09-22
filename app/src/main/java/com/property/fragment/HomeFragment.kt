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
import com.property.adapter.ListPropertyAdapter
import com.property.adapter.SliderHomeAdapter
import com.property.databinding.FragmentHomeBinding
import com.property.method.CirclePagerIndicatorDecoration
import com.property.viewModel.AllHouseViewModel
import com.property.viewModel.HomeViewModel
import java.util.*

class HomeFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var sliderHomeAdapter: SliderHomeAdapter
    private lateinit var listPropertyAdapter: ListPropertyAdapter
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

        binding.rvSlider.setHasFixedSize(true)
        sliderHomeAdapter = SliderHomeAdapter()
        sliderHomeAdapter.notifyDataSetChanged()

        binding.rvProperty.setHasFixedSize(true)
        listPropertyAdapter = ListPropertyAdapter()
        listPropertyAdapter.notifyDataSetChanged()

        binding.rvProperty.layoutManager = LinearLayoutManager(context)
        binding.rvProperty.adapter = listPropertyAdapter

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

    }

    private fun initViewModel() {
        homeViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(HomeViewModel::class.java)

        homeViewModel.getSlider().observe(viewLifecycleOwner, Observer { slider ->
            if (slider.isNotEmpty()) {
                sliderHomeAdapter.setData(slider)
                binding.shimmerSlider.stopShimmerAnimation()
                binding.shimmerSlider.visibility = View.GONE
                showLoading(false)
            }
        })

        homeViewModel.getProperty().observe(viewLifecycleOwner, Observer { property ->
            if (property.isNotEmpty()){
                listPropertyAdapter.setData(property)
                binding.shimmerProperty.stopShimmerAnimation()
                binding.shimmerProperty.visibility = View.GONE
                showLoading(false)
            }
        })
    }

    private fun initListener() {
        binding.tvLihatRumah.setOnClickListener(this)
        binding.swLayout.post {
            showLoading(true)
            binding.shimmerSlider.startShimmerAnimation()
            binding.shimmerProperty.startShimmerAnimation()
            homeViewModel.setSlider()
            homeViewModel.setProperty()

        }

        binding.swLayout.setOnRefreshListener {
            showLoading(true)
            binding.shimmerSlider.startShimmerAnimation()
            binding.shimmerProperty.startShimmerAnimation()
            homeViewModel.setSlider()
            homeViewModel.setProperty()
        }
    }

    private fun showLoading(state: Boolean) {
        binding.swLayout.isRefreshing = state
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_lihat_rumah -> {
                val intent = Intent(context, AllHousePropertyActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context!!.startActivity(intent)
            }
        }
    }

}