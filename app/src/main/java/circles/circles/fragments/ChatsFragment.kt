package circles.circles.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import circles.circles.recyclers.MessageAdapter
import android.support.v7.widget.DividerItemDecoration
import circles.circles.Message
import circles.circles.R


class ChatsFragment : Fragment() {

   companion object {
       fun newInstance(): ChatsFragment {
           val args = Bundle()
           val fragment = ChatsFragment()
           fragment.setArguments(args)
           return fragment
       }
   }

    var messageList = ArrayList<Message>()
    lateinit var recyclerView: RecyclerView
    lateinit var mAdapter: MessageAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater!!.inflate(R.layout.chats, container, false)

        recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView

        mAdapter = MessageAdapter(messageList)
        val mLayoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        recyclerView.adapter = mAdapter
        prepareMovieData()
        return view

    }

    private fun prepareMovieData() {
        var message = Message("David Magdy Nawar", "Thank you ", "12:00")
        messageList.add(message)

        message = Message("Khalid Ghanim", "Done", "1:33")
        messageList.add(message)

        message = Message("Hisham Magdy", "Check the Api Documentation, please", "2:15")
        messageList.add(message)

        message = Message("Ahmed Mohamed", "That's great", "5:00")
        messageList.add(message)





    }

}