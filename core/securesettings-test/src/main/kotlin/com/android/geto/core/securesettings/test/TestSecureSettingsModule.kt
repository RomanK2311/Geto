/*
 *
 *   Copyright 2023 Einstein Blanco
 *
 *   Licensed under the GNU General Public License v3.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.gnu.org/licenses/gpl-3.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.android.geto.core.securesettings.test

import com.android.geto.core.securesettings.SecureSettingsModule
import com.android.geto.core.securesettings.SecureSettingsPermissionWrapper
import com.android.geto.core.securesettings.fake.FakeSecureSettingsPermissionWrapper
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SecureSettingsModule::class],
)
interface TestSecureSettingsModule {

    @Binds
    @Singleton
    fun secureSettingsWrapper(impl: FakeSecureSettingsPermissionWrapper): SecureSettingsPermissionWrapper

}