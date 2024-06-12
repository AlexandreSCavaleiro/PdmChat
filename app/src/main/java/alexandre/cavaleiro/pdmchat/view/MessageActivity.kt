package alexandre.cavaleiro.pdmchat.view

import alexandre.cavaleiro.pdmchat.R
import alexandre.cavaleiro.pdmchat.databinding.ActivityMessageBinding
import alexandre.cavaleiro.pdmchat.model.Contant.EXTRA_MESSAGE
import alexandre.cavaleiro.pdmchat.model.MessageChat
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime

class MessageActivity : AppCompatActivity() {
    private val amb : ActivityMessageBinding by lazy {
        ActivityMessageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.toolbarIn.toolbar)
        supportActionBar?.title = getString(R.string.envie_uma_mensagem)

        with(amb){
            sendBt.setOnClickListener{
                val tmstp = LocalDateTime.now()
                val msg = MessageChat(
                    escritor = "",
                    destinatario = destinatarioEt.text.toString(),
                    conteudo = messageEt.text.toString(),
                    dataHora = tmstp
                )

                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_MESSAGE, msg)
                setResult(RESULT_OK, resultIntent)

                finish()
            }
        }
    }
}