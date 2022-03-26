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

package com.ryanhtech.vocabulario.setup.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.setup.config.UserSetupList
import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity
import com.ryanhtech.vocabulario.ui.animations.VocabularioListAnimation
import com.ryanhtech.vocabulario.utils.DataManager
import kotlinx.android.synthetic.main.activity_user_setup.*

class UserSetupActivity : VocabularioActivity() {
    private var currentFragment: AppSetupFragment? = null
    private var alreadySteppedIn = false
    override val isProtectedActivity: Boolean = true

    /**
     * This [List] contains everything you should dismiss when the variable
     * [AppSetupFragment.displaySetupItems] is set to `false`.
     */
    private lateinit var mSetupItemsToDismiss: List<View>

    // If this Activity launched another setup Activity
    var isNewSetupPageLaunched = false

    // The ListAnimation instance
    private lateinit var listAnimation: VocabularioListAnimation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_setup)

        // Get the fragment to inflate and set it.
        val lStartupExtraStep = intent.getIntExtra("step", UserSetupList.SETUP_EULA)
        val lFragmentToApply = UserSetupList.setupPages[lStartupExtraStep]
        setCurrentFragment(lFragmentToApply)

        // Initialize the lists.
        initializeLists()

        // Initialize the fragment's settings.
        applyShowSetupItems()

        setupClickListeners()
    }

    private fun setCurrentFragment(fragment: AppSetupFragment?) {
        currentFragment = fragment

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.setupContents, fragment!!)
            commit()
        }
    }

    private fun setupClickListeners() {
        setupNextButton.setOnClickListener {
            if (currentFragment!!.onNextPressed()) {
                // Check if the fragment allows the Next button
                // (dynamically). If it's the case, show the
                // progress bar and disable the next button.
                setupNextButton.isEnabled = false
                setupProgressBar.isVisible = true

                // Start the new activity in a new thread.
                Thread {
                    // Create a new Intent and put the requested step
                    // into it as a string extra.
                    val lActivityIntent = Intent(this, UserSetupActivity::class.java)
                    lActivityIntent.putExtra("step", currentFragment!!.nextStep)

                    // Start the requested Activity and remove the
                    // Activity transition
                    startActivity(lActivityIntent)
                    overridePendingTransition(0, 0)

                    // Register the event
                    isNewSetupPageLaunched = true
                }.start()
            }
        }

        setupBackButton.setOnClickListener {
            if (currentFragment!!.onBackPressed()) {
                // If the fragment allows the back button, end the Activity
                finish()
            }
        }

        setupBackButton.isEnabled = currentFragment!!.displayBackButton
        setupNextButton.isVisible = currentFragment!!.displayNextButton

        // Indicate that the back button is disabled, if it is
        if (!setupBackButton.isEnabled) {
            setupBackButton.alpha = 0.5F
        }

        setupImage.setImageDrawable(AppCompatResources.getDrawable(
            this, currentFragment!!.fragmentIconResource))
        setupTitle.text = getString(currentFragment!!.fragmentTitleResource)

        // Check if the description is not null
        val lSetupDescription = currentFragment!!.fragmentDescriptionResource
        val lSetupDescriptionExt: String = if (lSetupDescription == null) { "" }
        else {
            getString(lSetupDescription)
        }

        setupDescription.text = lSetupDescriptionExt
        if (lSetupDescriptionExt == "") setupDescription.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()

        overridePendingTransition(0, 0)

        val isFragmentInNonFinishExceptions =
            UserSetupList.nonSetupPagesExceptions.contains(currentFragment!!::class.java)

        if (DataManager.checkIfAppConfigured(this)
            && !isFragmentInNonFinishExceptions) finish()

        setupContents.isVisible = true
        setupNextButton.isEnabled = true
        setupProgressBar.visibility = View.INVISIBLE

        // If the Activity has already been initialized
        if (!alreadySteppedIn) {
            startVbListAnimationOnViews()
        }

        // If we just came back from another setup page, then just
        // start the reversed animation
        if (isNewSetupPageLaunched) {
            listAnimation.startReversedAnimation()
        }

        alreadySteppedIn = true
        isNewSetupPageLaunched = false
    }

    /**
     * Starts the [VocabularioListAnimation] to enhance the user experience.
     */
    private fun startVbListAnimationOnViews() {
        // Start by creating ths list of the Views to animate.
        val lViewList: List<View>
        var lViewMutableList = mutableListOf<View>()

        // If we can show all setup items, add these to the list
        if (currentFragment!!.displaySetupItems) {
            lViewMutableList = mutableListOf(setupImage, setupTitle, setupDescription)
        }

        // Add the contents
        lViewMutableList.add(setupContents)

        // Convert the mutable list into a read-only list
        lViewList = lViewMutableList.toList()

        // Instantiate a new VocabularioListAnimation by passing the view list
        listAnimation = VocabularioListAnimation(lViewList, this)

        // Now start the animation on the widgets
        listAnimation.startAnimation()
    }

    override fun onBackPressed() {
        if (currentFragment!!.displayBackButton) {
            // Go back only if the back button is enabled in the
            // setup fragment. Also, ask to the fragment if we
            // should let the user go back.
            if (currentFragment!!.onBackPressed()) {
                super.onBackPressed()
            }
        }
    }

    override fun onPause() {
        super.onPause()

        overridePendingTransition(0, 0)
    }

    /**
     * This initializes all the [List]s that have not been initialized yet.
     */
    private fun initializeLists() {
        // Initialize the mSetupItemsToDismiss list
        mSetupItemsToDismiss = listOf(setupBackButton, setupImage, setupTitle, setupDescription,
            setupNextButton)
    }

    /**
     * This applies the setting defined in [AppSetupFragment.displaySetupItems].
     */
    private fun applyShowSetupItems() {
        // If we are set to true, return because you have nothing to do.
        // (use "[stuff] != false" to handle null values)
        if (currentFragment?.displaySetupItems != false) {
            return
        }

        // We should dismiss all items on the screen. (= "setup items")
        for (fViewToDismiss in mSetupItemsToDismiss) {
            fViewToDismiss.visibility = View.GONE
        }
    }
}