/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.admin.deviceadmin

import android.app.Service
import android.content.Intent
import android.os.IBinder

class VocabularioAdminHang : Service() {
    override fun onBind(intent: Intent): IBinder? {
        TODO("Return the communication channel to the service.")
        return null
    }

    override fun onCreate() {
        super.onCreate()
        // This service is running until the device restarts.

    }
}