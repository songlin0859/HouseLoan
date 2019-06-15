package com.sl.houseloan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sl.houseloan.adapter.LoanAdapter;
import com.sl.houseloan.bean.LoanInfo;
import com.sl.houseloan.util.JsonUtil;
import com.sl.houseloan.widget.GridProgressView;
import com.sl.loanlibrary.LoanCalculatorUtil;
import com.sl.loanlibrary.LoanMonthBean;
import com.sl.loanlibrary.LoanResult;
import com.sl.loanlibrary.RateType;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    public static final String EXTRA_JSON = "json";
    private ListView mListView;
    private List<LoanMonthBean> mMonthList=new ArrayList<>();
    private LoanAdapter mAdapter;
    private GridProgressView gridProgressView;
    private TextView mInfoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setTitle(getString(R.string.activity_result_title));

        gridProgressView= (GridProgressView)findViewById(R.id.gridProgressView);
        mInfoView= (TextView) findViewById(R.id.info);
        String json=getIntent().getStringExtra(EXTRA_JSON);
        if (TextUtils.isEmpty(json)){
            return;
        }

        LoanInfo loanInfo =null;
        try {
            loanInfo = JsonUtil.fromJson(json, LoanInfo.class);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (loanInfo ==null){
            return ;
        }

        mListView= (ListView) findViewById(R.id.listView);
        mAdapter=new LoanAdapter(mMonthList);
        mListView.setAdapter(mAdapter);

        LoanResult loanResult=null;
        if (loanInfo.getLoanType()== LoanInfo.TYPE_DEBJ){
            loanResult = LoanCalculatorUtil.calculatorAC(
                    new BigDecimal(loanInfo.getTotalMoney()),
                    loanInfo.getTotalLength()*12,
                    loanInfo.getRate()* loanInfo.getRateDiscount(),
                    RateType.RATE_TYPE_YEAR);
        }else if (loanInfo.getLoanType()== LoanInfo.TYPE_DEBX){
            loanResult = LoanCalculatorUtil.calculatorACPI(
                    new BigDecimal(loanInfo.getTotalMoney()),
                    loanInfo.getTotalLength()*12,
                    loanInfo.getRate()* loanInfo.getRateDiscount(),
                    RateType.RATE_TYPE_YEAR);
        }

        if (loanResult==null){
            return ;
        }

        String info="贷款总额: "+loanResult.getTotalLoanMoney()+"元   总利息: "+loanResult.getTotalInterest()+"元"
                +"\n首月还款: "+loanResult.getFirstRepayment()+"元   月均还款: "+loanResult.getAvgRepayment()+"元"
                +"\n还款总额: "+loanResult.getTotalRepayment()+"元   每月减少: "+(loanResult.getMonthDec()==null?"0":loanResult.getMonthDec())+"元";
        if (loanInfo.getLoanType()== LoanInfo.TYPE_DEBJ){
            mInfoView.setText("**##等额本金##**\n"+info);
        }else if (loanInfo.getLoanType()== LoanInfo.TYPE_DEBX){
            mInfoView.setText("**##等额本息##**\n"+info);
        }

        mMonthList.clear();
        List<LoanMonthBean> allLoans = loanResult.getAllLoans();
        //添加上月份信息
        if (loanInfo.getFirstPayTime()>0){
            Calendar calendar=Calendar.getInstance();
            calendar.setTimeInMillis(loanInfo.getFirstPayTime());
            for (LoanMonthBean lbm:allLoans) {
                lbm.setDate(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)/*0~11  +1-->1~12*/+"-"+calendar.get(Calendar.DAY_OF_MONTH));
                lbm.setDateMills(calendar.getTimeInMillis());
                calendar.add(Calendar.MONTH,1);
            }
        }
        int hasGone=0;
        for (LoanMonthBean lbm:allLoans) {
            if (lbm.getDateMills()>0&&lbm.getDateMills()<System.currentTimeMillis()){
                hasGone++;
            }
        }

        gridProgressView.setRowColumnsAndDone(12,loanInfo.getTotalLength(),hasGone);

        ((TextView)findViewById(R.id.text)).setText("已还款月数比例"+hasGone+"/"+ loanInfo.getTotalLength()*12 +"["+getPercentString(hasGone,(loanInfo.getTotalLength()*12))+"]");

        mMonthList.addAll(allLoans);
        mAdapter.notifyDataSetChanged();

        mListView.setSelection(hasGone-1);
    }

    private String getPercentString(int hasGone, int total) {
        if (total==0){
            return "0.00%";
        }else{
            DecimalFormat df=new DecimalFormat("#0.00");
            return df.format(hasGone*100.0f/total)+"%";
        }
    }

    public static void actionStart(Context context, String json){
        Intent intent=new Intent(context,ResultActivity.class);
        intent.putExtra(EXTRA_JSON,json);
        context.startActivity(intent);
    }
}
