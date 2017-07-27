package io.thingcare.bootstrap.fe.ui.design;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import io.thingcare.bootstrap.fe.view.shared.BaseView;
import org.vaadin.spring.i18n.I18N;

public class UserDetailsDesign extends BaseView {

    protected TextField firstName = new TextField();
    protected TextField lastName = new TextField();
    protected TextField email = new TextField();
    protected Button updateButton = new Button();
    protected Button cancelButton = new Button();
    protected Button deleteButton = new Button();

    public UserDetailsDesign(I18N i18n) {
        super(i18n);

        designFirstName();
        designLastName();
        designEmail();

        designButtons();
        HorizontalLayout buttonLayout = new HorizontalLayout(updateButton, cancelButton, deleteButton);
        VerticalLayout mainLayout = new VerticalLayout(firstName, lastName, email, buttonLayout);
        addComponent(mainLayout);
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
        updateButton.setCaption("Update");
        cancelButton.setCaption("Cancel");
        deleteButton.setCaption("Delete");
    }
}
