package alexandre.cavaleiro.pdmchat.adapter

import alexandre.cavaleiro.pdmchat.R
import alexandre.cavaleiro.pdmchat.databinding.TileMessageBinding
import alexandre.cavaleiro.pdmchat.model.MessageChat
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.time.format.DateTimeFormatter

class MessageAdapter(
    context: Context,
    private val messageList: MutableList<MessageChat>
): ArrayAdapter<MessageChat>(context, R.layout.tile_message, messageList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View{
        val message = messageList[position]
        var tmb: TileMessageBinding? = null

        var messageTileView = convertView
        if (messageTileView == null){
            tmb = TileMessageBinding.inflate(
                context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent, false
            )

            messageTileView = tmb.root

            val tileMessageHolder = TileMessageHolder(
                tmb.senderTv,
                tmb.timestampTv,
                tmb.messageTv,
            )

            messageTileView.tag = tileMessageHolder
        }

        val holder = messageTileView.tag as TileMessageHolder
        val tmstpFormater = DateTimeFormatter.ofPattern("dd/MM/yy '|' HH:mm")

        holder.msgSenderTv.text = message.escritor
        holder.msgTimeStampTv.text = message.dataHora.format(tmstpFormater)
        holder.msgContentTv.text = message.conteudo

        return messageTileView

    }

    private class TileMessageHolder(
        val msgSenderTv: TextView,
        val msgTimeStampTv: TextView,
        val msgContentTv: TextView,
    )
}