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
package uk.co.reecedunn.intellij.plugin.marklogic.runner;

import com.intellij.execution.process.ProcessHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.reecedunn.intellij.plugin.marklogic.rest.Request;
import uk.co.reecedunn.intellij.plugin.marklogic.rest.Response;
import uk.co.reecedunn.intellij.plugin.marklogic.rest.Result;

import java.io.IOException;
import java.io.OutputStream;

public class MarkLogicRestHandler extends ProcessHandler implements MarkLogicResultsHandler {
    private final Request request;
    private final String mainModulePath;

    public MarkLogicRestHandler(@NotNull Request request, @NotNull String mainModulePath) {
        this.request = request;
        this.mainModulePath = mainModulePath;
    }

    @Override
    protected void destroyProcessImpl() {
    }

    @Override
    protected void detachProcessImpl() {
    }

    @Override
    public boolean detachIsDefault() {
        return false;
    }

    @Nullable
    @Override
    public OutputStream getProcessInput() {
        return null;
    }

    @Override
    public void startNotify() {
        super.startNotify();
        run(this);
        notifyProcessDetached();
    }

    public boolean run(MarkLogicResultsHandler handler) {
        Result[] results;
        try {
            Response response = request.run();
            if (response.getStatusCode() != 200) {
                throw new IOException(response.getStatusCode() + " " + response.getStatusReason());
            }
            results = response.getResults();
            response.close();
        } catch (IOException e) {
            handler.onException(e);
            return false;
        }

        handler.onStart();
        for (Result result : results) {
            handler.onResult(result.getContent(), result.getPrimitive(), result.getContentType());
        }
        handler.onCompleted();
        return true;
    }

    @Override
    public void onException(Exception e) {
        notifyTextAvailable(e.toString(), null);
        notifyTextAvailable("\n", null);
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onResult(String value, String itemType, String contentType) {
        notifyTextAvailable("----- " + itemType + " [" + contentType  + "]\n", null);
        notifyTextAvailable(value, null);
        notifyTextAvailable("\n", null);
    }

    @Override
    public void onCompleted() {
    }
}
