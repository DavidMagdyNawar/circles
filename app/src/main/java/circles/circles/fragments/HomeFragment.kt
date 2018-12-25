package circles.circles.fragments

import android.Manifest
import android.Manifest.permission.RECORD_AUDIO
import android.app.Activity
import android.app.Activity.*
import android.content.Intent
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import circles.circles.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.support.v7.widget.DefaultItemAnimator
import com.facebook.FacebookSdk.getApplicationContext
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.design.widget.FloatingActionButton
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Point
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.*
import android.support.design.R.attr.height
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.getSystemService
import android.util.Log
import android.view.WindowManager
import android.widget.*
import circles.circles.ConfirmUserActivity
import circles.circles.R
import circles.circles.recyclers.NewsFeedAdapter
import circles.circles.retrofit.data.NewsFeedData
import circles.circles.retrofit.responses.AddPostResponse
import circles.circles.retrofit.responses.LikedAndDislikeResponse
import circles.circles.retrofit.responses.NewsFeedResponse
import com.facebook.FacebookSdk
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Multipart
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class HomeFragment : Fragment() {
    private var items = ArrayList<NewsFeedData>()
    private var recyclerView: RecyclerView? = null
    private var mAdapter: NewsFeedAdapter? = null
    lateinit var accessToken: String
    lateinit var tokenType: String
    lateinit var myAudioRecorder: MediaRecorder

    companion object {
        fun newInstance(tokenType: String, accessToken: String): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString("ACCESS_TOKEN", accessToken)
            args.putString("TOKEN_TYPE", tokenType)
            fragment.arguments = args
            return fragment
        }

    }

    override fun onPause() {
        super.onPause()
//         mAdapter!!.stopPlayer()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        if (arguments != null) {
            accessToken = arguments!!.getString("ACCESS_TOKEN")
            tokenType = arguments!!.getString("TOKEN_TYPE")
            getNewsFeed(tokenType, accessToken)

        }

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recycler_posts_home) as RecyclerView
        var fab = view.findViewById<FloatingActionButton>(R.id.fab)
        var close_record = view.findViewById<ImageView>(R.id.close_record)
        var transparentOverlay = view.findViewById<RelativeLayout>(R.id.transparentOverlay)
        var send_record = view.findViewById<ImageView>(R.id.send_record)
        val mLayoutManager = LinearLayoutManager(getApplicationContext())
        mAdapter = NewsFeedAdapter(items, getApplicationContext(), arguments!!.getString("TOKEN_TYPE"),
                arguments!!.getString("ACCESS_TOKEN"))
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = mAdapter

        myAudioRecorder = MediaRecorder()
        var outputFile: String = Environment.getExternalStorageDirectory().absolutePath + "/recording.wav"

        send_record.setOnClickListener {
            try {
                myAudioRecorder.stop()
                myAudioRecorder.release()

            } catch (e: IllegalStateException) {
                // make something ...
                var s = ""
            } catch (e: IOException) {
                // make something
                var s = ""
            }

            addPostRecord(tokenType, accessToken, outputFile)
        }

        fab.setOnClickListener {
            if (requestRecordAudioPermission()) {
                try {

                    transparentOverlay.visibility = View.VISIBLE
                    fab.hide()
                    myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
                    myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.MPEG_4)
                    myAudioRecorder.setOutputFile(outputFile)

                    myAudioRecorder.prepare()
                    myAudioRecorder.start()
                } catch (e: IllegalStateException) {
                    // make something ...
                } catch (e: IOException) {
                    // make something
                }
                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            }

        }

        close_record.setOnClickListener {
            fab.show()
            transparentOverlay.visibility = View.GONE
            try {
                myAudioRecorder.stop()
                myAudioRecorder.release()
            } catch (e: IllegalStateException) {
                // make something ...
                var s = ""
            } catch (e: IOException) {
                // make something
                var s = ""
            }

            Toast.makeText(getApplicationContext(), "Audio Recorder stopped", Toast.LENGTH_LONG).show();

        }
        return view
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 101 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // This method is called when the  permissions are given
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                // Great! User has recorded and saved the audio file
            } else if (resultCode == RESULT_CANCELED) {
                // Oops! User has canceled the recording
            }
        }
    }


    private fun getNewsFeed(tokenType: String, accessToken: String) {
        val sss = tokenType
        val c = accessToken
        var call: Call<NewsFeedResponse> = RetrofitClient
                .getInstance()
                .api
                .getNewsfeed(tokenType + " " + accessToken)

        call.enqueue(object : Callback<NewsFeedResponse> {
            override fun onFailure(call: Call<NewsFeedResponse>?, t: Throwable?) {
                Toast.makeText(getApplicationContext(), t!!.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<NewsFeedResponse>?, response: Response<NewsFeedResponse>?) {
                val s = ""
                var se = response!!.isSuccessful
                var codee = response.code()
                if (response.code() == 300) {
                    Toast.makeText(activity, response.message(), Toast.LENGTH_LONG).show()
                } else if (response.code() == 400) {
                    Toast.makeText(activity, response.message() + response.code(), Toast.LENGTH_LONG).show()
                    val mIntent = Intent(activity, ConfirmUserActivity::class.java)
                    mIntent.putExtra("ACCESS_TOKEN", accessToken)
                    mIntent.putExtra("TOKEN_TYPE", tokenType)
                    startActivity(mIntent)
                } else if (response.code() == 200) {
                    var newsFeedResponse: NewsFeedResponse = response.body()!!
                    items.addAll(newsFeedResponse.data)
                    mAdapter!!.notifyDataSetChanged()
                    val s = ""
                }
            }

        })


    }

    private fun addPostRecord(tokenType: String, accessToken: String, filePath: String) {
        val sss = tokenType
        val c = accessToken
        var file = File(filePath)

        var requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        var body =
                MultipartBody.Part.createFormData("voice_note", file.getName(), requestFile)

        var call: Call<ResponseBody> = RetrofitClient
                .getInstance()
                .api
                .addPost(tokenType + " " + accessToken, body)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                Toast.makeText(getApplicationContext(), t!!.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                val s = ""
                var se = response!!.isSuccessful
                var codee = response.code()
                var message = response.message()
                
                var addPostResponse: ResponseBody = response.body()!!

                if (response.code() == 300) {
                    Toast.makeText(activity, response.message(), Toast.LENGTH_LONG).show()
                } else if (response.code() == 400) {

                    Toast.makeText(activity, response.message() + response.code(), Toast.LENGTH_LONG).show()
//                    val mIntent = Intent(activity, ConfirmUserActivity::class.java)
//                    mIntent.putExtra("ACCESS_TOKEN", accessToken)
//                    mIntent.putExtra("TOKEN_TYPE", tokenType)
//                    startActivity(mIntent)
                } else if (response.code() == 200) {
                    var addPostResponse: ResponseBody = response.body()!!
//                    items.addAll(newsFeedResponse.data)
//                    mAdapter!!.notifyDataSetChanged()
                    val s = ""
                }
            }

        })


    }


    fun requestRecordAudioPermission(): Boolean {

        var requiredPermission = Manifest.permission.RECORD_AUDIO;
        var requiredWSriteExternalStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE

        // If the user previously denied this permission then show a message explaining why
        // this permission is needed
        if (getActivity()!!.checkCallingOrSelfPermission(requiredPermission) ==
                PackageManager.PERMISSION_GRANTED
                && getActivity()!!.checkCallingOrSelfPermission(requiredWSriteExternalStorage) ==
                PackageManager.PERMISSION_GRANTED) {
            return true
        } else {
            requestPermissions(arrayOf(requiredPermission, requiredWSriteExternalStorage), 101)
            return false
        }


    }


}