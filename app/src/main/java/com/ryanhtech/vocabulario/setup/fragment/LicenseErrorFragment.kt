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

package com.ryanhtech.vocabulario.setup.fragment

import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.setup.base.AppSetupFragment

class LicenseErrorFragment : AppSetupFragment() {
    override val fragmentLayout: Int = R.layout.fragment_license_error
    override val fragmentIconResource: Int = R.drawable.ic_round_error_24
    override val fragmentTitleResource: Int = R.string.license_error_oops
    override val fragmentDescriptionResource: Int = R.string.license_error_descr
}