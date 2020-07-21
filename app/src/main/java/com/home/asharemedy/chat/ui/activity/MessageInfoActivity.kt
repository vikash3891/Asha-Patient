package com.home.asharemedy.chat.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import com.quickblox.chat.QBChatService
import com.quickblox.chat.listeners.QBMessageStatusListener
import com.quickblox.chat.model.QBChatMessage
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.home.asharemedy.R
import com.home.asharemedy.chat.ui.adapter.UsersAdapter
import com.home.asharemedy.chat.utils.chat.ChatHelper
import com.home.asharemedy.chat.utils.qb.QbUsersHolder
import com.home.asharemedy.chat.utils.shortToast
import com.quickblox.users.QBUsers
import com.quickblox.users.model.QBUser
import java.util.*

private const val EXTRA_MESSAGE = "extra_message"
private const val EXTRA_MSG_INFO_TYPE = "extra_message_info_type"
const val MESSAGE_INFO_DELIVERED_TO = "delivered_to"
const val MESSAGE_INFO_READ_BY = "read_by"

class MessageInfoActivity : BaseActivity(), QBMessageStatusListener {
    private val TAG = MessageInfoActivity::class.java.simpleName

    private lateinit var chatMessage: QBChatMessage
    private lateinit var messageInfoType: String
    private lateinit var usersListView: ListView
    private val deliveredUsers = ArrayList<QBUser>()
    private val readUsers = ArrayList<QBUser>()

    companion object {
        fun start(context: Context, chatMessage: QBChatMessage, messageInfoType: String) {
            val intent = Intent(context, MessageInfoActivity::class.java)
            intent.putExtra(EXTRA_MESSAGE, chatMessage)
            intent.putExtra(EXTRA_MSG_INFO_TYPE, messageInfoType)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_info)

        chatMessage = intent.getSerializableExtra(EXTRA_MESSAGE) as QBChatMessage
        messageInfoType = intent.getStringExtra(EXTRA_MSG_INFO_TYPE)
        usersListView = findViewById(R.id.list_chat_info_users)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (messageInfoType == MESSAGE_INFO_DELIVERED_TO) {
            fillByDeliveredUsers()
        } else if (messageInfoType == MESSAGE_INFO_READ_BY) {
            fillByReadUsers()
        }
    }

    override fun onResume() {
        super.onResume()
        QBChatService.getInstance().messageStatusesManager.addMessageStatusListener(this)
    }

    override fun onPause() {
        super.onPause()
        QBChatService.getInstance().messageStatusesManager.removeMessageStatusListener(this)
    }

    private fun fillByDeliveredUsers() {
        supportActionBar?.title = getString(R.string.message_info_delivered_to)
        loadDeliveredUsers()
    }

    private fun fillByReadUsers() {
        supportActionBar?.title = getString(R.string.message_info_read_by)
        loadReadUsers()
    }

    private fun loadDeliveredUsers() {
        val deliveredIDs = chatMessage.deliveredIds

        ChatHelper.getUsersFromMessage(chatMessage, object : QBEntityCallback<ArrayList<QBUser>> {
            override fun onSuccess(qbUsers: ArrayList<QBUser>?, b: Bundle?) {
                qbUsers?.let {
                    for (user in it) {
                        if (deliveredIDs.contains(user.id)) {
                            deliveredUsers.add(user)
                        }
                    }
                }
                supportActionBar?.subtitle = makeSubtitle(deliveredUsers.size)
                fillAdapter(deliveredUsers)
            }

            override fun onError(e: QBResponseException?) {
                showErrorSnackbar(R.string.select_users_get_users_error, e, null)
            }
        })
    }

    private fun loadReadUsers() {
        val readIDs = chatMessage.readIds

        ChatHelper.getUsersFromMessage(chatMessage, object : QBEntityCallback<ArrayList<QBUser>> {
            override fun onSuccess(qbUsers: ArrayList<QBUser>?, b: Bundle?) {
                qbUsers?.let {
                    for (user in it) {
                        if (readIDs.contains(user.id)) {
                            readUsers.add(user)
                        }
                    }
                }
                supportActionBar?.subtitle = makeSubtitle(readUsers.size)
                fillAdapter(readUsers)
            }

            override fun onError(e: QBResponseException?) {
                showErrorSnackbar(R.string.select_users_get_users_error, e, null)
            }
        })
    }

    private fun makeSubtitle(usersSize: Int): String {
        var result = ""
        if (usersSize == 0) {
            result = getString(R.string.message_info_noone)
        } else if (usersSize == 1) {
            result = getString(R.string.message_info_single_user)
        } else {
            result = usersSize.toString() + " " + getString(R.string.message_info_multiple_users)
        }
        return result
    }

    private fun fillAdapter(qbUsers: ArrayList<QBUser>) {
        usersListView.adapter = UsersAdapter(this, qbUsers as MutableList<QBUser>)
    }

    override fun processMessageDelivered(messageID: String?, dialogID: String?, userID: Int?) {
        if (dialogID == chatMessage.dialogId && messageID == chatMessage.id && userID != null) {
            val user = QbUsersHolder.getUserById(userID)
            if (user != null) {
                deliveredUsers.add(user)
                fillAdapter(deliveredUsers)
            } else {
                QBUsers.getUser(userID).performAsync(object : QBEntityCallback<QBUser> {
                    override fun onSuccess(qbUser: QBUser?, b: Bundle?) {
                        qbUser?.let {
                            deliveredUsers.add(qbUser)
                        }
                        fillAdapter(deliveredUsers)
                    }

                    override fun onError(e: QBResponseException?) {
                        shortToast(e?.message)
                        Log.d(TAG, e?.message)
                    }
                })
            }
        }
    }

    override fun processMessageRead(messageID: String?, dialogID: String?, userID: Int?) {
        if (dialogID == chatMessage.dialogId && messageID == chatMessage.id && userID != null) {
            val user = QbUsersHolder.getUserById(userID)
            if (user != null) {
                readUsers.add(user)
                fillAdapter(readUsers)
            } else {
                QBUsers.getUser(userID).performAsync(object : QBEntityCallback<QBUser> {
                    override fun onSuccess(qbUser: QBUser?, b: Bundle?) {
                        qbUser?.let {
                            readUsers.add(qbUser)
                        }
                        fillAdapter(readUsers)
                    }

                    override fun onError(e: QBResponseException?) {
                        shortToast(e?.message)
                        Log.d(TAG, e?.message)
                    }
                })
            }
        }
    }
}