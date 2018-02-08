package com.ankhrom.base.observable;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ankhrom.base.model.ItemModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdapterRecycleBinder<T extends ItemModel> extends RecyclerView.Adapter<AdapterRecycleBinder.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<T> items;

    private RecyclerView view;

    public AdapterRecycleBinder(Context context) {

        inflater = LayoutInflater.from(context);
        items = new ObservableArrayList<>();
    }

    public AdapterRecycleBinder(Context context, List<T> items) {
        this(context);

        this.items.addAll(items);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        view = recyclerView;
    }

    public void add(T item) {

        items.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    public void add(int index, T item) {

        items.add(index, item);
        notifyItemInserted(index);
    }

    public void addAll(@NonNull Collection<? extends T> collection) {

        items.addAll(collection);
        notifyDataSetChanged();
    }

    public void addAll(T[] items) {

        Collections.addAll(this.items, items);
        notifyDataSetChanged();
    }

    public void replace(@NonNull Collection<? extends T> collection) {

        items.clear();
        addAll(collection);
    }

    public void remove(T item) {

        remove(items.indexOf(item));
    }

    public void remove(int index) {

        items.remove(index);
        notifyItemRemoved(index);
    }

    public void sort(Comparator<T> comparator) {

        Collections.sort(items, comparator);
        notifyDataSetChanged();
    }

    public void clear() {

        items.clear();
        notifyDataSetChanged();
    }

    public T get(int index) {

        return items.get(index);
    }

    public List<T> getItems() {

        return items;
    }

    @SuppressWarnings("unchecked")
    public <U> List<U> getItems(Class<U> clazz) {

        List<U> list = new ArrayList<>();

        for (T item : items) {
            if (item.getClass().isAssignableFrom(clazz)) {
                list.add((U) item);
            }
        }

        return list;
    }

    public List<T> getItemsCopy() {

        return new ArrayList<>(items);
    }

    public void notifyItemChanged(T item) {

        int index = items.indexOf(item);

        if (index > -1) {
            notifyItemChanged(index);
        }
    }

    public void post(Runnable runnable) {

        if (view != null) {
            view.post(runnable);
        }
    }

    public void postDataSetChanged() {

        post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public void postItemRemoved(final int index) {

        post(new Runnable() {
            @Override
            public void run() {
                notifyItemRemoved(index);
            }
        });
    }

    public void postItemInserted(final int index) {

        post(new Runnable() {
            @Override
            public void run() {
                notifyItemInserted(index);
            }
        });
    }

    public void postItemChanged(final int index) {

        post(new Runnable() {
            @Override
            public void run() {
                notifyItemChanged(index);
            }
        });
    }

    public void scrollTo(int index, boolean animate) {

        if (view == null) {
            return;
        }

        if (animate) {
            view.smoothScrollToPosition(index);
        } else {
            view.scrollToPosition(index);
        }
    }

    public void scrollUp(boolean animate) {

        scrollTo(0, animate);
    }

    public void scrollDown(boolean animate) {

        scrollTo(getItemCount() - 1, animate);
    }

    public void addOnDataSetChangedListener(ObservableList.OnListChangedCallback<ObservableList<T>> listener) {

        ((ObservableList<T>) items).addOnListChangedCallback(listener);
    }

    public void removeOnDataSetChangedListener(ObservableList.OnListChangedCallback<ObservableList<T>> listener) {

        ((ObservableList<T>) items).addOnListChangedCallback(listener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ViewDataBinding binding = DataBindingUtil.inflate(inflater, viewType, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ItemModel model = items.get(position);
        ViewDataBinding binding = holder.binding;
        int variable = model.getVariableBindingResource();

        model.onItemBinded(binding);

        if (variable > 0) {
            binding.setVariable(model.getVariableBindingResource(), model);
            binding.executePendingBindings();
        }
    }

    @Override
    public int getItemViewType(int position) {

        return items.get(position).getLayoutResource();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final ViewDataBinding binding;

        public ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
