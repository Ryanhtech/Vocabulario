/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.internal.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast

@Deprecated("Disabled in Manifest, will be removed later")
class VocRevReminderService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        /**
         * This service will run every minute if the job has been scheduled.
         */

        // Start the service in a new thread to avoid ANR errors


        Thread {
            // Prepare the thread
            Looper.prepare()

            // DEBUG
            Log.d("VocRevReminderService", "Toasting!")
            Toast.makeText(this, "Hello World!", Toast.LENGTH_LONG).show()
        }.start()
    }
}