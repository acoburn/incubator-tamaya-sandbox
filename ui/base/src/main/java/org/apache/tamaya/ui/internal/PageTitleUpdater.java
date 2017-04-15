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
package org.apache.tamaya.ui.internal;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import org.apache.tamaya.spi.ServiceContextManager;
import org.apache.tamaya.ui.spi.MessageProvider;

/**
 * Listener that updates the page title when a new view is shown.
 */
public class PageTitleUpdater implements ViewChangeListener {

    @Override
    public boolean beforeViewChange(ViewChangeEvent event) {
        return true;
    }

    @Override
    public void afterViewChange(ViewChangeEvent event) {
        View view = event.getNewView();
        String displayName = ServiceContextManager.getServiceContext().getService(MessageProvider.class)
                .getMessage("view."+view.getClass().getSimpleName()+".name");
        if (displayName != null) {
            Page.getCurrent().setTitle(displayName);
        }

    }
}