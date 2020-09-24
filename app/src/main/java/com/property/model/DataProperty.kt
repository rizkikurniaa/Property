package com.property.model

import android.os.Parcel
import android.os.Parcelable

data class DataProperty (

    var idProperty: String? = null,
    var propertyName: String? = null,
    var location: String? = null,
    var subDistrict: String? = null,
    var bedRoomQty: String? = null,
    var bathRoomQty: String? = null,
    var areaWide: String? = null,
    var investmentCapital: String? = null,
    var price: String? = null,
    var installment: String? = null,
    var longInstallments: String? = null,
    var propertyImg: String? = null,
    var desc: String? = null,
    var totalInvest: String? = null,
    var totalInvestor: String? = null

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idProperty)
        parcel.writeString(propertyName)
        parcel.writeString(location)
        parcel.writeString(subDistrict)
        parcel.writeString(bedRoomQty)
        parcel.writeString(bathRoomQty)
        parcel.writeString(areaWide)
        parcel.writeString(investmentCapital)
        parcel.writeString(price)
        parcel.writeString(installment)
        parcel.writeString(longInstallments)
        parcel.writeString(propertyImg)
        parcel.writeString(desc)
        parcel.writeString(totalInvest)
        parcel.writeString(totalInvestor)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataProperty> {
        override fun createFromParcel(parcel: Parcel): DataProperty {
            return DataProperty(parcel)
        }

        override fun newArray(size: Int): Array<DataProperty?> {
            return arrayOfNulls(size)
        }
    }
}