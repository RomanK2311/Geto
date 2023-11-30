package com.feature.userappsettings

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.core.model.UserAppSettingsItem
import com.feature.userappsettings.components.AddSettingsDialog
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun UserAppSettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: UserAppSettingsViewModel = hiltViewModel(),
    onArrowBackClick: () -> Unit
) {
    val context = LocalContext.current

    val state = viewModel.state.collectAsState().value

    val userAppSettingsList = viewModel.userAppSettingsList.collectAsStateWithLifecycle().value

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UserAppSettingsViewModel.UIEvent.LaunchApp -> {
                    event.intent?.let {
                        context.startActivity(it)
                    }
                }

                is UserAppSettingsViewModel.UIEvent.Toast -> {
                    event.message?.let {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    StatelessScreen(modifier = modifier,
                    state = state,
                    onNavigationIconClick = {
                        onArrowBackClick()
                    },
                    onRevertSettingsIconClick = {
                        viewModel.onEvent(UserAppSettingsEvent.OnRevertSettings(userAppSettingsList))
                    },
                    userAppSettingsList = userAppSettingsList,
                    onUserAppSettingsItemCheckBoxChange = { checked, userAppSettingsItem ->
                        viewModel.onEvent(
                            UserAppSettingsEvent.OnUserAppSettingsItemCheckBoxChange(
                                checked = checked, userAppSettingsItem = userAppSettingsItem
                            )
                        )
                    },
                    onDeleteUserAppSettingsItem = {
                        viewModel.onEvent(UserAppSettingsEvent.OnDeleteUserAppSettingsItem(it))
                    },
                    onAddUserAppSettingsClick = { viewModel.onEvent(UserAppSettingsEvent.OnOpenAddSettingsDialog) },
                    onLaunchApp = {
                        viewModel.onEvent(UserAppSettingsEvent.OnLaunchApp(userAppSettingsList))
                    })

    if (state.openAddSettingsDialog) {
        AddSettingsDialog(
            onDismissRequest = { viewModel.onEvent(UserAppSettingsEvent.OnDismissAddSettingsDialog) },
            packageName = state.packageName
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatelessScreen(
    modifier: Modifier = Modifier,
    state: UserAppSettingsState,
    userAppSettingsList: List<UserAppSettingsItem>,
    onNavigationIconClick: () -> Unit,
    onRevertSettingsIconClick: () -> Unit,
    onUserAppSettingsItemCheckBoxChange: (Boolean, UserAppSettingsItem) -> Unit,
    onDeleteUserAppSettingsItem: (UserAppSettingsItem) -> Unit,
    onAddUserAppSettingsClick: () -> Unit,
    onLaunchApp: () -> Unit
) {
    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        TopAppBar(title = {
            Text(text = state.appName, maxLines = 1)
        }, navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack, contentDescription = null
                )
            }
        }, actions = {
            IconButton(onClick = onRevertSettingsIconClick) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
            }
        })
    }) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(userAppSettingsList) { settingsItem ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = settingsItem.enabled, onCheckedChange = {
                            onUserAppSettingsItemCheckBoxChange(
                                it, settingsItem
                            )
                        })

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = settingsItem.label,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                text = settingsItem.settingsType.label,
                                style = MaterialTheme.typography.bodySmall
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                text = settingsItem.key, style = MaterialTheme.typography.bodySmall
                            )
                        }

                        IconButton(onClick = { onDeleteUserAppSettingsItem(settingsItem) }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                        }
                    }
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                onClick = onAddUserAppSettingsClick
            ) {
                Text(text = "Add Settings")
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp), onClick = onLaunchApp
            ) {
                Text(text = "Launch app")
            }
        }
    }
}