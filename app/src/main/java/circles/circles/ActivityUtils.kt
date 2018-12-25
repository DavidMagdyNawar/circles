package circles.circles

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import java.util.ArrayList

class ActivityUtils {



    companion object {


        fun replaceFragmentToActivity(fragmentManager: FragmentManager,
                                      fragment: Fragment, frameId: Int) {

            val transaction = fragmentManager.beginTransaction()
            transaction.replace(frameId,fragment)
            transaction.commit()
        }
    }


}
