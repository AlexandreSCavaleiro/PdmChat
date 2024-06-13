package alexandre.cavaleiro.pdmchat.view

import alexandre.cavaleiro.pdmchat.R
import alexandre.cavaleiro.pdmchat.adapter.MessageAdapter
import alexandre.cavaleiro.pdmchat.controller.MessageController
import alexandre.cavaleiro.pdmchat.databinding.ActivityMainBinding
import alexandre.cavaleiro.pdmchat.model.Constant.EXTRA_MESSAGE
import alexandre.cavaleiro.pdmchat.model.Constant.MESSAGE_ARRAY
import alexandre.cavaleiro.pdmchat.model.MessageChat
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    //ViewBinding
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //Nome do usuario
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

    //Controller
    private val messageController: MessageController by lazy {
        MessageController(this)
    }
    companion object{
        const val GET_INTERVAL = 2000L
        const val GET_MSG = 1
    }

    val updateMessageListHandler = object: Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            if (msg.what == GET_MSG) {
                messageController.getMessages(nomeUsuario)

                sendMessageDelayed(
                    obtainMessage().apply { what = GET_MSG },
                    GET_INTERVAL
                )
            }else{
                msg.data.getParcelableArray(MESSAGE_ARRAY)?.also { msgArray ->
                    messageList.clear()
                    msgArray.forEach {
                        messageList.add(it as MessageChat)
                    }
                    messageAdapter.notifyDataSetChanged()
                }
            }
        }
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
                    messageController.insertMessage(msg)
                }
            }
        }

        registerForContextMenu(amb.messageLv)

        updateMessageListHandler.apply {
            sendMessage(
                obtainMessage().apply { what = GET_MSG }
            )
        }
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