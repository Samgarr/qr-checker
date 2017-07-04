package cz.lhoracek.qrchecker.util.adapter.handler;

public interface ItemBinder<T> {
    int getLayoutRes(T model);

    int getBindingVariable(T model);
}
