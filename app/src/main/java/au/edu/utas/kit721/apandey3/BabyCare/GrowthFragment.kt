package au.edu.utas.kit721.apandey3.BabyCare

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import au.edu.utas.kit721.apandey3.BabyCare.databinding.GrowthListBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
val growthList = mutableListOf<GrowthList>()



/**
 * A simple [Fragment] subclass.
 * Use the [GrowthFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GrowthFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private val growthList = mutableListOf<GrowthList>()
    private lateinit var growthAdapter: GrowthAdapter
    private lateinit var recyclerView: RecyclerView

    // ...

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_growth, container, false)
        val myButton = view.findViewById<ImageButton>(R.id.my_button)
        myButton.setOnClickListener {
            val myIntent = Intent(activity, GrowthActivity::class.java)
            startActivity(myIntent)
        }

        // Load data from Firebase and notify the adapter when it is done
        val growthHistory = Firebase.firestore.collection("growthHistory")
        growthHistory.get().addOnSuccessListener { result ->
            growthList.clear()

            Log.d(FIREBASE_TAG, "--- all growth list ---")

            for (document in result) {
                val growth = document.toObject<GrowthList>()
                growth.id = document.id
                Log.d(FIREBASE_TAG, growth.toString())
                growthList.add(growth)
            }

            // Initialize the adapter with the loaded data and set it to the RecyclerView
            growthAdapter = GrowthAdapter(growthList)
            recyclerView.adapter = growthAdapter

            growthAdapter.notifyDataSetChanged()

        }
        // Replace 'android.R.id.list' with the 'id' of your RecyclerView
        recyclerView = view.findViewById(R.id.growthList)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        return view
    }

    inner class GrowthAdapter(private val growth: MutableList<GrowthList>) : RecyclerView.Adapter<GrowthHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrowthHolder {
            val binding = GrowthListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return GrowthHolder(binding)
        }

        override fun getItemCount(): Int {
            return growth.size
        }

        override fun onBindViewHolder(holder: GrowthHolder, position: Int) {

            val growth = growth[position]
            holder.ui.txtGrowthHeight.text = growth.height.toString()
            holder.ui.txtGrowthMeasureDate.text = growth.measuredDate.toString()
            // set other UI elements here
            holder.ui.root.setOnClickListener {
                val i = Intent(holder.ui.root.context, GrowthActivity::class.java)
                startActivity(i)
            }
        }
    }

    inner class GrowthHolder(var ui: GrowthListBinding) : RecyclerView.ViewHolder(ui.root)
}