package com.automation.www.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.automation.www.R;
import com.automation.www.bean.PaymentMethodJavaBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/29.
 */

public class OrderDetailsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PaymentMethodJavaBean> list;

    public OrderDetailsAdapter(Context context, ArrayList<PaymentMethodJavaBean> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_order_details, null);
            holder.mTvOrderDetailsName = (TextView) convertView.findViewById(R.id.mTvOrderDetailsName);
            holder.mTvOrderDetailsWeight = (TextView) convertView.findViewById(R.id.mTvOrderDetailsWeight);
            holder.mTvOrderDetailsUnitPrice = (TextView) convertView.findViewById(R.id.mTvOrderDetailsUnitPrice);
            holder.mTvOrderDetailsAmountOfMoney = (TextView) convertView.findViewById(R.id.mTvOrderDetailsAmountOfMoney);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTvOrderDetailsName.setText(list.get(position).getContent());
        holder.mTvOrderDetailsWeight.setText(list.get(position).getWeight()+"g");
        holder.mTvOrderDetailsUnitPrice.setText(list.get(position).getMoney() + "/500g");
        holder.mTvOrderDetailsAmountOfMoney.setText(list.get(position).getTotal_price());

    return convertView;
}

    class ViewHolder {
        TextView mTvOrderDetailsName, mTvOrderDetailsWeight, mTvOrderDetailsUnitPrice, mTvOrderDetailsAmountOfMoney;
    }

}
