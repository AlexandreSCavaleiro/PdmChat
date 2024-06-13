package alexandre.cavaleiro.pdmchat.Banco

import alexandre.cavaleiro.pdmchat.model.MessageChat

interface MessageDAO {
    fun createMessage(message: MessageChat)

    fun retrieveMessages(): MutableList<MessageChat>
}