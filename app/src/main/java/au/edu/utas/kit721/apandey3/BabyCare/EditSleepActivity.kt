package au.edu.utas.kit721.apandey3.BabyCare

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import au.edu.utas.kit721.apandey3.BabyCare.databinding.ActivityEditSleepBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditSleepActivity : AppCompatActivity() {

    private lateinit var ui: ActivityEditSleepBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityEditSleepBinding.inflate(layoutInflater)
        setContentView(ui.root)

        val sleep = intent.getParcelableExtra<SleepList>("Sleep")

        Log.d(FIREBASE_TAG, sleep.toString())


        val db = Firebase.firestore
        val sleepCollection = db.collection("sleepHistory")

        if (sleep != null) {
            ui.startTime.setText(sleep.sleepStart.toString())
            ui.endTime.setText(sleep.sleepEnd.toString())
            ui.notes.setText(sleep.sleepNote)


            ui.btnDelete.setOnClickListener {
                if (sleep != null) {
                    sleepCollection.document(sleep.id!!)
                        .delete()
                        .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                        .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }

                    finish()
                }
            }

            ui.button.setOnClickListener {
                val sleepData = hashMapOf(
                    "sleepStart" to ui.startTime.text.toString(),
                    "sleepEnd" to ui.endTime.text.toString(),
                    "sleepNote" to ui.notes.text.toString()
                )

                sleepCollection.document(sleep.id!!)
                    .set(sleepData)
                    .addOnSuccessListener {
                            Log.d(FIREBASE_TAG, "Successfully updated feed details ${sleep.id}")
                            //return to the list
                            finish()
                        }
                }
            }
        }
    }
