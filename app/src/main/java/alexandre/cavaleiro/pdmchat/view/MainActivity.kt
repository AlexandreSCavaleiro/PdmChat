package alexandre.cavaleiro.pdmchat.view

import alexandre.cavaleiro.pdmchat.R
import alexandre.cavaleiro.pdmchat.adapter.MessageAdapter
import alexandre.cavaleiro.pdmchat.databinding.ActivityMainBinding
import alexandre.cavaleiro.pdmchat.model.Contant.EXTRA_MESSAGE
import alexandre.cavaleiro.pdmchat.model.MessageChat
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    //ViewBinding
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //nome do usuario
    private var nomeUsuario: String = ""

    //ARL
    private lateinit var carl: ActivityResultLauncher<Intent>

    //DataSource
    private val messageList: MutableList<MessageChat> = mutableListOf()

    //Adapter
    private val messageAdapter: MessageAdapter by lazy {
        MessageAdapter(this,
                messageList
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.toolbarIn.toolbar)
        supportActionBar?.title = getString(R.string.mensagens)


        amb.messageLv.adapter = messageAdapter

        amb.addMessageFab.setOnClickListener{
            msgLaunch()
        }

        carl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val messageChat = result.data?.getParcelableExtra<MessageChat>(EXTRA_MESSAGE)
                messageChat?.let { msg ->
                    msg.escritor = nomeUsuario
                    messageList.add(msg)
                    messageAdapter.notifyDataSetChanged()
                }
            }
        }

        registerForContextMenu(amb.messageLv)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {

            R.id.editNameMi -> {

                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("Edite o Seu nome (@)")

                val input = EditText(this)
                input.setText(nomeUsuario)

                input.inputType =
                    InputType.TYPE_CLASS_TEXT
                builder.setView(input)

                builder.setPositiveButton("OK",
                    DialogInterface.OnClickListener { dialog, which ->
                        nomeUsuario = input.text.toString()
                        supportActionBar?.subtitle = "ola $nomeUsuario"
                    })
                builder.setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

                builder.show()

                true
            }
            else -> false
        }
    }

    private fun msgLaunch(){
        carl.launch(Intent(this, MessageActivity::class.java))
    }
}