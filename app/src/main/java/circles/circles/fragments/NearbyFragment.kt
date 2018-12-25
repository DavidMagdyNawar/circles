package circles.circles.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import circles.circles.R

class NearbyFragment :  Fragment() {
    lateinit var accessToken: String
    lateinit var tokenType: String

    companion object {
        fun newInstance(tokenType: String, accsToken: String): NearbyFragment {
            val args = Bundle()
            val fragment = NearbyFragment()
            args.putString("ACCESS_TOKEN", accsToken)
            args.putString("TOKEN_TYPE", tokenType)
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (arguments != null) {
            accessToken = arguments!!.getString("ACCESS_TOKEN")
            tokenType = arguments!!.getString("TOKEN_TYPE")
        }

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater!!.inflate(R.layout.fragment_nearby, container, false)
        return view
    }
}
