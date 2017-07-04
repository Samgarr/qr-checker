package cz.lhoracek.qrchecker.util.adapter.binder;

/**
 * Allows binding of list items to layout by type checking
 */

public class TypeHandlingContitionalItemBinder<T> extends ConditionalDataBinder<T> {
    private Class<? extends T> itemClass;

    public TypeHandlingContitionalItemBinder(int bindingVariable, int layoutId, Class<? extends T> itemClass) {
        super(bindingVariable, layoutId);
        this.itemClass = itemClass;
    }

    @Override
    public boolean canHandle(T model) {
        return itemClass.isAssignableFrom(model.getClass());
    }
}
