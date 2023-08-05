package com.tiny.cash.loan.card.base;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class BaseBindingViewHolder<Model> extends RecyclerView.ViewHolder {
    Model model;

    public BaseBindingViewHolder(View itemView) {
        super(itemView);
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
