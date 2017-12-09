package com.sl.houseloan;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.sl.houseloan.util.JsonUtil;
import com.sl.houseloan.util.SpUtil;

import java.math.BigDecimal;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private Button mStart;
    private EditText mTotalMoney;
    private EditText mTotalTime;
    private EditText mRate;
    private EditText mRateDiscount;
    private Button mDateChoose;
    private RadioGroup mRadioGroup;

    private Calendar mCalendar;
    private LoanBean mLoanBean=new LoanBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        mCalendar=Calendar.getInstance();
        int year=SpUtil.getYear(-1);
        int month=SpUtil.getMonth(-1);
        int dayOfMonth=SpUtil.getDay(-1);
        if (year!=-1&&month!=-1&&dayOfMonth!=-1){
            mCalendar.set(Calendar.YEAR,year);
            mCalendar.set(Calendar.MONTH,month);
            mCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
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

        mStart.setOnClickListener(this);
        mDateChoose.setOnClickListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==mStart){
            mLoanBean.setTotalMoney(getDoubleValue(mTotalMoney));
            mLoanBean.setTotalLength(getIntValue(mTotalTime)*12);
            mLoanBean.setRate(getDoubleValue(mRate));
            mLoanBean.setRateDiscount(getDoubleValue(mRateDiscount));
            ResultActivity.actionStart(this, JsonUtil.toJson(mLoanBean));
        }else if (v==mDateChoose){
            DatePickerDialog dialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    mCalendar.set(Calendar.YEAR,year);
                    mCalendar.set(Calendar.MONTH,month);
                    mCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                    SpUtil.saveYear(year);
                    SpUtil.saveMonth(month);
                    SpUtil.saveDay(dayOfMonth);
                    mLoanBean.setFirstPayTime(mCalendar.getTimeInMillis());
                }
            },mCalendar.get(Calendar.YEAR),mCalendar.get(Calendar.MONTH),mCalendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
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
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (checkedId==R.id.debj){
            mLoanBean.setLoanType(LoanBean.TYPE_DEBJ);
        }else if (checkedId==R.id.debx){
            mLoanBean.setLoanType(LoanBean.TYPE_DEBX);
        }
    }
}
