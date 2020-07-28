package com.yican.www.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yican.www.R;
import com.yican.www.bean.PaymentMethodJavaBean;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 *  Adapter是连接后端数据和前端显示的适配器接口, 是数据和UI（View）之间一个重要的纽带。
 *  在常见的View(ListView,GridView)等地方都需要用到Adapter。
 * Created by Administrator on 2018/1/17.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<PaymentMethodJavaBean> data;
    private LayoutInflater Inflater;
    private OnItemClickLitener OnItemClickLitener;

    public HomeAdapter(Context context, ArrayList<PaymentMethodJavaBean> data) {
        this.mContext = context;
        this.data = data;
        Inflater = LayoutInflater.from(mContext);
    }

    public interface OnItemClickLitener {
        void onItemClick(int position, View view);
    }

    public void setOnItemClickListener(OnItemClickLitener listener) {
        this.OnItemClickLitener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = Inflater.inflate(R.layout.item_home, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mLlItem = (LinearLayout) view.findViewById(R.id.mLlItem);
        viewHolder.mImgHomeIcon = (ImageView) view.findViewById(R.id.mImgHomeIcon);
        viewHolder.mTItemPaymentMethodContent = (TextView) view.findViewById(R.id.mTItemPaymentMethodContent);
        viewHolder.mTItemPaymentMethodPrice = (TextView) view.findViewById(R.id.mTItemPaymentMethodPrice);
        viewHolder.mTItemWeight = (TextView) view.findViewById(R.id.mTItemWeight);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(mContext).load(data.get(position).getImgs()).skipMemoryCache(true).into(holder.mImgHomeIcon);
        holder.mLlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnItemClickLitener.onItemClick(position, holder.mLlItem);
            }
        });
        holder.mTItemPaymentMethodContent.setText(data.get(position).getContent());
        holder.mTItemPaymentMethodPrice.setText("¥" + data.get(position).getTotal_price());
        holder.mTItemPaymentMethodPrice.setText("¥" + data.get(position).getTotal_price());
        holder.mTItemWeight.setText(data.get(position).getWeight());
    }


    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        LinearLayout mLlItem;
        ImageView mImgHomeIcon;
        TextView mTItemPaymentMethodContent, mTItemPaymentMethodPrice,mTItemWeight;
    }
}
