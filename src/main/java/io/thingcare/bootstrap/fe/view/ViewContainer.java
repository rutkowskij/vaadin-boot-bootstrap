package io.thingcare.bootstrap.fe.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.ValoTheme;


@SpringViewDisplay
public class ViewContainer extends Panel implements ViewDisplay {

    public ViewContainer() {
        setStyleName(ValoTheme.PANEL_BORDERLESS);
    }

    @Override
    public void showView(View view) {
        setContent((Component) view);
    }
}
