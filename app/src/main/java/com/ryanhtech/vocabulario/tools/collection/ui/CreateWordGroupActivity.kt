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

package com.ryanhtech.vocabulario.tools.collection.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.DatePicker
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.internal.vocabulario.Vocabulario
import com.ryanhtech.vocabulario.tools.collection.CollectionManager
import com.ryanhtech.vocabulario.tools.collection.CollectionProperties
import com.ryanhtech.vocabulario.tools.collection.CollectionWord
import com.ryanhtech.vocabulario.tools.collection.WordGroup
import com.ryanhtech.vocabulario.tools.collection.db.CollectionData
import com.ryanhtech.vocabulario.tools.collection.wordpointers.CollectionWordContentManager
import com.ryanhtech.vocabulario.tools.collection.wordpointers.WordPointer
import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity
import com.ryanhtech.vocabulario.ui.easteregg.WindowsEasterEggActivity
import com.ryanhtech.vocabulario.utils.UiUtils
import kotlinx.android.synthetic.main.activity_create_word_group.*
import java.util.*

class CreateWordGroupActivity : VocabularioActivity() {
    override val isProtectedActivity: Boolean = true

    private var isDpdDisplayed = false

    // Easter egg stuff
    private var isEasterEggTextReached = false
    private var isEasterEggDateReached = false

    companion object {
        private const val EASTEREGG_TEXT = "I hate Windows 8 💩"

        private const val EASTEREGG_ENDOFLIFE_DAY = 12
        private const val EASTEREGG_ENDOFLIFE_MONTH = 1
        private const val EASTEREGG_ENDOFLIFE_YEAR = 2016
        private var recyclerViewUpdateRequired = false

        var wordGroup: WordGroup = WordGroup()
            set(value) {
                field = value
                recyclerViewUpdateRequired = true
            }
    }

