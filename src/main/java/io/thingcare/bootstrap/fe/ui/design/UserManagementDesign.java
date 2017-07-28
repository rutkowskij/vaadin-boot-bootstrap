package io.thingcare.bootstrap.fe.ui.design;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import io.thingcare.bootstrap.be.security.User;
import io.thingcare.bootstrap.fe.view.shared.BaseView;
import org.vaadin.spring.i18n.I18N;

import java.util.stream.Collectors;

public class UserManagementDesign extends BaseView {
    protected TextField search = new TextField();
    protected Button addButton = new Button();
    protected Button disableButton = new Button();
    protected Grid<User> userGrid = new Grid<>();
//    protected UserDetailsView userDetailsView;

    public UserManagementDesign(I18N i18n) {
        super(i18n);
//        this.userDetailsView = new UserDetailsView(i18n);

        designSearch();
        designAddButton();
        designDisableButton();
        designUserGrid();

        HorizontalLayout searchLayout = designSearchLayout();
        HorizontalLayout topBar = designTopBar(searchLayout);
        CssLayout gridLayout = designGridLayout();

        VerticalLayout mainLayout = designMainLayout(topBar, gridLayout);

        setSizeFull();
        addComponent(mainLayout);
    }


    private VerticalLayout designMainLayout(HorizontalLayout topBar, CssLayout gridLayout) {
        VerticalLayout main = new VerticalLayout(topBar, gridLayout);
        main.setExpandRatio(gridLayout, 1f);
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
        userGrid.addStyleName(ValoTheme.LABEL_HUGE);
        userGrid.addColumn(User::getEmail).setCaption("Email");
        userGrid.addColumn(User::getFirstName).setCaption("First name");
        userGrid.addColumn(User::getLastName).setCaption("Last name");
        userGrid.addColumn(User::getRoles, roles ->
                roles.stream().map(role -> role.getType().name()).collect(Collectors.joining("\n"))).setCaption("Roles");
    }

    private HorizontalLayout designSearchLayout() {
        HorizontalLayout searchLayout = new HorizontalLayout(search);
        searchLayout.setWidth(100, Unit.PERCENTAGE);
        searchLayout.setComponentAlignment(search, Alignment.MIDDLE_LEFT);
        return searchLayout;
    }

    private HorizontalLayout designTopBar(HorizontalLayout searchLayout) {
        HorizontalLayout topBar = new HorizontalLayout(searchLayout, addButton, disableButton);
        topBar.setWidth(100, Unit.PERCENTAGE);
        topBar.setHeight(50, Unit.PIXELS);
        topBar.setExpandRatio(searchLayout, 1.0f);
        topBar.setComponentAlignment(searchLayout, Alignment.MIDDLE_LEFT);
        topBar.setComponentAlignment(addButton, Alignment.MIDDLE_RIGHT);
        topBar.setComponentAlignment(disableButton, Alignment.MIDDLE_RIGHT);
        return topBar;
    }

    private void designAddButton() {
        addButton.setIcon(VaadinIcons.PLUS);
        addButton.setStyleName(ValoTheme.BUTTON_LARGE);
        addButton.setStyleName(ValoTheme.BUTTON_FRIENDLY, true);
        addButton.setCaption("Add new");
        addButton.setResponsive(true);
    }

    private void designDisableButton() {
        disableButton.setEnabled(false);
        disableButton.setIcon(VaadinIcons.LOCK);
        disableButton.setStyleName(ValoTheme.BUTTON_LARGE);
        disableButton.setStyleName(ValoTheme.BUTTON_DANGER, true);
        disableButton.setResponsive(true);
        disableButton.setCaption("Disable");
    }

    private void designSearch() {
        search.setId("userSearch");
        search.setPlaceholder("Search");
        search.setStyleName(ValoTheme.TEXTFIELD_LARGE);
        search.setWidth(80, Unit.PERCENTAGE);
    }
}
