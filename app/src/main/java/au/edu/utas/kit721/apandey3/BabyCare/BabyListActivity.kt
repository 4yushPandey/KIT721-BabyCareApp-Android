package au.edu.utas.kit721.apandey3.BabyCare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import au.edu.utas.kit721.apandey3.BabyCare.databinding.ActivityBabyListBinding
import au.edu.utas.kit721.apandey3.BabyCare.databinding.BabyListBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

const val FIREBASE_TAG = "FirebaseLogging"
val babyList = mutableListOf<BabyList>()
const val BABY_INDEX = "Baby_Index"


class BabyListActivity : AppCompatActivity() {

    private lateinit var ui : ActivityBabyListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityBabyListBinding.inflate(layoutInflater)
        setContentView(ui.root)

        val db = Firebase.firestore

        ui.babyList.layoutManager = LinearLayoutManager(this)
        ui.babyList.adapter = BabyAdapter(babies = babyList)

        val babyCollection = db.collection("babies")



        babyCollection
            .get()
            .addOnSuccessListener { result ->
                babyList.clear()
                Log.d(FIREBASE_TAG, "--- all babies ---")
                for (document in result) {
                    val baby = document.toObject<BabyList>()
                    baby.id = document.id
                    Log.d(FIREBASE_TAG, baby.toString())
                    babyList.add(baby)
                }
                (ui.babyList.adapter as BabyAdapter).notifyDataSetChanged()
            }
    }
    override fun onResume() {
        super.onResume()
        ui.babyList.adapter?.notifyDataSetChanged() //without a more complicated set-up, we can't be more specific than "dataset changed"
    }

    inner class BabyHolder(var ui: BabyListBinding) : RecyclerView.ViewHolder(ui.root)

    inner class BabyAdapter(private val babies: MutableList<BabyList>) : RecyclerView.Adapter<BabyHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): BabyHolder {
            val ui = BabyListBinding.inflate(
                layoutInflater,
                parent,
                false
            )   //inflate a new row from the my_list_item.xml
            return BabyHolder(ui)                                                            //wrap it in a ViewHolder
        }

        override fun getItemCount(): Int {
            return babies.size
        }

        override fun onBindViewHolder(holder: BabyHolder, position: Int) {
            val babyList = babies[position]   //get the data at the requested position
            holder.ui.txtBabyName.text = babyList.babyName
            holder.ui.txtBabyBirthDate.text = babyList.babyBirthDate

            holder.ui.root.setOnClickListener {
                val i = Intent(holder.ui.root.context, DashboardActivity::class.java)
                startActivity(i)
                finish()
            }
        }
    }
}