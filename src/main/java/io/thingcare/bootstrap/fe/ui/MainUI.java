package io.thingcare.bootstrap.fe.ui;

import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;
import io.thingcare.bootstrap.fe.event.MainEventBus;
import io.thingcare.bootstrap.fe.sidebar.VaadinSideBar;
import io.thingcare.bootstrap.fe.ui.shared.BaseUI;
import io.thingcare.bootstrap.fe.view.shared.AccessDeniedView;
import io.thingcare.bootstrap.fe.view.shared.ErrorView;
import io.thingcare.bootstrap.fe.view.shared.ViewContainer;
import org.vaadin.spring.i18n.I18N;

import javax.annotation.PostConstruct;


@SpringUI(path = "/")
public class MainUI extends BaseUI {

    private final SpringViewProvider springViewProvider;

    private final SpringNavigator springNavigator;

    private final VaadinSideBar vaadinSideBar;

    private final ViewContainer viewContainer;

    private final MainEventBus mainEventBus = new MainEventBus();

    public MainUI(SpringViewProvider springViewProvider, SpringNavigator springNavigator, VaadinSideBar vaadinSideBar, ViewContainer viewContainer, I18N i18n) {
        super(i18n);
        this.springViewProvider = springViewProvider;
        this.springNavigator = springNavigator;
        this.vaadinSideBar = vaadinSideBar;
        this.viewContainer = viewContainer;
    }

    public static MainEventBus mainEventBus() {
        return ((MainUI) getCurrent()).mainEventBus;
    }

    @PostConstruct
    public void init() {
        viewContainer.setSizeFull();
        viewContainer.addStyleName("view-content");
        springNavigator.setErrorView(ErrorView.class);
        springViewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
    }

    @Override
    protected void init(VaadinRequest request) {
        addStyleName(ValoTheme.UI_WITH_MENU);
        Responsive.makeResponsive(this);
        final HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();
        layout.setSpacing(false);
        layout.addComponent(vaadinSideBar);
        layout.addComponent(viewContainer);
        layout.setExpandRatio(viewContainer, 1.0f);
        setContent(layout);
    }

    @Override
    protected String titleCode() {
        return "Boostrap Project";
    }
}
