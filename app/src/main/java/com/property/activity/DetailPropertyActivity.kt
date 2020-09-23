package com.property.activity

import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.iss.method.CurrencyFormatter
import com.property.BuildConfig
import com.property.R
import com.property.databinding.ActivityDetailPropertyBinding
import com.property.model.DataProperty

class DetailPropertyActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        const val EXTRA_PROPERTY = "extra_property"
    }

    private lateinit var binding: ActivityDetailPropertyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initListener()
    }

    private fun initView() {

        val currencyFormatter = CurrencyFormatter()
        val selectedProperty: DataProperty? = intent.getParcelableExtra(EXTRA_PROPERTY)

        if (selectedProperty != null) {

            Glide.with(this)
                .load(BuildConfig.BASE_IMG_PROPERTY + selectedProperty.propertyImg)
                .into(binding.ivProperty)

            binding.tvPropertyName.text = selectedProperty.propertyName
            binding.tvAddress.text = selectedProperty.location
            binding.tvSubdistrict.text = selectedProperty.subDistrict
            binding.tvInstallment.text = currencyFormatter.currencyFormatter(selectedProperty.installment)
            binding.tvLongValue.text = "${selectedProperty.longInstallments} bulan"
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