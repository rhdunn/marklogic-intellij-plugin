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

import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessOutputTypes
import uk.co.reecedunn.intellij.plugin.marklogic.api.Item
import uk.co.reecedunn.intellij.plugin.marklogic.api.Request

import java.io.IOException
import java.io.OutputStream

class MarkLogicRequestHandler(private val request: Request, private val mainModulePath: String) : ProcessHandler(), MarkLogicResultsHandler {
    override fun destroyProcessImpl() {}

    override fun detachProcessImpl() {}

    override fun detachIsDefault(): Boolean =
        false

    override fun getProcessInput(): OutputStream? =
        null

    override fun startNotify() {
        super.startNotify()
        try {
            run(this)
        } catch (e: IOException) {
            throw RuntimeException(e)
        } finally {
            notifyProcessDetached()
        }
    }

    @Throws(IOException::class)
    fun run(handler: MarkLogicResultsHandler): Boolean {
        val response = request.run()
        val results = response.items
        response.close()

        handler.onStart()
        for (item in results) {
            handler.onItem(item)
        }
        handler.onCompleted()
        return true
    }

    override fun onException(e: Exception) {
        e.message?.let { notifyTextAvailable("$it\n", ProcessOutputTypes.STDOUT) }
    }

    override fun onStart() {}

    override fun onItem(item: Item) {
        notifyTextAvailable("----- ${item.itemType} [${item.contentType}]\n", ProcessOutputTypes.STDOUT)
        notifyTextAvailable("${item.content}\n", ProcessOutputTypes.STDOUT)
    }

    override fun onCompleted() {}
}
