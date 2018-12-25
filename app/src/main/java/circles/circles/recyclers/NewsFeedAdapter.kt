package circles.circles.recyclers

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.Image
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import android.view.View
import circles.circles.R
import circles.circles.retrofit.data.NewsFeedData
import android.os.Handler
import android.os.Message

import android.widget.SeekBar
import android.widget.TextView
import android.widget.ImageButton
import kotlinx.android.synthetic.main.post_item.view.*
import java.io.IOException
import android.media.MediaPlayer.OnPreparedListener
import android.util.Log
import android.widget.Toast
import circles.circles.retrofit.RetrofitClient
import circles.circles.retrofit.responses.LikedAndDislikeResponse
import circles.circles.retrofit.responses.NewsFeedResponse
import com.facebook.FacebookSdk.getApplicationContext

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NewsFeedAdapter(private val items: List<NewsFeedData>, val context: Context,tokenType: String,
                      accessToken: String)
    : RecyclerView.Adapter<NewsFeedAdapter.MyViewHolder>() {
    var tokenType = tokenType
    var accessToken = accessToken
    private var playingPosition = -1
    var mediaPlayer : MediaPlayer? = null
    private val seekBarUpdater = SeekBarUpdater()
    var viewHolder: MyViewHolder? = null

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.post_item, parent, false)
        viewHolder = MyViewHolder(view)
        return MyViewHolder(view).listen { pos, type ->
            val item = items[pos]
            //TODO do other stuff here
        }
    }

    override fun onBindViewHolder(holder: NewsFeedAdapter.MyViewHolder, position: Int) {
        holder.userName.text = items[position].fullName
        holder.text.text = items[position].text
//        holder.voice_path = items[position].voiceNote





    }




    private inner class SeekBarUpdater : Runnable {
        override fun run() {
            if (null != viewHolder && mediaPlayer!=null)
            {

                viewHolder!!.seekBar.max = mediaPlayer!!.duration
                viewHolder!!.seekBar.progress = mediaPlayer!!.currentPosition
                viewHolder!!.seekBar.postDelayed(this, 100)
            }
            else {
                viewHolder!!.seekBar.removeCallbacks(seekBarUpdater)
            }
        }
    }


    fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
        var play = itemView.findViewById<ImageButton>(R.id.play)
