package au.edu.utas.kit721.apandey3.BabyCare

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.Exclude

data class SleepList(
    @get:Exclude
    var id: String? = null,
    var sleepStart: String? = "",
    var sleepEnd: String? = null,
    var sleepNote: String? = null
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(sleepStart)
        parcel.writeString(sleepEnd)
        parcel.writeString(sleepNote)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SleepList> {
        override fun createFromParcel(parcel: Parcel): SleepList {
            return SleepList(parcel)
        }

        override fun newArray(size: Int): Array<SleepList?> {
            return arrayOfNulls(size)
        }
    }
}