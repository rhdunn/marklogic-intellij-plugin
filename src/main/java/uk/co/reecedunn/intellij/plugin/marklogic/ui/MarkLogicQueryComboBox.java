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
package uk.co.reecedunn.intellij.plugin.marklogic.ui;

import com.intellij.openapi.ui.ComboBox;
import uk.co.reecedunn.intellij.plugin.marklogic.api.Item;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.runner.MarkLogicResultsHandler;

public class MarkLogicQueryComboBox extends ComboBox<String> implements MarkLogicResultsHandler {
    private final String mDefaultItem;
    private String mSelected;

    public MarkLogicQueryComboBox(String defaultItem) {
        mDefaultItem = defaultItem;
        onStart();
    }

    public String getItem() {
        String item = (String)super.getSelectedItem();
        if (item == null || item.equals(mDefaultItem)) {
            return null;
        }
        return item;
    }

    public void setItem(String item) {
        if (getItemCount() == 1) {
            mSelected = item;
        } else {
            setSelectedItem(item == null ? mDefaultItem : item);
        }
    }

    @Override
    public void onException(Exception e) {
    }

    @Override
    public void onStart() {
        if (getItemCount() > 1) {
            mSelected = getItem();
        }
        removeAllItems();
        addItem(mDefaultItem);
    }

    @Override
    public void onItem(Item item) {
        addItem(item.getContent());
    }

    @Override
    public void onCompleted() {
        setItem(mSelected);
    }
}