//        var seekBar = itemView.findViewById<SeekBar>(R.id.seekbar)
        var pause = itemView.findViewById<ImageButton>(R.id.pause)
        var playedBefore = false
        var length = 0


        var like = itemView.findViewById<ImageButton>(R.id.like)
        like.setOnClickListener {
            likeAndDislikePost(tokenType, accessToken, items[adapterPosition].id.toString())
        }

        var share = itemView.findViewById<ImageButton>(R.id.share)
        share.setOnClickListener {
            sharePost(tokenType, accessToken, items[adapterPosition].id.toString())

        }

        play.setOnClickListener {
            playingPosition = adapterPosition


            play.visibility = View.INVISIBLE
            pause.visibility = View.VISIBLE

            Toast.makeText(context, "Played clicked", Toast.LENGTH_SHORT).show()
            playingPosition = adapterPosition
            if (position == playingPosition) {
                viewHolder = MyViewHolder(itemView)
                viewHolder!!.seekBar.post(seekBarUpdater)
            } else {
                viewHolder!!.seekBar.removeCallbacks(seekBarUpdater);
//                viewHolder!!.seekBar.progress = 0;
            }
            if (playedBefore) {
                mediaPlayer!!.seekTo(length)
                mediaPlayer!!.start()
//                playedBefore = false


            }
            else
            {
                if (mediaPlayer != null) {
                    if (null != viewHolder) {
                        viewHolder!!.seekBar.removeCallbacks(seekBarUpdater);
                        viewHolder!!.seekBar.progress = 0
                    }
                    mediaPlayer!!.release()
                }

                viewHolder = MyViewHolder(itemView)
//            viewHolder = this;
                mediaPlayer = MediaPlayer()

                startMediaPlayer(items[adapterPosition].voiceNote)
                viewHolder!!.seekBar.max = mediaPlayer!!.duration
                viewHolder!!.seekBar.post(seekBarUpdater)
                playedBefore = true

            }
        }

        pause.setOnClickListener {
            playingPosition = adapterPosition
            if (position == playingPosition) {
                viewHolder = MyViewHolder(itemView)
                viewHolder!!.seekBar.post(seekBarUpdater)
            } else {
                viewHolder!!.seekBar.removeCallbacks(seekBarUpdater);
//                viewHolder!!.seekBar.progress = 0;
            }
            play.visibility = View.VISIBLE
            pause.visibility = View.INVISIBLE
            mediaPlayer!!.pause()
            playedBefore = true
            length=mediaPlayer!!.currentPosition
//            viewHolder!!.seekBar.post(seekBarUpdater)

        }
        return this
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) , SeekBar.OnSeekBarChangeListener{


        lateinit var voice_path: String

        var userName: TextView
        var text: TextView
        var play: ImageButton
        var pause: ImageButton
        var seekBar: SeekBar
        var like: ImageButton

        init {
            userName = view.findViewById(R.id.userName) as TextView
            text = view.findViewById(R.id.text_post) as TextView
            play = view.findViewById(R.id.play) as ImageButton
            pause = view.findViewById(R.id.pause) as ImageButton
            seekBar = view.findViewById(R.id.seekbar) as SeekBar
            like = view.findViewById(R.id.like) as ImageButton
            seekBar.setOnSeekBarChangeListener(this)
        }

        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            if(p2 && mediaPlayer!!.isPlaying)
                mediaPlayer!!.seekTo(p1)
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {
        }

        override fun onStopTrackingTouch(p0: SeekBar?) {
        }


    }


    private fun startMediaPlayer(s: String) {
        var ss = s
//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer!!.setDataSource(s)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
//
//


        } catch (e: IllegalArgumentException) {
            Log.e("ArgumentException", e.toString())
        } catch (e: IllegalStateException) {
            Log.e("StateException", e.toString())
        } catch (e: IOException) {
            Log.e("IOException", e.toString())
        }

        mediaPlayer!!.setOnCompletionListener {
            mediaPlayer!!.reset()
            mediaPlayer!!.release()
            mediaPlayer = null
            playingPosition = -1
        }
    }

    fun likeAndDislikePost(tokenType: String, accessToken: String, id:String) {

        var call: Call<LikedAndDislikeResponse> = RetrofitClient
                .getInstance()
                .api
                .likeAndDislikePost(tokenType + " " + accessToken,id)

        call.enqueue(object : Callback<LikedAndDislikeResponse> {
            override fun onFailure(call: Call<LikedAndDislikeResponse>?, t: Throwable?) {
                Toast.makeText(getApplicationContext(), t!!.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<LikedAndDislikeResponse>?, response: Response<LikedAndDislikeResponse>?) {
                val s = ""
                var se = response!!.isSuccessful
                var codee = response.code()
                if (response.code() == 201) {
                    Toast.makeText(context, response.message() + response.code(), Toast.LENGTH_LONG).show()
                }
                 else if (response.code() == 200) {
                    var getUserLikedPostsResponse : LikedAndDislikeResponse = response.body()!!
                    Toast.makeText(context, response.message() + response.code(), Toast.LENGTH_LONG).show()

                }
            }

        })


    }


    fun sharePost(tokenType: String, accessToken: String, id:String) {

        var call: Call<LikedAndDislikeResponse> = RetrofitClient
                .getInstance()
                .api
                .sharePost(tokenType + " " + accessToken,id)

        call.enqueue(object : Callback<LikedAndDislikeResponse> {
            override fun onFailure(call: Call<LikedAndDislikeResponse>?, t: Throwable?) {
                Toast.makeText(getApplicationContext(), t!!.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<LikedAndDislikeResponse>?, response: Response<LikedAndDislikeResponse>?) {
                val s = ""
                var se = response!!.isSuccessful
                var codee = response.code()
                if (response.code() == 201) {
                    Toast.makeText(context, response.message() + response.code(), Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 200) {
                    var getUserLikedPostsResponse : LikedAndDislikeResponse = response.body()!!
                    Toast.makeText(context, response.message() + response.code(), Toast.LENGTH_LONG).show()

                }
            }

        })


    }

}


