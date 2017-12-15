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
package uk.co.reecedunn.intellij.plugin.marklogic.query

import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.executors.DefaultRunExecutor
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicVersion
import uk.co.reecedunn.intellij.plugin.marklogic.ui.profile.ProfileExecutor

class XQueryBuilder private constructor() : QueryBuilder {
    override fun createEvalBuilder(executorId: String, markLogicVersion: MarkLogicVersion): Function? {
        return if (DefaultRunExecutor.EXECUTOR_ID == executorId) {
            if (markLogicVersion.major >= 7) Function.XDMP_EVAL_70 else Function.XDMP_EVAL_50
        } else if (ProfileExecutor.EXECUTOR_ID == executorId) {
            if (markLogicVersion.major >= 8) Function.PROF_EVAL_80 else Function.PROF_EVAL_50
        } else if (DefaultDebugExecutor.EXECUTOR_ID == executorId) {
            if (markLogicVersion.major >= 8) Function.DBG_EVAL_80 else Function.DBG_EVAL_50
        } else {
            null
        }
    }

    override fun createInvokeBuilder(executorId: String, markLogicVersion: MarkLogicVersion): Function? {
        return if (DefaultRunExecutor.EXECUTOR_ID == executorId) {
            if (markLogicVersion.major >= 7) Function.XDMP_INVOKE_70 else Function.XDMP_INVOKE_50
        } else if (ProfileExecutor.EXECUTOR_ID == executorId) {
            if (markLogicVersion.major >= 8) Function.PROF_INVOKE_80 else Function.PROF_INVOKE_50
        } else if (DefaultDebugExecutor.EXECUTOR_ID == executorId) {
            if (markLogicVersion.major >= 8) Function.DBG_INVOKE_80 else Function.DBG_INVOKE_50
        } else {
            null
        }
    }

    companion object {
        var INSTANCE: QueryBuilder = XQueryBuilder()
    }
}
