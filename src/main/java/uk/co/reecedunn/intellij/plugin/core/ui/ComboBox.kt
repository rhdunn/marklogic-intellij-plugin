/*
 * Copyright (C) 2017-2018 Reece H. Dunn
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

import com.intellij.openapi.ui.ComboBox

private class ItemIterator<T>(val combobox: ComboBox<T>) : Iterator<T> {
    private val length: Int = combobox.itemCount
    private var current: Int = 0

    override fun hasNext(): Boolean =
        current != length

    override fun next(): T =
        combobox.getItemAt(++current)
}

open class ComboBox<T>: ComboBox<T>() {
    var items: List<T>
        get() = ItemIterator(this).asSequence().toList()
        set(value) {
            removeAllItems()
            value.forEach { item ->
                addItem(item)
            }
        }
}
