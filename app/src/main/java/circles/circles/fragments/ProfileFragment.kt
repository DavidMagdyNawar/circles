package circles.circles.fragments

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v4.widget.NestedScrollView
import circles.circles.MyPagerAdapter
import circles.circles.R


class ProfileFragment : Fragment() {
    lateinit var accessToken: String
    lateinit var tokenType: String

    companion object {
        fun newInstance(tokenType: String, accsToken: String): ProfileFragment {
            val args = Bundle()
            val fragment = ProfileFragment()
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
        var view = inflater!!.inflate(R.layout.fragment_profile, container, false)

        val scrollView = view.findViewById(R.id.nestedscroll) as NestedScrollView
        scrollView.isFillViewport = true
        val fragmentAdapter = MyPagerAdapter(this.childFragmentManager, tokenType, accessToken)

        var tabLayout: TabLayout = view.findViewById(R.id.tabs_main)

        var viewPager: ViewPager = view.findViewById(R.id.viewpager_main)
        tabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.colorPrimary))

        viewPager.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)

        tabLayout.addOnTabSelectedListener (object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
             when(p0!!.position){
                 0 -> UserPostsFragment.newInstance(tokenType, accessToken)
                 1 -> UserLikedPostsFragment.newInstance(tokenType, accessToken)
             }
            }
        })

        return view
    }

}