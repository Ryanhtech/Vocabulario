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

package com.ryanhtech.vocabulario.ui.viewmanager

/**
 * This object will help you pass Views in Intents, or any
 * other class instance.
 */
object ViewContainer {
    // The map of instances.
    private val mInstances = mutableMapOf<Int, Any>()

    // The current ID.
    private var mCurrId = 0

    /**
     * Use this to save a new instance into the container. It returns the ID that
     * you should use to get the value back.
     */
    fun saveInstance(inst: Any): Int {
        val lCurrId = mCurrId

        // Save the instance and increment the current ID
        mInstances[lCurrId] = inst
        mCurrId++

        // Return the ID
        return lCurrId
    }

    fun getInstance(id: Int, deleteAfter: Boolean = false): Any {
        // Do the exact same operation as the saveInstance but reversed
        val lInst = mInstances[id] ?: throw IllegalArgumentException("Can't find the provided ID")

        // If you should delete the value do it there
        if (deleteAfter) mInstances.remove(id)

        return lInst
    }
}