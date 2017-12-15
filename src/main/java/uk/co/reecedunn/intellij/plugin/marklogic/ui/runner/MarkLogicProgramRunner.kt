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
package uk.co.reecedunn.intellij.plugin.marklogic.ui.runner

import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.runners.DefaultProgramRunner
import uk.co.reecedunn.intellij.plugin.marklogic.ui.configuration.MarkLogicRunConfiguration
import uk.co.reecedunn.intellij.plugin.marklogic.query.QueryBuilderFactory

class MarkLogicProgramRunner : DefaultProgramRunner() {
    override fun getRunnerId(): String =
        "MarkLogicRunner"

    override fun canRun(executorId: String, profile: RunProfile): Boolean {
        if (profile !is MarkLogicRunConfiguration || DefaultDebugExecutor.EXECUTOR_ID == executorId) {
            return false
        }

        val queryBuilder = QueryBuilderFactory.createQueryBuilderForFile(profile.mainModulePath)
        return queryBuilder?.createEvalBuilder(executorId, profile.markLogicVersion) != null
    }
}
