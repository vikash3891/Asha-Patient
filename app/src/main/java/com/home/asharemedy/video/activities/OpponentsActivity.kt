package com.home.asharemedy.video.activities

import android.app.ActivityManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.home.asharemedy.R
import com.quickblox.chat.QBChatService
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.core.request.GenericQueryRule
import com.quickblox.core.request.QBPagedRequestBuilder
import com.quickblox.messages.services.SubscribeService
import com.home.asharemedy.video.adapters.UsersAdapter
import com.home.asharemedy.video.db.QbUsersDbManager
import com.home.asharemedy.video.services.CallService
import com.home.asharemedy.video.services.LoginService
import com.home.asharemedy.video.util.loadUsersByPagedRequestBuilder
import com.home.asharemedy.video.util.signOut
import com.home.asharemedy.video.utils.*
import com.quickblox.users.QBUsers
import com.quickblox.users.model.QBUser
import com.quickblox.videochat.webrtc.QBRTCClient
import com.quickblox.videochat.webrtc.QBRTCTypes


private const val PER_PAGE_SIZE_100 = 100
private const val ORDER_RULE = "order"
private const val ORDER_DESC_UPDATED = "desc date updated_at"
private const val TOTAL_PAGES_BUNDLE_PARAM = "total_pages"

class OpponentsActivity : BaseActivity() {
    private val TAG = OpponentsActivity::class.java.simpleName

    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var currentUser: QBUser

    private var usersAdapter: UsersAdapter? = null

