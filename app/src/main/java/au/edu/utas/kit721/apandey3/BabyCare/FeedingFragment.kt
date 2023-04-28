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
import au.edu.utas.kit721.apandey3.BabyCare.databinding.FeedListBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
val feedList = mutableListOf<FeedList>()


/**
 * A simple [Fragment] subclass.
 * Use the [FeedingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private var param1: String? = null
private var param2: String? = null


class FeedingFragment : Fragment() {

    private val feedList = mutableListOf<FeedList>()
    private lateinit var feedAdapter: FeedAdapter
    private lateinit var recyclerView: RecyclerView


    // ...

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feeding, container, false)

        val myButton = view.findViewById<ImageButton>(R.id.my_button)
        myButton.setOnClickListener {
            val myIntent = Intent(activity, FeedingActivity::class.java)
            startActivity(myIntent)
        }

        recyclerView = view.findViewById(R.id.feedList)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)

        // Load data from Firebase and initialize the adapter with the loaded data
        val feedingHistory = Firebase.firestore.collection("feedingHistory")
        feedingHistory.get().addOnSuccessListener { result ->
            feedList.clear()

            for (document in result) {
                Log.d(FIREBASE_TAG, document.toString())
                Log.d(FIREBASE_TAG, document.id)

                val feed = document.toObject<FeedList>()
                feed.id = document.id
                Log.d(FIREBASE_TAG, feed.toString())
                feedList.add(feed)
            }

            feedAdapter = FeedAdapter(feedList)
            recyclerView.adapter = feedAdapter
            feedAdapter.notifyDataSetChanged()
        }

        return view
    }

    inner class FeedAdapter(private val feeds: MutableList<FeedList>) : RecyclerView.Adapter<FeedHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHolder {
            val binding = FeedListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return FeedHolder(binding)
        }

        override fun getItemCount(): Int {
            return feeds.size
        }

        override fun onBindViewHolder(holder: FeedHolder, position: Int) {

            val feed = feeds[position]
            holder.ui.txtfeedDate.text = feed.feedStartTime.toString()
            holder.ui.txtfeedType.text = feed.milkType.toString()

            // set other UI elements here
            holder.ui.root.setOnClickListener {
                val i = Intent(holder.ui.root.context, EditFeed::class.java)
                i.putExtra("Feed", feeds[position])
                startActivity(i)
            }
        }
    }

    inner class FeedHolder(var ui: FeedListBinding) : RecyclerView.ViewHolder(ui.root)
}

