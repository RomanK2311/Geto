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

import androidx.activity.ComponentActivity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.android.geto.core.domain.ApplyAppSettingsResult
import com.android.geto.core.domain.AutoLaunchResult
import com.android.geto.core.domain.RequestPinShortcutResult
import com.android.geto.core.domain.RevertAppSettingsResult
import com.android.geto.core.domain.UpdateRequestPinShortcutResult
import org.junit.Rule
import org.junit.Test

class AppSettingsScreenSnackbarTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun snackbar_isShown_whenApplyAppSettingsResult_isDisabledAppSettings() {
        composeTestRule.setContent {
            AppSettingsScreen(
                packageName = "com.android.geto",
                appName = "Geto",
                appSettingsUiState = AppSettingsUiState.Success(emptyList()),
                snackbarHostState = SnackbarHostState(),
                applicationIcon = null,
                mappedShortcutInfoCompat = null,
                secureSettings = emptyList(),
                permissionCommandText = "",
                applyAppSettingsResult = ApplyAppSettingsResult.DisabledAppSettings,
                revertAppSettingsResult = RevertAppSettingsResult.NoResult,
                autoLaunchResult = AutoLaunchResult.NoResult,
                requestPinShortcutResult = null,
                updateRequestPinShortcutResult = null,
                clipboardResult = false,
                onNavigationIconClick = {},
                onRevertAppSettings = {},
                onGetShortcut = {},
                onCheckAppSetting = { _, _ -> },
                onDeleteAppSetting = {},
                onLaunchApp = {},
                onAutoLaunchApp = {},
                onGetApplicationIcon = {},
                onResetApplyAppSettingsResult = {},
                onResetRevertAppSettingsResult = {},
                onResetAutoLaunchResult = {},
                onResetShortcutResult = {},
                onResetClipboardResult = {},
                onGetSecureSettingsByName = { _, _ -> },
                onAddAppSetting = {},
                onCopyPermissionCommand = {},
                onAddShortcut = {},
                onUpdateShortcut = {},
            )
        }

