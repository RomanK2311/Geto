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

package com.android.geto.core.shortcutmanager

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DefaultShortcutManagerCompatWrapper @Inject constructor(@ApplicationContext private val context: Context) :
    ShortcutManagerCompatWrapper {

    override fun isRequestPinShortcutSupported(): Boolean {
        return ShortcutManagerCompat.isRequestPinShortcutSupported(context)
    }

    override fun createShortcutResultIntent(shortcutInfoCompat: ShortcutInfoCompat): Intent {
        return ShortcutManagerCompat.createShortcutResultIntent(
            context, shortcutInfoCompat
        )
    }

    override fun requestPinShortcut(
        id: String, shortLabel: String, longLabel: String, intent: Intent
    ): Boolean {
        return if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
            val shortcutInfo = ShortcutInfoCompat.Builder(context, id).setShortLabel(shortLabel)
                .setLongLabel(longLabel).setIntent(intent).build()

            val shortcutCallbackIntent =
                ShortcutManagerCompat.createShortcutResultIntent(context, shortcutInfo)

            val successCallback = PendingIntent.getBroadcast(
                context, 0, shortcutCallbackIntent, PendingIntent.FLAG_IMMUTABLE
            )

            ShortcutManagerCompat.requestPinShortcut(
                context, shortcutInfo, successCallback?.intentSender
            )

        } else false
    }
}