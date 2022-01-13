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

package com.ryanhtech.vocabulario.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.Fade
import android.util.Log
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.setup.base.UserSetupActivity
import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity
import com.ryanhtech.vocabulario.ui.animations.VocabularioListAnimation
import com.ryanhtech.vocabulario.ui.main.collection.CollectionFragment
import com.ryanhtech.vocabulario.utils.DataManager
import com.ryanhtech.vocabulario.utils.StaticData
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess

class MainActivity : VocabularioActivity() {
    // Main activity of Vocabulario. It can contain the main_activity view,
    // or the welcome_layout view depending on the context.

    // Variables
    private var currentLayout: Int = R.layout.activity_main

    /**---------------------------------------------------------------------------------------------
     * WELCOME LAYOUT WIDGETS
     */
    private lateinit var nextButtonWelcome: Button
    private lateinit var appLogo: ImageView
    private lateinit var appTitle: TextView
    private lateinit var appDescription: TextView
    private lateinit var appAssistanceInfo: Button
    private lateinit var mainConstraintLayout: ConstraintLayout

    /**---------------------------------------------------------------------------------------------
     * MAIN LAYOUT WIDGETS
     */
    private val mainFragment = HomeFragment()
    private val collectionFragment = CollectionFragment()
    private val quizFragment = QuizFragment()
    private val profileFragment = ProfileFragment()

    private lateinit var bottomNavigationView: BottomNavigationView

    // Resume variable used to know if the activity has been resumed from
    // another activity and if you should jump to a new activity
    private var jumpOnActivityWhenResumed: Class<*>?     =       null
    private var isInitialized                            =       false

    companion object {
        var stopOnResume                                 =       false

        private var currentFragment: Fragment?           =        null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Override the splash screen theme by the main theme
        setTheme(R.style.Theme_Vocabulario_Light)

        // Set up the transitions
        with(window) {
            requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
            enterTransition = Fade()
        }
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.fade_in, R.anim.out_one_second)

        // Check if the app has been configured
        if (!DataManager.checkIfAppConfigured(applicationContext)) {
            // If the app isn't configured
            currentLayout = R.layout.welcome_layout
        }

        Log.d("MainActivity", "Content view: $currentLayout")

        // Set the view to the requested one depending on the context.
        setContentView(currentLayout)

