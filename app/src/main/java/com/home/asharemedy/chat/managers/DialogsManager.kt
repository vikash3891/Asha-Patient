package com.home.asharemedy.chat.managers

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.home.asharemedy.AshaRemedyApp
import com.home.asharemedy.R
import com.quickblox.chat.QBChatService
import com.quickblox.chat.QBSystemMessagesManager
import com.quickblox.chat.model.QBChatDialog
import com.quickblox.chat.model.QBChatMessage
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.core.request.QBPagedRequestBuilder
import com.home.asharemedy.chat.utils.chat.CURRENT_PAGE_BUNDLE_PARAM
import com.home.asharemedy.chat.utils.chat.ChatHelper
import com.home.asharemedy.chat.utils.chat.TOTAL_PAGES_BUNDLE_PARAM
import com.home.asharemedy.chat.utils.chat.USERS_PER_PAGE
import com.home.asharemedy.chat.utils.qb.QbDialogHolder
import com.home.asharemedy.chat.utils.qb.QbUsersHolder
import com.home.asharemedy.chat.utils.qb.callback.QbEntityCallbackImpl
import com.home.asharemedy.chat.utils.qb.getOccupantsIdsStringFromList
import com.home.asharemedy.chat.utils.qb.getOccupantsNamesStringFromList
import com.quickblox.users.QBUsers
import com.quickblox.users.model.QBUser
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smackx.muc.DiscussionHistory
import java.util.*
import java.util.concurrent.CopyOnWriteArraySet
import kotlin.collections.ArrayList

const val PROPERTY_OCCUPANTS_IDS = "current_occupant_ids"
const val PROPERTY_DIALOG_TYPE = "type"
const val PROPERTY_DIALOG_NAME = "room_name"
const val PROPERTY_NOTIFICATION_TYPE = "notification_type"
const val CREATING_DIALOG = "1"
const val OCCUPANTS_ADDED = "2"
const val OCCUPANT_LEFT = "3"
const val PROPERTY_NEW_OCCUPANTS_IDS = "new_occupants_ids"

class DialogsManager {
    private val TAG = DialogsManager::class.java.simpleName

    private var addedUsersLoaded = ArrayList<QBUser>()
    private val managingDialogsCallbackListener = CopyOnWriteArraySet<ManagingDialogsCallbacks>()

    private fun isMessageCreatedDialog(message: QBChatMessage): Boolean {
        return CREATING_DIALOG == message.getProperty(PROPERTY_NOTIFICATION_TYPE)
    }

    private fun isMessageAddedUser(message: QBChatMessage): Boolean {
        return OCCUPANTS_ADDED == message.getProperty(PROPERTY_NOTIFICATION_TYPE)
    }

    private fun isMessageLeftUser(message: QBChatMessage): Boolean {
        return OCCUPANT_LEFT == message.getProperty(PROPERTY_NOTIFICATION_TYPE)
    }

    private fun buildMessageCreatedGroupDialog(dialog: QBChatDialog): QBChatMessage {
        val qbChatMessage = QBChatMessage()
        qbChatMessage.dialogId = dialog.dialogId
        qbChatMessage.setProperty(PROPERTY_OCCUPANTS_IDS, getOccupantsIdsStringFromList(dialog.occupants))
        qbChatMessage.setProperty(PROPERTY_DIALOG_TYPE, dialog.type.code.toString())
        qbChatMessage.setProperty(PROPERTY_DIALOG_NAME, dialog.name.toString())
        qbChatMessage.setProperty(PROPERTY_NOTIFICATION_TYPE, CREATING_DIALOG)
        qbChatMessage.dateSent = System.currentTimeMillis() / 1000
        qbChatMessage.body = AshaRemedyApp.getInstance().getString(R.string.new_chat_created, getCurrentUserName())
        qbChatMessage.setSaveToHistory(true)
        qbChatMessage.isMarkable = true
        return qbChatMessage
    }

    private fun buildMessageAddedUsers(dialog: QBChatDialog, userIds: String, usersNames: String): QBChatMessage {
        val qbChatMessage = QBChatMessage()
        qbChatMessage.dialogId = dialog.dialogId
        qbChatMessage.setProperty(PROPERTY_NOTIFICATION_TYPE, OCCUPANTS_ADDED)
        qbChatMessage.setProperty(PROPERTY_NEW_OCCUPANTS_IDS, userIds)
        qbChatMessage.body = AshaRemedyApp.getInstance().getString(R.string.occupant_added, getCurrentUserName(), usersNames)
        qbChatMessage.setSaveToHistory(true)
        qbChatMessage.isMarkable = true
        return qbChatMessage
    }

