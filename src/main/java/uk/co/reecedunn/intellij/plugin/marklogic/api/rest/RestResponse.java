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

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import uk.co.reecedunn.intellij.plugin.marklogic.api.Item;
import uk.co.reecedunn.intellij.plugin.marklogic.api.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RestResponse implements Response {
    private final CloseableHttpResponse response;

    RestResponse(CloseableHttpResponse response) {
        this.response = response;
    }

    @Override
    public void close() throws IOException {
        response.close();
    }

    @NotNull
    @Override
    public Item[] getItems() throws IOException {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            throw new IOException(statusCode + " " + response.getStatusLine().getReasonPhrase() + "\n" + getResponse());
        }

        List<Item> items = new ArrayList<>();
        String contentType = getHeader("Content-Type", "application/octet-stream");
        if (contentType.startsWith("multipart/")) {
            String internalContentType = getHeader("X-Content-Type", null); // e.g. from the SPARQL queries.
            parseMultiPartResponse(items, getResponse(), contentType, internalContentType);
        } else {
            items.add(new Item(getResponse(), contentType, getHeader("X-Primitive", null)));
        }
        if (items.isEmpty()) {
            items.add(new Item("()", "text/plain", "empty-sequence()"));
        }
        return items.toArray(new Item[items.size()]);
    }

    private String getHeader(String name, String defaultValue) {
        Header[] headers = response.getHeaders(name);
        if (headers.length == 0) {
            return defaultValue;
        }
        return headers[0].getValue();
    }

    private String getResponse() throws IOException {
        return EntityUtils.toString(response.getEntity());
    }

    private void parseMultiPartResponse(List<Item> items, String multipart, String contentType, String internalContentType) throws IOException {
        String[] boundaryParts = contentType.split("boundary=");
        if (boundaryParts.length == 0) {
            throw new IOException("Unsupported content type: " + contentType);
        }

        String boundary = "\r\n--" + boundaryParts[1];
        for (String part : multipart.split(boundary)) {
            if (part.isEmpty() || part.startsWith("--")) {
                continue;
            }

            String[] headersContent = part.split("\r\n\r\n");
            if (headersContent.length != 2) {
                throw new IOException("Incorrect part format: no MIME headers found");
            }

            String resultContentType = "application/octet-stream";
            String resultPrimitive = null;
            for (String header : headersContent[0].split("\r\n")) {
                if (header.startsWith("Content-Type:")) {
                    resultContentType = header.replaceAll("Content-Type:\\s+", "");
                } else if (header.startsWith("X-Primitive:")) {
                    resultPrimitive = header.replaceAll("X-Primitive:\\s+", "");
                }
            }

            if (internalContentType == null) {
                items.add(new Item(headersContent[1], resultContentType, resultPrimitive));
            } else {
                items.add(new Item(headersContent[1], internalContentType, resultPrimitive));
                internalContentType = null; // Only use this for the first result.
            }
        }
    }
}
