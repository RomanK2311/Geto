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
package com.android.geto.feature.appsettings

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.testing.invoke
import com.android.geto.core.domain.ApplyAppSettingsUseCase
import com.android.geto.core.domain.AutoLaunchUseCase
import com.android.geto.core.domain.RequestPinShortcutUseCase
import com.android.geto.core.domain.RevertAppSettingsUseCase
import com.android.geto.core.model.AppSetting
import com.android.geto.core.model.AppSettingsResult
import com.android.geto.core.model.GetoApplicationInfo
import com.android.geto.core.model.GetoShortcutInfoCompat
import com.android.geto.core.model.RequestPinShortcutResult
import com.android.geto.core.model.SecureSetting
import com.android.geto.core.model.SettingType
import com.android.geto.core.testing.repository.TestAppSettingsRepository
import com.android.geto.core.testing.repository.TestClipboardRepository
import com.android.geto.core.testing.repository.TestPackageRepository
import com.android.geto.core.testing.repository.TestSecureSettingsRepository
import com.android.geto.core.testing.repository.TestShortcutRepository
import com.android.geto.core.testing.repository.TestUserDataRepository
import com.android.geto.core.testing.util.MainDispatcherRule
import com.android.geto.feature.appsettings.navigation.AppSettingsRouteData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class AppSettingsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var packageRepository: TestPackageRepository

    private lateinit var appSettingsRepository: TestAppSettingsRepository

    private lateinit var secureSettingsRepository: TestSecureSettingsRepository

    private lateinit var clipboardRepository: TestClipboardRepository

    private lateinit var shortcutRepository: TestShortcutRepository

    private lateinit var userDataRepository: TestUserDataRepository

    private lateinit var applyAppSettingsUseCase: ApplyAppSettingsUseCase

    private lateinit var revertAppSettingsUseCase: RevertAppSettingsUseCase

    private lateinit var autoLaunchUseCase: AutoLaunchUseCase

    private lateinit var requestPinShortcutUseCase: RequestPinShortcutUseCase

    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var viewModel: AppSettingsViewModel

    private val packageName = "com.android.geto"

    private val appName = "Geto"

    @Before
    fun setup() {
        packageRepository = TestPackageRepository()

        appSettingsRepository = TestAppSettingsRepository()

        secureSettingsRepository = TestSecureSettingsRepository()

        clipboardRepository = TestClipboardRepository()

        shortcutRepository = TestShortcutRepository()

        userDataRepository = TestUserDataRepository()

        applyAppSettingsUseCase = ApplyAppSettingsUseCase(
            appSettingsRepository = appSettingsRepository,
            secureSettingsRepository = secureSettingsRepository,
        )

        revertAppSettingsUseCase = RevertAppSettingsUseCase(
            appSettingsRepository = appSettingsRepository,
            secureSettingsRepository = secureSettingsRepository,
        )

        autoLaunchUseCase = AutoLaunchUseCase(
            userDataRepository = userDataRepository,
            appSettingsRepository = appSettingsRepository,
            secureSettingsRepository = secureSettingsRepository,
        )

        requestPinShortcutUseCase =
            RequestPinShortcutUseCase(shortcutRepository = shortcutRepository)

        savedStateHandle = SavedStateHandle(
            route = AppSettingsRouteData(
                packageName = packageName,
                appName = appName,
            ),
        )

        viewModel = AppSettingsViewModel(
            savedStateHandle = savedStateHandle,
            appSettingsRepository = appSettingsRepository,
            packageRepository = packageRepository,
            clipboardRepository = clipboardRepository,
            secureSettingsRepository = secureSettingsRepository,
            applyAppSettingsUseCase = applyAppSettingsUseCase,
            revertAppSettingsUseCase = revertAppSettingsUseCase,
            autoLaunchUseCase = autoLaunchUseCase,
            requestPinShortcutUseCase = requestPinShortcutUseCase,
        )
    }

    @Test
    fun appSettingsUiState_isLoading_whenStarted() {
        assertIs<AppSettingsUiState.Loading>(viewModel.appSettingsUiState.value)
    }

    @Test
    fun applyAppSettingsResult_isNull_whenStarted() {
        assertNull(viewModel.applyAppSettingsResult.value)
    }

    @Test
    fun revertAppSettingsResult_isNull_whenStarted() {
        assertNull(viewModel.revertAppSettingsResult.value)
    }

    @Test
    fun autoLaunchResult_isNull_whenStarted() {
        assertNull(viewModel.autoLaunchResult.value)
    }

    @Test
    fun setPrimaryClipResult_isFalse_whenStarted() {
        assertFalse(viewModel.setPrimaryClipResult.value)
    }

    @Test
    fun secureSettings_isEmpty_whenStarted() {
        assertTrue(viewModel.secureSettings.value.isEmpty())
    }

    @Test
    fun requestPinShortcutResult_isNull_whenStarted() {
        assertNull(viewModel.requestPinShortcutResult.value)
    }

    @Test
    fun applicationIcon_isNull_whenStarted() {
        assertNull(viewModel.applicationIcon.value)
    }

    @Test
    fun appSettingsUiState_isSuccess_whenAppSettings_isNotEmpty() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.appSettingsUiState.collect()
        }

        val appSettings = List(5) { index ->
            AppSetting(
                id = index,
                enabled = false,
                settingType = SettingType.SYSTEM,
                packageName = packageName,
                label = "Geto",
                key = "Geto $index",
                valueOnLaunch = "0",
                valueOnRevert = "1",
            )
        }

        appSettingsRepository.setAppSettings(appSettings)

        assertIs<AppSettingsUiState.Success>(viewModel.appSettingsUiState.value)
    }

    @Test
    fun applyAppSettingsResult_isSuccess_whenApplyAppSettings() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.applyAppSettingsResult.collect()
        }

        val appSettings = List(5) { index ->
            AppSetting(
                id = index,
                enabled = true,
                settingType = SettingType.SYSTEM,
                packageName = packageName,
                label = "Geto",
                key = "Geto $index",
                valueOnLaunch = "0",
                valueOnRevert = "1",
            )
        }

        appSettingsRepository.setAppSettings(appSettings)

        secureSettingsRepository.setWriteSecureSettings(true)

        viewModel.applyAppSettings()

        assertEquals(
            expected = AppSettingsResult.Success,
            actual = viewModel.applyAppSettingsResult.value,
        )
    }

    @Test
    fun applyAppSettingsResult_isSecurityException_whenApplyAppSettings() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.applyAppSettingsResult.collect()
        }

        val appSettings = List(5) { index ->
            AppSetting(
                id = index,
                enabled = true,
                settingType = SettingType.SYSTEM,
                packageName = packageName,
                label = "Geto",
                key = "Geto $index",
                valueOnLaunch = "0",
                valueOnRevert = "1",
            )
        }

        appSettingsRepository.setAppSettings(appSettings)

        secureSettingsRepository.setWriteSecureSettings(false)

        viewModel.applyAppSettings()

        assertEquals(
            expected = AppSettingsResult.SecurityException,
            actual = viewModel.applyAppSettingsResult.value,
        )
    }

    @Test
    fun applyAppSettingsResult_isIllegalArgumentException_whenApplyAppSettings() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.applyAppSettingsResult.collect()
        }

        val appSettings = List(5) { index ->
            AppSetting(
                id = index,
                enabled = true,
                settingType = SettingType.SYSTEM,
                packageName = packageName,
                label = "Geto",
                key = "Geto $index",
                valueOnLaunch = "0",
                valueOnRevert = "1",
            )
        }

        appSettingsRepository.setAppSettings(appSettings)

        secureSettingsRepository.setWriteSecureSettings(true)

        secureSettingsRepository.setInvalidValues(true)

        viewModel.applyAppSettings()

        assertEquals(
            expected = AppSettingsResult.IllegalArgumentException,
            actual = viewModel.applyAppSettingsResult.value,
        )
    }

    @Test
    fun applyAppSettingsResult_isEmptyAppSettings_whenApplyAppSettings() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.applyAppSettingsResult.collect()
        }

        appSettingsRepository.setAppSettings(emptyList())

        secureSettingsRepository.setWriteSecureSettings(true)

        viewModel.applyAppSettings()

        assertEquals(
            expected = AppSettingsResult.EmptyAppSettings,
            actual = viewModel.applyAppSettingsResult.value,
        )
    }

    @Test
    fun applyAppSettingsResult_isDisabledAppSettings_whenApplyAppSettings() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.applyAppSettingsResult.collect()
        }

        val appSettings = List(5) { index ->
            AppSetting(
                id = index,
                enabled = false,
                settingType = SettingType.SYSTEM,
                packageName = packageName,
                label = "Geto",
                key = "Geto $index",
                valueOnLaunch = "0",
                valueOnRevert = "1",
            )
        }

        appSettingsRepository.setAppSettings(appSettings)

        secureSettingsRepository.setWriteSecureSettings(true)

        viewModel.applyAppSettings()

        assertEquals(
            expected = AppSettingsResult.DisabledAppSettings,
            actual = viewModel.applyAppSettingsResult.value,
        )
    }

    @Test
    fun revertAppSettingsResult_isSuccess_whenRevertAppSettings() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.revertAppSettingsResult.collect()
        }

        val appSettings = List(5) { index ->
            AppSetting(
                id = index,
                enabled = true,
                settingType = SettingType.SYSTEM,
                packageName = packageName,
                label = "Geto",
                key = "Geto $index",
                valueOnLaunch = "0",
                valueOnRevert = "1",
            )
        }

        secureSettingsRepository.setWriteSecureSettings(true)

        appSettingsRepository.setAppSettings(appSettings)

        viewModel.revertAppSettings()

        assertEquals(
            expected = AppSettingsResult.Success,
            actual = viewModel.revertAppSettingsResult.value,
        )
    }

    @Test
    fun revertAppSettingsResult_isSecurityException_whenRevertAppSettings() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.revertAppSettingsResult.collect()
        }

        val appSettings = List(5) { index ->
            AppSetting(
                id = index,
                enabled = true,
                settingType = SettingType.SYSTEM,
                packageName = packageName,
                label = "Geto",
                key = "Geto $index",
                valueOnLaunch = "0",
                valueOnRevert = "1",
            )
        }

        secureSettingsRepository.setWriteSecureSettings(false)

        appSettingsRepository.setAppSettings(appSettings)

        viewModel.revertAppSettings()

        assertEquals(
            expected = AppSettingsResult.SecurityException,
            actual = viewModel.revertAppSettingsResult.value,
        )
    }

    @Test
    fun revertAppSettingsResultI_isIllegalArgumentException_whenRevertAppSettings() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.revertAppSettingsResult.collect()
        }

        val appSettings = List(5) { index ->
            AppSetting(
                id = index,
                enabled = true,
                settingType = SettingType.SYSTEM,
                packageName = packageName,
                label = "Geto",
                key = "Geto $index",
                valueOnLaunch = "0",
                valueOnRevert = "1",
            )
        }

        appSettingsRepository.setAppSettings(appSettings)

        secureSettingsRepository.setWriteSecureSettings(true)

        secureSettingsRepository.setInvalidValues(true)

        viewModel.revertAppSettings()

        assertEquals(
            expected = AppSettingsResult.IllegalArgumentException,
            actual = viewModel.revertAppSettingsResult.value,
        )
    }

    @Test
    fun revertAppSettingsResult_isEmptyAppSettings_whenRevertAppSettings() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.revertAppSettingsResult.collect()
        }

        appSettingsRepository.setAppSettings(emptyList())

        viewModel.revertAppSettings()

        assertEquals(
            expected = AppSettingsResult.EmptyAppSettings,
            actual = viewModel.revertAppSettingsResult.value,
        )
    }

    @Test
    fun revertAppSettingsResult_isDisabledAppSettings_whenRevertAppSettings() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.revertAppSettingsResult.collect()
        }

        val appSettings = List(5) { index ->
            AppSetting(
                id = index,
                enabled = false,
                settingType = SettingType.SYSTEM,
                packageName = packageName,
                label = "Geto",
                key = "Geto $index",
                valueOnLaunch = "0",
                valueOnRevert = "1",
            )
        }

        appSettingsRepository.setAppSettings(appSettings)

        secureSettingsRepository.setWriteSecureSettings(true)

        viewModel.revertAppSettings()

        assertEquals(
            expected = AppSettingsResult.DisabledAppSettings,
            actual = viewModel.revertAppSettingsResult.value,
        )
    }

    @Test
    fun setPrimaryClipResult_isFalse_whenCopyPermissionCommand() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.revertAppSettingsResult.collect()
        }

        clipboardRepository.setSDKInt(33)

        viewModel.copyPermissionCommand()

        assertEquals(
            expected = false,
            actual = viewModel.setPrimaryClipResult.value,
        )
    }

    @Test
    fun setPrimaryClipResult_isTrue_whenCopyPermissionCommand() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.setPrimaryClipResult.collect()
        }

        clipboardRepository.setSDKInt(32)

        viewModel.copyPermissionCommand()

        assertTrue(viewModel.setPrimaryClipResult.value)
    }

    @Test
    fun secureSettings_isNotEmpty_whenGetSecureSettingsByName_ofSettingTypeSystem() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.secureSettings.collect()
        }

        val secureSettings = List(5) { index ->
            SecureSetting(
                settingType = SettingType.SYSTEM,
                id = index.toLong(),
                name = "SecureSetting",
                value = index.toString(),
            )
        }

        secureSettingsRepository.setSecureSettings(secureSettings)

        viewModel.getSecureSettingsByName(settingType = SettingType.SYSTEM, text = "SecureSetting")

        assertTrue(viewModel.secureSettings.value.isNotEmpty())
    }

    @Test
    fun secureSettings_isNotEmpty_whenGetSecureSettingsByName_ofSettingTypeSecure() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.secureSettings.collect()
        }

        val secureSettings = List(5) { index ->
            SecureSetting(
                settingType = SettingType.SECURE,
                id = index.toLong(),
                name = "SecureSetting",
                value = index.toString(),
            )
        }

        secureSettingsRepository.setSecureSettings(secureSettings)

        viewModel.getSecureSettingsByName(settingType = SettingType.SECURE, text = "SecureSetting")

        assertTrue(viewModel.secureSettings.value.isNotEmpty())
    }

    @Test
    fun secureSettings_isNotEmpty_whenGetSecureSettingsByName_ofSettingTypeGlobal() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.secureSettings.collect()
        }

        val secureSettings = List(5) { index ->
            SecureSetting(
                settingType = SettingType.GLOBAL,
                id = index.toLong(),
                name = "SecureSetting",
                value = index.toString(),
            )
        }

        secureSettingsRepository.setSecureSettings(secureSettings)

        viewModel.getSecureSettingsByName(settingType = SettingType.GLOBAL, text = "SecureSetting")

        assertTrue(viewModel.secureSettings.value.isNotEmpty())
    }

    @Test
    fun secureSettings_isEmpty_whenGetSecureSettingsByName_ofSettingTypeSystem() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.secureSettings.collect()
        }

        val secureSettings = List(5) { index ->
            SecureSetting(
                settingType = SettingType.SYSTEM,
                id = index.toLong(),
                name = "SecureSetting",
                value = index.toString(),
            )
        }

        secureSettingsRepository.setSecureSettings(secureSettings)

        viewModel.getSecureSettingsByName(settingType = SettingType.SYSTEM, text = "text")

        assertTrue(viewModel.secureSettings.value.isEmpty())
    }

    @Test
    fun secureSettings_isEmpty_whenGetSecureSettingsByName_ofSettingTypeSecure() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.secureSettings.collect()
        }

        val secureSettings = List(5) { index ->
            SecureSetting(
                settingType = SettingType.SECURE,
                id = index.toLong(),
                name = "SecureSetting",
                value = index.toString(),
            )
        }

        secureSettingsRepository.setSecureSettings(secureSettings)

        viewModel.getSecureSettingsByName(settingType = SettingType.SECURE, text = "text")

        assertTrue(viewModel.secureSettings.value.isEmpty())
    }

    @Test
    fun secureSettings_isEmpty_whenGetSecureSettingsByName_ofSettingTypeGlobal() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.secureSettings.collect()
        }

        val secureSettings = List(5) { index ->
            SecureSetting(
                settingType = SettingType.GLOBAL,
                id = index.toLong(),
                name = "SecureSetting",
                value = index.toString(),
            )
        }

        secureSettingsRepository.setSecureSettings(secureSettings)

        viewModel.getSecureSettingsByName(settingType = SettingType.GLOBAL, text = "text")

        assertTrue(viewModel.secureSettings.value.isEmpty())
    }

    @Test
    fun requestPinShortcutResult_isSupportedLauncher_whenRequestPinShortcut() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.requestPinShortcutResult.collect()
        }

        shortcutRepository.setRequestPinShortcutSupported(true)

        viewModel.requestPinShortcut(
            getoShortcutInfoCompat = GetoShortcutInfoCompat(
                id = "0",
                shortLabel = "shortLabel",
                longLabel = "longLabel",
            ),
        )

        assertEquals(
            expected = RequestPinShortcutResult.SupportedLauncher,
            actual = viewModel.requestPinShortcutResult.value,
        )
    }

    @Test
    fun requestPinShortcutResult_isUnsupportedLauncher_whenRequestPinShortcut() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.requestPinShortcutResult.collect()
        }

        shortcutRepository.setRequestPinShortcutSupported(false)

        viewModel.requestPinShortcut(
            getoShortcutInfoCompat = GetoShortcutInfoCompat(
                id = "0",
                shortLabel = "shortLabel",
                longLabel = "longLabel",
            ),
        )

        assertEquals(
            expected = RequestPinShortcutResult.UnsupportedLauncher,
            actual = viewModel.requestPinShortcutResult.value,
        )
    }

    @Test
    fun requestPinShortcutResult_isUpdateImmutableShortcuts_whenRequestPinShortcut() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.requestPinShortcutResult.collect()
        }

        val shortcuts = List(2) {
            GetoShortcutInfoCompat(
                id = "com.android.geto",
                shortLabel = "Geto",
                longLabel = "Geto",
            )
        }

        shortcutRepository.setRequestPinShortcutSupported(true)

        shortcutRepository.setUpdateImmutableShortcuts(true)

        shortcutRepository.setShortcuts(shortcuts)

        viewModel.requestPinShortcut(
            getoShortcutInfoCompat = GetoShortcutInfoCompat(
                id = "com.android.geto",
                shortLabel = "",
                longLabel = "",
            ),
        )

        assertEquals(
            expected = RequestPinShortcutResult.UpdateImmutableShortcuts,
            actual = viewModel.requestPinShortcutResult.value,
        )
    }

    @Test
    fun requestPinShortcutResult_isUpdateSuccess_whenRequestPinShortcut() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.requestPinShortcutResult.collect()
        }

        val shortcuts = List(2) {
            GetoShortcutInfoCompat(
                id = "com.android.geto",
                shortLabel = "Geto",
                longLabel = "Geto",
            )
        }

        shortcutRepository.setRequestPinShortcutSupported(true)

        shortcutRepository.setUpdateImmutableShortcuts(false)

        shortcutRepository.setShortcuts(shortcuts)

        viewModel.requestPinShortcut(
            getoShortcutInfoCompat = GetoShortcutInfoCompat(
                id = "com.android.geto",
                shortLabel = "",
                longLabel = "",
            ),
        )

        assertEquals(
            expected = RequestPinShortcutResult.UpdateSuccess,
            actual = viewModel.requestPinShortcutResult.value,
        )
    }

    @Test
    fun applicationIcon_isNotNull_whenGetApplicationIcon() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            val getoApplicationInfos = List(1) { _ ->
                GetoApplicationInfo(
                    flags = 0,
                    packageName = packageName,
                    label = appName,
                )
            }

            packageRepository.setApplicationInfos(getoApplicationInfos)

            viewModel.applicationIcon.collect()
        }

        assertNotNull(viewModel.applicationIcon.value)
    }

    @Test
    fun applicationIcon_isNull_whenGetApplicationIcon() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            val getoApplicationInfos = List(1) { _ ->
                GetoApplicationInfo(
                    flags = 0,
                    packageName = "",
                    label = appName,
                )
            }

            packageRepository.setApplicationInfos(getoApplicationInfos)

            viewModel.applicationIcon.collect()
        }

        assertNull(viewModel.applicationIcon.value)
    }
}