    private fun buildMessageLeftUser(dialog: QBChatDialog): QBChatMessage {
        val qbChatMessage = QBChatMessage()
        qbChatMessage.dialogId = dialog.dialogId
        qbChatMessage.setProperty(PROPERTY_NOTIFICATION_TYPE, OCCUPANT_LEFT)
        qbChatMessage.body = AshaRemedyApp.getInstance().getString(R.string.occupant_left, getCurrentUserName())
        qbChatMessage.setSaveToHistory(true)
        qbChatMessage.isMarkable = true
        return qbChatMessage
    }

    private fun buildChatDialogFromNotificationMessage(qbChatMessage: QBChatMessage): QBChatDialog {
        val chatDialog = QBChatDialog()
        chatDialog.dialogId = qbChatMessage.dialogId
        chatDialog.unreadMessageCount = 0
        return chatDialog
    }

    private fun getCurrentUserName(): String {
        val currentUser = QBChatService.getInstance().user
        return if (TextUtils.isEmpty(currentUser.fullName)) currentUser.login else currentUser.fullName
    }

    ////// Sending Notification Messages

    fun sendMessageCreatedDialog(dialog: QBChatDialog) {
        val messageCreatingDialog = buildMessageCreatedGroupDialog(dialog)
        try {
            Log.d(TAG, "Sending Notification Message about Creating Group Dialog")
            dialog.sendMessage(messageCreatingDialog)
        } catch (ignored: SmackException.NotConnectedException) {

        }
    }

    fun sendMessageAddedUsers(dialog: QBChatDialog, newUsersIds: List<Int>) {
        val requestBuilder = QBPagedRequestBuilder(USERS_PER_PAGE, 1)
        loadNewUsersByIDs(newUsersIds, requestBuilder, ArrayList(), object : QBEntityCallback<ArrayList<QBUser>>{
            override fun onSuccess(qbUsers: ArrayList<QBUser>?, b: Bundle?) {
                val usersIds = getOccupantsIdsStringFromList(newUsersIds)
                if (newUsersIds.isNotEmpty() && qbUsers != null) {
                    val usersNames = getOccupantsNamesStringFromList(qbUsers)
                    val messageUsersAdded = buildMessageAddedUsers(dialog, usersIds, usersNames)

                    try {
                        Log.d(TAG, "Sending Notification Message to Opponents about Adding Occupants")
                        dialog.sendMessage(messageUsersAdded)
                    } catch (e: SmackException.NotConnectedException) {
                        Log.d(TAG, "Sending Notification Message Error: " + e.message)
                    }
                }
            }

            override fun onError(ignored: QBResponseException?) {

            }
        })
    }

    private fun loadNewUsersByIDs(userIDs: Collection<Int>, requestBuilder: QBPagedRequestBuilder, alreadyLoadedUsers: ArrayList<QBUser>, callback: QBEntityCallback<ArrayList<QBUser>>) {
        QBUsers.getUsersByIDs(userIDs, requestBuilder).performAsync(object : QBEntityCallback<ArrayList<QBUser>>{
            override fun onSuccess(qbUsers: ArrayList<QBUser>?, bundle: Bundle?) {
                if (qbUsers != null) {
                    QbUsersHolder.putUsers(qbUsers)
                    alreadyLoadedUsers.addAll(qbUsers)
                    bundle?.let {
                        val totalPages = it.get(TOTAL_PAGES_BUNDLE_PARAM) as Int
                        val currentPage = it.get(CURRENT_PAGE_BUNDLE_PARAM) as Int
                        if (totalPages > currentPage) {
                            requestBuilder.page = currentPage + 1
                            loadNewUsersByIDs(userIDs, requestBuilder, alreadyLoadedUsers, callback)
                        } else {
                            callback.onSuccess(alreadyLoadedUsers, bundle)
                        }
                    }
                }
            }

            override fun onError(e: QBResponseException?) {
                callback.onError(e)
            }
        })
    }

