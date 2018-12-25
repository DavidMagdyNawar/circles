package circles.circles.recyclers

import android.graphics.Movie
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.support.v7.widget.RecyclerView
import android.telephony.SmsMessage
import android.view.View
import android.widget.ImageView
import circles.circles.Message
import circles.circles.R


class MessageAdapter(private val messageLists: List<Message>) : RecyclerView.Adapter<MessageAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView
        var time: TextView
        var lastMessage: TextView

        init {
            name = view.findViewById(R.id.userName)
            time = view.findViewById(R.id.time)
            lastMessage = view.findViewById(R.id.userLastMEssage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.messages, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val message = messageLists[position]
        holder.name.text = message.userName
        holder.time.text = message.time
        holder.lastMessage.text = message.userlastMessage

    }

    override fun getItemCount(): Int {
        return messageLists.size
    }
}