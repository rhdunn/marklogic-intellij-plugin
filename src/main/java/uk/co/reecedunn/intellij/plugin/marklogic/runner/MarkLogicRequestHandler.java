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
import com.marklogic.xcc.*;
import com.marklogic.xcc.exceptions.QueryException;
import com.marklogic.xcc.exceptions.QueryStackFrame;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.OutputStream;

public class MarkLogicRequestHandler extends ProcessHandler implements MarkLogicResultsHandler {
    private final Session session;
    private final Request request;
    private final String mainModulePath;

    public MarkLogicRequestHandler(@NotNull Session session, @NotNull Request request, @NotNull String mainModulePath) {
        this.session = session;
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
        ResultSequence results;
        try {
            results = session.submitRequest(request);
        } catch (Exception e) {
            handler.onException(e);
            return false;
        }

        handler.onStart();
        while (results.hasNext()) {
            ResultItem result = results.next();
            handler.onResult(result.asString(), result.getItem().getItemType().toString(), "text/plain");
        }

        results.close();
        handler.onCompleted();
        return true;
    }

    @Override
    public void onException(Exception e) {
        if (e instanceof QueryException) {
            formatError((QueryException)e);
        } else {
            notifyTextAvailable(e.toString(), null);
            notifyTextAvailable("\n", null);
        }
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

    private void formatError(QueryException e) {
        String error = e.getFormatString() + "\n";
        notifyTextAvailable(error, null);

        QueryStackFrame eval = null;
        for (QueryStackFrame s : e.getStack()) {
            final String path = s.getUri();
            if (eval != null) {
                if (path.equals("/eval")) {
                    formatStackFrame(eval, mainModulePath);
                } else {
                    formatStackFrame(eval, "xdmp:eval");
                }
                eval = null;
            }

            if (path == null) {
                eval = s;
            } else if (!path.equals("/eval")) {
                formatStackFrame(s, path);
            }
        }
    }

    private void formatStackFrame(QueryStackFrame s, String path) {
        String context = s.getContextItem();
        String line;
        if (context == null) {
            line = "\tat " + path + ":" + s.getLineNumber() + "\n";
        } else {
            line = "\tat " + context + "(" + path + ":" + s.getLineNumber() + ")\n";
        }
        notifyTextAvailable(line, null);
    }
}