    fun sendMessageLeftUser(dialog: QBChatDialog) {
        val messageLeftUser = buildMessageLeftUser(dialog)
        try {
            Log.d(TAG, "Sending Notification Message to Opponents about User Left")
            dialog.sendMessage(messageLeftUser)
        } catch (ignored: SmackException.NotConnectedException) {

        }
    }

    ////// Sending System Messages

    fun sendSystemMessageAboutCreatingDialog(systemMessagesManager: QBSystemMessagesManager, dialog: QBChatDialog) {
        val messageCreatingDialog = buildMessageCreatedGroupDialog(dialog)
        prepareSystemMessage(systemMessagesManager, messageCreatingDialog, dialog.occupants)
    }

    fun sendSystemMessageAddedUser(systemMessagesManager: QBSystemMessagesManager, dialog: QBChatDialog, newUsersIds: List<Int>) {
        val requestBuilder = QBPagedRequestBuilder(USERS_PER_PAGE, 1)
        loadNewUsersForSystemMsgsByIDs(newUsersIds, requestBuilder, ArrayList(), object : QBEntityCallback<ArrayList<QBUser>>{
            override fun onSuccess(qbUsers: ArrayList<QBUser>?, bundle: Bundle?) {
                val usersIds = getOccupantsIdsStringFromList(newUsersIds)
                if (newUsersIds.isNotEmpty() && qbUsers != null) {
                    val usersNames = getOccupantsNamesStringFromList(qbUsers)

                    val messageUsersAdded = buildMessageAddedUsers(dialog, usersIds, usersNames)
                    prepareSystemMessage(systemMessagesManager, messageUsersAdded, dialog.occupants)

                    val messageDialogCreated = buildMessageCreatedGroupDialog(dialog)
                    prepareSystemMessage(systemMessagesManager, messageDialogCreated, newUsersIds)
                }
            }

            override fun onError(ignored: QBResponseException?) {

            }
        })
    }

    private fun loadNewUsersForSystemMsgsByIDs(userIDs: Collection<Int>, requestBuilder: QBPagedRequestBuilder, alreadyLoadedUsers: ArrayList<QBUser>, callback: QBEntityCallback<ArrayList<QBUser>>) {
        QBUsers.getUsersByIDs(userIDs, requestBuilder).performAsync(object : QBEntityCallback<ArrayList<QBUser>>{
            override fun onSuccess(qbUsers: ArrayList<QBUser>?, bundle: Bundle?) {
                if (qbUsers != null) {
                    QbUsersHolder.putUsers(qbUsers)
                    alreadyLoadedUsers.addAll(qbUsers)
                    bundle?.let {
                        val totalPages = it.get(TOTAL_PAGES_BUNDLE_PARAM) as Int
                        val currentPage = it.get(CURRENT_PAGE_BUNDLE_PARAM) as Int
                        if (totalPages > currentPage) {
                            requestBuilder.page = currentPage + 1
                            loadNewUsersForSystemMsgsByIDs(userIDs, requestBuilder, alreadyLoadedUsers, callback)
                        } else {
                            callback.onSuccess(alreadyLoadedUsers, bundle)
                        }
                    }
                }
            }

            override fun onError(e: QBResponseException?) {
                callback.onError(e)
            }
        })
    }

    fun sendSystemMessageLeftUser(systemMessagesManager: QBSystemMessagesManager, dialog: QBChatDialog) {
        val messageLeftUser = buildMessageLeftUser(dialog)
        prepareSystemMessage(systemMessagesManager, messageLeftUser, dialog.occupants)
    }

    private fun prepareSystemMessage(systemMessagesManager: QBSystemMessagesManager, message: QBChatMessage, occupants: List<Int>) {
        message.setSaveToHistory(false)
        message.isMarkable = false

        try {
            for (opponentID in occupants) {
                if (opponentID != QBChatService.getInstance().user.id) {
                    message.recipientId = opponentID
                    Log.d(TAG, "Sending System Message to $opponentID")
                    systemMessagesManager.sendSystemMessage(message)
                }
            }
        } catch (e: SmackException.NotConnectedException) {
            Log.d(TAG, "Sending System Message Error: " + e.message)
            e.printStackTrace()
        }
    }

    ////// Message Receivers

