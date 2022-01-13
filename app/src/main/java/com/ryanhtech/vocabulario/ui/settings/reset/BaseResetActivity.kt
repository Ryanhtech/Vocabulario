/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.ui.settings.reset

import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity

/**
 * Provides the base class for reset-style Activities.
 * Reset-style activities have a black background, and white text and items. They
 * let the user know that we are somewhere else in the app, because the style is
 * not exactly the same as the other parts of the app.
 * Reset-style Activities are also Protected activities, which means that, if an
 * administrator password is set, you can't access this Activity without this
 * password. It helps protect devices used in schools against mischief behaviour
 * from students.
 * Also, the reset-style Activities are secure Activities, so you can't create
 * a bot to reset the app "for" the user. You may try though, but it won't work ;)
 *
 * @author Ryanhtech Labs
 * @sample ResetOptionsActivity
 * @since initial version
 */
open class BaseResetActivity : VocabularioActivity() {
    // Set this to enforce Emergency Block to these Activities,
    // so if the app is in Forgot Password mode, the admin will
    // be able to access those Activities only with the security
    // code.
    override val applyEmergencyBlock: Boolean = true

    // Set this to require the admin password in order to use the
    // Activity.
    override val isProtectedActivity: Boolean = true

    // Prevent screenshots and overlays
    override val isSecuredActivity: Boolean = true

    // Prevent undesired activity start while processing
    // reset operations
    override val applyLocalResetConfigurationRequest = false
}