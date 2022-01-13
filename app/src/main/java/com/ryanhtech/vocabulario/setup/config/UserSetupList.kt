/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * This Software (Vocabulario) is licensed under the RHT-PSLA
 * license (Ryanhtech Proprietary Software License Agreement).
 * You must not use this Software or this source code if you
 * don't comply with this license.
 */

package com.ryanhtech.vocabulario.setup.config

import com.ryanhtech.vocabulario.setup.base.AppSetupFragment
import com.ryanhtech.vocabulario.setup.fragment.*

object UserSetupList {
    const val SETUP_EULA:             Int = 1
    const val SETUP_PLACE:            Int = 2
    const val SETUP_ID_PERS:          Int = 3
    const val SETUP_LEVEL_PERS:       Int = 4
    const val SETUP_FEATURES_PERS:    Int = 5
    const val SETUP_FEATURES_INSTALL: Int = 6
    const val SETUP_RESET_APP:        Int = 7
    const val SETUP_RESET_APP_OPR:    Int = 8
    const val ORG_SETUP_ADMIN_INTRO:  Int = 9
    const val ORG_SETUP_ENTER_NAME:   Int = 10
    const val ORG_SETUP_ENTER_PWD:    Int = 11
    const val ORG_SETUP_SEC_CODE:     Int = 12
    const val ORG_ENABLE_ADMIN:       Int = 13
    const val SETUP_SUGGESTIONS_C:    Int = 14
    const val LICENSE_ERROR:          Int = 15

    var configIsSuggestionsEnabled: Boolean = false
    var isOrgPasswordConfigPending: Boolean = false

    val setupPages: Map<Int, AppSetupFragment> = mapOf(
        SETUP_EULA to SetupEulaFragment(),
        SETUP_PLACE to PlaceSetupFragment(),
        SETUP_ID_PERS to EnterIdentitySetupFragment(),
        SETUP_LEVEL_PERS to SelectLevelSetupFragment(),
        SETUP_FEATURES_PERS to FeaturesSetupFragment(),
        SETUP_FEATURES_INSTALL to FeaturesInstallFragment(),
        SETUP_RESET_APP to ResetAppFragment(),
        SETUP_RESET_APP_OPR to ResetAppProgressFragment(),
        ORG_SETUP_ADMIN_INTRO to OrganizationSetupIntroFragment(),
        ORG_SETUP_ENTER_NAME to EnterOrganizationNameSetupFragment(),
        ORG_SETUP_ENTER_PWD to EnterAdminPasswordSetupFragment(),
        ORG_SETUP_SEC_CODE to DisplayAdminSecurityCodeSetupFragment(),
        ORG_ENABLE_ADMIN to EnableDeviceAdminSetupFragment(),
        SETUP_SUGGESTIONS_C to SuggestionsChoiceSetupFragment(),
        LICENSE_ERROR to LicenseErrorFragment(),
    )

    val nonSetupPagesExceptions: List<Class<out AppSetupFragment>> = listOf(
        ResetAppFragment::class.java,
        ResetAppProgressFragment::class.java,
        LicenseErrorFragment::class.java,
    )
}