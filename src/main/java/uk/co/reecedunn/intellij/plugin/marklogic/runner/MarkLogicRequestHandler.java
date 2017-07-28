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
import uk.co.reecedunn.intellij.plugin.marklogic.api.Item;
import uk.co.reecedunn.intellij.plugin.marklogic.api.Request;
import uk.co.reecedunn.intellij.plugin.marklogic.api.Response;

import java.io.IOException;
import java.io.OutputStream;

public class MarkLogicRequestHandler extends ProcessHandler implements MarkLogicResultsHandler {
    private final Request request;
    private final String mainModulePath;

    public MarkLogicRequestHandler(@NotNull Request request, @NotNull String mainModulePath) {
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
        Item[] results;
        try {
            Response response = request.run();
            results = response.getItems();
            response.close();
        } catch (IOException e) {
            handler.onException(e);
            return false;
        }

        handler.onStart();
        for (Item item : results) {
            handler.onItem(item);
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
    public void onItem(Item item) {
        notifyTextAvailable("----- " + item.getItemType() + " [" + item.getContentType() + "]\n", null);
        notifyTextAvailable(item.getContent(), null);
        notifyTextAvailable("\n", null);
    }

    @Override
    public void onCompleted() {
    }
}
