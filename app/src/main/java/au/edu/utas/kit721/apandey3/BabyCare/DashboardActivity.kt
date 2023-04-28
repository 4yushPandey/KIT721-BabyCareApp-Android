package au.edu.utas.kit721.apandey3.BabyCare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import au.edu.utas.kit721.apandey3.BabyCare.databinding.ActivityDashboardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity : AppCompatActivity() {

    // https://www.geeksforgeeks.org/bottom-navigation-bar-in-android-using-kotlin/?ref=gcse

    private lateinit var ui : ActivityDashboardBinding

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var fragmentContainer: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(ui.root)

        bottomNavigationView = ui.bottomNavigation

        bottomNavigationView.itemIconTintList = null
        replaceFragment(FeedingFragment())

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.feeding -> replaceFragment(FeedingFragment())
                R.id.sleeping -> replaceFragment(SleepingFragment())
                R.id.pooping -> replaceFragment(PoopingFragment())
                R.id.growth -> replaceFragment(GrowthFragment())
                R.id.summary -> replaceFragment(SummaryFragment())

                else -> {

                }
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container,fragment)
        fragmentTransaction.commit()
    }
}