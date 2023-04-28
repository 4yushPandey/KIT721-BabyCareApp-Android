package au.edu.utas.kit721.apandey3.BabyCare

import com.google.firebase.firestore.Exclude
import com.google.type.Date

class GrowthList {
    @get:Exclude
    var id : String? = null
    var height: String = ""
    var measuredDate : String? = null
}