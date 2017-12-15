/*
 * Copyright (C) 2017 Reece H. Dunn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.reecedunn.intellij.plugin.core.ui

import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

// https://stackoverflow.com/questions/3953208/value-change-listener-to-jtextfield
abstract class DocumentChangedListener : DocumentListener {
    private var lastChange = 0
    private var lastNotifiedChange = 0

    override fun insertUpdate(e: DocumentEvent) {
        changedUpdate(e)
    }

    override fun removeUpdate(e: DocumentEvent) {
        changedUpdate(e)
    }

    override fun changedUpdate(e: DocumentEvent) {
        lastChange++
        SwingUtilities.invokeLater {
            if (lastChange != lastNotifiedChange) {
                lastNotifiedChange = lastChange
                changed()
            }
        }
    }

    abstract fun changed()
}
