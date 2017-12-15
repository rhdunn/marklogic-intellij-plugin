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

import com.intellij.execution.ExecutionException
import com.intellij.execution.Executor
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.filters.RegexpFilter
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.ui.ConsoleView
import com.intellij.openapi.actionSystem.AnAction
import uk.co.reecedunn.intellij.plugin.marklogic.api.Connection
import uk.co.reecedunn.intellij.plugin.marklogic.ui.configuration.MarkLogicRunConfiguration
import uk.co.reecedunn.intellij.plugin.marklogic.query.QueryBuilderFactory

class MarkLogicRunProfileState(environment: ExecutionEnvironment?) : CommandLineState(environment) {
    init {
        if (environment != null) {
            addConsoleFilters(RegexpFilter(environment.project, "\$FILE_PATH$:\$LINE$"))
        }
    }

    override fun createActions(console: ConsoleView?, processHandler: ProcessHandler, executor: Executor?): Array<AnAction> =
        AnAction.EMPTY_ARRAY

    @Throws(ExecutionException::class)
    override fun startProcess(): ProcessHandler {
        val configuration = environment.runProfile as MarkLogicRunConfiguration

        val queryBuilder = QueryBuilderFactory.createQueryBuilderForFile(configuration.mainModulePath) ?: throw ExecutionException("Unsupported query file: " + configuration.mainModulePath)

        val function = queryBuilder.createEvalBuilder(environment.executor.id, configuration.markLogicVersion) ?: throw ExecutionException("Cannot run the query file with MarkLogic " + configuration.markLogicVersion)

        val connection = createConnection(configuration)
        val builder = connection.createEvalRequestBuilder()
        builder.contentDatabase = configuration.contentDatabase
        builder.XQuery = function.buildQuery(configuration)
        return MarkLogicRequestHandler(builder.build(), configuration.mainModulePath)
    }

    fun run(query: String, handler: MarkLogicResultsHandler, configuration: MarkLogicRunConfiguration): Boolean {
        try {
            val connection = createConnection(configuration)
            val builder = connection.createEvalRequestBuilder()
            builder.contentDatabase = configuration.contentDatabase
            builder.XQuery = query
            val restHandler = MarkLogicRequestHandler(builder.build(), "/eval")
            return restHandler.run(handler)
        } catch (e: Exception) {
            return false
        }
    }

    @Throws(ExecutionException::class)
    private fun createConnection(configuration: MarkLogicRunConfiguration): Connection {
        return Connection.newConnection(
            configuration.server!!.hostname,
            configuration.server!!.appServerPort,
            configuration.server!!.username,
            configuration.server!!.password,
            configuration.markLogicVersion)
    }
}
