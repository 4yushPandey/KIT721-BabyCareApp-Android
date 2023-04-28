package au.edu.utas.kit721.apandey3.BabyCare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import au.edu.utas.kit721.apandey3.BabyCare.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var ui : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)
        supportActionBar?.hide()

        val db = Firebase.firestore
        FirebaseApp.initializeApp(this)
        Log.d("FIREBASE", "Firebase connected: ${db.app.name}")

        Timer().schedule(object : TimerTask() {
            override fun run() {
                val intent = Intent(applicationContext, BabyListActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000)


    }
}