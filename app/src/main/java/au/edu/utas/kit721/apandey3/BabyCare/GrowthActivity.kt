package au.edu.utas.kit721.apandey3.BabyCare

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import au.edu.utas.kit721.apandey3.BabyCare.databinding.ActivityGrowthBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate

class GrowthActivity : AppCompatActivity() {

    private lateinit var ui : ActivityGrowthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityGrowthBinding.inflate(layoutInflater)
        setContentView(ui.root)

        val currentDate = LocalDate.now()
        val dayOfMonth = currentDate.dayOfMonth
        val month = currentDate.monthValue
        val year = currentDate.year

        val db = Firebase.firestore


        ui.button.setOnClickListener {
            val growthData = hashMapOf(
                "height" to ui.height.text.toString(),
                "measuredDate" to "$dayOfMonth-$month-$year",
            )

            db.collection("growthHistory")
                .add(growthData)
                .addOnSuccessListener { documentReference ->
                    Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                    Toast.makeText(this, "Error saving data!", Toast.LENGTH_SHORT).show()
                }
            finish()
        }
    }
}