package com.tiny.cash.loan.card.base;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.ui.listener.OnItemClickListener;
import com.tiny.cash.loan.card.ui.listener.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;


public abstract class AbsBindingAdapter<Model, Binding extends ViewDataBinding> extends RecyclerView.Adapter<BaseBindingViewHolder<Model>> {
    private static final int EMPTY_VIEW = -1;
    protected Context context;
    protected List<Model> items;
    protected OnItemClickListener onItemClickListener;
    protected OnItemLongClickListener onItemLongClickListener;

    boolean isEmptyViewEnabled = false;

    public AbsBindingAdapter(Context context) {
        this.context = context.getApplicationContext();
    }

    public void setEmptyViewEnabled(boolean enabled) {
        isEmptyViewEnabled = enabled;
    }

    public Context getContext() {
        return context;
    }

    public void setOnItemClickListener(OnItemClickListener<Model, Binding> listener) {
        onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<Model, Binding> listener) {
        onItemLongClickListener = listener;
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        } else {
            if (isEmptyViewEnabled) {
                return items.size() == 0 ? 1 : items.size();
            } else {
                return items.size();
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (isEmptyViewEnabled) {
            if (items.size() == 0) {
                return EMPTY_VIEW;
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public BaseBindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Binding binding;
        if (viewType == EMPTY_VIEW) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(this.context), getEmptyViewBindingId(), parent, false);
        } else {
            binding = DataBindingUtil.inflate(LayoutInflater.from(this.context), this.getLayoutResId(viewType), parent, false);
            onBindItemCreate(binding);
        }
        return new BaseBindingViewHolder(binding.getRoot());
    }

    @LayoutRes
    public int getEmptyViewBindingId() {
        return R.layout.item_empty;
    }

    @Override
    public void onBindViewHolder(BaseBindingViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == EMPTY_VIEW) {

        } else {
            Binding binding = DataBindingUtil.getBinding(holder.itemView);
            Model model = null;
            if (items != null) {
                model = items.get(position);
            }
            holder.setModel(model);
            this.onBindItem(binding, model, position);
        }

    }


    @LayoutRes
    protected abstract int getLayoutResId(int viewType);

    public void addData(List<Model> data) {
        if (items == null) {
            items = new ArrayList<>();
        }
        if (data != null) {
            items.addAll(data);
        }
    }

    public List<Model> getData() {
        return items;
    }

    public void setData(List<Model> data) {
        items = data;
    }

    protected abstract void onBindItemCreate(Binding binding);

    protected abstract void onBindItem(Binding binding, Model item, int position);

    protected String getString(int resId) {
        return context.getResources().getString(resId);
    }

    protected String getString(int resId, Object... formatArgs) {
        return context.getResources().getString(resId, formatArgs);
    }

    protected int getColor(int resId) {
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(resId, context.getTheme());
        } else {
            return context.getResources().getColor(resId);
        }
    }
}