        // Finally, set up the activity with the onClick listeners, for example.
        if (setupActivity(currentLayout)) {
            isInitialized = true
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupActivity(activityName: Int): Boolean {
        // Set up the activity

        if (activityName == R.layout.activity_main) {
            // If the layout is activity_main, initialize the different
            // widgets and do stuff with them

            bottomNavigationView = findViewById(R.id.bottomNavigationBarMainActivity)

            startMainFragmentButtonListenerThread()

            if (!isInitialized) {
                currentFragment = mainFragment
                setCurrentFragment(mainFragment)
                initializeFragments(false)
            }
        }

        if (activityName == R.layout.welcome_layout) {
            // If the layout is the welcome layout, set the onClick listeners and initialize
            // the activity so that it can be used

            nextButtonWelcome     =      findViewById(R.id.nextButtonWelcome)
            appTitle              =      findViewById(R.id.appTtitle)
            appLogo               =      findViewById(R.id.appIcon)
            appDescription        =      findViewById(R.id.appDescription)
            appAssistanceInfo     =      findViewById(R.id.assistanceInformationButtonWelcome)
            mainConstraintLayout  =      findViewById(R.id.welcomeConstraintLayout)

            nextButtonWelcome.setOnClickListener {
                startActivity(
                    Intent(
                        this,
                        UserSetupActivity::class.java
                    ).putExtra("step", StaticData.setupStep)
                )
            }

            // Set the animation to start after 1s
            val widgetsToAnimate = listOf(appLogo, appTitle, appAssistanceInfo, nextButtonWelcome)

            // Create a VocabularioListAnimation instance and run it after 1s
            val lVocListAnim = VocabularioListAnimation(widgetsToAnimate, this)
            lVocListAnim.hideViews()

            Handler(Looper.getMainLooper()).postDelayed({
                lVocListAnim.startVlaAnimation()
            }, 1000)

            return true
        }
        return true
    }

    // =============================================================================================
    // ¡VOCABULARIO! SETUP

    override fun onResume() {
        super.onResume()

        Log.d("MainActivity", "onResume() called!")

        try {
            initializeFragments(false)
        } catch (e: UninitializedPropertyAccessException) {
            if (DataManager.checkIfAppConfigured(this)) {
                finish()
            }
        }

        if (!isInitialized) {
            return
        }

        if (stopOnResume) {
            // Wait a bit so it doesn't restart the child Activity.
            Handler(Looper.getMainLooper()).postDelayed({
                    exitProcess(0)
                },100
            )
        }

        if (jumpOnActivityWhenResumed == null) {
            return
        }

        Log.d("Performance", "Intent is starting...")

        // Create a new thread to start the activity and improve performance

        // Put the context into a variable to pass it to the thread
        val mainThreadContext: Context = this
        val activityToRun: Class<*> = jumpOnActivityWhenResumed as Class<*>

        val startActivityThread = Thread {
            startActivity(
                Intent(
                        mainThreadContext,
                        activityToRun
                )
            )
        }

        // Hide all the widgets so it doesn't seems like we changed activity for the user
        nextButtonWelcome.isVisible  =      false
        appTitle.isVisible           =      false
        appLogo.isVisible            =      false
        appDescription.isVisible     =      false

        Handler(Looper.getMainLooper()).postDelayed({
                startActivityThread.start()
            }, 50
        )

        jumpOnActivityWhenResumed = null
    }

    private fun setCurrentFragment(fragment: Fragment, hasAnimation: Boolean = true) {
        currentFragment = fragment

        Thread {
            val zoomOutAnim = AnimationUtils.loadAnimation(
                this,
                R.anim.zoom_out
            )

            runOnUiThread {
                if (hasAnimation) {
                    /*containerFragment.startAnimation(
                        zoomOutAnim
                    )*/
                }
            }

            //Thread.sleep(zoomOutAnim.duration)

            runOnUiThread {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.containerFragment, fragment)
                    commit()
                    runOnCommit {
                        initializeFragments(hasAnimation)
                    }
                }

                // Change the theme to adapt to the fragment
                setTheme(when(currentFragment) {
                    collectionFragment -> R.style.CollectionUITheme
                    else -> R.style.Theme_Vocabulario_Light
                })
            }

        }.start()
    }

    private fun getCurrentFragmentSelection(item: Int): Fragment = when(item) {
        R.id.homeNavBarItem -> mainFragment
        R.id.collectionNavBarItem -> collectionFragment
        R.id.quizNavBarItem -> quizFragment
        R.id.profileNavBarItem -> profileFragment
        else -> throw IllegalStateException()
    }

    private fun initializeFragments(hasAnimation: Boolean = true) {
        bottomNavigationView.setOnItemSelectedListener {
            val selection = getCurrentFragmentSelection(it.itemId)

            setCurrentFragment(selection)
            true
        }
        containerFragment.isVisible = true


        Handler(Looper.getMainLooper()).postDelayed({
            // Bug fixer -- DO NOT REMOVE!

            if (currentFragment == null) setCurrentFragment(mainFragment)
        }, 300)

        /*val containerAnim = AnimationUtils.loadAnimation(
            this,
            R.anim.raise_up_welcome
        )

        containerAnim.startOffset = 0

        if (hasAnimation) containerFragment.startAnimation(containerAnim) */

        if (hasAnimation) {
            containerFragment.let {
                it.isVisible = true
                it.alpha = 1F

                val view = it

                val sa = SpringAnimation(it, DynamicAnimation.TRANSLATION_Y, 0F).apply {
                    spring.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
                    spring.stiffness = SpringForce.STIFFNESS_LOW
                }

                it.translationY -= (it.measuredHeight * 2) * -1

                sa.start()
            }
        }
    }

    private fun startMainFragmentButtonListenerThread() {
        Thread {
            while (true) {
                if (mainFragment.collectionFragmentToGoTo != null) {
                    val buttonToSelect = when (mainFragment.collectionFragmentToGoTo) {
                        HomeFragment.FRAGMENT_COLLECTION -> R.id.collectionNavBarItem
                        HomeFragment.FRAGMENT_QUIZ -> R.id.quizNavBarItem
                        HomeFragment.FRAGMENT_PROFILE -> R.id.profileNavBarItem
                        else -> throw NullPointerException("Unknown Button!")
                    }

                    runOnUiThread {
                        bottomNavigationView.selectedItemId = buttonToSelect
                    }

                    mainFragment.collectionFragmentToGoTo = null
                }

                Thread.sleep(50)
            }
        }.start()
    }
}