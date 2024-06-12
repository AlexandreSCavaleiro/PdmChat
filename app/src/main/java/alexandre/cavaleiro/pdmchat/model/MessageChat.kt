package alexandre.cavaleiro.pdmchat.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class MessageChat(
    var escritor: String,
    var destinatario: String,
    var conteudo: String,
    var dataHora: LocalDateTime
):Parcelable