package cz.lhoracek.qrchecker.util.adapter.binder;

/**
 * Conditional binder can decide if it can hadle given itemType. This is used in Composite DataBinder
 * @param <T>
 */
public abstract class ConditionalDataBinder<T> extends ItemBinderBase<T> {
    public ConditionalDataBinder(int bindingVariable, int layoutId) {
        super(bindingVariable, layoutId);
    }

    public abstract boolean canHandle(T model);

    public boolean canHandle(int layoutId) {
        return this.layoutId == layoutId;
    }
}
