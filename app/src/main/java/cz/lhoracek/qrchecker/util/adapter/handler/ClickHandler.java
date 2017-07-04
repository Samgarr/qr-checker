package cz.lhoracek.qrchecker.util.adapter.handler;

import android.view.View;

/**
 * Allows handling click on item and its sub-views
 * @param <T>
 */
public interface ClickHandler<T> {
    void onClick(T viewModel, int viewId);

    interface ClickDelegare<T> {
        void onClick(T item, View v);
    }
}