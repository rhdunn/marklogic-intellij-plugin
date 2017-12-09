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

import com.intellij.execution.executors.DefaultRunExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicVersion;

public class SQLBuilder implements QueryBuilder {
    public static QueryBuilder INSTANCE = new SQLBuilder();

    private SQLBuilder() {
    }

    @Nullable
    public Function createEvalBuilder(@NotNull String executorId, @NotNull MarkLogicVersion markLogicVersion) {
        if (DefaultRunExecutor.EXECUTOR_ID.equals(executorId) && markLogicVersion.getMajor() >= 8) {
            return markLogicVersion.getMajor() >= 9 ? Function.XDMP_SQL_90 : Function.XDMP_SQL_80;
        }
        return null;
    }

    @Nullable
    public Function createInvokeBuilder(@NotNull String executorId, @NotNull MarkLogicVersion markLogicVersion) {
        return null;
    }
}
