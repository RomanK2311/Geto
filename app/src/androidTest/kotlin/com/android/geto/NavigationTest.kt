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

package com.android.geto

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.android.geto.core.data.repository.PackageRepository
import com.android.geto.feature.applist.navigation.APP_LIST_NAVIGATION_ROUTE
import com.android.geto.navigation.GetoNavHost
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
class NavigationTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: TestNavHostController

    @Inject
    lateinit var packageRepository: PackageRepository

    @Before
    fun setUp() {
        hiltRule.inject()

        composeTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            GetoNavHost(navController = navController)

        }
    }

    @Test
    fun navHost_verify_start_destination() {
        composeTestRule.onNodeWithTag("applist").assertIsDisplayed()
    }

    @Test
    fun navHost_click_app_item_navigate_to_AppSettingsScreen() {
        composeTestRule.onNodeWithText("Application 0").performClick()

        val appSettingsRoute = navController.currentBackStackEntry?.destination?.route

        assertEquals(
            expected = "app_settings_route/{package_name}/{app_name}", actual = appSettingsRoute
        )

        composeTestRule.onNodeWithTag("appsettings:topAppBar").assertIsDisplayed()
    }

    @Test
    fun navHost_click_app_item_navigate_to_AppSettingsScreen_then_press_navigationArrow_to_go_AppListScreen() {
        composeTestRule.onNodeWithText("Application 0").performClick()

        composeTestRule.onNodeWithContentDescription(
            label = "Navigation icon", useUnmergedTree = true
        ).performClick()

        val appListRoute = navController.currentBackStackEntry?.destination?.route

        assertEquals(
            expected = APP_LIST_NAVIGATION_ROUTE, actual = appListRoute
        )
    }
}