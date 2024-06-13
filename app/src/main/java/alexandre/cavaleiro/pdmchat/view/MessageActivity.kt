package alexandre.cavaleiro.pdmchat.view

import alexandre.cavaleiro.pdmchat.R
import alexandre.cavaleiro.pdmchat.databinding.ActivityMessageBinding
import alexandre.cavaleiro.pdmchat.model.Constant.EXTRA_MESSAGE
import alexandre.cavaleiro.pdmchat.model.MessageChat
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

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
                val tmstpFormater = DateTimeFormatter.ofPattern("dd/MM/yy '|' HH:mm")
                val msg = MessageChat(
                    id = generateId(),
                    escritor = "",
                    destinatario = destinatarioEt.text.toString(),
                    conteudo = messageEt.text.toString(),// provavelmente tem que filtrar os 150c
                    dataHora = tmstp.format(tmstpFormater)
                )

                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_MESSAGE, msg)
                setResult(RESULT_OK, resultIntent)

                finish()
            }
        }
    }

    private fun generateId(): Int{
        return Random(System.currentTimeMillis()).nextInt()
    }
}