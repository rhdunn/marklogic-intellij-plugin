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
package uk.co.reecedunn.intellij.plugin.marklogic.debugger.stack.view

import com.intellij.lang.Language
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFileFactory
import com.intellij.util.LocalTimeCounter
import com.intellij.xdebugger.XDebugProcess
import com.intellij.xdebugger.XDebugProcessStarter
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XSourcePosition
import com.intellij.xdebugger.evaluation.EvaluationMode
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider
import com.intellij.xdebugger.frame.XExecutionStack

private class MarkLogicDebugEditorsProvider(private var mimeType: String): XDebuggerEditorsProvider() {
    override fun createDocument(project: Project, text: String, sourcePosition: XSourcePosition?, mode: EvaluationMode): Document {
        val type = fileType
        val psiFile = PsiFileFactory.getInstance(project).createFileFromText(
            "debug." + type.getDefaultExtension(),
            type,
            text,
            LocalTimeCounter.currentTime(),
            true)
        return PsiDocumentManager.getInstance(project).getDocument(psiFile)!!
    }

    override fun getFileType(): FileType {
        val lang = Language.findInstancesByMimeType(mimeType).first()
        return lang.associatedFileType as FileType
    }
}

private class MarkLogicErrorProcess(session: XDebugSession): XDebugProcess(session) {
    override fun getEditorsProvider(): XDebuggerEditorsProvider {
        return MarkLogicDebugEditorsProvider("application/xquery")
    }
}

class MarkLogicErrorProcessStarter(val error: XExecutionStack): XDebugProcessStarter() {
    override fun start(session: XDebugSession): XDebugProcess {
        return MarkLogicErrorProcess(session)
    }
}
