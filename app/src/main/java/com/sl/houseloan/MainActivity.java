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

import com.sl.houseloan.util.JsonUtil;
import com.sl.houseloan.util.MoneyConvertor;
import com.sl.houseloan.util.SpUtil;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private Button mStart;
    private EditText mTotalMoney;
    private EditText mTotalTime;
    private EditText mRate;
    private EditText mRateDiscount;
    private Button mDateChoose;
    private RadioGroup mRadioGroup;
    private TextView mMoneyInfo;

    private Calendar mCalendar;
    private LoanBean mLoanBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mLoanBean=SpUtil.getLoanInfo();
        mCalendar=Calendar.getInstance();
        if (mLoanBean.getFirstPayTime()>0){
            mCalendar.setTimeInMillis(mLoanBean.getFirstPayTime());
        }
        initShowView();
    }

    private void initShowView() {
        if (mLoanBean.getTotalMoney()>0){
            mTotalMoney.setText(String.valueOf(mLoanBean.getTotalMoney()));
        }
        if (mLoanBean.getTotalLength()>0){
            mTotalTime.setText(String.valueOf(mLoanBean.getTotalLength()));
        }
        if (mLoanBean.getRate()>0){
            mRate.setText(String.valueOf(mLoanBean.getRate()));
        }
        if (mLoanBean.getRateDiscount()>0){
            mRateDiscount.setText(String.valueOf(mLoanBean.getRateDiscount()));
        }

        if (mLoanBean.getLoanType()==LoanBean.TYPE_DEBX){
            ((RadioButton)findViewById(R.id.debx)).setChecked(true);
        }else if (mLoanBean.getLoanType()==LoanBean.TYPE_DEBJ){
            ((RadioButton)findViewById(R.id.debj)).setChecked(true);
        }else{
            ((RadioButton)findViewById(R.id.debj)).setChecked(true);
            mLoanBean.setLoanType(LoanBean.TYPE_DEBJ);
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
        mMoneyInfo.setVisibility(View.GONE);

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
        if (v==mStart){
            if (checkData()){
                mLoanBean.setTotalMoney(getDoubleValue(mTotalMoney));
                mLoanBean.setTotalLength(getIntValue(mTotalTime));
                mLoanBean.setRate(getDoubleValue(mRate));
                mLoanBean.setRateDiscount(getDoubleValue(mRateDiscount));
                SpUtil.saveLoanInfo(mLoanBean);
                ResultActivity.actionStart(this, JsonUtil.toJson(mLoanBean));
            }
        }else if (v==mDateChoose){
            DatePickerDialog dialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    mCalendar.set(Calendar.YEAR,year);
                    mCalendar.set(Calendar.MONTH,month);
                    mCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                    mLoanBean.setFirstPayTime(mCalendar.getTimeInMillis());
                }
            },mCalendar.get(Calendar.YEAR),mCalendar.get(Calendar.MONTH),mCalendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        }
    }

    private boolean checkData() {
        if (TextUtils.isEmpty(mTotalMoney.getText())){
            Toast.makeText(this,"请输入贷款总额",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(mTotalTime.getText())){
            Toast.makeText(this,"请输入还款期限",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(mRate.getText())){
            Toast.makeText(this,"请输入贷款利率",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(mRateDiscount.getText())){
            Toast.makeText(this,"请输入利率倍率",Toast.LENGTH_SHORT).show();
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
            mLoanBean.setLoanType(LoanBean.TYPE_DEBJ);
        }else if (checkedId==R.id.debx){
            mLoanBean.setLoanType(LoanBean.TYPE_DEBX);
        }
    }
}
