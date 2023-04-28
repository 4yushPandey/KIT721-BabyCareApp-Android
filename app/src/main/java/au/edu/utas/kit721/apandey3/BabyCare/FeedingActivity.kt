package au.edu.utas.kit721.apandey3.BabyCare

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import au.edu.utas.kit721.apandey3.BabyCare.databinding.ActivityFeedingBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate



class FeedingActivity : AppCompatActivity() {
    private lateinit var ui : ActivityFeedingBinding

    private lateinit var radioGroup: RadioGroup
    private lateinit var radioGroup2: RadioGroup
    private var selectedSide: RadioButton? = null
    private var selectedMilk: RadioButton? = null


    // https://firebase.google.com/docs/firestore/manage-data/add-data

    // https://www.tutorialspoint.com/how-to-get-the-selected-index-of-a-radiogroup-in-android-using-kotlin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityFeedingBinding.inflate(layoutInflater)
        setContentView(ui.root)

        val notes = ui.notes
        val btnSave = ui.button
        val quantity = ui.quantity

        val currentDate = LocalDate.now()
        val dayOfMonth = currentDate.dayOfMonth
        val month = currentDate.monthValue
        val year = currentDate.year

        radioGroup = ui.radioGroup
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            selectedMilk = findViewById<RadioButton>(checkedId)
        }

        radioGroup2 = ui.radioGroup2
        radioGroup2.setOnCheckedChangeListener { group, checkedId ->
            selectedSide = findViewById<RadioButton>(checkedId)
        }

        val db = Firebase.firestore

        btnSave.setOnClickListener {
            val durationEditText = findViewById<EditText>(R.id.duration)
            val startTimeEditText = findViewById<EditText>(R.id.time)

            val duration = durationEditText.text.toString()
            val startTime = startTimeEditText.text.toString()

            val feedingData = hashMapOf(
                "feedStartTime" to startTime,
                "feedDuration" to duration,
                "quantity" to quantity.text.toString(),
                "feedSide" to selectedSide?.text.toString(),
                "milkType" to selectedMilk?.text.toString(),
                "feedDate" to "$dayOfMonth-$month-$year",
                "feedNote" to notes.text.toString()
            )

            db.collection("feedingHistory")
                .add(feedingData)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                    Toast.makeText(this, "Error saving data!", Toast.LENGTH_SHORT).show()
                }

            finish()
        }
    }
}