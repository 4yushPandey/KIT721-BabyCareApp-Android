package au.edu.utas.kit721.apandey3.BabyCare

import android.content.ContentValues
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import au.edu.utas.kit721.apandey3.BabyCare.databinding.ActivityEditFeedBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditFeed : AppCompatActivity() {

    // https://firebase.google.com/docs/firestore/manage-data/delete-data


    private lateinit var ui : ActivityEditFeedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityEditFeedBinding.inflate(layoutInflater)
        setContentView(ui.root)

        val feed = intent.getParcelableExtra<FeedList>("Feed")

        Log.d(TAG, feed.toString())

        if (feed != null) {
            ui.time.setText(feed.feedStartTime.toString())
            ui.duration.setText(feed.feedDuration)
            ui.notes.setText(feed.feedNote)
            ui.quantity.setText(feed.quantity)

            if(feed.feedSide == "Left Side"){
                ui.rdoLeft.isChecked = true
                ui.rdoRight.isChecked = false
            }
            else {
                ui.rdoRight.isChecked = true
            }

            if(feed.milkType == "Mothers Milk"){
                ui.rdMotherMilk.isChecked = true
                ui.rdoBottleMilk.isChecked = false
            }
            else{
                ui.rdoBottleMilk.isChecked = true
            }
        }


        val db = Firebase.firestore
        val feedCollection = db.collection("feedingHistory")

        ui.btnDelete.setOnClickListener {
            if (feed != null) {
                feedCollection.document(feed.id!!)
                    .delete()
                    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                    .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }

                finish()
            }
        }
        ui.button.setOnClickListener {
            val updates = hashMapOf(
                "feedStartTime" to ui.time.text.toString(),
                "feedDuration" to ui.duration.text.toString(),
                "quantity" to ui.quantity.text.toString(),
                "feedSide" to if (ui.rdoLeft.isChecked) "Left Side" else "Right Side",
                "milkType" to if (ui.rdMotherMilk.isChecked) "Mothers Milk" else "Bottle Milk",
                "feedNote" to ui.notes.text.toString()
            )

            if (feed != null) {
                feedCollection.document(feed.id!!)
                    .set(updates)
                    .addOnSuccessListener {
                        Log.d(FIREBASE_TAG, "Successfully updated feed details ${feed.id}")
                        //return to the list
                        finish()
                    }
            }
    }
}
}