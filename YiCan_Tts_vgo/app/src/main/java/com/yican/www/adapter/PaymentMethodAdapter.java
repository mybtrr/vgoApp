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

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/17.
 */

public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<PaymentMethodJavaBean> data;
    private LayoutInflater Inflater;
    private OnItemClickLitener OnItemClickLitener;

    public PaymentMethodAdapter(Context context, ArrayList<PaymentMethodJavaBean> data) {
        this.mContext = context;
        this.data = data;
        Inflater = LayoutInflater.from(mContext);
    }

    public interface OnItemClickLitener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickLitener listener) {
        this.OnItemClickLitener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = Inflater.inflate(R.layout.item_payment_method, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mLlItemPaymentMethod = (LinearLayout) view.findViewById(R.id.mLlItemPaymentMethod);
        viewHolder.mTItemPaymentMethodContent = (TextView) view.findViewById(R.id.mTItemPaymentMethodContent);
        viewHolder.mIvItemPaymentMethod = (ImageView) view.findViewById(R.id.mIvItemPaymentMethod);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mLlItemPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnItemClickLitener.onItemClick(position);
            }
        });
        holder.mTItemPaymentMethodContent.setText(data.get(position).getContent());
        holder.mIvItemPaymentMethod.setImageResource(data.get(position).getImg());
    }


    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        LinearLayout mLlItemPaymentMethod;
        TextView mTItemPaymentMethodContent;
        ImageView mIvItemPaymentMethod;
    }
}
