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

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.tools.collection.db.CollectionData
import com.ryanhtech.vocabulario.utils.UiUtils
import kotlinx.android.synthetic.main.collection_word_group_recyclerview_element.view.*

class WordGroupAdapter(val list: List<CollectionData>): RecyclerView.Adapter<WordGroupItemHolder>() {
    var data = list
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: WordGroupItemHolder, position: Int) {
        val item = data[position]
        holder.view.collectionWordGroupTitleTextView.text = item.title
        holder.view.collectionWordGroupDateTextView.text = UiUtils.formatDateToCollectionDescriptor(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordGroupItemHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val view = layoutInflater.inflate(R.layout.collection_word_group_recyclerview_element,
                        parent, false) as ConstraintLayout

        return WordGroupItemHolder(view)
    }
}