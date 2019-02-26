package com.appreciated.masterdetail.view.masterdetail;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.vaddon.CustomMediaQuery;
import org.vaddon.css.query.MediaQuery;
import org.vaddon.css.query.values.WidthAttributes;

@Tag("master-detail-view")
@HtmlImport("frontend://com/github/appreciated/master-detail/master-detail-view.html")
public abstract class MasterDetailView<M extends Component & MasterView<T>, D extends Component & HasUrlParameter<T> & DetailView<T>, T> extends PolymerTemplate<TemplateModel> implements HasSize, HasUrlParameter<T> {

    @Id("master-content")
    Div masterContent;

    @Id("detail-content")
    Div detailContent;

    boolean isMasterAndDetail = false;

    private CustomMediaQuery isMasterAndDetailQuery;
    private Class<D> detailViewClass;
    private Component oldDetailView;

    public MasterDetailView() {
        setSizeFull();
    }

    public void setMaster(M component) {
        component.getElement().setAttribute("slot", "master-content-slot");
        getElement().appendChild(component.getElement());
        isMasterAndDetailQuery = new CustomMediaQuery(aBoolean -> setMasterAndDetail(!aBoolean));
        isMasterAndDetailQuery.setQuery(new MediaQuery(new WidthAttributes.MaxWidth("600px")));
        getElement().appendChild(isMasterAndDetailQuery.getElement());
        component.setNavigationListener(parameter -> {
            if (isMasterAndDetail) {
                setParameter(null, parameter);
            } else {
                UI.getCurrent().navigate(detailViewClass, parameter);
            }
        });
    }

    private void setMasterAndDetail(boolean masterAndDetail) {
        System.out.println("setMasterAndDetail: " + masterAndDetail);
        isMasterAndDetail = masterAndDetail;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, T t) {
        System.out.println("isMasterAndDetail: " + isMasterAndDetail);
        if (isMasterAndDetail) {
            try {
                if (oldDetailView != null) {
                    getElement().removeChild(oldDetailView.getElement());
                }
                D instance = detailViewClass.newInstance();
                instance.getElement().setAttribute("slot", "detail-content-slot");
                instance.setParameter(null, t);
                getElement().appendChild(instance.getElement());
                oldDetailView = instance;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void setDetail(Class<D> tClass) {
        this.detailViewClass = tClass;
    }

    /**
     * @return
     */
    public Div getMasterContent() {
        return masterContent;
    }

    /**
     * @return
     */
    public Div getDetailContent() {
        return detailContent;
    }
}
