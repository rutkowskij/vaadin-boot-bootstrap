package io.thingcare.bootstrap.fe.ui.design.security;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import io.thingcare.bootstrap.fe.view.shared.BaseView;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.i18n.I18N;

public class UserDetailsDesign extends BaseView {

    protected TextField firstName = new TextField();
    protected TextField lastName = new TextField();
    protected TextField email = new TextField();
    protected Button saveButton = new Button();
    protected Button cancelButton = new Button();

    protected HorizontalLayout topBar;
    protected VerticalLayout mainLayout;

    public UserDetailsDesign(I18N i18n, EventBus.ViewEventBus eventBus) {
        super(i18n, eventBus);

        designFirstName();
        designLastName();
        designEmail();

        designButtons();
        topBar = designTopBar();
        mainLayout = new VerticalLayout(topBar, firstName, lastName, email);
        addComponent(mainLayout);
    }

    private HorizontalLayout designTopBar() {
        HorizontalLayout leftSideBar = new HorizontalLayout();
        HorizontalLayout topBar = new HorizontalLayout(leftSideBar, saveButton, cancelButton);
        topBar.setWidth(100, Unit.PERCENTAGE);
        topBar.setHeight(50, Unit.PIXELS);
        topBar.setExpandRatio(leftSideBar, 1.0f);
        topBar.setComponentAlignment(saveButton, Alignment.MIDDLE_RIGHT);
        topBar.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
        return topBar;
    }


    private void designFirstName() {
        firstName.setCaption(i18n.get("First name"));
    }

    private void designLastName() {
        lastName.setCaption(i18n.get("Last name"));
    }

    private void designEmail() {
        email.setCaption(i18n.get("Email"));
    }

    private void designButtons() {
        saveButton.setCaption("Save");
        saveButton.setStyleName(ValoTheme.BUTTON_LARGE);
        saveButton.setStyleName(ValoTheme.BUTTON_FRIENDLY, true);

        cancelButton.setCaption("Cancel");
        cancelButton.setStyleName(ValoTheme.BUTTON_LARGE);
    }
}
