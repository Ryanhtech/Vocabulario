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