/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
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