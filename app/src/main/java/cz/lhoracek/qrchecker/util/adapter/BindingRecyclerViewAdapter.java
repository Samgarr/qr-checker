package cz.lhoracek.qrchecker.util.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.Collection;

import cz.lhoracek.qrchecker.BR;
import cz.lhoracek.qrchecker.util.adapter.handler.ClickHandler;
import cz.lhoracek.qrchecker.util.adapter.handler.ItemBinder;
import cz.lhoracek.qrchecker.util.adapter.handler.LongClickHandler;

import static android.view.View.OnClickListener;
import static android.view.View.OnLongClickListener;

/**
 * Adapter that will provide items from provided list throu ItemBinder to item views. It asks ItemBinder which layout and what variable to use for
 * binding. It is possible to create composite adapter that provide different layouts for different item types with view recycling correctly
 * implemented.
 *
 * @param <T> Type of Item, that
 */
public class BindingRecyclerViewAdapter<T> extends RecyclerView.Adapter<BindingRecyclerViewAdapter.ViewHolder> implements
        ClickHandler.ClickDelegare<T>, OnLongClickListener {
    private static final int ITEM_MODEL = -124;
    private final WeakReferenceOnListChangedCallback onListChangedCallback;
    private final ItemBinder<T> itemBinder;
    private ObservableList<T> items;
    private LayoutInflater inflater;
    private ClickHandler<T> clickHandler;
    private LongClickHandler<T> longClickHandler;

    public BindingRecyclerViewAdapter(ItemBinder<T> itemBinder, @Nullable Collection<T> items) {
        this.itemBinder = itemBinder;
        this.onListChangedCallback = new WeakReferenceOnListChangedCallback<>(this);
        setItems(items);
    }

    public ObservableList<T> getItems() {
        return items;
    }

    public void setItems(@Nullable Collection<T> items) {
        if (this.items == items) {
            return;
        }

        if (this.items != null) {
            this.items.removeOnListChangedCallback(onListChangedCallback);
            notifyItemRangeRemoved(0, this.items.size());
        }

        if (items instanceof ObservableList) {
            this.items = (ObservableList<T>) items;
            notifyItemRangeInserted(0, this.items.size());
            this.items.addOnListChangedCallback(onListChangedCallback);
        } else if (items != null) {
            this.items = new ObservableArrayList<>();
            this.items.addOnListChangedCallback(onListChangedCallback);
            this.items.addAll(items);
        } else {
            this.items = null;
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        if (items != null) {
            items.removeOnListChangedCallback(onListChangedCallback);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int layoutId) {
        if (inflater == null) {
            inflater = LayoutInflater.from(viewGroup.getContext());
        }

        ViewDataBinding binding = DataBindingUtil.inflate(inflater, layoutId, viewGroup, false);
        return new ViewHolder(binding, this);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final T item = items.get(position);
        viewHolder.binding.setVariable(itemBinder.getBindingVariable(item), item);
        viewHolder.binding.setVariable(BR.listener, viewHolder);
        viewHolder.binding.getRoot().setTag(ITEM_MODEL, item);
        viewHolder.binding.getRoot().setOnClickListener(viewHolder);
        viewHolder.binding.getRoot().setOnLongClickListener(this);
        viewHolder.binding.executePendingBindings();
    }

    /**
     * This is where the magi happens, as based on position, item is passed to ItemBinder and it decides what layout ID belong to this item type.
     * By returning layoutId, it works correctly with recyclerView viewHolder recycling.
     *
     * @param position
     * @return viewId
     */
    @Override
    public int getItemViewType(int position) {
        return itemBinder.getLayoutRes(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }


    /**
     * Click is handled by delegate, that will fetch the view ID and pass it to registered handler. This involves some magic as it is injected to
     * item view by fixed varibale name, but allow easy handling of sub-view clicks.
     *
     * @param item
     * @param v
     */
    @Override
    public void onClick(T item, View v) {
        if (clickHandler != null) {
            clickHandler.onClick(item, v.getId());
        }
    }

    /**
     * Long clicks are only whole item related
     *
     * @param v
     * @return
     */
    @Override
    public boolean onLongClick(View v) {
        if (longClickHandler != null) {
            T item = (T) v.getTag(ITEM_MODEL);
            longClickHandler.onLongClick(item);
            return true;
        }
        return false;
    }

    /**
     * This is the "hadle-them-all" generic viewHolder, that only keeps binding instace. For purposes of sub-view clicking it handles the unwrapping
     * of simple onClick listener call to item+viewId format.
     *
     * @param <T>
     */
    public static class ViewHolder<T> extends RecyclerView.ViewHolder implements OnClickListener {
        final ViewDataBinding binding;
        final WeakReference<ClickHandler.ClickDelegare<T>> clickListener;

        ViewHolder(ViewDataBinding binding, ClickHandler.ClickDelegare<T> clickListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.clickListener = new WeakReference<>(clickListener);
        }

        @Override
        public void onClick(View v) {
            if (clickListener.get() != null) {
                clickListener.get().onClick((T) binding.getRoot().getTag(ITEM_MODEL), v);
            }
        }
    }

    /**
     * As all databinding is realised through weak referencing listeners, this handles the items same way and allows propagation of insert actions,
     * that are handle with animation in recyclerView
     *
     * @param <T>
     */
    private static class WeakReferenceOnListChangedCallback<T> extends ObservableList.OnListChangedCallback {

        private final WeakReference<BindingRecyclerViewAdapter<T>> adapterReference;

        WeakReferenceOnListChangedCallback(BindingRecyclerViewAdapter<T> bindingRecyclerViewAdapter) {
            this.adapterReference = new WeakReference<>(bindingRecyclerViewAdapter);
        }

        @Override
        public void onChanged(ObservableList sender) {
            Adapter adapter = adapterReference.get();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
            Adapter adapter = adapterReference.get();
            if (adapter != null) {
                adapter.notifyItemRangeChanged(positionStart, itemCount);
            }
        }

        @Override
        public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
            Adapter adapter = adapterReference.get();
            if (adapter != null) {
                adapter.notifyItemRangeInserted(positionStart, itemCount);
            }
        }

        @Override
        public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
            Adapter adapter = adapterReference.get();
            if (adapter != null) {
                adapter.notifyItemMoved(fromPosition, toPosition);
            }
        }

        @Override
        public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
            Adapter adapter = adapterReference.get();
            if (adapter != null) {
                adapter.notifyItemRangeRemoved(positionStart, itemCount);
            }
        }

    }

    public void setClickHandler(ClickHandler<T> clickHandler) {
        this.clickHandler = clickHandler;
    }

    public void setLongClickHandler(LongClickHandler<T> clickHandler) {
        this.longClickHandler = clickHandler;
    }
}