    fun onGlobalMessageReceived(dialogId: String, chatMessage: QBChatMessage) {
        Log.d(TAG, "Global Message Received: " + chatMessage.id)
        if (isMessageCreatedDialog(chatMessage) && !QbDialogHolder.hasDialogWithId(dialogId)) {
            loadNewDialogByNotificationMessage(chatMessage)
        }

        if (isMessageAddedUser(chatMessage) || isMessageLeftUser(chatMessage)) {
            if (QbDialogHolder.hasDialogWithId(dialogId)) {
                notifyListenersDialogUpdated(dialogId)
            } else {
                loadNewDialogByNotificationMessage(chatMessage)
            }
        }

        if (chatMessage.isMarkable) {
            if (QbDialogHolder.hasDialogWithId(dialogId)) {
                QbDialogHolder.updateDialog(dialogId, chatMessage)
                notifyListenersDialogUpdated(dialogId)
            } else {
                ChatHelper.getDialogById(dialogId, object : QBEntityCallback<QBChatDialog> {
                    override fun onSuccess(qbChatDialog: QBChatDialog, bundle: Bundle?) {
                        Log.d(TAG, "Loading Dialog Successful")
                        loadUsersFromDialog(qbChatDialog)
                        QbDialogHolder.addDialog(qbChatDialog)
                        notifyListenersNewDialogLoaded(qbChatDialog)
                    }

                    override fun onError(e: QBResponseException) {
                        Log.d(TAG, "Loading Dialog Error: " + e.message)
                    }
                })
            }
        }
    }

    fun onSystemMessageReceived(systemMessage: QBChatMessage) {
        Log.d(TAG, "System Message Received: " + systemMessage.body + " Notification Type: " + systemMessage.getProperty(PROPERTY_NOTIFICATION_TYPE));
        onGlobalMessageReceived(systemMessage.dialogId, systemMessage)
    }

    ////// END

    private fun loadNewDialogByNotificationMessage(chatMessage: QBChatMessage) {
        val chatDialog = buildChatDialogFromNotificationMessage(chatMessage)
        ChatHelper.getDialogById(chatDialog.dialogId, object : QBEntityCallback<QBChatDialog> {
            override fun onSuccess(qbChatDialog: QBChatDialog, bundle: Bundle) {
                val history = DiscussionHistory()
                history.maxStanzas = 0

                qbChatDialog.initForChat(QBChatService.getInstance())
                qbChatDialog.join(history, object : QbEntityCallbackImpl<QBChatDialog?>() {
                    override fun onSuccess(result: QBChatDialog?, bundle: Bundle?) {
                        QbDialogHolder.addDialog(qbChatDialog)
                        notifyListenersDialogCreated(qbChatDialog)
                    }
                })
            }

            override fun onError(e: QBResponseException) {
                Log.d(TAG, "Loading Dialog Error: " + e.message)
            }
        })
    }

    private fun loadUsersFromDialog(chatDialog: QBChatDialog) {
        ChatHelper.getUsersFromDialog(chatDialog, QbEntityCallbackImpl())
    }

    private fun notifyListenersDialogCreated(chatDialog: QBChatDialog) {
        for (listener in getManagingDialogsCallbackListeners()) {
            listener.onDialogCreated(chatDialog)
        }
    }

    private fun notifyListenersDialogUpdated(dialogId: String) {
        for (listener in getManagingDialogsCallbackListeners()) {
            listener.onDialogUpdated(dialogId)
        }
    }

    private fun notifyListenersNewDialogLoaded(chatDialog: QBChatDialog) {
        for (listener in getManagingDialogsCallbackListeners()) {
            listener.onNewDialogLoaded(chatDialog)
        }
    }

    fun addManagingDialogsCallbackListener(listener: ManagingDialogsCallbacks?) {
        if (listener != null) {
            managingDialogsCallbackListener.add(listener)
        }
    }

    fun removeManagingDialogsCallbackListener(listener: ManagingDialogsCallbacks) {
        managingDialogsCallbackListener.remove(listener)
    }

    fun getManagingDialogsCallbackListeners(): Collection<ManagingDialogsCallbacks> {
        return Collections.unmodifiableCollection<ManagingDialogsCallbacks>(managingDialogsCallbackListener)
    }

    interface ManagingDialogsCallbacks {

        fun onDialogCreated(chatDialog: QBChatDialog)

        fun onDialogUpdated(chatDialog: String)

        fun onNewDialogLoaded(chatDialog: QBChatDialog)
    }
}