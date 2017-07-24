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
package uk.co.reecedunn.intellij.plugin.marklogic.query;

import org.jetbrains.annotations.Nullable;

public class JavaScriptBuilder implements QueryBuilder {
    public static QueryBuilder INSTANCE = new JavaScriptBuilder();

    private JavaScriptBuilder() {
    }

    @Nullable
    public Function createEvalBuilder(ExecMode mode, double markLogicVersion) {
        if (mode == ExecMode.Run && markLogicVersion >= 8.0) {
            return Function.XDMP_JAVASCRIPT_EVAL_80;
        }
        return null;
    }

    @Nullable
    public Function createInvokeBuilder(ExecMode mode, double markLogicVersion) {
        if (mode == ExecMode.Run && markLogicVersion >= 8.0) {
            return Function.XDMP_INVOKE_70;
        }
        return null;
    }
}
