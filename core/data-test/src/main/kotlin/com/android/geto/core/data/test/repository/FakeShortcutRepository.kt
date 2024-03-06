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

package com.android.geto.core.data.test.repository

import com.android.geto.core.data.repository.ShortcutRepository
import com.android.geto.core.model.TargetShortcutInfoCompat
import javax.inject.Inject

class FakeShortcutRepository @Inject constructor() : ShortcutRepository {

    override fun requestPinShortcut(targetShortcutInfoCompat: TargetShortcutInfoCompat): Boolean {
        return false
    }

    override fun updateRequestPinShortcut(targetShortcutInfoCompat: TargetShortcutInfoCompat): Result<Boolean> {
        return Result.success(true)
    }

    override fun enableShortcuts(id: String, enabled: Boolean): Result<String> {
        return Result.success("")
    }

    override fun getShortcut(id: String): TargetShortcutInfoCompat? {
        return null
    }
}