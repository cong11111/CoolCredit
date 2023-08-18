package com.tiny.cash.loan.card.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tiny.cash.loan.card.kudicredit.BuildConfig;
import com.tiny.cash.loan.card.kudicredit.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 抽屉adapter
 * Created by zly on 2016/3/30.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder> {

    private ArrayList<DrawerItem> dataList = new ArrayList<>();

    public DrawerAdapter(){
        List list = Arrays.asList(
                new DrawerItemNormal(R.mipmap.ic_loan, R.string.menu_My_loan),
                new DrawerItemNormal(R.mipmap.ic_profile, R.string.menu_My_Profile),
                new DrawerItemNormal(R.mipmap.ic_card, R.string.menu_Card),
                new DrawerItemNormal(R.mipmap.ic_account, R.string.menu_Bank_Account),
                new DrawerItemNormal(R.mipmap.ic_account, R.string.menu_off_payment),
                new DrawerItemNormal(R.mipmap.ic_message, R.string.menu_Message),
                new DrawerItemNormal(R.mipmap.ic_about, R.string.menu_ContactUs),
                new DrawerItemNormal(R.mipmap.ic_about, R.string.menu_About),
                new DrawerItemNormal(R.mipmap.ic_out, R.string.menu_Log_out)
        );
        dataList.addAll(list);
        if (!BuildConfig.IS_AAB_BUILD) {
            dataList.add(new DrawerItemNormal(R.mipmap.ic_out, R.string.menu_test1));
            dataList.add(new DrawerItemNormal(R.mipmap.ic_out, R.string.menu_share_file));
        }
    }

    private  int mCount = 0 ;
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setMessageRead(int count){
        mCount = count;
    }
    @Override
    public int getItemCount() {
        return (dataList == null || dataList.size() == 0) ? 0 : dataList.size();
    }

    @Override
    public DrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DrawerViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                viewHolder = new NormalViewHolder(inflater.inflate(R.layout.item_drawer_normal, parent, false));
        return viewHolder;
    }

    private int   currentPosition = 0;

    @Override
    public void onBindViewHolder(DrawerViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        final DrawerItem item = dataList.get(pos);
        if (holder instanceof NormalViewHolder) {
            NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
            final DrawerItemNormal itemNormal = (DrawerItemNormal) item;
            normalViewHolder.iv.setBackgroundResource(itemNormal.iconRes);
            normalViewHolder.tv.setText(itemNormal.titleRes);
            //判断传入的position是否和当前一致
            if (pos == currentPosition) {
                normalViewHolder.ll_item.setBackgroundColor(mContext.getResources().getColor(R.color.color_ffebebeb));
            } else {
                normalViewHolder.ll_item.setBackgroundColor(mContext.getResources().getColor(R.color.color_FFFFFF));
            }
            if (pos == 5 && mCount > 0) {
                normalViewHolder.tv_messageCount.setText(mCount+"");
                normalViewHolder.tv_messageCount.setVisibility(View.VISIBLE);
            }else {
                normalViewHolder.tv_messageCount.setVisibility(View.GONE);
            }
            normalViewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        currentPosition = pos;
                        if (pos == 5){
                            mCount=0;
                        }
                        notifyDataSetChanged();
                        listener.itemClick(itemNormal);
                    }
                }
            });
        }

    }

    public void selectPosition(int position){
        currentPosition = position;
    }
    public OnItemClickListener listener;
    public Context mContext;
    public void setOnItemClickListener(Context context,OnItemClickListener listener){
        this.listener = listener;
        this.mContext = context;
    }

    public interface OnItemClickListener{
        void itemClick(DrawerItemNormal drawerItemNormal);
    }




    //-------------------------item数据模型------------------------------
    // drawerlayout item统一的数据模型
    public interface DrawerItem {
    }


    //有图片和文字的item
    public class DrawerItemNormal implements DrawerItem {
        public int iconRes;
        public int titleRes;

        public DrawerItemNormal(int iconRes, int titleRes) {
            this.iconRes = iconRes;
            this.titleRes = titleRes;
        }

    }

    //----------------------------------ViewHolder数据模型---------------------------
    //抽屉ViewHolder模型
    public class DrawerViewHolder extends RecyclerView.ViewHolder {

        public DrawerViewHolder(View itemView) {
            super(itemView);
        }
    }

    //有图标有文字ViewHolder
    public class NormalViewHolder extends DrawerViewHolder {
        public View view;
        public TextView tv,tv_messageCount;
        public ImageView iv;
        RelativeLayout ll_item;

        public NormalViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            tv = (TextView) itemView.findViewById(R.id.tv);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            ll_item = itemView.findViewById(R.id.ll_item_bg);
            tv_messageCount = itemView.findViewById(R.id.tv_messageCount);
        }
    }


}
