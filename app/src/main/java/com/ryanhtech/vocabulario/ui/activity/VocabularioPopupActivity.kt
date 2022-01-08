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

package com.ryanhtech.vocabulario.ui.activity

import android.os.Bundle
import com.ryanhtech.vocabulario.R

open class VocabularioPopupActivity : VocabularioActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_bottom_transition,
            R.anim.fade_out_popup_back_transition)
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.fade_in_popup_back_transition,
            R.anim.slide_out_bottom_transition)
    }
}