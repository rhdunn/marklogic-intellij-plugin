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

public class EvalRequestBuilder {
    private final RestConnection connection;
    private String xquery = null;
    private String javascript = null;
    private JsonObject vars = null;
    private String database = null;
    private String txid = null;

    EvalRequestBuilder(RestConnection connection) {
        this.connection = connection;
    }

    public void setXQuery(String xquery) {
        this.xquery = xquery;
        this.javascript = null; // There can only be one!
    }

    public void setJavaScript(String javascript) {
        this.xquery = null; // There can only be one!
        this.javascript = javascript;
    }

    public void addVariable(String name, String value) {
        if (vars == null) {
            vars = new JsonObject();
        }
        vars.addProperty(name, value);
    }

    public void setContentDatabase(String database) {
        this.database = database;
    }

    public void setTransactionID(String txid) {
        this.txid = txid;
    }

    public RestRequest build() {
        RequestBuilder builder = RequestBuilder.post(connection.getBaseUri() + "/v1/eval");
        if (xquery != null) {
            builder.addParameter("xquery", xquery);
        }
        if (javascript != null) {
            builder.addParameter("javascript", javascript);
        }
        if (vars != null) {
            builder.addParameter("vars", vars.toString());
        }
        if (database != null) {
            builder.addParameter("database", database);
        }
        if (txid != null) {
            builder.addParameter("txid", txid);
        }
        return new RestRequest(builder.build(), connection);
    }
}
