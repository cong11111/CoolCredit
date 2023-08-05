package com.tiny.cash.loan.card.ui.adapter;

import android.content.Context;

import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.base.AbsBindingAdapter;
import com.tiny.cash.loan.card.kudicredit.databinding.ItemMessageBinding;

import com.tiny.cash.loan.card.kudicredit.databinding.ItemMessageBinding;
import com.tiny.cash.loan.card.net.response.data.bean.MessageResult;


public class MessageAdapter extends AbsBindingAdapter<MessageResult.ItemsBean, ItemMessageBinding> {
    Context mContext;
    public MessageAdapter(Context context) {
        super(context);
        mContext = context;
        setData(null);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_message;
    }

    @Override
    protected void onBindItemCreate(ItemMessageBinding binding) {

    }

    public int getSelectPosition() {
        return selectPosition;
    }

    private int selectPosition = -1;

    public void setSelectPosition(int cposition) {
        selectPosition = cposition;
        notifyDataSetChanged();
    }
    @Override
    protected void onBindItem(ItemMessageBinding binding, MessageResult.ItemsBean item, int position) {
        if (item == null) {
            binding.tvMessage.setText("");
            binding.tvMessageName.setText("");
            binding.tvMessageTime.setText("");
        } else {
            binding.tvMessage.setText(item.getContent());
            binding.tvMessageName.setText(item.getTitle());
            binding.tvMessageTime.setText(item.getCtime());
        }
        binding.llLout.setOnClickListener(v -> {
            listener.itemClick(item);
        });

    }

    public OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void itemClick(MessageResult.ItemsBean item);
    }
}
