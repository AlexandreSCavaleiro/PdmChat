package alexandre.cavaleiro.pdmchat.Banco

import alexandre.cavaleiro.pdmchat.model.MessageChat
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class MessageChatDAOFirebase : MessageDAO {

    companion object{
        private const val MESSAGE_LIST_ROOT_NODE = "messageList"
    }

    private val messageChatFirebaseReference = Firebase.database.getReference(MESSAGE_LIST_ROOT_NODE)

    private val messageChatList: MutableList<MessageChat> = mutableListOf()

    init {
        messageChatFirebaseReference.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val msg: MessageChat? = snapshot.getValue<MessageChat>()

                msg?.also { _msg->
                    if (!messageChatList.any{ it.id == _msg.id }){
                        messageChatList.add(_msg)
                    }
                }
                orderMessageList()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                //nao aplica
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                //nao aplica
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //nao aplica
            }

            override fun onCancelled(error: DatabaseError) {
                //nao aplica
            }

        })


        messageChatFirebaseReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val message =snapshot.getValue()
                if (message is HashMap<*, *>) {

                    val messageMap = snapshot.getValue<Map<String, MessageChat>>()

                    messageChatList.clear()

                    messageMap?.values?.also {
                        messageChatList.addAll(it)
                    }

                    orderMessageList()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                //nao aplica
            }
        })

    }

    override fun createMessage(message: MessageChat) {
        messageChatFirebaseReference.child(message.id.toString()).setValue(message).toString()
    }

    override fun retrieveMessages(): MutableList<MessageChat> {
        return messageChatList
    }

    private fun orderMessageList() {
        messageChatList.sortBy { it.id }
    }
}
