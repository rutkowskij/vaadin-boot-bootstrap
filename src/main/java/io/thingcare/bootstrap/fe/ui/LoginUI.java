package io.thingcare.bootstrap.fe.ui;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import io.thingcare.bootstrap.fe.ui.shared.BaseUI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.security.shared.VaadinSharedSecurity;

import javax.annotation.PostConstruct;

@Slf4j
@SpringUI(path = "/login")
public class LoginUI extends BaseUI {
    public static final String TITLE_CODE = "login.title";

    private final VaadinSharedSecurity vaadinSecurity;

    private TextField username;

    private PasswordField password;

    private CheckBox rememberMe;

    private Button login;

    private Label loginFailedLabel;
    private Label loggedOutLabel;

    public LoginUI(VaadinSharedSecurity vaadinSecurity, I18N i18n) {
        super(i18n);
        this.vaadinSecurity = vaadinSecurity;
    }

    @PostConstruct
    public void init() {
        addStyleName("login-view");
        Responsive.makeResponsive(this);
    }

    @Override
    protected void init(VaadinRequest request) {
        Component loginLayout = buildLoginLayout(request);
        VerticalLayout rootLayout = new VerticalLayout(loginLayout);
        rootLayout.setSizeFull();
        rootLayout.setMargin(false);
        rootLayout.setSpacing(false);
        rootLayout.setComponentAlignment(loginLayout, Alignment.MIDDLE_CENTER);
        super.setContent(rootLayout);
        super.setSizeFull();
    }

    private Component buildLoginLayout(VaadinRequest request) {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setMargin(false);
        loginPanel.addStyleName("login-panel");
        Responsive.makeResponsive(loginPanel);

        if (request.getParameter("logout") != null) {
            loggedOutLabel = new Label(i18n.get("login.logged.out"));
            loggedOutLabel.addStyleName(ValoTheme.LABEL_SUCCESS);
            loggedOutLabel.setWidth(100, Unit.PERCENTAGE);
            loginPanel.addComponent(loggedOutLabel);
        }
        loginFailedLabel = new Label();
        loginFailedLabel.setWidth(100, Unit.PERCENTAGE);
        loginFailedLabel.addStyleName(ValoTheme.LABEL_FAILURE);
        loginFailedLabel.setVisible(false);

        loginPanel.addComponent(loginFailedLabel);

        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());
        loginPanel.addComponent(rememberMe = new CheckBox(i18n.get("login.remember.me"), true));
        return loginPanel;
    }

    private Component buildLabels() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        Label welcome = new Label(i18n.get("login.welcome"));
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

        Label title = new Label(i18n.get("login.project.name"));
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        labels.addComponent(title);
        return labels;
    }

    private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.addStyleName("fields");

        username = new TextField(i18n.get("login.username"));
        username.setIcon(VaadinIcons.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        password = new PasswordField(i18n.get("login.password"));
        password.setIcon(VaadinIcons.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        login = new Button(i18n.get("login.login"));
        login.addStyleName(ValoTheme.BUTTON_PRIMARY);
        login.setDisableOnClick(true);
        login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        login.focus();

        fields.addComponents(username, password, login);
        fields.setComponentAlignment(login, Alignment.BOTTOM_LEFT);
        login.addClickListener(e -> login());
        return fields;
    }

    private void login() {
        try {
            vaadinSecurity.login(username.getValue(), password.getValue(), rememberMe.getValue());
        } catch (AuthenticationException ex) {
            username.focus();
            username.selectAll();
            password.setValue("");
            loginFailedLabel.setValue(String.format(i18n.get("login.failure"), ex.getMessage()));
            loginFailedLabel.setVisible(true);
            if (loggedOutLabel != null) {
                loggedOutLabel.setVisible(false);
            }
        } catch (Exception ex) {
            Notification.show(i18n.get("common.error.unexpected"), ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            log.error("Unexpected error while logging in", ex);
        } finally {
            login.setEnabled(true);
        }
    }

    @Override
    protected String titleCode() {
        return TITLE_CODE;
    }
}
