package circles.circles.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import circles.circles.R
import circles.circles.recyclers.NewsFeedAdapter
import circles.circles.retrofit.RetrofitClient
import circles.circles.retrofit.data.NewsFeedData
import circles.circles.retrofit.responses.NewsFeedResponse
import com.facebook.FacebookSdk.getApplicationContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class UserPostsFragment : Fragment() {
    private var items = ArrayList<NewsFeedData>()
    private var recyclerView: RecyclerView? = null
    private var mAdapter: NewsFeedAdapter? = null
    lateinit var accessToken: String
    lateinit var tokenType: String

    companion object {
        fun newInstance(tokenType: String, accessToken: String): UserPostsFragment {
            val args = Bundle()
            val fragment = UserPostsFragment()
            args.putString("ACCESS_TOKEN", accessToken)
            args.putString("TOKEN_TYPE", tokenType)
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (arguments != null) {
             accessToken = arguments!!.getString("ACCESS_TOKEN")
             tokenType = arguments!!.getString("TOKEN_TYPE")
            getUserPosts(tokenType!!, accessToken!!)
        }
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view =  inflater!!.inflate(R.layout.user_posts, container, false)

        recyclerView = view.findViewById(R.id.postsRecycler) as RecyclerView
        val mLayoutManager = LinearLayoutManager(getApplicationContext())
        mAdapter = NewsFeedAdapter(items, getApplicationContext(),tokenType,accessToken)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = mAdapter

        return view
    }

    private fun getUserPosts(tokenType: String, accessToken: String) {

        var call: Call<NewsFeedResponse> = RetrofitClient
                .getInstance()
                .api
                .getUserPosts(tokenType + " " + accessToken)

        call.enqueue(object : Callback<NewsFeedResponse> {
            override fun onFailure(call: Call<NewsFeedResponse>?, t: Throwable?) {
                Toast.makeText(getApplicationContext(), t!!.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<NewsFeedResponse>?, response: Response<NewsFeedResponse>?) {
                val s = ""
                var se = response!!.isSuccessful
                var codee = response.code()
                if (response.code() == 300) {
                    Log.e("UserPost : ","Done without Data")
                    var newsFeedResponse : NewsFeedResponse = response.body()!!

                    Toast.makeText(activity, newsFeedResponse.message, Toast.LENGTH_LONG).show()
                } else if (response.code() == 400) {
                    Toast.makeText(activity, response.message() + response.code(), Toast.LENGTH_LONG).show()

                } else if (response.code() == 200) {
                    var newsFeedResponse : NewsFeedResponse = response.body()!!
                    items.addAll(newsFeedResponse.data)

                    mAdapter!!.notifyDataSetChanged()
                    val s = ""
                }
            }

        })


    }

}