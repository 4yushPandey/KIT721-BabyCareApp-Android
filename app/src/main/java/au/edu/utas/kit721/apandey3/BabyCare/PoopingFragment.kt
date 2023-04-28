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
import au.edu.utas.kit721.apandey3.BabyCare.databinding.PoopListBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
val poopList = mutableListOf<PoopList>()


/**
 * A simple [Fragment] subclass.
 * Use the [PoopingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PoopingFragment : Fragment() {
        // TODO: Rename and change types of parameters
        private val poopList = mutableListOf<PoopList>()
        private lateinit var poopAdapter: PoopAdapter
        private lateinit var recyclerView: RecyclerView

        // ...

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_pooping, container, false)
            val myButton = view.findViewById<ImageButton>(R.id.my_button)
            myButton.setOnClickListener {
                val myIntent = Intent(activity, PoopActivity::class.java)
                startActivity(myIntent)
            }

            // Load data from Firebase and notify the adapter when it is done
            val poopHistory = Firebase.firestore.collection("poopHistory")
            poopHistory.get().addOnSuccessListener { result ->
                poopList.clear()

                Log.d(FIREBASE_TAG, "--- all poop list ---")

                for (document in result) {
                    val poop = document.toObject<PoopList>()
                    poop.id = document.id
                    poopList.add(poop)
                }

                // Initialize the adapter with the loaded data and set it to the RecyclerView
                poopAdapter = PoopAdapter(poopList)
                recyclerView.adapter = poopAdapter

                poopAdapter.notifyDataSetChanged()

            }
            // Replace 'android.R.id.list' with the 'id' of your RecyclerView
            recyclerView = view.findViewById(R.id.poopList)
            recyclerView.layoutManager = LinearLayoutManager(this.activity)
            return view
        }

        inner class PoopAdapter(private val poop: MutableList<PoopList>) : RecyclerView.Adapter<PoopHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoopHolder {
                val binding = PoopListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return PoopHolder(binding)
            }

            override fun getItemCount(): Int {
                return poop.size
            }

            override fun onBindViewHolder(holder: PoopHolder, position: Int) {

                val poop = poop[position]
                holder.ui.txtpoopDate.text = poop.poopTime
                holder.ui.txtNappyType.text = poop.nappyType
                // set other UI elements here
                holder.ui.root.setOnClickListener {
                    val i = Intent(holder.ui.root.context, EditPoopActivity::class.java)
                    i.putExtra("Poop", poopList[position])
                    startActivity(i)
                }

            }
        }
        inner class PoopHolder(var ui: PoopListBinding) : RecyclerView.ViewHolder(ui.root)
}