        composeTestRule.onNodeWithTag("appSettings:snackbar").assertExists()
    }

    @Test
    fun snackbar_isShown_whenApplyAppSettingsResult_isEmptyAppSettings() {
        composeTestRule.setContent {
            AppSettingsScreen(
                packageName = "com.android.geto",
                appName = "Geto",
                appSettingsUiState = AppSettingsUiState.Success(emptyList()),
                snackbarHostState = SnackbarHostState(),
                applicationIcon = null,
                mappedShortcutInfoCompat = null,
                secureSettings = emptyList(),
                permissionCommandText = "",
                applyAppSettingsResult = ApplyAppSettingsResult.EmptyAppSettings,
                revertAppSettingsResult = RevertAppSettingsResult.NoResult,
                autoLaunchResult = AutoLaunchResult.NoResult,
                requestPinShortcutResult = null,
                updateRequestPinShortcutResult = null,
                clipboardResult = false,
                onNavigationIconClick = {},
                onRevertAppSettings = {},
                onGetShortcut = {},
                onCheckAppSetting = { _, _ -> },
                onDeleteAppSetting = {},
                onLaunchApp = {},
                onAutoLaunchApp = {},
                onGetApplicationIcon = {},
                onResetApplyAppSettingsResult = {},
                onResetRevertAppSettingsResult = {},
                onResetAutoLaunchResult = {},
                onResetShortcutResult = {},
                onResetClipboardResult = {},
                onGetSecureSettingsByName = { _, _ -> },
                onAddAppSetting = {},
                onCopyPermissionCommand = {},
                onAddShortcut = {},
                onUpdateShortcut = {},
            )
        }

        composeTestRule.onNodeWithTag("appSettings:snackbar").assertExists()
    }

    @Test
    fun snackbar_isShown_whenApplyAppSettingsResult_isFailure() {
        composeTestRule.setContent {
            AppSettingsScreen(
                packageName = "com.android.geto",
                appName = "Geto",
                appSettingsUiState = AppSettingsUiState.Success(emptyList()),
                snackbarHostState = SnackbarHostState(),
                applicationIcon = null,
                mappedShortcutInfoCompat = null,
                secureSettings = emptyList(),
                permissionCommandText = "",
                applyAppSettingsResult = ApplyAppSettingsResult.Failure,
                revertAppSettingsResult = RevertAppSettingsResult.NoResult,
                autoLaunchResult = AutoLaunchResult.NoResult,
                requestPinShortcutResult = null,
                updateRequestPinShortcutResult = null,
                clipboardResult = false,
                onNavigationIconClick = {},
                onRevertAppSettings = {},
                onGetShortcut = {},
                onCheckAppSetting = { _, _ -> },
                onDeleteAppSetting = {},
                onLaunchApp = {},
                onAutoLaunchApp = {},
                onGetApplicationIcon = {},
                onResetApplyAppSettingsResult = {},
                onResetRevertAppSettingsResult = {},
                onResetAutoLaunchResult = {},
                onResetShortcutResult = {},
                onResetClipboardResult = {},
                onGetSecureSettingsByName = { _, _ -> },
                onAddAppSetting = {},
                onCopyPermissionCommand = {},
                onAddShortcut = {},
                onUpdateShortcut = {},
            )
        }

        composeTestRule.onNodeWithTag("appSettings:snackbar").assertExists()
    }

    @Test
    fun snackbar_isShown_whenApplyAppSettingsResult_isIllegalArgumentException() {
        composeTestRule.setContent {
            AppSettingsScreen(
                packageName = "com.android.geto",
                appName = "Geto",
                appSettingsUiState = AppSettingsUiState.Success(emptyList()),
                snackbarHostState = SnackbarHostState(),
                applicationIcon = null,
                mappedShortcutInfoCompat = null,
                secureSettings = emptyList(),
                permissionCommandText = "",
                applyAppSettingsResult = ApplyAppSettingsResult.IllegalArgumentException,
                revertAppSettingsResult = RevertAppSettingsResult.NoResult,
                autoLaunchResult = AutoLaunchResult.NoResult,
                requestPinShortcutResult = null,
                updateRequestPinShortcutResult = null,
                clipboardResult = false,
                onNavigationIconClick = {},
                onRevertAppSettings = {},
                onGetShortcut = {},
                onCheckAppSetting = { _, _ -> },
                onDeleteAppSetting = {},
                onLaunchApp = {},
                onAutoLaunchApp = {},
                onGetApplicationIcon = {},
                onResetApplyAppSettingsResult = {},
                onResetRevertAppSettingsResult = {},
                onResetAutoLaunchResult = {},
                onResetShortcutResult = {},
                onResetClipboardResult = {},
                onGetSecureSettingsByName = { _, _ -> },
                onAddAppSetting = {},
                onCopyPermissionCommand = {},
                onAddShortcut = {},
                onUpdateShortcut = {},
            )
        }

        composeTestRule.onNodeWithTag("appSettings:snackbar").assertExists()
    }

    @Test
    fun snackbar_isShown_whenRevertAppSettingsResult_isDisabledAppSettings() {
        composeTestRule.setContent {
            AppSettingsScreen(
                packageName = "com.android.geto",
                appName = "Geto",
                appSettingsUiState = AppSettingsUiState.Success(emptyList()),
                snackbarHostState = SnackbarHostState(),
                applicationIcon = null,
                mappedShortcutInfoCompat = null,
                secureSettings = emptyList(),
                permissionCommandText = "",
                applyAppSettingsResult = ApplyAppSettingsResult.NoResult,
                revertAppSettingsResult = RevertAppSettingsResult.DisabledAppSettings,
                autoLaunchResult = AutoLaunchResult.NoResult,
                requestPinShortcutResult = null,
                updateRequestPinShortcutResult = null,
                clipboardResult = false,
                onNavigationIconClick = {},
                onRevertAppSettings = {},
                onGetShortcut = {},
                onCheckAppSetting = { _, _ -> },
                onDeleteAppSetting = {},
                onLaunchApp = {},
                onAutoLaunchApp = {},
                onGetApplicationIcon = {},
                onResetApplyAppSettingsResult = {},
                onResetRevertAppSettingsResult = {},
                onResetAutoLaunchResult = {},
                onResetShortcutResult = {},
                onResetClipboardResult = {},
                onGetSecureSettingsByName = { _, _ -> },
                onAddAppSetting = {},
                onCopyPermissionCommand = {},
                onAddShortcut = {},
                onUpdateShortcut = {},
            )
        }

        composeTestRule.onNodeWithTag("appSettings:snackbar").assertExists()
    }

    @Test
    fun snackbar_isShown_whenRevertAppSettingsResult_isEmptyAppSettings() {
        composeTestRule.setContent {
            AppSettingsScreen(
                packageName = "com.android.geto",
                appName = "Geto",
                appSettingsUiState = AppSettingsUiState.Success(emptyList()),
                snackbarHostState = SnackbarHostState(),
                applicationIcon = null,
                mappedShortcutInfoCompat = null,
                secureSettings = emptyList(),
                permissionCommandText = "",
                applyAppSettingsResult = ApplyAppSettingsResult.NoResult,
                revertAppSettingsResult = RevertAppSettingsResult.EmptyAppSettings,
                autoLaunchResult = AutoLaunchResult.NoResult,
                requestPinShortcutResult = null,
                updateRequestPinShortcutResult = null,
                clipboardResult = false,
                onNavigationIconClick = {},
                onRevertAppSettings = {},
                onGetShortcut = {},
                onCheckAppSetting = { _, _ -> },
                onDeleteAppSetting = {},
                onLaunchApp = {},
                onAutoLaunchApp = {},
                onGetApplicationIcon = {},
                onResetApplyAppSettingsResult = {},
                onResetRevertAppSettingsResult = {},
                onResetAutoLaunchResult = {},
                onResetShortcutResult = {},
                onResetClipboardResult = {},
                onGetSecureSettingsByName = { _, _ -> },
                onAddAppSetting = {},
                onCopyPermissionCommand = {},
                onAddShortcut = {},
                onUpdateShortcut = {},
            )
        }

        composeTestRule.onNodeWithTag("appSettings:snackbar").assertExists()
    }

    @Test
    fun snackbar_isShown_whenRevertAppSettingsResult_isFailure() {
        composeTestRule.setContent {
            AppSettingsScreen(
                packageName = "com.android.geto",
                appName = "Geto",
                appSettingsUiState = AppSettingsUiState.Success(emptyList()),
                snackbarHostState = SnackbarHostState(),
                applicationIcon = null,
                mappedShortcutInfoCompat = null,
                secureSettings = emptyList(),
                permissionCommandText = "",
                applyAppSettingsResult = ApplyAppSettingsResult.NoResult,
                revertAppSettingsResult = RevertAppSettingsResult.Failure,
                autoLaunchResult = AutoLaunchResult.NoResult,
                requestPinShortcutResult = null,
                updateRequestPinShortcutResult = null,
                clipboardResult = false,
                onNavigationIconClick = {},
                onRevertAppSettings = {},
                onGetShortcut = {},
                onCheckAppSetting = { _, _ -> },
                onDeleteAppSetting = {},
                onLaunchApp = {},
                onAutoLaunchApp = {},
                onGetApplicationIcon = {},
                onResetApplyAppSettingsResult = {},
                onResetRevertAppSettingsResult = {},
                onResetAutoLaunchResult = {},
                onResetShortcutResult = {},
                onResetClipboardResult = {},
                onGetSecureSettingsByName = { _, _ -> },
                onAddAppSetting = {},
                onCopyPermissionCommand = {},
                onAddShortcut = {},
                onUpdateShortcut = {},
            )
        }

        composeTestRule.onNodeWithTag("appSettings:snackbar").assertExists()
    }

    @Test
    fun snackbar_isShown_whenRevertAppSettingsResult_isIllegalArgumentException() {
        composeTestRule.setContent {
            AppSettingsScreen(
                packageName = "com.android.geto",
                appName = "Geto",
                appSettingsUiState = AppSettingsUiState.Success(emptyList()),
                snackbarHostState = SnackbarHostState(),
                applicationIcon = null,
                mappedShortcutInfoCompat = null,
                secureSettings = emptyList(),
                permissionCommandText = "",
                applyAppSettingsResult = ApplyAppSettingsResult.NoResult,
                revertAppSettingsResult = RevertAppSettingsResult.IllegalArgumentException,
                autoLaunchResult = AutoLaunchResult.NoResult,
                requestPinShortcutResult = null,
                updateRequestPinShortcutResult = null,
                clipboardResult = false,
                onNavigationIconClick = {},
                onRevertAppSettings = {},
                onGetShortcut = {},
                onCheckAppSetting = { _, _ -> },
                onDeleteAppSetting = {},
                onLaunchApp = {},
                onAutoLaunchApp = {},
                onGetApplicationIcon = {},
                onResetApplyAppSettingsResult = {},
                onResetRevertAppSettingsResult = {},
                onResetAutoLaunchResult = {},
                onResetShortcutResult = {},
                onResetClipboardResult = {},
                onGetSecureSettingsByName = { _, _ -> },
                onAddAppSetting = {},
                onCopyPermissionCommand = {},
                onAddShortcut = {},
                onUpdateShortcut = {},
            )
        }

        composeTestRule.onNodeWithTag("appSettings:snackbar").assertExists()
    }

    @Test
    fun snackbar_isShown_whenRequestPinShortcutResult_isSupportedLauncher() {
        composeTestRule.setContent {
            AppSettingsScreen(
                packageName = "com.android.geto",
                appName = "Geto",
                appSettingsUiState = AppSettingsUiState.Success(emptyList()),
                snackbarHostState = SnackbarHostState(),
                applicationIcon = null,
                mappedShortcutInfoCompat = null,
                secureSettings = emptyList(),
                permissionCommandText = "",
                applyAppSettingsResult = ApplyAppSettingsResult.NoResult,
                revertAppSettingsResult = RevertAppSettingsResult.NoResult,
                autoLaunchResult = AutoLaunchResult.NoResult,
                requestPinShortcutResult = RequestPinShortcutResult.SupportedLauncher,
                updateRequestPinShortcutResult = null,
                clipboardResult = false,
                onNavigationIconClick = {},
                onRevertAppSettings = {},
                onGetShortcut = {},
                onCheckAppSetting = { _, _ -> },
                onDeleteAppSetting = {},
                onLaunchApp = {},
                onAutoLaunchApp = {},
                onGetApplicationIcon = {},
                onResetApplyAppSettingsResult = {},
                onResetRevertAppSettingsResult = {},
                onResetAutoLaunchResult = {},
                onResetShortcutResult = {},
                onResetClipboardResult = {},
                onGetSecureSettingsByName = { _, _ -> },
                onAddAppSetting = {},
                onCopyPermissionCommand = {},
                onAddShortcut = {},
                onUpdateShortcut = {},
            )
        }

        composeTestRule.onNodeWithTag("appSettings:snackbar").assertExists()
    }

    @Test
    fun snackbar_isShown_whenRequestPinShortcutResult_isUnSupportedLauncher() {
        composeTestRule.setContent {
            AppSettingsScreen(
                packageName = "com.android.geto",
                appName = "Geto",
                appSettingsUiState = AppSettingsUiState.Success(emptyList()),
                snackbarHostState = SnackbarHostState(),
                applicationIcon = null,
                mappedShortcutInfoCompat = null,
                secureSettings = emptyList(),
                permissionCommandText = "",
                applyAppSettingsResult = ApplyAppSettingsResult.NoResult,
                revertAppSettingsResult = RevertAppSettingsResult.NoResult,
                autoLaunchResult = AutoLaunchResult.NoResult,
                requestPinShortcutResult = RequestPinShortcutResult.UnSupportedLauncher,
                updateRequestPinShortcutResult = null,
                clipboardResult = false,
                onNavigationIconClick = {},
                onRevertAppSettings = {},
                onGetShortcut = {},
                onCheckAppSetting = { _, _ -> },
                onDeleteAppSetting = {},
                onLaunchApp = {},
                onAutoLaunchApp = {},
                onGetApplicationIcon = {},
                onResetApplyAppSettingsResult = {},
                onResetRevertAppSettingsResult = {},
                onResetAutoLaunchResult = {},
                onResetShortcutResult = {},
                onResetClipboardResult = {},
                onGetSecureSettingsByName = { _, _ -> },
                onAddAppSetting = {},
                onCopyPermissionCommand = {},
                onAddShortcut = {},
                onUpdateShortcut = {},
            )
        }

        composeTestRule.onNodeWithTag("appSettings:snackbar").assertExists()
    }

    @Test
    fun snackbar_isShown_whenUpdateRequestPinShortcutResult_isUnSupportedLauncher() {
        composeTestRule.setContent {
            AppSettingsScreen(
                packageName = "com.android.geto",
                appName = "Geto",
                appSettingsUiState = AppSettingsUiState.Success(emptyList()),
                snackbarHostState = SnackbarHostState(),
                applicationIcon = null,
                mappedShortcutInfoCompat = null,
                secureSettings = emptyList(),
                permissionCommandText = "",
                applyAppSettingsResult = ApplyAppSettingsResult.NoResult,
                revertAppSettingsResult = RevertAppSettingsResult.NoResult,
                autoLaunchResult = AutoLaunchResult.NoResult,
                requestPinShortcutResult = null,
                updateRequestPinShortcutResult = UpdateRequestPinShortcutResult.Failed,
                clipboardResult = false,
                onNavigationIconClick = {},
                onRevertAppSettings = {},
                onGetShortcut = {},
                onCheckAppSetting = { _, _ -> },
                onDeleteAppSetting = {},
                onLaunchApp = {},
                onAutoLaunchApp = {},
                onGetApplicationIcon = {},
                onResetApplyAppSettingsResult = {},
                onResetRevertAppSettingsResult = {},
                onResetAutoLaunchResult = {},
                onResetShortcutResult = {},
                onResetClipboardResult = {},
                onGetSecureSettingsByName = { _, _ -> },
                onAddAppSetting = {},
                onCopyPermissionCommand = {},
                onAddShortcut = {},
                onUpdateShortcut = {},
            )
        }

        composeTestRule.onNodeWithTag("appSettings:snackbar").assertExists()
    }

    @Test
    fun snackbar_isShown_whenUpdateRequestPinShortcutResult_isIDNotFound() {
        composeTestRule.setContent {
            AppSettingsScreen(
                packageName = "com.android.geto",
                appName = "Geto",
                appSettingsUiState = AppSettingsUiState.Success(emptyList()),
                snackbarHostState = SnackbarHostState(),
                applicationIcon = null,
                mappedShortcutInfoCompat = null,
                secureSettings = emptyList(),
                permissionCommandText = "",
                applyAppSettingsResult = ApplyAppSettingsResult.NoResult,
                revertAppSettingsResult = RevertAppSettingsResult.NoResult,
                autoLaunchResult = AutoLaunchResult.NoResult,
                requestPinShortcutResult = null,
                updateRequestPinShortcutResult = UpdateRequestPinShortcutResult.IDNotFound,
                clipboardResult = false,
                onNavigationIconClick = {},
                onRevertAppSettings = {},
                onGetShortcut = {},
                onCheckAppSetting = { _, _ -> },
                onDeleteAppSetting = {},
                onLaunchApp = {},
                onAutoLaunchApp = {},
                onGetApplicationIcon = {},
                onResetApplyAppSettingsResult = {},
                onResetRevertAppSettingsResult = {},
                onResetAutoLaunchResult = {},
                onResetShortcutResult = {},
                onResetClipboardResult = {},
                onGetSecureSettingsByName = { _, _ -> },
                onAddAppSetting = {},
                onCopyPermissionCommand = {},
                onAddShortcut = {},
                onUpdateShortcut = {},
            )
        }

        composeTestRule.onNodeWithTag("appSettings:snackbar").assertExists()
    }

    @Test
    fun snackbar_isShown_whenUpdateRequestPinShortcutResult_isSuccess() {
        composeTestRule.setContent {
            AppSettingsScreen(
                packageName = "com.android.geto",
                appName = "Geto",
                appSettingsUiState = AppSettingsUiState.Success(emptyList()),
                snackbarHostState = SnackbarHostState(),
                applicationIcon = null,
                mappedShortcutInfoCompat = null,
                secureSettings = emptyList(),
                permissionCommandText = "",
                applyAppSettingsResult = ApplyAppSettingsResult.NoResult,
                revertAppSettingsResult = RevertAppSettingsResult.NoResult,
                autoLaunchResult = AutoLaunchResult.NoResult,
                requestPinShortcutResult = null,
                updateRequestPinShortcutResult = UpdateRequestPinShortcutResult.Success,
                clipboardResult = false,
                onNavigationIconClick = {},
                onRevertAppSettings = {},
                onGetShortcut = {},
                onCheckAppSetting = { _, _ -> },
                onDeleteAppSetting = {},
                onLaunchApp = {},
                onAutoLaunchApp = {},
                onGetApplicationIcon = {},
                onResetApplyAppSettingsResult = {},
                onResetRevertAppSettingsResult = {},
                onResetAutoLaunchResult = {},
                onResetShortcutResult = {},
                onResetClipboardResult = {},
                onGetSecureSettingsByName = { _, _ -> },
                onAddAppSetting = {},
                onCopyPermissionCommand = {},
                onAddShortcut = {},
                onUpdateShortcut = {},
            )
        }

        composeTestRule.onNodeWithTag("appSettings:snackbar").assertExists()
    }

    @Test
    fun snackbar_isShown_whenUpdateRequestPinShortcutResult_isUpdateImmutableShortcuts() {
        composeTestRule.setContent {
            AppSettingsScreen(
                packageName = "com.android.geto",
                appName = "Geto",
                appSettingsUiState = AppSettingsUiState.Success(emptyList()),
                snackbarHostState = SnackbarHostState(),
                applicationIcon = null,
                mappedShortcutInfoCompat = null,
                secureSettings = emptyList(),
                permissionCommandText = "",
                applyAppSettingsResult = ApplyAppSettingsResult.NoResult,
                revertAppSettingsResult = RevertAppSettingsResult.NoResult,
                autoLaunchResult = AutoLaunchResult.NoResult,
                requestPinShortcutResult = null,
                updateRequestPinShortcutResult = UpdateRequestPinShortcutResult.UpdateImmutableShortcuts,
                clipboardResult = false,
                onNavigationIconClick = {},
                onRevertAppSettings = {},
                onGetShortcut = {},
                onCheckAppSetting = { _, _ -> },
                onDeleteAppSetting = {},
                onLaunchApp = {},
                onAutoLaunchApp = {},
                onGetApplicationIcon = {},
                onResetApplyAppSettingsResult = {},
                onResetRevertAppSettingsResult = {},
                onResetAutoLaunchResult = {},
                onResetShortcutResult = {},
                onResetClipboardResult = {},
                onGetSecureSettingsByName = { _, _ -> },
                onAddAppSetting = {},
                onCopyPermissionCommand = {},
                onAddShortcut = {},
                onUpdateShortcut = {},
            )
        }

        composeTestRule.onNodeWithTag("appSettings:snackbar").assertExists()
    }

    @Test
    fun snackbar_isShown_whenClipboardResult_isTrue() {
        composeTestRule.setContent {
            AppSettingsScreen(
                packageName = "com.android.geto",
                appName = "Geto",
                appSettingsUiState = AppSettingsUiState.Success(emptyList()),
                snackbarHostState = SnackbarHostState(),
                applicationIcon = null,
                mappedShortcutInfoCompat = null,
                secureSettings = emptyList(),
                permissionCommandText = "permission command text",
                applyAppSettingsResult = ApplyAppSettingsResult.NoResult,
                revertAppSettingsResult = RevertAppSettingsResult.NoResult,
                autoLaunchResult = AutoLaunchResult.NoResult,
                requestPinShortcutResult = null,
                updateRequestPinShortcutResult = null,
                clipboardResult = true,
                onNavigationIconClick = {},
                onRevertAppSettings = {},
                onGetShortcut = {},
                onCheckAppSetting = { _, _ -> },
                onDeleteAppSetting = {},
                onLaunchApp = {},
                onAutoLaunchApp = {},
                onGetApplicationIcon = {},
                onResetApplyAppSettingsResult = {},
                onResetRevertAppSettingsResult = {},
                onResetAutoLaunchResult = {},
                onResetShortcutResult = {},
                onResetClipboardResult = {},
                onGetSecureSettingsByName = { _, _ -> },
                onAddAppSetting = {},
                onCopyPermissionCommand = {},
                onAddShortcut = {},
                onUpdateShortcut = {},
            )
        }

        composeTestRule.onNodeWithTag("appSettings:snackbar").assertExists()
    }
}