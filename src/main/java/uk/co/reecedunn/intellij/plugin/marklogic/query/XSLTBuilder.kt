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

import com.intellij.execution.executors.DefaultRunExecutor
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicVersion
import uk.co.reecedunn.intellij.plugin.marklogic.ui.profile.ProfileExecutor

object XSLTBuilder : QueryBuilder {
    override fun createEvalBuilder(executorId: String, markLogicVersion: MarkLogicVersion): Function? {
        return when (executorId) {
            DefaultRunExecutor.EXECUTOR_ID -> Function.XDMP_XSLT_EVAL_50
            ProfileExecutor.EXECUTOR_ID -> Function.PROF_XSLT_EVAL_50
            else -> null
        }
    }

    override fun createInvokeBuilder(executorId: String, markLogicVersion: MarkLogicVersion): Function? {
        return when (executorId) {
            DefaultRunExecutor.EXECUTOR_ID -> Function.XDMP_XSLT_INVOKE_50
            ProfileExecutor.EXECUTOR_ID -> Function.PROF_XSLT_INVOKE_50
            else -> null
        }
    }
}
