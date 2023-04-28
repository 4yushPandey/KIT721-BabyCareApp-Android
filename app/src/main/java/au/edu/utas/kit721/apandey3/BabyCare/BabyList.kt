package au.edu.utas.kit721.apandey3.BabyCare

import com.google.firebase.firestore.Exclude

class BabyList {

    @get:Exclude var id : String? = null

    var babyName : String? = null
    var babyBirthDate : String? = null

}