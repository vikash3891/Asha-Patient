package com.home.asharemedy.chat.managers

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.home.asharemedy.chat.utils.chat.ChatHelper

class BackgroundListener : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    internal fun onBackground() {
        ChatHelper.destroy()
        Log.d("BackgroundListener", "Background Successful")
    }
}