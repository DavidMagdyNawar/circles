package circles.circles

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import circles.circles.fragments.ChatsFragment
import circles.circles.fragments.HomeFragment
import circles.circles.fragments.NearbyFragment
import circles.circles.fragments.ProfileFragment
import kotlinx.android.synthetic.main.toolbar.*


public class Home : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //toolbar
        var toolbar = findViewById<View>(R.id.toolbar) as Toolbar?
        setSupportActionBar(toolbar)



        val accesToken=intent.getStringExtra("ACCESS_TOKEN")
        val tokenType=intent.getStringExtra("TOKEN_TYPE")



        // Home is the Default selection for the bottom nav
        val home = HomeFragment.newInstance(tokenType,accesToken)

        ActivityUtils.replaceFragmentToActivity(
                supportFragmentManager, home, R.id.content)
        //end

        //bottom Nav on select item change the content
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation)
        lateinit var selectedFragment :Fragment

        bottomNavigationView.setOnNavigationItemSelectedListener {
            item ->
            when (item.itemId) {
                R.id.home -> {
                    val home = HomeFragment.newInstance(tokenType,accesToken)
                     selectedFragment  = home
                }
                R.id.nearby -> {
                    selectedFragment = NearbyFragment.newInstance(tokenType,accesToken)
                }
                R.id.profile -> {
                    selectedFragment = ProfileFragment.newInstance(tokenType,accesToken)

                }
                R.id.chats -> {
                    selectedFragment = ChatsFragment.newInstance()
                }
            }
            ActivityUtils.replaceFragmentToActivity(
                    supportFragmentManager, selectedFragment, R.id.content)
            true
        }
        //end bottom nav

        //search bar
        search_bar_edit_text.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                performSearch()
                return@OnEditorActionListener true
            }
            false
        })
        //end search bar

    }

    //replace fragments inside the framelayout
    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }
}
