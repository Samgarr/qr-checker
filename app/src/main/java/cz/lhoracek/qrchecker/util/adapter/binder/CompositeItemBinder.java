package cz.lhoracek.qrchecker.util.adapter.binder;

import my.com.iflix.core.ui.bindings.recyclerview.ItemBinder;

/**
 * Takes list of ConditionalItemBidenrs and asks each of them in order as they are defined if it can hadle the itemType. At lease one of the
 * ConditionalItemBinders has to be able to handle the item or IllegalStateException is thrown.
 *
 * @param <T>
 */
public class CompositeItemBinder<T> implements ItemBinder<T> {
    private final ConditionalDataBinder<T>[] conditionalDataBinders;

    public CompositeItemBinder(ConditionalDataBinder<T>... conditionalDataBinders) {
        this.conditionalDataBinders = conditionalDataBinders;
    }

    @Override
    public int getLayoutRes(T model) {
        for (int i = 0; i < conditionalDataBinders.length; i++) {
            ConditionalDataBinder<T> dataBinder = conditionalDataBinders[i];
            if (dataBinder.canHandle(model)) {
                return dataBinder.getLayoutRes(model);
            }
        }

        throw new IllegalStateException();
    }

    @Override
    public int getBindingVariable(T model) {
        for (int i = 0; i < conditionalDataBinders.length; i++) {
            ConditionalDataBinder<T> dataBinder = conditionalDataBinders[i];
            if (dataBinder.canHandle(model)) {
                return dataBinder.getBindingVariable(model);
            }
        }

        throw new IllegalStateException();
    }
}
