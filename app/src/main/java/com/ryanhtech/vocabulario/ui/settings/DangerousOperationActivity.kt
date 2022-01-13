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

package com.ryanhtech.vocabulario.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.utils.DataManager

@Deprecated("Activity disabled in Manifest")
class DangerousOperationActivity : AppCompatActivity() {
    // Widgets
    private lateinit var doOperationButton:     Button
    private lateinit var cancelOperationButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dangerous_operation)

        // Initialize the widgets
        doOperationButton = findViewById(R.id.doDangerousOperation)
        cancelOperationButton = findViewById(R.id.cancelDangerousOperation)

        // Do a sort of disabled/enabled style, to make sure the user reads all
        // the text before continuing
        doOperationButton.isEnabled = false

        // Wait 2 seconds to enable the button
        Handler(Looper.getMainLooper()).postDelayed ({
            doOperationButton.isEnabled = true
        }, 2000
        )

        // Set the onClickListeners
        cancelOperationButton.setOnClickListener {
            finish()
        }

        doOperationButton.setOnClickListener {
            DataManager().clearAppData(applicationContext)
        }
    }
}