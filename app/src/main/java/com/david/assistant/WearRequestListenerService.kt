package com.david.assistant

import android.content.Intent
import com.google.android.gms.wearable.WearableListenerService

/** Receives a deliberate press-to-talk request from the paired Wear OS DAVID app. */
class WearRequestListenerService : WearableListenerService() {
    override fun onMessageReceived(event: com.google.android.gms.wearable.MessageEvent) {
        if (event.path == "/david/open") {
            startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }
}
