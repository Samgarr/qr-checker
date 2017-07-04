package cz.lhoracek.qrchecker.util.adapter.binder;

import cz.lhoracek.qrchecker.util.adapter.handler.ItemBinder;

/**
 * Base binder just represents tuple of layoutId and variable inside it, that expects the adapter item.
 * @param <T>
 */
public class ItemBinderBase<T> implements ItemBinder<T> {
    protected final int bindingVariable;
    protected final int layoutId;

    public ItemBinderBase(int bindingVariable, int layoutId) {
        this.bindingVariable = bindingVariable;
        this.layoutId = layoutId;
    }

    public int getLayoutRes(T model) {
        return layoutId;
    }

    public int getBindingVariable(T model) {
        return bindingVariable;
    }
}
