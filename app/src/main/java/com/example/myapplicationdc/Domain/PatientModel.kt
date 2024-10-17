package com.example.myapplicationdc.Domain

import android.os.Parcel
import android.os.Parcelable

data class PatientModel (
    var id: Int = 0,
    var pname: String = "",
    var age: Int = 0,
    var gender: String = "",
    var medicalHistory: String = "",
    var prescriptionPictures: String = "",
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(pname)
        parcel.writeInt(age)
        parcel.writeString(gender)
        parcel.writeString(medicalHistory)
        parcel.writeString(prescriptionPictures)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PatientModel> {
        override fun createFromParcel(parcel: Parcel): PatientModel {
            return PatientModel(parcel)
        }

        override fun newArray(size: Int): Array<PatientModel?> {
            return arrayOfNulls(size)
        }
    }
}
