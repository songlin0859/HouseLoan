package com.sl.houseloan;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sl.houseloan.bean.LoanInfo;
import com.sl.houseloan.util.JsonUtil;
import com.sl.houseloan.util.MoneyConvertor;
import com.sl.houseloan.util.SpUtil;
import com.sl.loanlibrary.LoanCalculatorUtil;
import com.sl.loanlibrary.LoanResult;
import com.sl.loanlibrary.RateType;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private Button mStart;
    private EditText mTotalMoney;
    private EditText mTotalTime;
    private EditText mRate;
    private EditText mRateDiscount;
    private Button mDateChoose;
    private RadioGroup mRadioGroup;
    private TextView mMoneyInfo;

    private TextView mInfoView;

    private Calendar mCalendar;
    private LoanInfo mLoanInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mLoanInfo =SpUtil.getLoanInfo();
        mCalendar=Calendar.getInstance();
        if (mLoanInfo.getFirstPayTime()>0){
            mCalendar.setTimeInMillis(mLoanInfo.getFirstPayTime());
        }
        initShowView();
    }

    private void initShowView() {
        if (mLoanInfo.getTotalMoney()>0){
            mTotalMoney.setText(String.valueOf(mLoanInfo.getTotalMoney()));
            mTotalMoney.setSelection(String.valueOf(mLoanInfo.getTotalMoney()).length());
        }
        if (mLoanInfo.getTotalLength()>0){
            mTotalTime.setText(String.valueOf(mLoanInfo.getTotalLength()));
        }
        if (mLoanInfo.getRate()>0){
            mRate.setText(String.valueOf(mLoanInfo.getRate()));
        }
        if (mLoanInfo.getRateDiscount()>0){
            mRateDiscount.setText(String.valueOf(mLoanInfo.getRateDiscount()));
        }

        if (mLoanInfo.getLoanType()== LoanInfo.TYPE_DEBX){
            ((RadioButton)findViewById(R.id.debx)).setChecked(true);
        }else if (mLoanInfo.getLoanType()== LoanInfo.TYPE_DEBJ){
            ((RadioButton)findViewById(R.id.debj)).setChecked(true);
        }else{
            ((RadioButton)findViewById(R.id.debj)).setChecked(true);
            mLoanInfo.setLoanType(LoanInfo.TYPE_DEBJ);
        }

        if (mLoanInfo.getFirstPayTime()>0){
            SimpleDateFormat sdf=new SimpleDateFormat(getString(R.string.activity_main_date_format));
            mDateChoose.setText(sdf.format(new Date(mLoanInfo.getFirstPayTime())));
        }
    }

    private void initView() {
        mTotalMoney= (EditText) findViewById(R.id.totalMoney);
        mTotalTime= (EditText) findViewById(R.id.totalTime);
        mRate= (EditText) findViewById(R.id.rate);
        mRateDiscount= (EditText) findViewById(R.id.rateDiscount);
        mStart= (Button) findViewById(R.id.go);
        mDateChoose= (Button) findViewById(R.id.firstPayDay);
        mRadioGroup= (RadioGroup) findViewById(R.id.radioGroup);
        mMoneyInfo= (TextView) findViewById(R.id.moneyInfo);
        mInfoView= (TextView) findViewById(R.id.info);

        mMoneyInfo.setVisibility(View.GONE);

        findViewById(R.id.detail).setOnClickListener(this);
        mStart.setOnClickListener(this);
        mDateChoose.setOnClickListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);

        mTotalMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable)){
                    mMoneyInfo.setVisibility(View.GONE);
                }else{
                    mMoneyInfo.setVisibility(View.VISIBLE);
                    try {
                        mMoneyInfo.setText(MoneyConvertor.convert(editable.toString()));
                    }catch (Exception ignor){
                        //
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.detail){
            if (checkData()){
                mLoanInfo.setTotalMoney(getDoubleValue(mTotalMoney));
                mLoanInfo.setTotalLength(getIntValue(mTotalTime));
                mLoanInfo.setRate(getDoubleValue(mRate));
                mLoanInfo.setRateDiscount(getDoubleValue(mRateDiscount));
                SpUtil.saveLoanInfo(mLoanInfo);
                ResultActivity.actionStart(this, JsonUtil.toJson(mLoanInfo));
            }
        }else if (v==mDateChoose){
            DatePickerDialog dialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    mCalendar.set(Calendar.YEAR,year);
                    mCalendar.set(Calendar.MONTH,month);
                    mCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                    mLoanInfo.setFirstPayTime(mCalendar.getTimeInMillis());
                    SimpleDateFormat sdf=new SimpleDateFormat(getString(R.string.activity_main_date_format));
                    mDateChoose.setText(sdf.format(new Date(mLoanInfo.getFirstPayTime())));
                }
            },mCalendar.get(Calendar.YEAR),mCalendar.get(Calendar.MONTH),mCalendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        }else if (v==mStart){
            if (checkData()){
                LoanResult loanResult=null;
                if (mLoanInfo.getLoanType()== LoanInfo.TYPE_DEBJ){
                    loanResult = LoanCalculatorUtil.calculatorAC(
                            new BigDecimal(mLoanInfo.getTotalMoney()),
                            mLoanInfo.getTotalLength()*12,
                            mLoanInfo.getRate()* mLoanInfo.getRateDiscount(),
                            RateType.RATE_TYPE_YEAR);
                }else if (mLoanInfo.getLoanType()== LoanInfo.TYPE_DEBX){
                    loanResult = LoanCalculatorUtil.calculatorACPI(
                            new BigDecimal(mLoanInfo.getTotalMoney()),
                            mLoanInfo.getTotalLength()*12,
                            mLoanInfo.getRate()* mLoanInfo.getRateDiscount(),
                            RateType.RATE_TYPE_YEAR);
                }

                if (loanResult==null){
                    return ;
                }

                String info="贷款总额: "+loanResult.getTotalLoanMoney()+"元   总利息: "+loanResult.getTotalInterest()+"元"
                        +"\n首月还款: "+loanResult.getFirstRepayment()+"元   月均还款: "+loanResult.getAvgRepayment()+"元"
                        +"\n还款总额: "+loanResult.getTotalRepayment()+"元   每月减少: "+(loanResult.getMonthDec()==null?"0":loanResult.getMonthDec())+"元";
                if (mLoanInfo.getLoanType()== LoanInfo.TYPE_DEBJ){
                    mInfoView.setText("**##等额本金##**\n"+info);
                }else if (mLoanInfo.getLoanType()== LoanInfo.TYPE_DEBX){
                    mInfoView.setText("**##等额本息##**\n"+info);
                }
            }
        }
    }

    private boolean checkData() {
        if (TextUtils.isEmpty(mTotalMoney.getText())){
            Toast.makeText(this, R.string.activity_main_input_total_money_toast,Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(mTotalTime.getText())){
            Toast.makeText(this, R.string.activity_main_input_total_time_toast,Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(mRate.getText())){
            Toast.makeText(this, R.string.activity_main_input_rate_toast,Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(mRateDiscount.getText())){
            Toast.makeText(this, R.string.activity_main_input_rate_discount_toast,Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (checkedId==R.id.debj){
            mLoanInfo.setLoanType(LoanInfo.TYPE_DEBJ);
        }else if (checkedId==R.id.debx){
            mLoanInfo.setLoanType(LoanInfo.TYPE_DEBX);
        }
    }
}
