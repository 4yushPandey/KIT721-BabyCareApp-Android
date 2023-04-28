package au.edu.utas.kit721.apandey3.BabyCare

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

data class FeedList(

    @get:Exclude
    var id: String? = null,
    var feedStartTime: String? = "",
    var feedDuration: String? = "",

    var milkType: String? = "",
    var feedSide: String? = "",
    var quantity: String? = "",
    var feedNote: String? = ""
):Parcelable {
    // Override the getter for feedDate to convert Timestamp to String
    var feedStartTimeObj: Timestamp? = null
        set(value) {
            field = value
            feedStartTime = value?.toDate().toString()
        }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(feedStartTime)
        parcel.writeString(feedDuration)
        parcel.writeString(milkType)
        parcel.writeString(feedSide)
        parcel.writeString(quantity)
        parcel.writeString(feedNote)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FeedList> {
        override fun createFromParcel(parcel: Parcel): FeedList {
            return FeedList(parcel)
        }

        override fun newArray(size: Int): Array<FeedList?> {
            return arrayOfNulls(size)
        }
    }
}