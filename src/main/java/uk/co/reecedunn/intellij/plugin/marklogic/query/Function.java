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
package uk.co.reecedunn.intellij.plugin.marklogic.query;

import uk.co.reecedunn.intellij.plugin.marklogic.query.options.EvalOptionsBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.query.options.OptionsBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.query.vars.KeyValueVarsBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.query.vars.MapVarsBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.query.vars.VarsBuilder;

public enum Function {
    DBG_EVAL_50(
        "dbg:eval($query, $vars, $options)",
        KeyValueVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE),
    DBG_EVAL_80(
        "dbg:eval($query, $vars, $options)",
        MapVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE),

    DBG_INVOKE_50(
        "dbg:invoke($path, $vars, $options)",
        KeyValueVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE),
    DBG_INVOKE_80(
        "dbg:invoke($path, $vars, $options)",
        MapVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE),

    PROF_EVAL_50(
        "prof:eval($query, $vars, $options)",
        KeyValueVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE),
    PROF_EVAL_80(
        "prof:eval($query, $vars, $options)",
        MapVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE),

    PROF_INVOKE_50(
        "prof:invoke($path, $vars, $options)",
        KeyValueVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE),
    PROF_INVOKE_80(
        "prof:invoke($path, $vars, $options)",
        MapVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE),

    SEM_SPARQL_70(
        "sem:sparql($query, $vars)",
        MapVarsBuilder.INSTANCE,
        null),

    SEM_SPARQL_UPDATE_80(
            "sem:sparql-update($query, $vars)",
            MapVarsBuilder.INSTANCE,
            null),

    XDMP_EVAL_50(
        "xdmp:eval($query, $vars, $options)",
        KeyValueVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE),
    XDMP_EVAL_70(
        "xdmp:eval($query, $vars, $options)",
        MapVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE),

    XDMP_INVOKE_50(
        "xdmp:invoke($path, $vars, $options)",
        KeyValueVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE),
    XDMP_INVOKE_70(
        "xdmp:invoke($path, $vars, $options)",
        MapVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE),

    XDMP_JAVASCRIPT_EVAL_80(
        "xdmp:javascript-eval($query, $vars, $options)",
        MapVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE),

    XDMP_SQL_80(
        "xdmp:sql($query)",
        null,
        null),
    XDMP_SQL_90(
        "xdmp:sql($query, (), $vars)",
        MapVarsBuilder.INSTANCE,
        null);

    private final String function;
    private final VarsBuilder varsBuilder;
    private final OptionsBuilder optionsBuilder;

    Function(String function, VarsBuilder varsBuilder, OptionsBuilder optionsBuilder) {
        this.function = function;
        this.varsBuilder = varsBuilder;
        this.optionsBuilder = optionsBuilder;
    }

    public String getFunction() {
        return function;
    }

    public VarsBuilder getVarsBuilder() {
        return varsBuilder;
    }

    public OptionsBuilder getOptionsBuilder() {
        return optionsBuilder;
    }
}
