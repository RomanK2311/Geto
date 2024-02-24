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

package com.android.geto.core.data.repository

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.android.geto.core.common.Dispatcher
import com.android.geto.core.common.GetoDispatchers.Default
import com.android.geto.core.model.NonSystemApp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DefaultPackageRepository @Inject constructor(
    private val packageManagerWrapper: com.android.geto.core.clipboardmanager.PackageManagerWrapper,
    @Dispatcher(Default) private val defaultDispatcher: CoroutineDispatcher
) : PackageRepository {
    override suspend fun getNonSystemApps(): List<NonSystemApp> {
        return withContext(defaultDispatcher) {
            packageManagerWrapper.getInstalledApplications()
                .filter { (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0 }.map {
                val label = packageManagerWrapper.getApplicationLabel(it)

                val icon = try {
                    packageManagerWrapper.getApplicationIcon(it)
                } catch (e: PackageManager.NameNotFoundException) {
                    null
                }

                NonSystemApp(
                    icon = icon, packageName = it.packageName, label = label
                )
            }.sortedBy { it.label }
        }
    }
}