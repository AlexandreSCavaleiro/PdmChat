package alexandre.cavaleiro.pdmchat.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.Month

@Parcelize
data class MessageChat(
    var id: Int = -1,
    var escritor: String = "",
    var destinatario: String = "",
    var conteudo: String = "",
    var dataHora: String = ""
):Parcelable

