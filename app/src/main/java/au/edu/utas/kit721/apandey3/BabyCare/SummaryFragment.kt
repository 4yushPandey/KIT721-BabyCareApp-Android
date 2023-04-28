package au.edu.utas.kit721.apandey3.BabyCare

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.time.LocalDate

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SummaryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SummaryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val sleepList = mutableListOf<SleepList>()
    val poopList = mutableListOf<PoopList>()
    val growthList = mutableListOf<GrowthList>()
    val feedList = mutableListOf<FeedList>()

    lateinit var txtFeed: TextView
    lateinit var txtSleep: TextView
    lateinit var txtGrowth: TextView
    lateinit var txtNappy: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_summary, container, false)

         txtFeed = view.findViewById(R.id.txtFeed)
         txtSleep = view.findViewById(R.id.txtSleep)
         txtGrowth = view.findViewById(R.id.txtGrowth)
         txtNappy = view.findViewById(R.id.txtNappy)

        val currentDate = LocalDate.now()
        val dayOfMonth = currentDate.dayOfMonth
        val month = currentDate.monthValue
        val year = currentDate.year


        val db = Firebase.firestore
        val feedCollection = db.collection("feedingHistory")
        val poopCollection = db.collection("poopHistory")
        val sleepCollection = db.collection("sleepHistory")
        val growthCollction = db.collection("growthHistory")


        val query = feedCollection.whereEqualTo("feedDate", "$dayOfMonth-$month-$year")
        query.get().addOnSuccessListener { result ->
            babyList.clear()
            Log.d(FIREBASE_TAG, "--- all feeds for today ---")
            for (document in result) {
                val feed = document.toObject<FeedList>()
                feed.id = document.id
                Log.d(FIREBASE_TAG, feed.toString())
                feedList.add(feed)
                txtFeed.text = feedList[0].feedDuration.toString()
            }
        }

        val query2 = poopCollection.whereEqualTo("poopDate", "$dayOfMonth-$month-$year")
        query2.get().addOnSuccessListener { result ->
            Log.d(FIREBASE_TAG, "--- all poops for today ---")
            for (document in result) {
                val poop = document.toObject<PoopList>()
                poop.id = document.id
                Log.d(FIREBASE_TAG, poop.toString())
                poopList.add(poop)
                txtNappy.text = poopList[0].poopTime.toString()
            }
        }

        growthCollction.get().addOnSuccessListener { result ->
            Log.d(FIREBASE_TAG, "--- all growth for today ---")
            for (document in result) {
                val growth = document.toObject<GrowthList>()
                growth.id = document.id
                Log.d(FIREBASE_TAG, growth.toString())
                growthList.add(growth)
                txtGrowth.text = growthList[0].height.toString()
            }
        }
        sleepCollection.get().addOnSuccessListener { result ->
            Log.d(FIREBASE_TAG, "--- all sleeps for today ---")
            for (document in result) {
                val sleep = document.toObject<SleepList>()
                sleep.id = document.id
                Log.d(FIREBASE_TAG, sleep.toString())
                sleepList.add(sleep)

                txtSleep.text = sleepList[0].sleepStart.toString()
            }
        }

        Log.d("Summary Data", txtFeed.toString()+ txtSleep.toString()+ txtGrowth.toString()+ txtSleep.toString())

        return view
    }
}

