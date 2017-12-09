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
package uk.co.reecedunn.intellij.plugin.marklogic.api;

import com.intellij.execution.ExecutionException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.reecedunn.intellij.plugin.marklogic.api.rest.RestConnection;
import uk.co.reecedunn.intellij.plugin.marklogic.api.xcc.XCCConnection;
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicVersion;
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicVersionKt;

import java.io.IOException;

public abstract class Connection {
    /**
     * MarkLogic version to use the XCC API in newConnection.
     */
    public static final MarkLogicVersion XCC = MarkLogicVersionKt.getMARKLOGIC_5();
    /**
     * MarkLogic version to use the REST API in newConnection, including fetching error logs.
     */
    public static final MarkLogicVersion REST = MarkLogicVersionKt.getMARKLOGIC_8();

    public static final MarkLogicVersion[] SUPPORTED_MARKLOGIC_VERSIONS = new MarkLogicVersion[] {
        MarkLogicVersionKt.getMARKLOGIC_5(),
        MarkLogicVersionKt.getMARKLOGIC_6(),
        MarkLogicVersionKt.getMARKLOGIC_7(),
        MarkLogicVersionKt.getMARKLOGIC_8(),
        MarkLogicVersionKt.getMARKLOGIC_9(),
    };

    protected Connection() {
    }

    public abstract void close() throws IOException;

    public abstract EvalRequestBuilder createEvalRequestBuilder();

    public abstract LogRequestBuilder createLogRequestBuilder();

    @NotNull
    public static Connection newConnection(String hostname, int port, @Nullable String username, @Nullable String password, @NotNull MarkLogicVersion markLogicVersion) throws ExecutionException {
        if (markLogicVersion.getMajor() >= REST.getMajor()) {
            return RestConnection.newConnection(hostname, port, username, password);
        }
        return XCCConnection.newConnection(hostname, port, username, password);
    }
}
