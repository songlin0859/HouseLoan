package com.sl.houseloan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sl.houseloan.adapter.LoanAdapter;
import com.sl.houseloan.loan.LoanCalculatorUtil;
import com.sl.houseloan.loan.LoanMonthBean;
import com.sl.houseloan.loan.LoanResult;
import com.sl.houseloan.loan.RateType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    public static final String TOTAL_MONEY = "totalMoney";
    public static final String TOTAL_MONTH = "totalMonth";
    public static final String LOAN_RATE = "loanRate";
    private ListView mListView;
    private List<LoanMonthBean> mMonthList=new ArrayList<>();
    private LoanAdapter mAdapter;

    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setTitle("**还款列表**");

        mProgressBar= (ProgressBar) findViewById(R.id.progressBar);

        BigDecimal totalMoney= (BigDecimal) getIntent().getSerializableExtra(TOTAL_MONEY);
        int totalMonth=getIntent().getIntExtra(TOTAL_MONTH,0);
        double loanRate=getIntent().getDoubleExtra(LOAN_RATE,0.0);

        mListView= (ListView) findViewById(R.id.listView);
        mAdapter=new LoanAdapter(mMonthList);
        mListView.setAdapter(mAdapter);
        mProgressBar.setMax(totalMonth);

        LoanResult loanResult = LoanCalculatorUtil.calculatorAC(totalMoney, totalMonth, loanRate, RateType.RATE_TYPE_YEAR);
        mMonthList.clear();
        List<LoanMonthBean> allLoans = loanResult.getAllLoans();
        //添加上月份信息
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,2016);
        calendar.set(Calendar.MONTH,8);//一月是0，所以九月是8
        calendar.set(Calendar.DAY_OF_MONTH,12);
        for (LoanMonthBean lbm:allLoans) {
            lbm.setDate(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)/*0~11  +1-->1~12*/+"-"+calendar.get(Calendar.DAY_OF_MONTH));
            lbm.setDateMills(calendar.getTimeInMillis());
            calendar.add(Calendar.MONTH,1);
        }

        int hasGone=0;
        for (LoanMonthBean lbm:allLoans) {
            if (lbm.getDateMills()<System.currentTimeMillis()){
                hasGone++;
            }
        }
        mProgressBar.setProgress(hasGone);
        ((TextView)findViewById(R.id.text)).setText("已还款比例(月数)"+hasGone+"/"+totalMonth);

        mMonthList.addAll(allLoans);
        mAdapter.notifyDataSetChanged();

        mListView.setSelection(hasGone-1);
    }

    public static void actionStart(Context context,BigDecimal totalMoney, int totalMonth, double loanRate){
        Intent intent=new Intent(context,ResultActivity.class);
        intent.putExtra(TOTAL_MONEY,totalMoney);
        intent.putExtra(TOTAL_MONTH,totalMonth);
        intent.putExtra(LOAN_RATE,loanRate);
        context.startActivity(intent);
    }
}
