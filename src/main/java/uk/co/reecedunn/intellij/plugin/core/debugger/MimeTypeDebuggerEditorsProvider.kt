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
package uk.co.reecedunn.intellij.plugin.core.debugger

import com.intellij.lang.Language
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.util.LocalTimeCounter
import com.intellij.xdebugger.XSourcePosition
import com.intellij.xdebugger.evaluation.EvaluationMode
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider

open class MimeTypeDebuggerEditorsProvider(private var mimeType: String): XDebuggerEditorsProvider() {
    private fun setupFile(file: PsiFile) {
    }

    override fun createDocument(project: Project, text: String, sourcePosition: XSourcePosition?, mode: EvaluationMode): Document {
        val type = fileType
        val file = PsiFileFactory.getInstance(project).createFileFromText(
            "debug.${type.defaultExtension}",
            type,
            text,
            LocalTimeCounter.currentTime(),
            true)
        setupFile(file)
        return PsiDocumentManager.getInstance(project).getDocument(file)!!
    }

    override fun getFileType(): FileType {
        val lang = Language.findInstancesByMimeType(mimeType).first()
        return lang.associatedFileType as FileType
    }
}
