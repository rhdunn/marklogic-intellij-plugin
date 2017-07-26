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
package uk.co.reecedunn.intellij.plugin.marklogic.configuration.script;

import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.apache.xmlbeans.impl.common.IOUtil;
import uk.co.reecedunn.intellij.plugin.marklogic.configuration.MarkLogicRunConfiguration;

import java.io.*;

public abstract class ScriptFactory {
    public String createScript(MarkLogicRunConfiguration configuration) {
        final String script = readFileContent(configuration.getMainModuleFile());
        StringBuilder query = new StringBuilder();
        query.append("try {");
        createEvalScript(query, script, configuration);
        query.append("} catch ($e) { $e }");
        return query.toString();
    }

    public abstract void createEvalScript(StringBuilder result, String script, MarkLogicRunConfiguration configuration);

    protected String asXQueryStringContent(String query) {
        return query.replaceAll("\"", "\"\"").replaceAll("&", "&amp;");
    }

    private String readFileContent(VirtualFile file) {
        if (file != null) {
            try {
                return streamToString(file.getInputStream());
            } catch (IOException e) {
                //
            }
        }
        return null;
    }

    private String streamToString(InputStream stream) throws IOException {
        StringWriter writer = new StringWriter();
        IOUtil.copyCompletely(new InputStreamReader(stream), writer);
        return writer.toString();
    }
}
