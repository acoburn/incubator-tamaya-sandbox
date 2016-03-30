package org.apache.tamaya.ui.components;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import org.apache.tamaya.ui.ViewConfig;

public class PageTitleUpdater implements ViewChangeListener {
    @Override
    public boolean beforeViewChange(ViewChangeEvent event) {
        return true;
    }

    @Override
    public void afterViewChange(ViewChangeEvent event) {

        View view = event.getNewView();
        ViewConfig viewConfig = view.getClass().getAnnotation(ViewConfig.class);

        if (viewConfig != null) {
            Page.getCurrent().setTitle(viewConfig.displayName());
        }

    }
}