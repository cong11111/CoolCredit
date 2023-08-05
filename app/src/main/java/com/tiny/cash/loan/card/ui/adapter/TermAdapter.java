package com.tiny.cash.loan.card.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tiny.cash.loan.card.kudicredit.R;

import java.util.ArrayList;
import java.util.List;

public class TermAdapter extends BaseAdapter {
    private Context context;
    private List<String> mList;
    private String mSel = "";

    public TermAdapter(Context context) {
        mList = new ArrayList<>();
        this.context = context;
    }

    public void setData(List<String> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public void selectItem(String sel){
        mSel = sel;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_term, parent, false);
            holder = new ViewHolder();
            holder.tv_term = convertView.findViewById(R.id.tv_term);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String s = mList.get(position);
            holder.tv_term.setText(s);
        if (mSel.equals(s)) {
            holder.tv_term.setBackground(context.getResources().getDrawable(R.drawable.bg_blue_radius_3));
            holder.tv_term.setTextColor(context.getResources().getColor(R.color.color_FFFFFF));
        }else {
            holder.tv_term.setBackground(context.getResources().getDrawable(R.drawable.bg_translucent_radius_8));
            holder.tv_term.setTextColor(context.getResources().getColor(R.color.color_ff999999));
        }

//        if (TextUtils.isEmpty(mSel) && position == 0){
//            holder.tv_term.setBackground(context.getResources().getDrawable(R.drawable.bg_blue_radius_3));
//            holder.tv_term.setTextColor(context.getResources().getColor(R.color.color_FFFFFF));
//        }

        return convertView;

    }

    class ViewHolder {
        TextView tv_term;
    }

}
