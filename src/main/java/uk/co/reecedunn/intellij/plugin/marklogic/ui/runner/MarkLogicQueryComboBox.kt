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
package uk.co.reecedunn.intellij.plugin.marklogic.ui.runner

import com.intellij.openapi.ui.ComboBox
import uk.co.reecedunn.intellij.plugin.marklogic.api.Item

class MarkLogicQueryComboBox(private val mDefaultItem: String) : ComboBox<String>(), MarkLogicResultsHandler {
    private var mSelected: String? = null

    var item: String?
        get() {
            val item = super.getSelectedItem() as String
            return if (item == mDefaultItem) null else item
        }
        set(item) {
            if (itemCount == 1) {
                mSelected = item
            } else {
                selectedItem = item ?: mDefaultItem
            }
        }

    init {
        onStart()
    }

    override fun onException(e: Exception) {}

    override fun onStart() {
        if (itemCount > 1) {
            mSelected = item
        }
        removeAllItems()
        addItem(mDefaultItem)
    }

    override fun onItem(item: Item) {
        addItem(item.content)
    }

    override fun onCompleted() {
        item = mSelected
    }
}
