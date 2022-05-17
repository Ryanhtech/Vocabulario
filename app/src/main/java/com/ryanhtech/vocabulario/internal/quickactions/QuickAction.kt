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

package com.ryanhtech.vocabulario.internal.quickactions

/**
 * This class represents a quick action (in the Launcher and in the home screen).
 */
open class QuickAction {
    /**
     * This is the quick action ID. It should be the same here and in the `shortcuts.xml` file.
     */
    open val quickActionId: String = "myQuickAction"

    /**
     * When your event occurs, call this.
     */

}