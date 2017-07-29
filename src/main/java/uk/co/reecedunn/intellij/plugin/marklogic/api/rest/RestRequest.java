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
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import uk.co.reecedunn.intellij.plugin.marklogic.api.Request;
import uk.co.reecedunn.intellij.plugin.marklogic.api.Response;
import uk.co.reecedunn.intellij.plugin.marklogic.api.mime.MimeResponse;

import java.io.IOException;

public class RestRequest implements Request {
    private final HttpUriRequest request;
    private final RestConnection connection;

    RestRequest(HttpUriRequest request, RestConnection connection) {
        this.request = request;
        this.connection = connection;
    }

    @NotNull
    @Override
    public Response run() throws IOException {
        final CloseableHttpResponse response = connection.getClient().execute(request);
        final StatusLine status = response.getStatusLine();
        final Header[] headers = response.getAllHeaders();
        final String body = EntityUtils.toString(response.getEntity());
        response.close();

        return new RestResponse(new MimeResponse(status, headers, body));
    }
}
