package com.example.mydiary.models

import android.os.Parcel
import android.os.Parcelable

data class Diary(
    var id: Int = 0,
    var name: String? = "",
    var about: String? = "",
    var deleted: Int = 0,
    var favorite: Int = 0,
    var pack_name: String? = "",
    var deleted_time: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(about)
        parcel.writeInt(deleted)
        parcel.writeInt(favorite)
        parcel.writeString(pack_name)
        parcel.writeString(deleted_time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Diary> {
        override fun createFromParcel(parcel: Parcel): Diary {
            return Diary(parcel)
        }

        override fun newArray(size: Int): Array<Diary?> {
            return arrayOfNulls(size)
        }
    }
}
