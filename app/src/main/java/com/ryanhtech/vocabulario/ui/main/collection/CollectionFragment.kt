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

package com.ryanhtech.vocabulario.ui.main.collection

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.tools.collection.db.CollectionData
import com.ryanhtech.vocabulario.tools.collection.db.CollectionDatabase
import com.ryanhtech.vocabulario.tools.collection.ui.CreateWordGroupActivity
import kotlinx.android.synthetic.main.fragment_collection.view.*

class CollectionFragment : Fragment(R.layout.fragment_collection) {
    private lateinit var viewLayout: View

    override fun onResume() {
        super.onResume()

        Thread {
            val wordList = CollectionDatabase.getDatabase(requireActivity()).wordGroupDao().getWords().reversed()

            setupFragmentViewAdaptive(wordList)

            // Set up the RecyclerView
            val recyclerViewAdapter = WordGroupAdapter(wordList)

            requireActivity().runOnUiThread {
                viewLayout.collectionFragmentWordGroups.adapter = recyclerViewAdapter
            }
        }.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        viewLayout = inflater.inflate(
            R.layout.fragment_collection,
            container,
            false
        )

        viewLayout.addWordGroupCollectionFragmentButton.setOnClickListener {
            startActivity(Intent(requireActivity(), CreateWordGroupActivity::class.java))
        }

        return viewLayout
    }

    private fun setupFragmentViewAdaptive(list: List<CollectionData>) {
        // Shows the RecyclerView if the database contains
        // stuff, else shows the LinearLayout with the image.
        val isListEmpty = list.isEmpty()

        requireActivity().runOnUiThread {
            viewLayout.collectionFragmentWordGroups.isVisible = !isListEmpty
            viewLayout.noWordGroupFragment.isVisible = isListEmpty
        }
    }
}