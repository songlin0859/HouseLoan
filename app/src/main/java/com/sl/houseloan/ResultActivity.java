package com.sl.houseloan;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sl.houseloan.adapter.LoanAdapter;
import com.sl.houseloan.loan.Loan;
import com.sl.houseloan.loan.LoanByMonth;
import com.sl.houseloan.loan.LoanCalculatorUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    public static final String TOTAL_MONEY = "totalMoney";
    public static final String TOTAL_MONTH = "totalMonth";
    public static final String LOAN_RATE = "loanRate";
    public static final String RATE_TYPE = "rateType";
    private ListView mListView;
    private List<LoanByMonth> mMonthList=new ArrayList<>();
    private LoanAdapter mAdapter;

    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mProgressBar= (ProgressBar) findViewById(R.id.progressBar);

        BigDecimal totalMoney= (BigDecimal) getIntent().getSerializableExtra(TOTAL_MONEY);
        int totalMonth=getIntent().getIntExtra(TOTAL_MONTH,0);
        double loanRate=getIntent().getDoubleExtra(LOAN_RATE,0.0);
        int rateType=getIntent().getIntExtra(RATE_TYPE,-1);

        mListView= (ListView) findViewById(R.id.listView);
        mAdapter=new LoanAdapter(mMonthList);
        mListView.setAdapter(mAdapter);
        mProgressBar.setMax(totalMonth);

        Loan loan = LoanCalculatorUtil.calculatorAC(totalMoney, totalMonth, loanRate, rateType);
        mMonthList.clear();
        List<LoanByMonth> allLoans = loan.getAllLoans();

        int hasGone=0;
        for (LoanByMonth lbm:allLoans) {
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

    public static void actionStart(Context context,BigDecimal totalMoney, int totalMonth, double loanRate, int rateType){
        Intent intent=new Intent(context,ResultActivity.class);
        intent.putExtra(TOTAL_MONEY,totalMoney);
        intent.putExtra(TOTAL_MONTH,totalMonth);
        intent.putExtra(LOAN_RATE,loanRate);
        intent.putExtra(RATE_TYPE,rateType);
        context.startActivity(intent);
    }
}
