package alexandre.cavaleiro.pdmchat.controller

import alexandre.cavaleiro.pdmchat.Banco.MessageChatDAOFirebase
import alexandre.cavaleiro.pdmchat.Banco.MessageDAO
import alexandre.cavaleiro.pdmchat.model.Constant.MESSAGE_ARRAY
import alexandre.cavaleiro.pdmchat.model.MessageChat
import alexandre.cavaleiro.pdmchat.view.MainActivity
import android.os.Message

class MessageController(private val mainActivity: MainActivity) {
    private val messageDAOImpl: MessageDAO by lazy {
        MessageChatDAOFirebase()
    }

    fun insertMessage(msg: MessageChat){
        Thread {
            messageDAOImpl.createMessage(msg)
        }.start()
    }

    fun getMessages( userName: String){
        Thread {
            val retorno = messageDAOImpl.retrieveMessages()

            val retornoFiltrado =retorno.filter{it.destinatario == userName }

            val msg = Message()
            msg.data.putParcelableArray(
                MESSAGE_ARRAY,
                retornoFiltrado.toTypedArray()
            )

            mainActivity.updateMessageListHandler.sendMessage(msg)
        }.start()
    }
}