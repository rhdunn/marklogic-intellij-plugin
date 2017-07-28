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
package uk.co.reecedunn.intellij.plugin.marklogic.api.http;

import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.jetbrains.annotations.NotNull;
import uk.co.reecedunn.intellij.plugin.marklogic.api.Item;
import uk.co.reecedunn.intellij.plugin.marklogic.api.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpResponse implements Response {
    HttpResponse(StatusLine status, Header[] headers, String body) {
    }

    @Override
    public void close() throws IOException {
    }

    @NotNull
    @Override
    public Item[] getItems() throws IOException {
        List<Item> items = new ArrayList<>();
        if (items.isEmpty()) {
            items.add(Item.create("()", "text/plain", "empty-sequence()"));
        }
        return items.toArray(new Item[items.size()]);
    }
}
