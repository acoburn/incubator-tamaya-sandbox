/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.tamaya.karaf.shell;

import org.apache.karaf.shell.api.action.*;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.tamaya.osgi.commands.HistoryCommands;
import org.apache.tamaya.osgi.commands.TamayaConfigService;

import java.io.IOException;

@Command(scope = "tamaya", name = "tm_history_delete", description="Deletes the getHistory of changes Tamaya applied to the OSGI configuration.")
@Service
public class HistoryDeleteCommand implements Action{

    @Argument(index = 0, name = "pid", description = "Allows to filter on the given PID.",
            required = true, multiValued = false)
    String pid;

    @Reference
    private TamayaConfigService configPlugin;

    @Override
    public String execute() throws IOException {
        return HistoryCommands.clearHistory(configPlugin, pid);
    }

}