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
import com.sl.houseloan.loan.LoanCalculatorUtil;
import com.sl.houseloan.loan.LoanMonthBean;
import com.sl.houseloan.loan.LoanResult;
import com.sl.houseloan.loan.RateType;
import com.sl.houseloan.util.JsonUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.sl.houseloan.R.id.totalMoney;

public class ResultActivity extends AppCompatActivity {
    public static final String EXTRA_JSON = "json";
    private ListView mListView;
    private List<LoanMonthBean> mMonthList=new ArrayList<>();
    private LoanAdapter mAdapter;

    private ProgressBar mProgressBar;
    private TextView mInfoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setTitle("**还款列表**");

        mProgressBar= (ProgressBar) findViewById(R.id.progressBar);
        mInfoView= (TextView) findViewById(R.id.info);
        String json=getIntent().getStringExtra(EXTRA_JSON);
        if (TextUtils.isEmpty(json)){
            return;
        }

        LoanBean loanBean=null;
        try {
            loanBean = JsonUtil.fromJson(json, LoanBean.class);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (loanBean==null){
            return ;
        }

        mListView= (ListView) findViewById(R.id.listView);
        mAdapter=new LoanAdapter(mMonthList);
        mListView.setAdapter(mAdapter);
        mProgressBar.setMax(loanBean.getTotalLength()*20);

        LoanResult loanResult=null;
        if (loanBean.getLoanType()==LoanBean.TYPE_DEBJ){
            loanResult = LoanCalculatorUtil.calculatorAC(
                    new BigDecimal(loanBean.getTotalMoney()),
                    loanBean.getTotalLength()*12,
                    loanBean.getRate()*loanBean.getRateDiscount(),
                    RateType.RATE_TYPE_YEAR);
        }else if (loanBean.getLoanType()==LoanBean.TYPE_DEBX){
            loanResult = LoanCalculatorUtil.calculatorACPI(
                    new BigDecimal(loanBean.getTotalMoney()),
                    loanBean.getTotalLength()*12,
                    loanBean.getRate()*loanBean.getRateDiscount(),
                    RateType.RATE_TYPE_YEAR);
        }

        if (loanResult==null){
            return ;
        }

        String info="贷款总额: "+loanResult.getTotalLoanMoney()+"元   总利息: "+loanResult.getTotalInterest()+"元"
                +"\n首月还款: "+loanResult.getFirstRepayment()+"元   月均还款: "+loanResult.getAvgRepayment()+"元"
                +"\n还款总额: "+loanResult.getTotalRepayment()+"元   每月减少: "+(loanResult.getMonthDec()==null?"0":loanResult.getMonthDec())+"元";
        if (loanBean.getLoanType()==LoanBean.TYPE_DEBJ){
            mInfoView.setText("**等额本金-开始还的多，逐月递减**\n"+info);
        }else if (loanBean.getLoanType()==LoanBean.TYPE_DEBX){
            mInfoView.setText("**等额本息-每个月还的一样的**\n"+info);
        }

        mMonthList.clear();
        List<LoanMonthBean> allLoans = loanResult.getAllLoans();
        //添加上月份信息
        if (loanBean.getFirstPayTime()>0){
            Calendar calendar=Calendar.getInstance();
            calendar.setTimeInMillis(loanBean.getFirstPayTime());
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
        mProgressBar.setProgress(hasGone);
        ((TextView)findViewById(R.id.text)).setText("已还款比例(月数)"+hasGone+"/"+loanBean.getTotalLength()*12);

        mMonthList.addAll(allLoans);
        mAdapter.notifyDataSetChanged();

        mListView.setSelection(hasGone-1);
    }

    public static void actionStart(Context context, String json){
        Intent intent=new Intent(context,ResultActivity.class);
        intent.putExtra(EXTRA_JSON,json);
        context.startActivity(intent);
    }
}
