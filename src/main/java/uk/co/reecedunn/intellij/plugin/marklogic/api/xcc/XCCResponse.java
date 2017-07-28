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
package uk.co.reecedunn.intellij.plugin.marklogic.api.xcc;

import com.marklogic.xcc.ResultItem;
import com.marklogic.xcc.ResultSequence;
import com.marklogic.xcc.Session;
import org.jetbrains.annotations.NotNull;
import uk.co.reecedunn.intellij.plugin.marklogic.api.Item;
import uk.co.reecedunn.intellij.plugin.marklogic.api.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XCCResponse implements Response {
    private final ResultSequence resultSequence;
    private final Session session;

    XCCResponse(ResultSequence resultSequence, Session session) {
        this.resultSequence = resultSequence;
        this.session = session;
    }

    @Override
    public void close() throws IOException {
        session.close();
        resultSequence.close();
    }

    @NotNull
    @Override
    public Item[] getResults() throws IOException {
        List<Item> results = new ArrayList<>();
        while (resultSequence.hasNext()) {
            ResultItem result = resultSequence.next();
            results.add(new Item(result.asString(), result.getItemType().toString()));
        }
        if (results.isEmpty()) {
            results.add(new Item("()", "text/plain", "empty-sequence()"));
        }
        return results.toArray(new Item[results.size()]);
    }
}
