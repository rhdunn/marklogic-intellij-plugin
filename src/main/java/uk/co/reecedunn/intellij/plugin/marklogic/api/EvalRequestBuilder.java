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
package uk.co.reecedunn.intellij.plugin.marklogic.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class EvalRequestBuilder {
    private String xquery = null;
    private String javascript = null;
    private final HashMap<QName, Item> vars = new HashMap<>();
    private String database = null;
    private String txid = null;

    protected EvalRequestBuilder() {
    }

    public void setXQuery(String xquery) {
        this.xquery = xquery;
        this.javascript = null; // There can only be one!
    }

    public String getXQuery() {
        return xquery;
    }

    public void setJavaScript(String javascript) {
        this.xquery = null; // There can only be one!
        this.javascript = javascript;
    }

    public String getJavaScript() {
        return javascript;
    }

    public void addVariables(Map<QName, Item> variables) {
        for (Map.Entry<QName, Item> variable: variables.entrySet()) {
            vars.put(variable.getKey(), variable.getValue());
        }
    }

    public void addVariable(QName name, Item value) {
        vars.put(name, value);
    }

    public Item getVariable(QName name) {
        return vars.get(name);
    }

    public Set<QName> getVariableNames() {
        return vars.keySet();
    }

    public void setContentDatabase(String database) {
        this.database = database;
    }

    public String getContentDatabase() {
        return database;
    }

    public void setTransactionID(String txid) {
        this.txid = txid;
    }

    public String getTransactionID() {
        return txid;
    }

    public abstract Request build();
}
