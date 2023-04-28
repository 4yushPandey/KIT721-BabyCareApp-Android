package au.edu.utas.kit721.apandey3.BabyCare

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import au.edu.utas.kit721.apandey3.BabyCare.databinding.ActivitySleepBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SleepActivity : AppCompatActivity() {
    private lateinit var ui :ActivitySleepBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivitySleepBinding.inflate(layoutInflater)
        setContentView(ui.root)

        val db = Firebase.firestore

        ui.button.setOnClickListener {
            val sleepData = hashMapOf(
                "sleepStart" to ui.startTime.text.toString(),
                "sleepEnd" to ui.endTime.text.toString(),
                "sleepNote" to ui.notes.text.toString()
                )

            db.collection("sleepHistory")
                .add(sleepData)
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
