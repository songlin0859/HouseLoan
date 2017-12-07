package com.sl.houseloan;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.sl.houseloan.adapter.LoanAdapter;
import com.sl.houseloan.loan.Loan;
import com.sl.houseloan.loan.LoanByMonth;
import com.sl.houseloan.loan.LoanCalculatorUtil;
import com.sl.houseloan.loan.LoanUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private Button mStart;
    private EditText mTotalMoney;
    private EditText mTotalTime;
    private EditText mRate;
    private EditText mRateDiscount;
    private RadioGroup mRateType;

    private int rateType=LoanUtil.RATE_TYPE_YEAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mTotalMoney= (EditText) findViewById(R.id.totalMoney);
        mTotalTime= (EditText) findViewById(R.id.totalTime);
        mRate= (EditText) findViewById(R.id.rate);
        mRateDiscount= (EditText) findViewById(R.id.rateDiscount);
        mRateType= (RadioGroup) findViewById(R.id.rateType);
        mStart= (Button) findViewById(R.id.go);

        mStart.setOnClickListener(this);
        mRateType.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==mStart){
            BigDecimal totalMoney=new BigDecimal(getDoubleValue(mTotalMoney));
            int totalMonth=getIntValue(mTotalTime)*12;
            double loanRate =getDoubleValue(mRate)*getDoubleValue(mRateDiscount);

            if (totalMonth*loanRate==0){
                Toast.makeText(this,"参数非法",Toast.LENGTH_SHORT).show();
                return;
            }

            ResultActivity.actionStart(this,totalMoney,totalMonth,loanRate,rateType);
        }
    }

    private int getIntValue(EditText editText){
        int val=0;
        try {
            val=Integer.parseInt(editText.getText().toString());
        }catch (Exception e){
            e.printStackTrace();
        }

        return val;
    }

    private double getDoubleValue(EditText editText){
        double val=0.0;
        try {
            val=Double.parseDouble(editText.getText().toString());
        }catch (Exception e){
            e.printStackTrace();
        }

        return val;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (checkedId==R.id.typeYear){
            rateType=LoanUtil.RATE_TYPE_YEAR;
        }else if (checkedId==R.id.typeMonth){
            rateType=LoanUtil.RATE_TYPE_MONTH;
        }else{
            throw new IllegalStateException("不可能吧");
        }
    }
}
