package com.iss.method

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.ParseException

class CurrencyFormatter {

    fun currencyFormatter(currencyFormat: String?): String? {
        try {
            val kursIdr =
                DecimalFormat.getCurrencyInstance() as DecimalFormat
            val formatRp = DecimalFormatSymbols()

            formatRp.currencySymbol = "IDR "
            formatRp.monetaryDecimalSeparator = ','
            formatRp.groupingSeparator = '.'
            kursIdr.decimalFormatSymbols = formatRp

            return kursIdr.format(currencyFormat?.let { java.lang.Long.valueOf(it) })
        } catch (ignored: ParseException) {
        }
        return currencyFormat
    }
}