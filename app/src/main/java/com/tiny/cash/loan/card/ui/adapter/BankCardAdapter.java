package com.tiny.cash.loan.card.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tiny.cash.loan.card.kudicredit.R;

import com.tiny.cash.loan.card.net.response.data.bean.BankList;

import java.util.ArrayList;
import java.util.List;

public class BankCardAdapter extends BaseAdapter {
    private Context context;
    private List<BankList.CardList> mList;

    public BankCardAdapter(Context context) {
        mList = new ArrayList<>();
        this.context = context;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bank_card, parent, false);
            holder = new ViewHolder();
            holder.tv_bankName = convertView.findViewById(R.id.tv_bankName);
            holder.tv_bankNum = convertView.findViewById(R.id.tv_bankNum);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mList.get(position) != null) {
            String cardNumber = mList.get(position).getCardNumber();
            holder.tv_bankNum.setText(cardNumber.substring(0,cardNumber.length()-8)+"****"+cardNumber.substring(cardNumber.length()-4));
        }
        return convertView;

    }

    class ViewHolder {
        TextView tv_bankName;
        TextView tv_bankNum;
    }

}
