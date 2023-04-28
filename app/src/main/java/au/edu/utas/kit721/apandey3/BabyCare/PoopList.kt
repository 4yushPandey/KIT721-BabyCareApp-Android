package au.edu.utas.kit721.apandey3.BabyCare

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.Exclude

data class PoopList (
    @get:Exclude
    var id : String? = null,
    var poopTime: String? = null,
    var nappyType: String? = null,
    var poopImage: String? = null,
    var poopNote: String? = null

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(poopTime)
        parcel.writeString(nappyType)
        parcel.writeString(poopImage)
        parcel.writeString(poopNote)
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<PoopList> {
        override fun createFromParcel(parcel: Parcel): PoopList {
            return PoopList(parcel)
        }

        override fun newArray(size: Int): Array<PoopList?> {
            return arrayOfNulls(size)
        }
    }
}