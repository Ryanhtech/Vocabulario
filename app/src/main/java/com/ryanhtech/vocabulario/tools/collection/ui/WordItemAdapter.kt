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

package com.ryanhtech.vocabulario.tools.collection.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.RecyclerView
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.internal.vocabulario.Vocabulario
import com.ryanhtech.vocabulario.tools.collection.CollectionWord
import kotlinx.android.synthetic.main.create_word_group_word_recyclerview_element.view.*

class WordItemAdapter(val content: List<CollectionWord>): RecyclerView.Adapter<WordItemHolder>() {
    var data = content
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = content.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordItemHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val view = layoutInflater.inflate(R.layout.create_word_group_word_recyclerview_element,
            parent, false) as ConstraintLayout

        return WordItemHolder(view)
    }

    override fun onBindViewHolder(holder: WordItemHolder, position: Int) {
        val item = data[position]

        // This is where the serious stuff begins.
        holder.view.collectionWordTextView.text = item.mBaseWord
        holder.view.collectionWordTranslationTextView.text = item.mTargetWord

        holder.view.deleteWordButton.setOnClickListener {
            holder.view.let {
                val sa = SpringAnimation(it, DynamicAnimation.TRANSLATION_X, it.width.toFloat()).apply {
                    spring.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
                }

                sa.start()
                sa.addEndListener { _, _, _, _ ->
                    val wg = CreateWordGroupActivity.wordGroup
                    wg.delete(item)
                    CreateWordGroupActivity.wordGroup = wg
                }
            }
        }

        holder.view.editWordButton.setOnClickListener {
            Vocabulario.getContext().startActivity(
                Intent(Vocabulario.getContext(),
                    CreateWordActivity::class.java)

                    .putExtra(CreateWordActivity.EXTRA_WORD_TO_DISPLAY_TOKEN,
                        Vocabulario.getGson().toJson(EditWordToken(item,
                            CreateWordGroupActivity.wordGroup))
                    )

                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }
}