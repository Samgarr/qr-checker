package cz.lhoracek.qrchecker.util.adapter.handler;

/**
 * Provides handling of long click on recycler view item
 * @param <T>
 */
public interface LongClickHandler<T> {
    void onLongClick(T viewModel);
}
