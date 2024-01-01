package com.core.domain.usecase

import com.core.domain.repository.SettingsRepository
import com.core.domain.repository.SettingsResultMessage
import com.core.model.UserAppSettings
import javax.inject.Inject

class ValidateSettingsList @Inject constructor(private val settingsRepository: SettingsRepository) {

    suspend operator fun invoke(appSettingsList: List<UserAppSettings>): Result<SettingsResultMessage> {
        if (appSettingsList.isEmpty() || appSettingsList.all { !it.enabled }) {

            return Result.failure(ValidationException("No settings to apply"))
        }

        return settingsRepository.applySettings(appSettingsList)
    }
}