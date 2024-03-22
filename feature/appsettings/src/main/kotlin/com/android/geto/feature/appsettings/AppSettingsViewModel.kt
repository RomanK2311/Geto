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

import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.geto.core.data.repository.AppSettingsRepository
import com.android.geto.core.data.repository.ClipboardRepository
import com.android.geto.core.data.repository.ClipboardResult
import com.android.geto.core.data.repository.PackageRepository
import com.android.geto.core.data.repository.SecureSettingsRepository
import com.android.geto.core.data.repository.ShortcutRepository
import com.android.geto.core.data.repository.ShortcutResult
import com.android.geto.core.domain.AppSettingsResult
import com.android.geto.core.domain.ApplyAppSettingsUseCase
import com.android.geto.core.domain.AutoLaunchUseCase
import com.android.geto.core.domain.RevertAppSettingsUseCase
import com.android.geto.core.model.AppSettings
import com.android.geto.core.model.SecureSettings
import com.android.geto.core.model.SettingsType
import com.android.geto.core.model.TargetShortcutInfoCompat
import com.android.geto.feature.appsettings.navigation.AppSettingsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppSettingsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val appSettingsRepository: AppSettingsRepository,
    private val clipboardRepository: ClipboardRepository,
    private val packageRepository: PackageRepository,
    private val secureSettingsRepository: SecureSettingsRepository,
    private val shortcutRepository: ShortcutRepository,
    private val applyAppSettingsUseCase: ApplyAppSettingsUseCase,
    private val revertAppSettingsUseCase: RevertAppSettingsUseCase,
    private val autoLaunchUseCase: AutoLaunchUseCase
) : ViewModel() {
    private var _secureSettings = MutableStateFlow<List<SecureSettings>>(emptyList())
    val secureSettings = _secureSettings.asStateFlow()

    private val _applyAppSettingsResult = MutableStateFlow<AppSettingsResult?>(null)
    val applyAppSettingsResult = _applyAppSettingsResult.asStateFlow()

    private val _revertAppSettingsResult = MutableStateFlow<AppSettingsResult?>(null)
    val revertAppSettingsResult = _revertAppSettingsResult.asStateFlow()

    private val _shortcutResult = MutableStateFlow<ShortcutResult?>(null)
    val shortcutResult = _shortcutResult.asStateFlow()

    private val _clipboardResult = MutableStateFlow<ClipboardResult?>(null)
    val clipboardResult = _clipboardResult.asStateFlow()

    private val _applicationIcon = MutableStateFlow<Drawable?>(null)
    val applicationIcon = _applicationIcon.asStateFlow()

    private val appSettingsArgs: AppSettingsArgs = AppSettingsArgs(savedStateHandle)

    val packageName = appSettingsArgs.packageName

    val appName = appSettingsArgs.appName

    val appSettingsUiState: StateFlow<AppSettingsUiState> =
        appSettingsRepository.getAppSettingsList(packageName)
            .map<List<AppSettings>, AppSettingsUiState>(AppSettingsUiState::Success)
            .onStart { emit(AppSettingsUiState.Loading) }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = AppSettingsUiState.Loading
            )

    fun applySettings() {
        viewModelScope.launch {
            _applyAppSettingsResult.update { applyAppSettingsUseCase(packageName = packageName) }
        }
    }

    fun autoLaunchApp() {
        viewModelScope.launch {
            _applyAppSettingsResult.update { autoLaunchUseCase(packageName = packageName) }
        }
    }

    fun appSettingsItemCheckBoxChange(checked: Boolean, appSettings: AppSettings) {
        viewModelScope.launch {
            val updatedUserAppSettingsItem = appSettings.copy(enabled = checked)

            appSettingsRepository.upsertAppSettings(
                updatedUserAppSettingsItem
            )
        }
    }

    fun deleteAppSettingsItem(appSettings: AppSettings) {
        viewModelScope.launch {
            appSettingsRepository.deleteAppSettings(appSettings)
        }
    }

    fun addSettings(appSettings: AppSettings) {
        viewModelScope.launch {
            appSettingsRepository.upsertAppSettings(appSettings)
        }
    }

    fun getShortcut(id: String = packageName) {
        viewModelScope.launch {
            _shortcutResult.update { shortcutRepository.getShortcut(id) }
        }
    }

    fun copyPermissionCommand() {
        _clipboardResult.update {
            clipboardRepository.setPrimaryClip(
                label = "Command",
                text = "pm grant com.android.geto android.permission.WRITE_SECURE_SETTINGS"
            )
        }
    }

    fun revertSettings() {
        viewModelScope.launch {
            _revertAppSettingsResult.update { revertAppSettingsUseCase(packageName = packageName) }
        }
    }

    fun requestPinShortcut(targetShortcutInfoCompat: TargetShortcutInfoCompat) {
        viewModelScope.launch {
            val icon = packageRepository.getApplicationIcon(packageName = packageName)

            _shortcutResult.update {
                shortcutRepository.requestPinShortcut(
                    icon = icon?.toBitmap(), targetShortcutInfoCompat = targetShortcutInfoCompat
                )
            }
        }
    }

    fun updateRequestPinShortcut(targetShortcutInfoCompat: TargetShortcutInfoCompat) {
        viewModelScope.launch {
            val icon = packageRepository.getApplicationIcon(packageName = packageName)

            _shortcutResult.update {
                shortcutRepository.updateRequestPinShortcut(
                    icon = icon?.toBitmap(), targetShortcutInfoCompat
                )
            }
        }
    }

    fun getSecureSettings(text: String, settingsType: SettingsType) {
        viewModelScope.launch {
            val filteredSecureSettings = secureSettingsRepository.getSecureSettings(settingsType)
                .filter { it.name!!.contains(text) }.take(20)

            _secureSettings.update { filteredSecureSettings }
        }
    }

    fun getApplicationIcon(id: String = packageName) {
        viewModelScope.launch {
            _applicationIcon.update { packageRepository.getApplicationIcon(packageName = id) }
        }
    }

    fun clearAppSettingsResult() {
        _applyAppSettingsResult.update { null }
        _revertAppSettingsResult.update { null }
    }

    fun clearShortcutResult() {
        _shortcutResult.update { null }
    }

    fun clearClipboardResult() {
        _clipboardResult.update { null }
    }
}