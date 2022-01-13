/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.internal.ryanhtech.webdb

import android.annotation.SuppressLint
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseUtil {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var firebaseFirestore: FirebaseFirestore? = null

        fun getFirestore(): FirebaseFirestore {
            if (firebaseFirestore == null)
                firebaseFirestore = FirebaseFirestore.getInstance()

            return firebaseFirestore as FirebaseFirestore
        }
    }
}