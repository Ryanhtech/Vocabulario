/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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