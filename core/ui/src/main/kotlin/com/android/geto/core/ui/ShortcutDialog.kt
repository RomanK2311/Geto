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

package com.android.geto.core.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImage
import com.android.geto.core.designsystem.icon.GetoIcons
import com.android.geto.core.model.Shortcut

@Composable
fun AddShortcutDialog(
    modifier: Modifier = Modifier,
    shortcutDialogState: ShortcutDialogState,
    onDismissRequest: () -> Unit,
    onRefreshShortcut: () -> Unit,
    onAddShortcut: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentSize()
                .padding(16.dp)
                .testTag("addShortcutDialog"),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .weight(1f),
                        text = "Add Shortcut",
                        style = MaterialTheme.typography.titleLarge
                    )

                    IconButton(onClick = onRefreshShortcut) {
                        Icon(
                            imageVector = GetoIcons.Refresh,
                            contentDescription = "updateShortcutDialog:refresh"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                AsyncImage(
                    model = shortcutDialogState.icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    value = shortcutDialogState.shortLabel,
                    onValueChange = shortcutDialogState::updateShortLabel,
                    label = {
                        Text(text = "Short label")
                    },
                    isError = shortcutDialogState.shortLabelError.isNotBlank(),
                    supportingText = {
                        if (shortcutDialogState.shortLabelError.isNotBlank()) Text(
                            text = shortcutDialogState.shortLabelError,
                            modifier = Modifier.testTag("addShortcutDialog:shortLabelSupportingText")
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    value = shortcutDialogState.longLabel,
                    onValueChange = shortcutDialogState::updateLongLabel,
                    label = {
                        Text(text = "Long label")
                    },
                    isError = shortcutDialogState.longLabelError.isNotBlank(),
                    supportingText = {
                        if (shortcutDialogState.longLabelError.isNotBlank()) Text(
                            text = shortcutDialogState.longLabelError,
                            modifier = Modifier.testTag("addShortcutDialog:longLabelSupportingText")
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = onDismissRequest, modifier = Modifier.padding(5.dp)
                    ) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick = onAddShortcut,
                        modifier = Modifier
                            .padding(5.dp)
                            .testTag("addShortcutDialog:add")
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}

@Composable
fun UpdateShortcutDialog(
    modifier: Modifier = Modifier,
    shortcutDialogState: ShortcutDialogState,
    onDismissRequest: () -> Unit,
    onRefreshShortcut: () -> Unit,
    onUpdateShortcut: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentSize()
                .padding(16.dp)
                .testTag("updateShortcutDialog"),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .weight(1f),
                        text = "Update Shortcut",
                        style = MaterialTheme.typography.titleLarge
                    )

                    IconButton(onClick = onRefreshShortcut) {
                        Icon(
                            imageVector = GetoIcons.Refresh,
                            contentDescription = "updateShortcutDialog:refresh"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                AsyncImage(
                    model = shortcutDialogState.icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    value = shortcutDialogState.shortLabel,
                    onValueChange = shortcutDialogState::updateShortLabel,
                    label = {
                        Text(text = "Short label")
                    },
                    isError = shortcutDialogState.shortLabelError.isNotBlank(),
                    supportingText = {
                        if (shortcutDialogState.shortLabelError.isNotBlank()) Text(
                            text = shortcutDialogState.shortLabelError,
                            modifier = Modifier.testTag("updateShortcutDialog:shortLabelSupportingText")
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    value = shortcutDialogState.longLabel,
                    onValueChange = shortcutDialogState::updateLongLabel,
                    label = {
                        Text(text = "Long label")
                    },
                    isError = shortcutDialogState.longLabelError.isNotBlank(),
                    supportingText = {
                        if (shortcutDialogState.longLabelError.isNotBlank()) Text(
                            text = shortcutDialogState.longLabelError,
                            modifier = Modifier.testTag("updateShortcutDialog:longLabelSupportingText")
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = onDismissRequest, modifier = Modifier.padding(5.dp)
                    ) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick = onUpdateShortcut,
                        modifier = Modifier
                            .padding(5.dp)
                            .testTag("updateShortcutDialog:update")
                    ) {
                        Text("Update")
                    }
                }
            }
        }
    }
}


@Composable
fun rememberAddShortcutDialogState(): ShortcutDialogState {
    return rememberSaveable(saver = ShortcutDialogState.Saver) {
        ShortcutDialogState()
    }
}

@Composable
fun rememberUpdateShortcutDialogState(): ShortcutDialogState {
    return rememberSaveable(saver = ShortcutDialogState.Saver) {
        ShortcutDialogState()
    }
}

@Stable
class ShortcutDialogState {
    var showDialog by mutableStateOf(false)
        private set

    var icon by mutableStateOf<Bitmap?>(null)
        private set

    var shortLabel by mutableStateOf("")
        private set

    var shortLabelError by mutableStateOf("")
        private set

    var longLabel by mutableStateOf("")
        private set

    var longLabelError by mutableStateOf("")
        private set

    fun updateShowDialog(value: Boolean) {
        showDialog = value
    }

    fun updateIcon(value: Drawable?) {
        icon = value?.toBitmap()
    }

    fun updateShortLabel(value: String) {
        shortLabel = value
    }

    fun updateLongLabel(value: String) {
        longLabel = value
    }

    fun resetState() {
        showDialog = false
        longLabel = ""
        shortLabel = ""
    }

    fun getShortcut(packageName: String, intent: Intent): Shortcut? {
        shortLabelError = if (shortLabel.isBlank()) "Short label is blank" else ""

        longLabelError = if (longLabel.isBlank()) "Long label is blank" else ""

        return if (shortLabelError.isBlank() && longLabelError.isBlank()) {
            Shortcut(
                icon = icon,
                id = packageName,
                shortLabel = shortLabel,
                longLabel = longLabel,
                intent = intent
            )
        } else {
            null
        }
    }

    companion object {
        val Saver = listSaver<ShortcutDialogState, Any>(save = { state ->
            listOf(
                state.showDialog,
                state.shortLabel,
                state.shortLabelError,
                state.longLabel,
                state.longLabelError
            )
        }, restore = {
            ShortcutDialogState().apply {
                showDialog = it[0] as Boolean

                shortLabel = it[1] as String

                shortLabelError = it[2] as String

                longLabel = it[3] as String

                longLabelError = it[4] as String
            }
        })
    }
}