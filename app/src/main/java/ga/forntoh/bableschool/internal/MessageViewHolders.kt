package ga.forntoh.bableschool.internal

import android.graphics.Color
import android.view.View
import com.stfalcon.chatkit.messages.MessageHolders
import ga.forntoh.bableschool.data.model.forum.FUser
import ga.forntoh.bableschool.data.model.forum.Message
import kotlinx.android.synthetic.main.item_incoming_text_message.view.*

class IncomingMessageViewHolder(private val myView: View): MessageHolders.IncomingTextMessageViewHolder<Message>(myView, Any()) {

    override fun onBind(data: Message?) {
        super.onBind(data)

        myView.messageSender.text = data?.user?.name
        myView.messageSender.setTextColor(Color.parseColor((data?.user as FUser).color))
    }

}