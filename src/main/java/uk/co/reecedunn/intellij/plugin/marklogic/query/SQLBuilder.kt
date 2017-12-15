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

class SQLBuilder private constructor() : QueryBuilder {
    override fun createEvalBuilder(executorId: String, markLogicVersion: MarkLogicVersion): Function? {
        return if (DefaultRunExecutor.EXECUTOR_ID == executorId && markLogicVersion.major >= 8) {
            if (markLogicVersion.major >= 9) Function.XDMP_SQL_90 else Function.XDMP_SQL_80
        } else {
            null
        }
    }

    override fun createInvokeBuilder(executorId: String, markLogicVersion: MarkLogicVersion): Function? {
        return null
    }

    companion object {
        var INSTANCE: QueryBuilder = SQLBuilder()
    }
}
