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
import com.android.geto.core.data.repository.ClipboardResult
import com.android.geto.core.data.repository.ShortcutResult
import com.android.geto.core.domain.AppSettingsResult
import com.android.geto.core.domain.ApplyAppSettingsUseCase
import com.android.geto.core.domain.RevertAppSettingsUseCase
import com.android.geto.core.model.AppSettings
import com.android.geto.core.model.SecureSettings
import com.android.geto.core.model.SettingsType
import com.android.geto.core.model.TargetApplicationInfo
import com.android.geto.core.model.TargetShortcutInfoCompat
import com.android.geto.core.testing.repository.TestAppSettingsRepository
import com.android.geto.core.testing.repository.TestClipboardRepository
import com.android.geto.core.testing.repository.TestPackageRepository
import com.android.geto.core.testing.repository.TestSecureSettingsRepository
import com.android.geto.core.testing.repository.TestShortcutRepository
import com.android.geto.core.testing.util.MainDispatcherRule
import com.android.geto.feature.appsettings.navigation.APP_NAME_ARG
import com.android.geto.feature.appsettings.navigation.PACKAGE_NAME_ARG
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AppSettingsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var packageRepository: TestPackageRepository

    private lateinit var appSettingsRepository: TestAppSettingsRepository

    private lateinit var secureSettingsRepository: TestSecureSettingsRepository

    private lateinit var clipboardRepository: TestClipboardRepository

    private lateinit var shortcutRepository: TestShortcutRepository

    private val savedStateHandle = SavedStateHandle()

    private lateinit var viewModel: AppSettingsViewModel

    private val packageNameTest = "packageNameTest"

    private val appNameTest = "appNameTest"

    @Before
    fun setup() {
        appSettingsRepository = TestAppSettingsRepository()

        secureSettingsRepository = TestSecureSettingsRepository()

        clipboardRepository = TestClipboardRepository()

        packageRepository = TestPackageRepository()

        shortcutRepository = TestShortcutRepository()

        savedStateHandle[PACKAGE_NAME_ARG] = packageNameTest

        savedStateHandle[APP_NAME_ARG] = appNameTest

        viewModel = AppSettingsViewModel(
            savedStateHandle = savedStateHandle,
            appSettingsRepository = appSettingsRepository,
            clipboardRepository = clipboardRepository,
            secureSettingsRepository = secureSettingsRepository,
            shortcutRepository = shortcutRepository,
            packageRepository = packageRepository,
            applyAppSettingsUseCase = ApplyAppSettingsUseCase(
                appSettingsRepository = appSettingsRepository,
                secureSettingsRepository = secureSettingsRepository
            ),
            revertAppSettingsUseCase = RevertAppSettingsUseCase(
                appSettingsRepository = appSettingsRepository,
                secureSettingsRepository = secureSettingsRepository
            )
        )
    }

    @Test
    fun appSettingsUiStateIsLoading_whenStarted() = runTest {
        assertIs<AppSettingsUiState.Loading>(viewModel.appSettingsUiState.value)
    }

    @Test
    fun appSettingsUiStateIsSuccess_whenDataIsNotEmpty() = runTest {
        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.appSettingsUiState.collect() }

        appSettingsRepository.setAppSettings(
            listOf(
                AppSettings(
                    id = 0,
                    enabled = true,
                    settingsType = SettingsType.SYSTEM,
                    packageName = packageNameTest,
                    label = "system",
                    key = "key",
                    valueOnLaunch = "test",
                    valueOnRevert = "test"
                )
            )
        )

        val item = viewModel.appSettingsUiState.value

        assertIs<AppSettingsUiState.Success>(item)

        collectJob.cancel()
    }

    @Test
    fun applyAppSettingsResultIsSuccess_whenLaunchApp() = runTest {
        packageRepository.setNonSystemApps(
            listOf(
                TargetApplicationInfo(
                    flags = 0, packageName = packageNameTest, label = "label"
                )
            )
        )

        appSettingsRepository.setAppSettings(
            listOf(
                AppSettings(
                    id = 0, enabled = true,
                    settingsType = SettingsType.SYSTEM,
                    packageName = packageNameTest,
                    label = "system",
                    key = "key",
                    valueOnLaunch = "test",
                    valueOnRevert = "test"
                )
            )
        )

        secureSettingsRepository.setWriteSecureSettings(true)

        viewModel.launchApp()

        assertIs<AppSettingsResult.Success>(viewModel.applyAppSettingsResult.value)

    }

    @Test
    fun applyAppSettingsResultIsSecurityException_whenLaunchApp() = runTest {
        packageRepository.setNonSystemApps(
            listOf(
                TargetApplicationInfo(
                    flags = 0, packageName = packageNameTest, label = "label"
                )
            )
        )

        appSettingsRepository.setAppSettings(
            listOf(
                AppSettings(
                    id = 0,
                    enabled = true,
                    settingsType = SettingsType.SYSTEM,
                    packageName = packageNameTest,
                    label = "system",
                    key = "key",
                    valueOnLaunch = "test",
                    valueOnRevert = "test"
                )
            )
        )

        secureSettingsRepository.setWriteSecureSettings(false)

        viewModel.launchApp()

        assertIs<AppSettingsResult.SecurityException>(viewModel.applyAppSettingsResult.value)

    }

    @Test
    fun applyAppSettingsResultIsEmptyAppSettingsList_whenLaunchApp() = runTest {
        packageRepository.setNonSystemApps(
            listOf(
                TargetApplicationInfo(
                    flags = 0, packageName = packageNameTest, label = "label"
                )
            )
        )

        appSettingsRepository.setAppSettings(
            listOf(
                AppSettings(
                    id = 0, enabled = false,
                    settingsType = SettingsType.SYSTEM,
                    packageName = packageNameTest,
                    label = "system",
                    key = "key",
                    valueOnLaunch = "test",
                    valueOnRevert = "test"
                )
            )
        )

        secureSettingsRepository.setWriteSecureSettings(false)

        viewModel.launchApp()

        assertIs<AppSettingsResult.AppSettingsDisabled>(viewModel.applyAppSettingsResult.value)

    }

    @Test
    fun applyAppSettingsResultIsAppSettingsDisabled_whenLaunchApp() = runTest {
        packageRepository.setNonSystemApps(
            listOf(
                TargetApplicationInfo(
                    flags = 0, packageName = packageNameTest, label = "label"
                )
            )
        )

        appSettingsRepository.setAppSettings(emptyList())

        secureSettingsRepository.setWriteSecureSettings(false)

        viewModel.launchApp()

        assertIs<AppSettingsResult.EmptyAppSettingsList>(viewModel.applyAppSettingsResult.value)

    }

    @Test
    fun revertAppSettingsResultIsSuccess_whenRevertSettings() = runTest {
        packageRepository.setNonSystemApps(
            listOf(
                TargetApplicationInfo(
                    flags = 0, packageName = packageNameTest, label = "label"
                )
            )
        )

        appSettingsRepository.setAppSettings(
            listOf(
                AppSettings(
                    id = 0,
                    enabled = true,
                    settingsType = SettingsType.SYSTEM,
                    packageName = packageNameTest,
                    label = "system",
                    key = "key",
                    valueOnLaunch = "test",
                    valueOnRevert = "test"
                )
            )
        )

        secureSettingsRepository.setWriteSecureSettings(true)

        viewModel.revertSettings()

        assertIs<AppSettingsResult.Success>(viewModel.revertAppSettingsResult.value)

    }

    @Test
    fun revertAppSettingsResultIsSecurityException_whenRevertSettings() = runTest {
        packageRepository.setNonSystemApps(
            listOf(
                TargetApplicationInfo(
                    flags = 0, packageName = packageNameTest, label = "label"
                )
            )
        )

        appSettingsRepository.setAppSettings(
            listOf(
                AppSettings(
                    id = 0, enabled = true,
                    settingsType = SettingsType.SYSTEM,
                    packageName = packageNameTest,
                    label = "system",
                    key = "key",
                    valueOnLaunch = "test",
                    valueOnRevert = "test"
                )
            )
        )

        secureSettingsRepository.setWriteSecureSettings(false)

        viewModel.revertSettings()

        assertIs<AppSettingsResult.SecurityException>(viewModel.revertAppSettingsResult.value)

    }

    @Test
    fun revertAppSettingsResultIsEmptyAppSettingsList_whenRevertSettings() = runTest {
        packageRepository.setNonSystemApps(
            listOf(
                TargetApplicationInfo(
                    flags = 0, packageName = packageNameTest, label = "label"
                )
            )
        )

        appSettingsRepository.setAppSettings(
            listOf(
                AppSettings(
                    id = 0, enabled = false,
                    settingsType = SettingsType.SYSTEM,
                    packageName = packageNameTest,
                    label = "system",
                    key = "key",
                    valueOnLaunch = "test",
                    valueOnRevert = "test"
                )
            )
        )

        secureSettingsRepository.setWriteSecureSettings(false)

        viewModel.revertSettings()

        assertIs<AppSettingsResult.AppSettingsDisabled>(viewModel.revertAppSettingsResult.value)

    }

    @Test
    fun revertAppSettingsResultIsAppSettingsDisabled_whenRevertSettings() = runTest {
        packageRepository.setNonSystemApps(
            listOf(
                TargetApplicationInfo(
                    flags = 0, packageName = packageNameTest, label = "label"
                )
            )
        )

        appSettingsRepository.setAppSettings(emptyList())

        secureSettingsRepository.setWriteSecureSettings(false)

        viewModel.revertSettings()

        assertIs<AppSettingsResult.EmptyAppSettingsList>(viewModel.revertAppSettingsResult.value)

    }

    @Test
    fun clipboardResultIsNotify_whenCopyPermissionCommand() = runTest {

        clipboardRepository.setApi32(false)

        viewModel.copyPermissionCommand()

        assertIs<ClipboardResult.Notify>(viewModel.clipboardResult.value)

    }

    @Test
    fun clipboardResultIsHideNotify_whenCopyPermissionCommand() = runTest {

        clipboardRepository.setApi32(true)

        viewModel.copyPermissionCommand()

        assertIs<ClipboardResult.HideNotify>(viewModel.clipboardResult.value)

    }

    @Test
    fun applicationIconIsNotNull_whenGetApplicationIcon() = runTest {
        packageRepository.setNonSystemApps(
            listOf(
                TargetApplicationInfo(
                    flags = 0, packageName = packageNameTest, label = "label"
                )
            )
        )

        appSettingsRepository.setAppSettings(
            listOf(
                AppSettings(
                    id = 0,
                    enabled = true,
                    settingsType = SettingsType.SYSTEM,
                    packageName = packageNameTest,
                    label = "system",
                    key = "key",
                    valueOnLaunch = "test",
                    valueOnRevert = "test"
                )
            )
        )

        secureSettingsRepository.setWriteSecureSettings(true)

        viewModel.getApplicationIcon()

        assertNotNull(viewModel.icon.value)
    }

    @Test
    fun secureSettingsIsNotEmpty_whenGetSecureSettings() = runTest {
        secureSettingsRepository.setSecureSettings(testSecureSettingsList)

        viewModel.getSecureSettings(text = "name0", settingsType = SettingsType.GLOBAL)

        val item = viewModel.secureSettings.value

        assertTrue(item.isNotEmpty())
    }

    @Test
    fun secureSettingsIsEmpty_whenGetSecureSettings() = runTest {
        secureSettingsRepository.setSecureSettings(testSecureSettingsList)

        viewModel.getSecureSettings(text = "nameNotFound", settingsType = SettingsType.GLOBAL)

        val item = viewModel.secureSettings.value

        assertTrue(item.isEmpty())
    }

    @Test
    fun shortcutIsNull_whenGetShortcut() = runTest {
        shortcutRepository.setShortcuts(testShortcutsLists)

        viewModel.getShortcut("idNotFound")

        val item = viewModel.shortcut.value

        assertNull(item)
    }

    @Test
    fun shortcutIsNotNull_whenGetShortcut() = runTest {
        shortcutRepository.setShortcuts(testShortcutsLists)

        viewModel.getShortcut("id0")

        val item = viewModel.shortcut.value

        assertNotNull(item)
    }

    @Test
    fun shortcutResultIsSupportedLauncher_whenRequestPinShortcut() = runTest {
        shortcutRepository.setRequestPinShortcutSupported(true)

        viewModel.requestPinShortcut(
            targetShortcutInfoCompat = TargetShortcutInfoCompat(
                id = "0", shortLabel = "shortLabel", longLabel = "longLabel"
            )
        )

        assertIs<ShortcutResult.SupportedLauncher>(viewModel.shortcutResult.value)
    }

    @Test
    fun shortcutResultIsUnSupportedLauncher_whenRequestPinShortcut() = runTest {
        shortcutRepository.setRequestPinShortcutSupported(false)

        viewModel.requestPinShortcut(
            targetShortcutInfoCompat = TargetShortcutInfoCompat(
                id = "0", shortLabel = "shortLabel", longLabel = "longLabel"
            )
        )

        assertIs<ShortcutResult.UnsupportedLauncher>(viewModel.shortcutResult.value)
    }

    @Test
    fun shortcutResultIsShortcutUpdateImmutableShortcuts_whenUpdateRequestPinShortcut() = runTest {
        shortcutRepository.setUpdateImmutableShortcuts(true)

        viewModel.updateRequestPinShortcut(
            targetShortcutInfoCompat = TargetShortcutInfoCompat(
                id = "0", shortLabel = "shortLabel", longLabel = "longLabel"
            )
        )

        assertIs<ShortcutResult.ShortcutUpdateImmutableShortcuts>(viewModel.shortcutResult.value)
    }

    @Test
    fun shortcutResultIsShortcutUpdateSuccess_whenUpdateRequestPinShortcut() = runTest {
        shortcutRepository.setUpdateImmutableShortcuts(false)

        viewModel.updateRequestPinShortcut(
            targetShortcutInfoCompat = TargetShortcutInfoCompat(
                id = "0", shortLabel = "shortLabel", longLabel = "longLabel"
            )
        )

        assertIs<ShortcutResult.ShortcutUpdateSuccess>(viewModel.shortcutResult.value)
    }
}

private val testSecureSettingsList = listOf(
    SecureSettings(id = 0L, name = "name0", value = "value0"),
    SecureSettings(id = 1L, name = "name1", value = "value1")
)

private val testShortcutsLists = listOf(
    TargetShortcutInfoCompat(id = "id0"),
    TargetShortcutInfoCompat(id = "id1"),
)