package com.github.appreciated.masterdetail;


public interface MasterView<T> {
    void setNavigationListener(MasterViewNavigationElementListener<T> listener);

    void setActiveElement(T element);

    void onMasterStateChanged(boolean masterOnly);
}
