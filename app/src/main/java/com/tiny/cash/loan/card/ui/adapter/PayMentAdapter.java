package com.tiny.cash.loan.card.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tiny.cash.loan.card.kudicredit.R;

import com.tiny.cash.loan.card.net.response.data.bean.BankList;

import java.util.ArrayList;
import java.util.List;

public class PayMentAdapter extends BaseAdapter {
    private Context context;
    private List<BankList.CardList> mList;

    public PayMentAdapter(Context context) {
        mList = new ArrayList<>();
        this.context = context;
    }

    public String getSelectData() {
        if (selectPosition < mList.size()){
            return mList.get(selectPosition).getCardNumber();
        }
        return "";
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int cposition) {
        selectPosition = cposition;
        notifyDataSetChanged();
    }

    public void setData(List<BankList.CardList> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private int selectPosition = -1;
    boolean click = false;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_payment, parent, false);
            holder = new ViewHolder();
            holder.cbAgree = convertView.findViewById(R.id.cb_agree);
            holder.llItem = convertView.findViewById(R.id.ll_item);
            holder.tv_bankNum = convertView.findViewById(R.id.tv_bankNum);
            holder.iv_bank_icon = convertView.findViewById(R.id.iv_bank_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mList.get(position) != null) {
            String cardNumber = mList.get(position).getCardNumber();
            holder.tv_bankNum.setText(cardNumber.substring(0,cardNumber.length()-8)+"****"+cardNumber.substring(cardNumber.length()-4));
        }

        if (click) {
            if (selectPosition == position) {
                holder.cbAgree.setVisibility(View.VISIBLE);
                holder.tv_bankNum.setTextColor(context.getResources().getColor(R.color.color_FFFFFF));
                holder.iv_bank_icon.setTextColor(context.getResources().getColor(R.color.color_FFFFFF));
                holder.llItem.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_item_sel_card));
            }else {
                holder.llItem.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_item_card));
                holder.cbAgree.setVisibility(View.GONE);
                holder.tv_bankNum.setTextColor(context.getResources().getColor(R.color.color_333333));
                holder.iv_bank_icon.setTextColor(context.getResources().getColor(R.color.color_333333));
            }

        }else {
            if (selectPosition == -1 && position == 0) {
                holder.cbAgree.setVisibility(View.VISIBLE);
                holder.llItem.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_item_sel_card));
                holder.tv_bankNum.setTextColor(context.getResources().getColor(R.color.color_FFFFFF));
                holder.iv_bank_icon.setTextColor(context.getResources().getColor(R.color.color_FFFFFF));
                selectPosition = position;
                click = true;
            } else {
                holder.cbAgree.setVisibility(View.GONE);
                holder.llItem.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_item_card ));
                holder.tv_bankNum.setTextColor(context.getResources().getColor(R.color.color_333333));
                holder.iv_bank_icon.setTextColor(context.getResources().getColor(R.color.color_333333));
            }
        }

        holder.llItem.setOnClickListener(view -> {
            click = true;
            selectPosition = position;
            notifyDataSetChanged();
        });
        return convertView;

    }

    class ViewHolder {
        ImageView cbAgree;
        RelativeLayout llItem;
        TextView tv_bankNum,iv_bank_icon;
    }

}
