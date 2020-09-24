package com.property.activity

import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.iss.method.CurrencyFormatter
import com.property.BuildConfig
import com.property.R
import com.property.databinding.ActivityDetailInvestmentBinding
import com.property.model.DataProperty

class DetailInvestmentActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        const val EXTRA_INVESTMENT = "extra_investment"
    }

    private lateinit var binding: ActivityDetailInvestmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailInvestmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initListener()
    }

    private fun initView() {

        val currencyFormatter = CurrencyFormatter()
        val selectedProperty: DataProperty? = intent.getParcelableExtra(EXTRA_INVESTMENT)
        val maxCapital: Double = selectedProperty!!.investmentCapital!!.toDouble()
        val currentCapital: Double = selectedProperty.totalInvest!!.toDouble()

        var result: Double = (currentCapital/maxCapital) * 100

        if (selectedProperty != null) {

            Glide.with(this)
                .load(BuildConfig.BASE_IMG_PROPERTY + selectedProperty.propertyImg)
                .into(binding.ivProperty)

            binding.tvPropertyName.text = selectedProperty.propertyName
            binding.progressBar.progress = result.toInt()
            binding.tvPercent.text = "${String.format("%.2f", result)} %"
            binding.tvCurrentCapital.text = currencyFormatter.currencyFormatter(selectedProperty.totalInvest)
            binding.tvInvestor.text = selectedProperty.totalInvestor
            binding.tvAddress.text = selectedProperty.location
            binding.tvSubdistrict.text = selectedProperty.subDistrict
            binding.tvInvestmentCapital.text = currencyFormatter.currencyFormatter(selectedProperty.investmentCapital)
            binding.tvBed.text = selectedProperty.bedRoomQty
            binding.tvBath.text = selectedProperty.bathRoomQty
            binding.tvAreaWide.text = "${selectedProperty.areaWide} ha"
            binding.tvDescription.text = Html.fromHtml(selectedProperty.desc)

        }

    }

    private fun initListener(){
        binding.toolbar.ibBack.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ib_back -> {
                onBackPressed()
            }
        }
    }

}