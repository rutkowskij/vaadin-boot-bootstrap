package io.thingcare.bootstrap.fe.ui.design;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import io.thingcare.bootstrap.be.security.User;
import io.thingcare.bootstrap.fe.view.security.UserDetailsView;
import io.thingcare.bootstrap.fe.view.shared.BaseView;
import org.vaadin.spring.i18n.I18N;

public class UserManagementDesign extends BaseView {
    protected TextField search = new TextField();
    protected Button addButton = new Button();
    protected Grid<User> userGrid = new Grid<>();
    protected UserDetailsView userDetailsView;

    public UserManagementDesign(I18N i18n) {
        super(i18n);
        this.userDetailsView = new UserDetailsView(i18n);

        designSearch();
        designAddButton();
        designUserGrid();

        HorizontalLayout searchLayout = designSearchLayout();
        HorizontalLayout topBar = designTopBar(searchLayout);
        CssLayout gridLayout = designGridLayout();

        VerticalLayout mainLayout = designMainLayout(topBar, gridLayout);
        addComponent(mainLayout);
    }


    private VerticalLayout designMainLayout(HorizontalLayout topBar, CssLayout gridLayout) {
        VerticalLayout main = new VerticalLayout(topBar, gridLayout, userDetailsView);
        main.setResponsive(true);
        main.setSizeFull();
        return main;
    }

    private CssLayout designGridLayout() {
        CssLayout content = new CssLayout(userGrid);
        content.setSizeFull();
        return content;
    }

    private void designUserGrid() {
        userGrid.getEditor().setEnabled(true);
        userGrid.setSizeFull();
        userGrid.addColumn(User::getId).setCaption("Id");
        userGrid.addColumn(User::getFirstName).setCaption("First name");
        userGrid.addColumn(User::getLastName).setCaption("Last name");
        userGrid.addColumn(User::getEmail).setCaption("Email");
        userGrid.addColumn(User::getCreatedDate).setCaption("Create date");
        userGrid.addColumn(User::getRoles).setCaption("Roles");
    }

    private HorizontalLayout designSearchLayout() {
        HorizontalLayout searchLayout = new HorizontalLayout(search);
        searchLayout.setWidth(100, Unit.PERCENTAGE);
        searchLayout.setComponentAlignment(search, Alignment.MIDDLE_LEFT);
        return searchLayout;
    }

    private HorizontalLayout designTopBar(HorizontalLayout searchLayout) {
        HorizontalLayout topBar = new HorizontalLayout(searchLayout, addButton);
        topBar.setWidth(100, Unit.PERCENTAGE);
        topBar.setHeight(50, Unit.PIXELS);
        topBar.setComponentAlignment(searchLayout, Alignment.MIDDLE_LEFT);
        topBar.setComponentAlignment(addButton, Alignment.MIDDLE_RIGHT);
        return topBar;
    }

    private void designAddButton() {
        addButton.setIcon(VaadinIcons.PLUS);
        addButton.setCaption("Add new");
    }

    private void designSearch() {
        search.setId("userSearch");
        search.setPlaceholder("Search");
        search.setWidth(100, Unit.PERCENTAGE);
    }
}
