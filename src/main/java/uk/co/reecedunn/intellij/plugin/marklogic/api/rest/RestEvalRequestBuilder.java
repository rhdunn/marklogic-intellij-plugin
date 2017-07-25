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

import com.google.gson.JsonObject;
import org.apache.http.client.methods.RequestBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.reecedunn.intellij.plugin.marklogic.api.EvalRequestBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.api.Request;

public class RestEvalRequestBuilder extends EvalRequestBuilder {
    private final RestConnection connection;

    RestEvalRequestBuilder(RestConnection connection) {
        this.connection = connection;
    }

    public Request build() {
        RequestBuilder builder = RequestBuilder.post(connection.getBaseUri() + "/v1/eval");
        addParameter(builder, "xquery", getXQuery());
        addParameter(builder, "javascript", getJavaScript());
        addParameter(builder, "vars", getVarsJson());
        addParameter(builder, "database", getContentDatabase());
        addParameter(builder, "txid", getTransactionID());
        return new RestRequest(builder.build(), connection);
    }

    private void addParameter(@NotNull RequestBuilder builder, @NotNull String key, @Nullable String value) {
        if (value != null && !value.isEmpty()) {
            builder.addParameter(key, value);
        }
    }
}
