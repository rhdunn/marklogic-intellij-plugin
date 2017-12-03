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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.debugger

import com.intellij.ui.SimpleTextAttributes
import com.intellij.xdebugger.frame.*
import javax.swing.Icon

class CompositeNode : XCompositeNode {
    var children = XValueChildrenList()

    override fun setAlreadySorted(alreadySorted: Boolean) {
    }

    override fun tooManyChildren(remaining: Int) {
    }

    override fun setErrorMessage(errorMessage: String) {
        setErrorMessage(errorMessage, null)
    }

    override fun setErrorMessage(errorMessage: String, link: XDebuggerTreeNodeHyperlink?) {
    }

    override fun isObsolete(): Boolean = false

    override fun addChildren(children: XValueChildrenList, last: Boolean) {
        this.children = children
    }

    override fun setMessage(message: String, icon: Icon?, attributes: SimpleTextAttributes, link: XDebuggerTreeNodeHyperlink?) {
    }
}
