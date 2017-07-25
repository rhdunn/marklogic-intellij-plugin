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

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jetbrains.annotations.NotNull;
import uk.co.reecedunn.intellij.plugin.marklogic.api.Connection;

import java.io.IOException;

public class RestConnection extends Connection {
    private final String baseUri;
    private final CloseableHttpClient client;

    private RestConnection(@NotNull String baseUri, @NotNull CloseableHttpClient client) {
        this.baseUri = baseUri;
        this.client = client;
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

    public RestEvalRequestBuilder createEvalRequestBuilder() {
        return new RestEvalRequestBuilder(this);
    }

    String getBaseUri() {
        return baseUri;
    }

    CloseableHttpClient getClient() {
        return client;
    }

    public static RestConnection newConnection(String hostname, int port, String username, String password) {
        final String baseUri = "http://" + hostname + ":" + port;
        if (username == null || password == null) {
            return new RestConnection(baseUri, HttpClients.createDefault());
        }

        final CredentialsProvider credentials = new BasicCredentialsProvider();
        credentials.setCredentials(
            new AuthScope(hostname, port),
            new UsernamePasswordCredentials(username, password));
        return new RestConnection(baseUri, HttpClients.custom().setDefaultCredentialsProvider(credentials).build());
    }
}
