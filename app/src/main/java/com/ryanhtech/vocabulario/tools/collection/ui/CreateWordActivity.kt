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

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.google.android.gms.tasks.RuntimeExecutionException
import com.google.mlkit.common.MlKitException
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.internal.vocabulario.Vocabulario
import com.ryanhtech.vocabulario.tools.collection.CollectionWord
import com.ryanhtech.vocabulario.tools.collection.ui.exceptions.InvalidEditTokenException
import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity
import com.ryanhtech.vocabulario.utils.StaticData
import kotlinx.android.synthetic.main.activity_create_word.*

class CreateWordActivity : VocabularioActivity() {
    private var isSuggestionsReady = false

    companion object {
        private const val SUGGESTIONS_PENDING = 1
        private const val SUGGESTIONS_TRANSLATING = 2
        private const val SUGGESTIONS_TOO_LONG = 3

        const val EXTRA_WORD_TO_DISPLAY_TOKEN = "w"
    }

    private lateinit var translator: Translator
    private var currentPanel = SUGGESTIONS_PENDING
    private var isLiveTranslationInProgress = false
    private lateinit var word: CollectionWord
    var isEditModeEnabled = false
    set(value) {
        // If the value to set == false AND isEditModeEnabled == true
        if (!value && field)
            throw SecurityException("Can't switch to non-edit mode in an edit session")

        field = value
    }
    private lateinit var editToken: EditWordToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_word)

        checkForExtras()

        findViewById<Button>(R.id.cancelButtonAddWord).setOnClickListener {
            closeActivityWithResult()
        }

        findViewById<Button>(R.id.createWordButton).setOnClickListener {
            saveStatus()
        }

        wordText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (StaticData.isSuggestionsPaused) return

                startTextWatcherThread()

                if (s?.length!! < 2) {
                    setSuggestionsPanelStatus(SUGGESTIONS_PENDING)
                } else if (s.length in 2..14) {
                    setSuggestionsPanelStatus(SUGGESTIONS_TRANSLATING)
                    startLiveTranslation()
                } else if (s.length > 15) {
                    setSuggestionsPanelStatus(SUGGESTIONS_TOO_LONG)
                }
            }
        })

        startTextWatcherThread()
    }

    private fun checkForExtras() {
        // Checks if the activity has started with extras,
        // which means that you should activate the "Edit
        // word" option
        val extraJson = intent.getStringExtra(EXTRA_WORD_TO_DISPLAY_TOKEN)
            ?: throw InvalidEditTokenException("Token is missing")

        val extra: EditWordToken = Vocabulario.getGson().fromJson(
            extraJson, EditWordToken::class.java) as EditWordToken

        switchToEditMode(extra)

        editToken = extra
    }

    private fun switchToEditMode(token: EditWordToken) {
        val word = token.getWord() ?: return  // No word exists for now

        createWordTopTileMainTitle.text = getString(R.string.edit_word)

        wordText.setText(word.mBaseWord)
        wordTranslationText.setText(word.mTargetWord)

        isEditModeEnabled = true
    }

    private fun startLiveTranslation() {
        isLiveTranslationInProgress = true

        getTranslator().translate(wordText.text.toString()).addOnSuccessListener {
            wordTranslationText.setText(it)
            isLiveTranslationInProgress = false
        }
    }

    private fun getTranslator(): Translator {
        try {
            return translator
        } catch (e: UninitializedPropertyAccessException) {}

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.SPANISH)
            .setTargetLanguage(TranslateLanguage.FRENCH)
            .build()

        translator = Translation.getClient(options)

        return translator
    }

    override fun onResume() {
        super.onResume()

        // Show the right Suggestions info panel
        val translatorOptions = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.FRENCH)
            .setTargetLanguage(TranslateLanguage.SPANISH)
            .build()

        val translator = Translation.getClient(translatorOptions)

        var isSuggestionsActivated: Boolean? = isSuggestionsActivated(translator)

        if (isSuggestionsActivated == null) {
            // This value will update over time
            isSuggestionsActivated = true
        }

        try {
            pauseSuggestionsFromAddWordButton.setOnClickListener {
                StaticData.isSuggestionsPaused = true

                suggestionsProcessAddWord.isVisible = false
                suggestionsPausedAddWord.isVisible = true
            }

            resumeSuggestionsButtonAddWord.setOnClickListener {
                StaticData.isSuggestionsPaused = false

                suggestionsPausedAddWord.isVisible = false
                suggestionsProcessAddWord.isVisible = true

                animateInsidePanel()
            }
        } catch (e: NullPointerException) {}

        updateSuggestionsActivationStatus(isSuggestionsActivated)
    }

    private fun updateSuggestionsActivationStatus(ok: Boolean) {
        isSuggestionsReady = ok

        suggestionsDisabledAddWord.isVisible = !ok
        suggestionsProcessAddWord.isVisible = ok
    }

    private fun isSuggestionsActivated(
        translator: Translator): Boolean?
    {
        try {
            translator.translate("hola").addOnSuccessListener { updateSuggestionsActivationStatus(true) }
                .addOnFailureListener { updateSuggestionsActivationStatus(false) }
        } catch (e: MlKitException) {
            if (e.errorCode == MlKitException.NOT_FOUND) return false
        } catch (e: IllegalStateException) {
            Toast.makeText(this, getString(R.string.cant_verify_suggestions_activation),
                Toast.LENGTH_SHORT).show()
            return false
        } catch (e: RuntimeExecutionException) {
            return false
        }

        return null
    }

    private fun setSuggestionsPanelStatus(status: Int) {
        if (status == SUGGESTIONS_PENDING) {
            suggestionsLiveTranslateStatusAddWord.text = getString(R.string.suggestions_waiting)
            pauseSuggestionsFromAddWordButton.isVisible = true
            addwordSuggestionsIcon.alpha = 1F
            pauseSuggestionsFromAddWordButton.text = getString(R.string.pause)
            suggestionsLiveTranslateProgressBar.isVisible = false
        } else if (status == SUGGESTIONS_TRANSLATING) {
            suggestionsLiveTranslateStatusAddWord.text = getString(R.string.translation_in_progress)
            pauseSuggestionsFromAddWordButton.isVisible = false
            suggestionsLiveTranslateProgressBar.isVisible = true
        } else if (status == SUGGESTIONS_TOO_LONG) {
            suggestionsLiveTranslateStatusAddWord.text = getString(R.string.word_too_long)
            pauseSuggestionsFromAddWordButton.isVisible = false
            suggestionsLiveTranslateProgressBar.isVisible = false
        } else {
            throw IllegalStateException("The status value that you passed is not supported by this function.")
        }

        if (currentPanel != status && currentPanel != SUGGESTIONS_PENDING && status != SUGGESTIONS_TRANSLATING) {
            animateInsidePanel()
        }

        currentPanel = status
    }

    private fun animateInsidePanel() {
        val springAnimation = suggestionsProcessAddWord.let { view ->
            SpringAnimation(view, DynamicAnimation.TRANSLATION_X, 0F).apply {
                spring.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
            }
        }

        suggestionsProcessAddWord.translationX += 100F

        springAnimation.start()
    }

    private fun startTextWatcherThread() {
        val thread = Thread {
            var elapsed = 0
            var oldWord = wordText.text.toString()

            while (true) {
                Thread.sleep(200)
                if (oldWord != wordText.text.toString()) elapsed = 0

                oldWord = wordText.text.toString()

                if (StaticData.isSuggestionsPaused || isLiveTranslationInProgress)
                    elapsed = 0 else elapsed += 200

                if (elapsed >= 500 && currentPanel != SUGGESTIONS_TOO_LONG) runOnUiThread {
                        setSuggestionsPanelStatus(SUGGESTIONS_PENDING)
                }
            }
        }

        thread.start()
    }

    private fun closeActivityWithResult() {
        val data = Intent()

        // Get the Word instance
        val wordInstance: CollectionWord? = try {
            word
        } catch (e: UninitializedPropertyAccessException) {
            // If an UninitializedPropertyAccessException occurs, it means
            // that the word variable has not been initialized, so set the
            // instance to null.
            null
        }

        // Generate the token and set the data
        val token = createToken()

        data.data = Uri.parse(Vocabulario.getGson().toJson(token))

        val result = if (wordInstance == null) RESULT_CANCELED else RESULT_OK

        // Set the Activity result passing the CollectionWord as JSON
        setResult(result, data)

        finish()
    }

    private fun saveStatus() {
        // Saves the changes and finish, by returning the result
        // to the parent Activity.

        // Disable the buttons (or LinearLayouts)
        createWordButton.isEnabled = false
        cancelButtonAddWord.isEnabled = false

        // Show the progress bar (with an animation to avoid perturbation of
        // the user)
        addWordLoadingProgressBar.let { progressBar ->
            // Create the SpringAnimation
            val sa = SpringAnimation(progressBar, DynamicAnimation.TRANSLATION_Y,
                addWordLoadingProgressBar.translationY).apply {
                    spring.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
                    spring.stiffness = SpringForce.STIFFNESS_LOW
            }

            progressBar.translationY -= 25
            sa.start()
            progressBar.isVisible = true
        }

        // Instantiate a new CollectionWord() to set it as result later
        word = CollectionWord(wordText.text.toString(), wordTranslationText.text.toString())

        // The word has been saved and is ready to be returned.

        // Closes the activity with the result, within 1s.
        Handler(Looper.getMainLooper()).postDelayed({
            closeActivityWithResult()
        }, 1000)
    }

    private fun checkForSaveProblems() {

    }

    private fun createToken(): EditWordToken {
        val mWord: CollectionWord? = try {
            word
        } catch (e: UninitializedPropertyAccessException) {
            null
        }

        if (editToken.getWord() == null) editToken.setWord(mWord)

        return editToken
    }
}