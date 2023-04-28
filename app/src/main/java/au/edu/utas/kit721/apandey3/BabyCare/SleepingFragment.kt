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
import au.edu.utas.kit721.apandey3.BabyCare.databinding.SleepListBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
val sleepList = mutableListOf<SleepList>()


/**
 * A simple [Fragment] subclass.
 * Use the [SleepingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SleepingFragment : Fragment() {
    private val sleepList = mutableListOf<SleepList>()
    private lateinit var sleepAdapter: SleepAdapter
    private lateinit var recyclerView: RecyclerView

    // ...


    // https://gist.github.com/chrisblakely01/fbaa95cbf736342e09354e3695e3a31d


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sleeping, container, false)
        val myButton = view.findViewById<ImageButton>(R.id.my_button)
        myButton.setOnClickListener {
            val myIntent = Intent(activity, SleepActivity::class.java)
            startActivity(myIntent)
        }

        // Load data from Firebase and notify the adapter when it is done
        val sleepHistory = Firebase.firestore.collection("sleepHistory")
        sleepHistory.get().addOnSuccessListener { result ->
            sleepList.clear()

            Log.d(FIREBASE_TAG, "--- all poop list ---")

            for (document in result) {
                val sleep = document.toObject<SleepList>()
                sleep.id = document.id
                sleepList.add(sleep)
            }

            // Initialize the adapter with the loaded data and set it to the RecyclerView
            sleepAdapter = SleepAdapter(sleepList)
            recyclerView.adapter = sleepAdapter

            sleepAdapter.notifyDataSetChanged()

        }
        // Replace 'android.R.id.list' with the 'id' of your RecyclerView
        recyclerView = view.findViewById(R.id.sleepList)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        return view
    }

    inner class SleepAdapter(private val sleep: MutableList<SleepList>) : RecyclerView.Adapter<SleepHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SleepHolder {
            val binding = SleepListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SleepHolder(binding)
        }

        override fun getItemCount(): Int {
            return sleep.size
        }

        override fun onBindViewHolder(holder: SleepHolder, position: Int) {

            val sleep = sleep[position]
            holder.ui.txtSleepStart.text = sleep.sleepStart
            holder.ui.txtSleepEnd.text = sleep.sleepEnd
            // set other UI elements here
            holder.ui.root.setOnClickListener {
                val i = Intent(holder.ui.root.context, EditSleepActivity::class.java)
                i.putExtra("Sleep", sleepList[position])
                Log.d(FIREBASE_TAG, sleepList[position].toString())

                startActivity(i)
            }
        }
    }

    inner class SleepHolder(var ui: SleepListBinding) : RecyclerView.ViewHolder(ui.root)
}