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

import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.executors.DefaultRunExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.executors.ProfileExecutor;

public class XQueryBuilder implements QueryBuilder {
    public static QueryBuilder INSTANCE = new XQueryBuilder();

    private XQueryBuilder() {
    }

    @Nullable
    public Function createEvalBuilder(@NotNull String executorId, double markLogicVersion) {
        if (DefaultRunExecutor.EXECUTOR_ID.equals(executorId)) {
            return markLogicVersion >= 7.0 ? Function.XDMP_EVAL_70 : Function.XDMP_EVAL_50;
        } else if (ProfileExecutor.EXECUTOR_ID.equals(executorId)) {
            return markLogicVersion >= 8.0 ? Function.PROF_EVAL_80 : Function.PROF_EVAL_50;
        } else if (DefaultDebugExecutor.EXECUTOR_ID.equals(executorId)) {
            return markLogicVersion >= 8.0 ? Function.DBG_EVAL_80 : Function.DBG_EVAL_50;
        } else {
            return null;
        }
    }

    @Nullable
    public Function createInvokeBuilder(@NotNull String executorId, double markLogicVersion) {
        if (DefaultRunExecutor.EXECUTOR_ID.equals(executorId)) {
            return markLogicVersion >= 7.0 ? Function.XDMP_INVOKE_70 : Function.XDMP_INVOKE_50;
        } else if (ProfileExecutor.EXECUTOR_ID.equals(executorId)) {
            return markLogicVersion >= 8.0 ? Function.PROF_INVOKE_80 : Function.PROF_INVOKE_50;
        } else if (DefaultDebugExecutor.EXECUTOR_ID.equals(executorId)) {
            return markLogicVersion >= 8.0 ? Function.DBG_INVOKE_80 : Function.DBG_INVOKE_50;
        } else {
            return null;
        }
    }
}
