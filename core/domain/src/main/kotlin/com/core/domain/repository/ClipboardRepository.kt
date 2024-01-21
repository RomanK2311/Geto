package com.core.domain.repository

typealias CopySettingsResultMessage = String

interface ClipboardRepository {

    fun putTextToClipboard(secureSettings: String): Result<CopySettingsResultMessage?>

    companion object {

        const val COPIED_TO_CLIPBOARD_MESSAGE = "copied to clipboard"

        const val NO_TEXT_TO_COPY_ERROR_MESSAGE = "No text to copy"
    }
}