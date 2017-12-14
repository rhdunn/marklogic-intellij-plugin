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

import com.marklogic.xcc.RequestOptions;
import com.marklogic.xcc.Session;
import uk.co.reecedunn.intellij.plugin.marklogic.api.EvalRequestBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.api.Request;

public class XCCEvalRequestBuilder extends EvalRequestBuilder {
    private final XCCConnection connection;

    XCCEvalRequestBuilder(XCCConnection connection) {
        this.connection = connection;
    }

    public Request build() {
        RequestOptions options = new RequestOptions();
        String query = getXQuery();
        if (query != null) {
            options.setQueryLanguage("xquery");
        } else {
            query = getJavaScript();
            options.setQueryLanguage("javascript");
        }

        Session session = connection.getContentSource().newSession(getContentDatabase());
        com.marklogic.xcc.Request request = session.newAdhocQuery(query, options);
        return new XCCRequest(session, request);
    }
}