    override fun onResume() {
        super.onResume()
        updateRecyclerView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val registerResult = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Add the CollectionWord to the WordGroup

                val token = Vocabulario.getGson().fromJson(result.data?.dataString,
                    EditWordToken::class.java)
                        as EditWordToken

                val wgInstance = wordGroup

                wgInstance.append((token.getWord() ?: showWordNullDialog()) as CollectionWord)

                wordGroup = wgInstance  // Fire the setter
            }
        }

        // Set the animations
        overridePendingTransition(R.anim.slide_in_bottom_transition, R.anim.out_one_second)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_word_group)

        init()

        findViewById<ConstraintLayout>(R.id.addWordConstraintLayout).setOnClickListener {
            //startActivityForResult(Intent(this, CreateWordActivity::class.java), 0)
            registerResult.launch(Intent(this, CreateWordActivity::class.java)
                .putExtra(CreateWordActivity.EXTRA_WORD_TO_DISPLAY_TOKEN,
                    Vocabulario.getGson().toJson(EditWordToken(
                        null, wordGroup
                ))))
        }

        startUpdateRecyclerViewCheckThread()
    }

    private fun init() {
        /**
         * Initializes the Activity.
         */

        setWgDateUi(Calendar.getInstance())

        wordGroup = WordGroup()

        setClickListeners()

        updateRecyclerView()
    }

    private fun setClickListeners() {
        changeWordGroupDateConstraintLayout.setOnClickListener {
            updateDateUi()
        }

        createWordGroupBackButton.setOnClickListener { finish() }

        // Set the onTextChanged listener
        wordGroupTitle.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Update the length TextView
                wordGroupTitleLength.text = "${s?.length}/${CollectionProperties.WGROUP_TITLE_MAX_LENGTH}"

                // Play the anim if max length is reached
                if (s?.length!! > CollectionProperties.WGROUP_TITLE_MAX_LENGTH) {
                    UiUtils.animateViewIncorrectValue(wordGroupTitleLength)

                    // Remove the last letter
                    wordGroupTitle.setText(wordGroupTitle.text.dropLast(1))
                    wordGroupTitle.setSelection(CollectionProperties.WGROUP_TITLE_MAX_LENGTH)
                } else { wordGroup.title = s.toString() }

                checkEasterEggText(s)
            }
        })

        cancelButtonAddWordGroup.setOnClickListener {
            finish()
        }

        createWordGroupButton.setOnClickListener {
            createWordGroup()
        }
    }

    private fun updateDateUi() {
        if (isDpdDisplayed) return
        isDpdDisplayed = true

        val calendar = Calendar.getInstance()

        val dpd = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            setWgDateUi(Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            })
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH))

        dpd.show()

        dpd.setOnDismissListener {
            isDpdDisplayed = false

            checkEasterEggDpd(dpd.datePicker)
        }
    }

    private fun setWgDateUi(date: Calendar) {
        wordGroup.setDateWg(date)
        var textToDisplayUi
            = "${date.get(Calendar.DAY_OF_MONTH)}/${date.get(Calendar.MONTH) + 1}/${date.get(Calendar.YEAR)}"

        if (date == Calendar.getInstance()) textToDisplayUi = getString(R.string.today)

        createWordGroupDate.text = textToDisplayUi
    }

    override fun finish() {
        super.finish()
        finishAnim()
    }

    private fun finishAnim() {
        overridePendingTransition(0, R.anim.slide_out_bottom_transition)
    }

    private fun checkEasterEggDpd(dp: DatePicker) {
        isEasterEggDateReached = dp.dayOfMonth == EASTEREGG_ENDOFLIFE_DAY
                && dp.month + 1 == EASTEREGG_ENDOFLIFE_MONTH
                && dp.year == EASTEREGG_ENDOFLIFE_YEAR

        Log.d("isEasterEggDateReachedD",
            "${dp.dayOfMonth}-$EASTEREGG_ENDOFLIFE_DAY/${dp.month}-$EASTEREGG_ENDOFLIFE_MONTH/${dp.year}-$EASTEREGG_ENDOFLIFE_YEAR")

        Log.d("isEasterEggDateReached", isEasterEggDateReached.toString())

        checkEasterEggGlobal()
    }

    private fun checkEasterEggText(text: CharSequence) {
        isEasterEggTextReached = text.toString() == EASTEREGG_TEXT

        Log.d("isEasterEggTextReached", isEasterEggTextReached.toString())
    }

    private fun checkEasterEggGlobal() {
        if (isEasterEggDateReached && isEasterEggTextReached) {
            // Run easter egg
            runEasterEgg()
        }
    }

    private fun runEasterEgg() {
        startActivity(
            Intent(this, WindowsEasterEggActivity::class.java)
        )
    }

    private fun createWordGroup() {
        // Create word group
        Thread {
            val words = wordGroup.ext()
            val pointerList = mutableListOf<WordPointer>()
            for (word in words) {
                val pointer = CollectionWordContentManager.registerWord(word, this)
                pointerList.add(pointer)
            }

            CollectionManager.registerWordGroupInDatabase(
                this, CollectionData(
                    generateCollectionId(),
                    wordGroup.date,
                    wordGroup.title,
                    pointerList.toList()
                )
            )

            // Exit
            finish()
        }.start()
    }

    private fun updateRecyclerView() {
        // Updates the RecyclerView.
        val recyclerViewAdapter = WordItemAdapter(wordGroup.extWords().toList())

        wordGroupContentRecyclerView.adapter = recyclerViewAdapter

        noWordInWordGroupTextView.isVisible = wordGroup.extWords().isEmpty()
    }

    private fun startUpdateRecyclerViewCheckThread() {
        Thread {
            while(true) {
                if (recyclerViewUpdateRequired) {
                    recyclerViewUpdateRequired = false

                    runOnUiThread {
                        updateRecyclerView()
                    }
                }

                Thread.sleep(50)
            }
        }.start()
    }

    private fun generateCollectionId(): Int {
        return 0x0000000
        /* val baseFile = File(Vocabulario.getContext().filesDir, "cid")

        if (!baseFile.exists()) baseFile.writeText("0")

        // Increment current Collection ID
        val id = baseFile.readText().toInt() + 1
        baseFile.writeText(id.toString())

        // Return current collection ID
        return id */
    }

    private fun showWordNullDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.no_word_returned)
            .setMessage(R.string.no_word_returned_descr)
            .setPositiveButton(android.R.string.ok) {_, _ -> }
            .show()
    }
}