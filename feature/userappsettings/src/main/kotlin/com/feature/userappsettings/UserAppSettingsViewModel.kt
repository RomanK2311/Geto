package com.feature.userappsettings

import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.repository.SettingsRepository
import com.core.domain.repository.UserAppSettingsRepository
import com.core.domain.usecase.userappsettings.ValidateUserAppSettingsList
import com.core.model.UserAppSettingsItem
import com.feature.userappsettings.navigation.navKeyAppName
import com.feature.userappsettings.navigation.navKeyPackageName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAppSettingsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userAppSettingsRepository: UserAppSettingsRepository,
    private val settingsRepository: SettingsRepository,
    private val validateUserAppSettingsList: ValidateUserAppSettingsList,
    private val packageManager: PackageManager
) : ViewModel() {
    private val _state = MutableStateFlow(UserAppSettingsState())

    val state = _state.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UIEvent>()

    val uiEvent = _uiEvent.asSharedFlow()

    private val packageName = savedStateHandle.get<String>(navKeyPackageName) ?: ""

    private val appName = savedStateHandle.get<String>(navKeyAppName) ?: ""

    val userAppSettingsList: StateFlow<List<UserAppSettingsItem>> =
        userAppSettingsRepository.getUserAppSettingsList(packageName).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    init {
        onEvent(UserAppSettingsEvent.GetUserAppInfo)
    }

    fun onEvent(event: UserAppSettingsEvent) {
        when (event) {
            is UserAppSettingsEvent.GetUserAppInfo -> {
                _state.value = _state.value.copy(
                    appName = appName, packageName = packageName
                )
            }

            UserAppSettingsEvent.OnDismissAddSettingsDialog -> {
                _state.value = _state.value.copy(openAddSettingsDialog = false)
            }

            UserAppSettingsEvent.OnOpenAddSettingsDialog -> {
                _state.value = _state.value.copy(openAddSettingsDialog = true)
            }

            is UserAppSettingsEvent.OnLaunchApp -> {
                val userAppSettingsListResult =
                    validateUserAppSettingsList(event.userAppSettingsList)

                if (!userAppSettingsListResult.successful) {
                    viewModelScope.launch {
                        _uiEvent.emit(UIEvent.Toast(userAppSettingsListResult.errorMessage))
                    }

                    return
                }

                viewModelScope.launch {
                    settingsRepository.applySettings(event.userAppSettingsList).onSuccess {
                        val appIntent = packageManager.getLaunchIntentForPackage(packageName)

                        _uiEvent.emit(UIEvent.LaunchApp(appIntent))
                    }.onFailure {
                        _uiEvent.emit(UIEvent.Toast(it.message))
                    }
                }
            }

            is UserAppSettingsEvent.OnRevertSettings -> {
                val userAppSettingsListResult =
                    validateUserAppSettingsList(event.userAppSettingsList)

                if (!userAppSettingsListResult.successful) {
                    viewModelScope.launch {
                        _uiEvent.emit(UIEvent.Toast(userAppSettingsListResult.errorMessage))
                    }

                    return
                }

                viewModelScope.launch {
                    settingsRepository.revertSettings(event.userAppSettingsList).onSuccess {
                        _uiEvent.emit(UIEvent.Toast(it))
                    }.onFailure {
                        _uiEvent.emit(UIEvent.Toast(it.message))
                    }
                }
            }

            is UserAppSettingsEvent.OnUserAppSettingsItemCheckBoxChange -> {
                viewModelScope.launch {
                    val updatedUserAppSettingsItem =
                        event.userAppSettingsItem.copy(enabled = event.checked)

                    userAppSettingsRepository.upsertUserAppSettingsEnabled(
                        updatedUserAppSettingsItem
                    ).onSuccess {
                        _uiEvent.emit(UIEvent.Toast(it))
                    }.onFailure {
                        _uiEvent.emit(UIEvent.Toast(it.localizedMessage))
                    }
                }
            }

            is UserAppSettingsEvent.OnDeleteUserAppSettingsItem -> {
                viewModelScope.launch {
                    userAppSettingsRepository.deleteUserAppSettings(event.userAppSettingsItem)
                        .onSuccess {
                            _uiEvent.emit(UIEvent.Toast(it))
                        }.onFailure {
                            _uiEvent.emit(UIEvent.Toast(it.localizedMessage))
                        }
                }
            }
        }
    }

    sealed class UIEvent {
        data class Toast(val message: String? = null) : UIEvent()

        data class LaunchApp(val intent: Intent? = null) : UIEvent()
    }
}