    private var currentPage = 0
    private var isLoading: Boolean = false
    private var hasNextPage: Boolean = true

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, OpponentsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_select_users)
            currentUser = SharedPrefsHelper.getQbUser()
            initDefaultActionBar()
            initUI()
            startLoginService()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            val isIncomingCall = SharedPrefsHelper.get(EXTRA_IS_INCOMING_CALL, false)
            if (isCallServiceRunning(CallService::class.java)) {
                Log.d(TAG, "CallService is running now")
                CallActivity.start(this, isIncomingCall)
            }
            clearAppNotifications()
            loadUsers()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isCallServiceRunning(serviceClass: Class<*>): Boolean {
        try {
            val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val services = manager.getRunningServices(Integer.MAX_VALUE)
            for (service in services) {
                if (CallService::class.java.name == service.service.className) {
                    return true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun clearAppNotifications() {
        try {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancelAll()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startPermissionsActivity(checkOnlyAudio: Boolean) {
        PermissionsActivity.startForResult(this, checkOnlyAudio, PERMISSIONS)
    }

    private fun loadUsers() {
        try {
            isLoading = true
            showProgressDialog(R.string.dlg_loading_opponents)
            currentPage += 1
            val rules = ArrayList<GenericQueryRule>()
            rules.add(GenericQueryRule(ORDER_RULE, ORDER_DESC_UPDATED))
            val requestBuilder = QBPagedRequestBuilder()
            requestBuilder.rules = rules
            requestBuilder.perPage = PER_PAGE_SIZE_100
            requestBuilder.page = currentPage

            QBUsers.getUsers(requestBuilder)
                .performAsync(object : QBEntityCallback<ArrayList<QBUser>> {
                    override fun onSuccess(qbUsers: ArrayList<QBUser>?, bundle: Bundle?) {
                        qbUsers?.let {
                            try {
                                Log.d(TAG, "Successfully loaded users")
                                QbUsersDbManager.saveAllUsers(qbUsers, true)

                                val totalPagesFromParams =
                                    bundle?.get(TOTAL_PAGES_BUNDLE_PARAM) as Int
                                if (currentPage >= totalPagesFromParams) {
                                    hasNextPage = false
                                }

                                if (currentPage == 1) {
                                    initUsersList()
                                } else {
                                    usersAdapter?.addUsers(qbUsers)
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        hideProgressDialog()
                        isLoading = false
                    }

                    override fun onError(e: QBResponseException?) {
                        e?.let {
                            Log.d(TAG, "Error load users" + e.message)
                            hideProgressDialog()
                            isLoading = false
                            currentPage -= 1
                            showErrorSnackbar(
                                R.string.loading_users_error,
                                e,
                                View.OnClickListener { loadUsers() })
                        }
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initUI() {
        try {
            usersRecyclerView = findViewById(R.id.list_select_users)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initUsersList() {
        try {
            val currentOpponentsList = QbUsersDbManager.allUsers
            currentOpponentsList.remove(SharedPrefsHelper.getQbUser())
            if (usersAdapter == null) {
                usersAdapter = UsersAdapter(this, currentOpponentsList)
                usersAdapter!!.setSelectedItemsCountsChangedListener(object :
                    UsersAdapter.SelectedItemsCountsChangedListener {
                    override fun onCountSelectedItemsChanged(count: Int) {
                        updateActionBar(count)
                    }
                })

                usersRecyclerView.layoutManager = LinearLayoutManager(this)
                usersRecyclerView.adapter = usersAdapter
                usersRecyclerView.addOnScrollListener(ScrollListener(usersRecyclerView.layoutManager as LinearLayoutManager))
            } else {
                usersAdapter!!.updateUsersList(currentOpponentsList)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (usersAdapter != null && usersAdapter!!.selectedUsers.isNotEmpty()) {
            menuInflater.inflate(R.menu.activity_selected_opponents, menu)
        } else {
            menuInflater.inflate(R.menu.activity_opponents, menu)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.update_opponents_list -> {
                currentPage = 0
                loadUsers()
                return true
            }
            R.id.settings -> {
                SettingsActivity.start(this)
                return true
            }
            R.id.log_out -> {
                logout()
                return true
            }
            R.id.start_video_call -> {
                if (checkPermissions(PERMISSIONS)) {
                    startPermissionsActivity(false)
                }
                if (checkIsLoggedInChat()) {

                    startCall(true)
                }

                return true
            }
            R.id.start_audio_call -> {
                if (checkPermission(PERMISSIONS[1])) {
                    startPermissionsActivity(true)
                }
                if (checkIsLoggedInChat()) {
                    startCall(false)
                }

                return true
            }
            R.id.appinfo -> {
                AppInfoActivity.start(this)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun checkIsLoggedInChat(): Boolean {
        try {
            if (!QBChatService.getInstance().isLoggedIn) {
                startLoginService()
                shortToast(R.string.login_chat_retry)
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }

    private fun startLoginService() {
        try {
            if (SharedPrefsHelper.hasQbUser()) {
                LoginService.start(this, SharedPrefsHelper.getQbUser())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startCall(isVideoCall: Boolean) {
        try {
            val usersCount = usersAdapter!!.selectedUsers.size
            if (usersCount > MAX_OPPONENTS_COUNT) {
                longToast(
                    String.format(
                        getString(R.string.error_max_opponents_count),
                        MAX_OPPONENTS_COUNT
                    )
                )
                return
            }

            val opponentsList = getIdsSelectedOpponents(usersAdapter!!.selectedUsers)
            val conferenceType = if (isVideoCall) {
                QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
            } else {
                QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO
            }
            val qbrtcClient = QBRTCClient.getInstance(applicationContext)
            val newQbRtcSession =
                qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType)
            WebRtcSessionManager.setCurrentSession(newQbRtcSession)

            // Make Users FullName Strings and ID's list for iOS VOIP push
            val newSessionID = newQbRtcSession.sessionID
            val opponentsIDsList = java.util.ArrayList<String>()
            val opponentsNamesList = java.util.ArrayList<String>()
            val usersInCall: ArrayList<QBUser> = usersAdapter!!.selectedUsers as ArrayList<QBUser>

            // the Caller in exactly first position is needed regarding to iOS 13 functionality
            usersInCall.add(0, currentUser)

            for (user in usersInCall) {
                val userId = user.id!!.toString()
                var userName = ""
                if (TextUtils.isEmpty(user.fullName)) {
                    userName = user.login
                } else {
                    userName = user.fullName
                }

                opponentsIDsList.add(userId)
                opponentsNamesList.add(userName)
            }

            val opponentsIDsString = TextUtils.join(",", opponentsIDsList)
            val opponentNamesString = TextUtils.join(",", opponentsNamesList)

            Log.d(
                TAG,
                "New Session with ID: $newSessionID\n Users in Call: \n$opponentsIDsString\n$opponentNamesString"
            )
            /*sendPushMessage(
                opponentsList,
                currentUser.fullName,
                newSessionID,
                opponentsIDsString,
                opponentNamesString,
                isVideoCall
            )*/

            CallActivity.start(this, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getIdsSelectedOpponents(selectedUsers: Collection<QBUser>): ArrayList<Int> {
        val opponentsIds = ArrayList<Int>()
        if (!selectedUsers.isEmpty()) {
            for (qbUser in selectedUsers) {
                opponentsIds.add(qbUser.id)
            }
        }
        return opponentsIds
    }

    private fun updateActionBar(countSelectedUsers: Int) {
        if (countSelectedUsers < 1) {
            initDefaultActionBar()
        } else {
            val title = if (countSelectedUsers > 1) {
                R.string.tile_many_users_selected
            } else {
                R.string.title_one_user_selected
            }
            supportActionBar?.title = getString(title, countSelectedUsers)
            supportActionBar?.subtitle = null
        }
        invalidateOptionsMenu()
    }

    private fun initDefaultActionBar() {
        val currentUserFullName = SharedPrefsHelper.getQbUser().fullName
        supportActionBar?.title = ""
        supportActionBar?.subtitle =
            getString(R.string.subtitle_text_logged_in_as, currentUserFullName)
    }

    private fun logout() {
        SubscribeService.unSubscribeFromPushes(this)
        LoginService.logout(this)
        removeAllUserData()
        startLoginActivity()
    }

    private fun removeAllUserData() {
        SharedPrefsHelper.clearAllData()
        QbUsersDbManager.clearDB()
        signOut()
    }

    private fun startLoginActivity() {
        LoginActivity.start(this)
        finish()
    }

    private inner class ScrollListener(val layoutManager: LinearLayoutManager) :
        RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (!isLoading && hasNextPage && dy > 0) {
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                val needToLoadMore = visibleItemCount * 2 + firstVisibleItem >= totalItemCount
                if (needToLoadMore) {
                    loadUsers()
                }
            }
        }
    }
}