/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.ryanhtech.vocabulario.internal.framework

import android.content.Context
import android.media.AudioManager
import android.util.Log
import com.ryanhtech.vocabulario.internal.framework.utilsinterfaces.VbUtilsAndroidSystemServices
import com.ryanhtech.vocabulario.internal.framework.utilsinterfaces.VbUtilsMedia
import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity

/**
 * This class contains the utilities that can be used by any [VocabularioActivity] in the app. To
 * check out the definitions, go directly to the interface you are interested in. Here is the list
 * of interfaces that are implemented by [VbUtils]:
 * - [VbUtilsMedia]
 * - [VbUtilsAndroidSystemServices]
 */
class VbUtils(currentContext: Context) : VbUtilsMedia, VbUtilsAndroidSystemServices {
    private val mContext = currentContext

    // The log tag to be used in Logcat
    private val mLogTag = "VbUtils"
    
    // Overrode from VbUtilsMedia
    override fun vbIsMusicPlaying(): Boolean {
        // Get the audio manager and determine if music is active
        val lAudioManager = vbGetAudioManager()
        return lAudioManager.isMusicActive
    }

    // Overrode from VbUtilsAndroidSystemServices
    override fun vbGetAudioManager(): AudioManager {
        // Get the audio manager
        val lAuMgr: AudioManager
        try {
            lAuMgr = mContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        }
        catch (e: Exception) {
            // Log the exception (to debug properly) and rethrow it
            Log.e(mLogTag, "Error while getting audio manager: ${e.localizedMessage}")
            throw e
        }

        // Return the audio manager
        Log.v(mLogTag, "Got AudioManager instance")
        return lAuMgr
    }
}