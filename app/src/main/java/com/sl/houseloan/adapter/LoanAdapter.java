package com.sl.houseloan.adapter;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sl.houseloan.R;
import com.sl.loanlibrary.LoanMonthBean;

import java.util.List;

/**
 * Created by csl on 2017/12/7.
 *
 */

public class LoanAdapter extends BaseAdapter {
    private List<LoanMonthBean> mMonthList;

    public LoanAdapter(List<LoanMonthBean> monthList) {
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
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loan_month,parent,false);
            viewHolder.mTextView=convertView.findViewById(R.id.text);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        LoanMonthBean month = mMonthList.get(position);

        if (month.getDateMills()>0&&month.getDateMills()<System.currentTimeMillis()){
            viewHolder.mTextView.setEnabled(false);
            viewHolder.mTextView.setText(getMonthDetail(month,false));
        }else{
            viewHolder.mTextView.setEnabled(true);
            viewHolder.mTextView.setText(getMonthDetail(month,true));
        }

        return convertView;
    }

    private CharSequence getMonthDetail(LoanMonthBean bean, boolean b){
        String str=(bean.getDate()==null?"":bean.getDate() )+ " 总第" + String.format("%3d", bean.getMonth()) + "月" + " 还款额=" + bean.getRepayment()
                + "\n本金=" + bean.getPayPrincipal() + ", 利息=" + bean.getInterest()
                + "\n剩余贷款=" + bean.getRemainTotal() + ", 剩余总本金=" + bean.getRemainPrincipal();
        SpannableString ss =new SpannableString(str);
        RelativeSizeSpan sizeSpan02 = new RelativeSizeSpan(1.2f);
        ss.setSpan(sizeSpan02,0,str.indexOf("本金"), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        if (b){
            ForegroundColorSpan colorSpan =new ForegroundColorSpan(Color.parseColor("#0099EE"));
            ss.setSpan(colorSpan,0,str.indexOf("本金"), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return  ss;
    }

    private class ViewHolder{
        private TextView mTextView;
    }
}
