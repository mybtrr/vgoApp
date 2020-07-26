package com.automation.www.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.automation.www.R;
import com.automation.www.bean.PaymentMethodJavaBean;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/17.
 */

public class HomeShopAdapter extends RecyclerView.Adapter<HomeShopAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<PaymentMethodJavaBean> data;
    private LayoutInflater Inflater;
    private OnItemClickLitener OnItemClickLitener;

    public HomeShopAdapter(Context context, ArrayList<PaymentMethodJavaBean> data) {
        this.mContext = context;
        this.data = data;
        Inflater = LayoutInflater.from(mContext);
    }

    public interface OnItemClickLitener {
        void onItemClick(int position);

        void onItemDelected(int position);

    }

    public void setOnItemClickListener(OnItemClickLitener listener) {
        this.OnItemClickLitener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = Inflater.inflate(R.layout.item_home_shop, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mRlHomeShop = (RelativeLayout) view.findViewById(R.id.mRlHomeShop);
        viewHolder.mTvHomeShopName = (TextView) view.findViewById(R.id.mTvHomeShopName);
        viewHolder.mTvHomeShopNumber = (TextView) view.findViewById(R.id.mTvHomeShopNumber);
        viewHolder.mTvHomeShopUnitPrice = (TextView) view.findViewById(R.id.mTvHomeShopUnitPrice);
        viewHolder.mTvHomeShopWeight = (TextView) view.findViewById(R.id.mTvHomeShopWeight);
        viewHolder.mImgDeletect = (ImageView) view.findViewById(R.id.mImgDeletect);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mImgDeletect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnItemClickLitener.onItemDelected(position);
            }
        });
        holder.mRlHomeShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnItemClickLitener.onItemClick(position);
            }
        });
        holder.mTvHomeShopName.setText(data.get(position).getContent());

        holder.mTvHomeShopUnitPrice.setText("单价:" + formatDouble(Float.valueOf(data.get(position).getMoney())) + "/500g");
        holder.mTvHomeShopWeight.setText("重量:" + data.get(position).getWeight() + "g");
        holder.mTvHomeShopNumber.setText("金额:" + data.get(position).getTotal_price() + "元");
//        holder.mIvItemPaymentMethod.setImageResource(data.get(position).getImg());

    }


    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        //
//        LinearLayout mLlItemPaymentMethod;
        RelativeLayout mRlHomeShop;
        TextView mTvHomeShopName, mTvHomeShopNumber, mTvHomeShopUnitPrice, mTvHomeShopWeight;
        ImageView mImgDeletect;
    }

    /**
     * NumberFormat is the abstract base class for all number formats.
     * This class provides the interface for formatting and parsing numbers.
     *
     * @param d
     * @return
     */
    public static String formatDouble(float d) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        // 保留两位小数
        nf.setMaximumFractionDigits(2);
        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        nf.setRoundingMode(RoundingMode.UP);
        return nf.format(d);
    }
}
