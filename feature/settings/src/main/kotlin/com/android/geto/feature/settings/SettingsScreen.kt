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

package com.android.geto.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.geto.core.designsystem.component.GetoLoadingWheel
import com.android.geto.core.designsystem.icon.GetoIcons
import com.android.geto.core.designsystem.theme.GetoTheme
import com.android.geto.core.designsystem.theme.supportsDynamicTheming
import com.android.geto.core.model.DarkThemeConfig
import com.android.geto.core.model.ThemeBrand
import com.android.geto.feature.settings.dialog.dark.DarkDialog
import com.android.geto.feature.settings.dialog.dark.DarkDialogState
import com.android.geto.feature.settings.dialog.dark.rememberDarkDialogState
import com.android.geto.feature.settings.dialog.theme.ThemeDialog
import com.android.geto.feature.settings.dialog.theme.ThemeDialogState
import com.android.geto.feature.settings.dialog.theme.rememberThemeDialogState

@Composable
internal fun SettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    onNavigationIconClick: () -> Unit
) {

    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()

    val themeDialogState = rememberThemeDialogState()

    val darkDialogState = rememberDarkDialogState()

    LaunchedEffect(key1 = settingsUiState) {
        if (settingsUiState is SettingsUiState.Success) {
            val brandIndex =
                ThemeBrand.entries.indexOf((settingsUiState as SettingsUiState.Success).settings.brand)
            val darkThemeConfigIndex =
                DarkThemeConfig.entries.indexOf((settingsUiState as SettingsUiState.Success).settings.darkThemeConfig)

            themeDialogState.updateSelectedRadioOptionIndex(brandIndex)
            darkDialogState.updateSelectedRadioOptionIndex(darkThemeConfigIndex)
        }
    }

    SettingsScreen(
        modifier = modifier,
        settingsUiState = settingsUiState,
        themeDialogState = themeDialogState,
        darkDialogState = darkDialogState,
        onChangeThemeBrand = viewModel::updateThemeBrand,
        onChangeDynamicColorPreference = viewModel::updateDynamicColorPreference,
        onChangeDarkThemeConfig = viewModel::updateDarkThemeConfig,
        onNavigationIconClick = onNavigationIconClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreen(
    modifier: Modifier = Modifier,
    settingsUiState: SettingsUiState,
    themeDialogState: ThemeDialogState,
    darkDialogState: DarkDialogState,
    supportDynamicColor: Boolean = supportsDynamicTheming(),
    onChangeThemeBrand: (themeBrand: ThemeBrand) -> Unit,
    onChangeDynamicColorPreference: (useDynamicColor: Boolean) -> Unit,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
    onNavigationIconClick: () -> Unit
) {

    if (themeDialogState.showDialog) {
        ThemeDialog(
            modifier = Modifier.testTag("settings:themeDialog"),
            themeDialogState = themeDialogState,
            onChangeTheme = {
                onChangeThemeBrand(ThemeBrand.entries[themeDialogState.selectedRadioOptionIndex])
                themeDialogState.updateShowDialog(false)
            }, contentDescription = "Theme Dialog"
        )
    }

    if (darkDialogState.showDialog) {
        DarkDialog(
            darkDialogState = darkDialogState, onChangeDark = {
                onChangeDarkThemeConfig(DarkThemeConfig.entries[darkDialogState.selectedRadioOptionIndex])
                darkDialogState.updateShowDialog(false)
            }, contentDescription = "Dark Dialog"
        )
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "Settings")
        }, navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(imageVector = GetoIcons.Back, contentDescription = "Navigation icon")
            }
        })
    }) { innerPadding ->

        Box(
            modifier = modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding)
                .testTag("applist")
        ) {
            when (settingsUiState) {
                SettingsUiState.Loading -> LoadingState(
                    modifier = Modifier.align(Alignment.Center)
                )

                is SettingsUiState.Success -> {
                    SuccessState(
                        contentPadding = innerPadding,
                        settingsUiState = settingsUiState,
                        supportDynamicColor = supportDynamicColor,
                        onThemeDialog = {
                            themeDialogState.updateShowDialog(true)
                        },
                        onDarkDialog = {
                            darkDialogState.updateShowDialog(true)
                        },
                        onChangeDynamicColorPreference = onChangeDynamicColorPreference
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    GetoLoadingWheel(
        modifier = modifier, contentDescription = "GetoOverlayLoadingWheel"
    )
}

@Composable
fun SuccessState(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    settingsUiState: SettingsUiState.Success,
    supportDynamicColor: Boolean = supportsDynamicTheming(),
    onThemeDialog: () -> Unit,
    onDarkDialog: () -> Unit,
    onChangeDynamicColorPreference: (useDynamicColor: Boolean) -> Unit
) {
    Column(
        modifier = modifier
            .consumeWindowInsets(contentPadding)
            .padding(contentPadding)
            .testTag("settings:column")
    ) {

        Column(modifier = Modifier
            .fillMaxWidth()
            .clickable { onThemeDialog() }
            .padding(10.dp)
            .testTag("settings:theme")) {
            Text(text = "Theme", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = settingsUiState.settings.brand.title,
                style = MaterialTheme.typography.bodySmall
            )
        }

        if (settingsUiState.settings.brand == ThemeBrand.DEFAULT && supportDynamicColor) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .testTag("settings:dynamic"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Dynamic Color",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )

                Switch(
                    modifier = Modifier.testTag("settings:dynamic:switch"),
                    checked = settingsUiState.settings.useDynamicColor,
                    onCheckedChange = onChangeDynamicColorPreference
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier
            .fillMaxWidth()
            .clickable { onDarkDialog() }
            .padding(10.dp)
            .testTag("settings:dark")) {
            Text(text = "Dark Mode", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = settingsUiState.settings.darkThemeConfig.title,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


@Preview
@Composable
private fun LoadingStatePreview() {
    GetoTheme {
        LoadingState()
    }
}

@Preview
@Composable
private fun SuccessStatePreview() {
    GetoTheme {
        SuccessState(contentPadding = PaddingValues(20.dp),
                     settingsUiState = SettingsUiState.Success(
                         settings = UserEditableSettings(
                             brand = ThemeBrand.DEFAULT,
                             useDynamicColor = true,
                             darkThemeConfig = DarkThemeConfig.DARK
                         )
                     ),
                     supportDynamicColor = true,
                     onThemeDialog = {},
                     onDarkDialog = {},
                     onChangeDynamicColorPreference = {})
    }
}