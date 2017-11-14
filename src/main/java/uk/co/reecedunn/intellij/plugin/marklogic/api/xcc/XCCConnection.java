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

import com.marklogic.xcc.ContentSource;
import com.marklogic.xcc.ContentSourceFactory;
import org.jetbrains.annotations.NotNull;
import uk.co.reecedunn.intellij.plugin.marklogic.api.Connection;
import uk.co.reecedunn.intellij.plugin.marklogic.api.EvalRequestBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.api.LogRequestBuilder;

import java.io.IOException;

public class XCCConnection extends Connection {
    private final ContentSource source;

    private XCCConnection(ContentSource source) {
        this.source = source;
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public EvalRequestBuilder createEvalRequestBuilder() {
        return new XCCEvalRequestBuilder(this);
    }

    @Override
    public LogRequestBuilder createLogRequestBuilder() {
        throw new UnsupportedOperationException("The XCC API does not support error logs.");
    }

    ContentSource getContentSource() {
        return source;
    }

    @NotNull
    public static Connection newConnection(String hostname, int port, String username, String password) {
        ContentSource source = ContentSourceFactory.newContentSource(hostname, port, username, password);
        return new XCCConnection(source);
    }
}
