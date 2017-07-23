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
package uk.co.reecedunn.intellij.plugin.marklogic.rest;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Response {
    private final CloseableHttpResponse response;

    Response(CloseableHttpResponse response) {
        this.response = response;
    }

    public void close() throws IOException {
        response.close();
    }

    public int getStatusCode() {
        return response.getStatusLine().getStatusCode();
    }

    public String getStatusReason() {
        return response.getStatusLine().getReasonPhrase();
    }

    public String getContentType() {
        Header[] headers = response.getHeaders("Content-Type");
        if (headers.length == 0) {
            return "application/octet-stream";
        }
        return headers[0].getValue();
    }

    public String getResponse() throws IOException {
        return EntityUtils.toString(response.getEntity());
    }
}
