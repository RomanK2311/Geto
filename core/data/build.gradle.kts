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

plugins {
    alias(libs.plugins.com.android.geto.library)
    alias(libs.plugins.com.android.geto.libraryJacoco)
    alias(libs.plugins.com.android.geto.hilt)
}

android {
    namespace = "com.android.geto.core.data"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.database)
    implementation(projects.core.datastore)
    implementation(projects.core.model)

    implementation(projects.framework.clipboardManager)
    implementation(projects.framework.packageManager)
    implementation(projects.framework.secureSettings)
    implementation(projects.framework.shortcutManager)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(projects.core.datastoreTest)
    testImplementation(projects.core.testing)
}