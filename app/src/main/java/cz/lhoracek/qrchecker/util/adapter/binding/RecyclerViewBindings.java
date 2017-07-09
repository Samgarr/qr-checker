package cz.lhoracek.qrchecker.util.adapter.binding;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Collection;

import cz.lhoracek.qrchecker.util.adapter.BindingRecyclerViewAdapter;
import cz.lhoracek.qrchecker.util.adapter.handler.ClickHandler;
import cz.lhoracek.qrchecker.util.adapter.handler.ItemBinder;
import cz.lhoracek.qrchecker.util.adapter.handler.LongClickHandler;

/**
 * Binding adapter allowing binding content to recycler view through databinding expression
 */
public abstract class RecyclerViewBindings extends BaseObservable {
    private static final int KEY_ITEMS = -123;
    private static final int KEY_CLICK_HANDLER = -124;
    private static final int KEY_LONG_CLICK_HANDLER = -125;

    /**
     * Defined getter to create BR class field BR.listener for binder inner listener binding variable
     */
    @Bindable
    public abstract View.OnClickListener getListener();

    @SuppressWarnings("unchecked")
    @BindingAdapter("items")
    public static <T> void setItems(RecyclerView recyclerView, Collection<T> items) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        recyclerView.setTag(KEY_ITEMS, items);
        if (adapter != null) {
            adapter.setItems(items);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("clickHandler")
    public static <T> void setHandler(RecyclerView recyclerView, ClickHandler<T> handler) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        recyclerView.setTag(KEY_CLICK_HANDLER, handler);
        if (adapter != null) {
            adapter.setClickHandler(handler);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("longClickHandler")
    public static <T> void setLongHandler(RecyclerView recyclerView, LongClickHandler<T> handler) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        recyclerView.setTag(KEY_LONG_CLICK_HANDLER, handler);
        if (adapter != null) {
            adapter.setLongClickHandler(handler);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("itemViewBinder")
    public static <T> void setItemViewBinder(RecyclerView recyclerView, ItemBinder<T> itemViewMapper) {
        Collection<T> items = (Collection<T>) recyclerView.getTag(KEY_ITEMS);
        ClickHandler<T> clickHandler = (ClickHandler<T>) recyclerView.getTag(KEY_CLICK_HANDLER);
        LongClickHandler<T> longClickHandler = (LongClickHandler<T>) recyclerView.getTag(KEY_LONG_CLICK_HANDLER);
        BindingRecyclerViewAdapter<T> adapter = new BindingRecyclerViewAdapter<>(itemViewMapper, items);
        if (clickHandler != null) {
            adapter.setClickHandler(clickHandler);
            adapter.setLongClickHandler(longClickHandler);
        }
        recyclerView.setAdapter(adapter);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("divider")
    public static void setItemDivider(RecyclerView recyclerView, Drawable drawable) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            DividerItemDecoration itemDivider = new DividerItemDecoration(recyclerView.getContext(),
                    ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation());
            itemDivider.setDrawable(drawable);

            recyclerView.addItemDecoration(itemDivider);
        } else {
            throw new IllegalArgumentException("Divider has to be applied to linear layout managed recycler view.");
        }
    }
}