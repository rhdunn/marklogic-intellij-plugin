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
package uk.co.reecedunn.intellij.plugin.marklogic.api.rest;

import org.jetbrains.annotations.NotNull;
import uk.co.reecedunn.intellij.plugin.marklogic.api.Item;
import uk.co.reecedunn.intellij.plugin.marklogic.api.Response;
import uk.co.reecedunn.intellij.plugin.marklogic.api.mime.Message;
import uk.co.reecedunn.intellij.plugin.marklogic.api.mime.MimeResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RestResponse implements Response {
    private final MimeResponse response;

    public RestResponse(MimeResponse response) {
        this.response = response;
    }

    @Override
    public void close() throws IOException {
    }

    @NotNull
    @Override
    public Item[] getItems() throws IOException {
        int statusCode = response.getStatus().getStatusCode();
        if (statusCode != 200) {
            final String message = response.getParts()[0].getBody();
            throw new IOException(statusCode + " " + response.getStatus().getReasonPhrase() + "\n" + message);
        }

        List<Item> items = new ArrayList<>();
        final String internalContentType = response.getHeader("X-Content-Type");
        for (Message part : response.getParts()) {
            final String contentType = part.getHeader("Content-Type");
            if (contentType == null) {
                continue;
            }

            final String primitive = part.getHeader("X-Primitive");
            if (internalContentType == null) {
                items.add(Item.create(part.getBody(), contentType, primitive));
            } else {
                items.add(Item.create(part.getBody(), internalContentType, primitive));
            }
        }
        if (items.isEmpty()) {
            items.add(Item.create("()", "text/plain", "empty-sequence()"));
        }
        return items.toArray(new Item[items.size()]);
    }
}
