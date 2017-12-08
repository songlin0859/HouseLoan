package com.sl.houseloan;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.sl.houseloan.util.SpUtil;

import java.math.BigDecimal;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mStart;
    private EditText mTotalMoney;
    private EditText mTotalTime;
    private EditText mRate;
    private EditText mRateDiscount;
    private Button mDateChoose;

    private long timeMills=-1;
    private Calendar mCalendar;

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
            timeMills=mCalendar.getTimeInMillis();
        }
    }

    private void initView() {
        mTotalMoney= (EditText) findViewById(R.id.totalMoney);
        mTotalTime= (EditText) findViewById(R.id.totalTime);
        mRate= (EditText) findViewById(R.id.rate);
        mRateDiscount= (EditText) findViewById(R.id.rateDiscount);
        mStart= (Button) findViewById(R.id.go);
        mDateChoose= (Button) findViewById(R.id.firstPayDay);

        mStart.setOnClickListener(this);
        mDateChoose.setOnClickListener(this);
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

            ResultActivity.actionStart(this,totalMoney,totalMonth,loanRate,timeMills);
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
                    timeMills=mCalendar.getTimeInMillis();
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
}
