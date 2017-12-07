package com.sl.houseloan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sl.houseloan.R;
import com.sl.houseloan.loan.LoanByMonth;

import java.util.List;

/**
 * Created by csl on 2017/12/7.
 *
 */

public class LoanAdapter extends BaseAdapter {
    private List<LoanByMonth> mMonthList;

    public LoanAdapter(List<LoanByMonth> monthList) {
        mMonthList = monthList;
    }

    @Override
    public int getCount() {
        return mMonthList==null?0:mMonthList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMonthList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loan_month,null);
            viewHolder.mTextView=convertView.findViewById(R.id.text);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        LoanByMonth month = mMonthList.get(position);

        if (month.getDateMills()<System.currentTimeMillis()){
            viewHolder.mTextView.setEnabled(false);
        }else{
            viewHolder.mTextView.setEnabled(true);
        }
        viewHolder.mTextView.setText(month.toString());

        return convertView;
    }

    private class ViewHolder{
        private TextView mTextView;
    }
}
