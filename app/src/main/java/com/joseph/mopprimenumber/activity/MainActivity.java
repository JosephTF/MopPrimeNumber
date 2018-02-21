package com.joseph.mopprimenumber.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.joseph.mopprimenumber.R;
import com.joseph.mopprimenumber.thread.CheckThread;
import com.joseph.mopprimenumber.thread.SplashThread;
import com.joseph.mopprimenumber.util.StatusBarUtil;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_start, et_end;
    private Button bt_getAnswer;
    private RadioGroup rg_type;
    private TextView tv_answer;
    private ProgressBar pb;
    private CheckThread ct;
    private boolean typeIsMop;
    private int num_start, num_end;

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        StatusBarUtil.setTranslucent(MainActivity.this, 0);//状态栏半透明
        et_start = findViewById(R.id.et_start);
        et_end = findViewById(R.id.et_end);
        bt_getAnswer = findViewById(R.id.bt_getAnswer);
        rg_type = findViewById(R.id.rg_type);
        tv_answer = findViewById(R.id.tv_answer);
        pb = findViewById(R.id.progressbar);
        bt_getAnswer.setOnClickListener(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_getAnswer:
                pb.setVisibility(View.VISIBLE);
                bt_getAnswer.setClickable(false);
                typeIsMop = rg_type.getCheckedRadioButtonId() == R.id.rb_mop;   //素数类型
                num_start = Integer.parseInt(et_start.getText().toString());
                num_end = Integer.parseInt(et_end.getText().toString());
                if(num_start>num_end){
                    int num_mid = num_start;
                    num_start = num_end;
                    num_end = num_mid;
                }
                ct = new CheckThread(this, handler, num_start, num_end, typeIsMop);
                ct.start();
                break;
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    tv_answer.setText("范围取值有误，素数不能小于2");
                    pb.setVisibility(View.GONE);
                    bt_getAnswer.setClickable(true);
                    break;
                case 100:
                    ArrayList<Integer> prime = msg.getData().getIntegerArrayList("intArrayList");
                    if (prime.size() > 0) {
                        if (typeIsMop) {
                            String result = "结果：" + num_start + "-" + num_end + "内包含" + prime.size() + "个猫扑素数\n" + prime.toString();
                            tv_answer.setText(result);
                        } else {
                            String result = "结果：" + num_start + "-" + num_end + "内包含" + prime.size() + "个素数\n" + prime.toString();
                            tv_answer.setText(result);
                        }
                    } else {
                        if (typeIsMop)
                            tv_answer.setText("结果：" + num_start + "-" + num_end + "内不包含猫扑素数");
                        else
                            tv_answer.setText("结果：" + num_start + "-" + num_end + "内不包含素数");
                    }
                    pb.setVisibility(View.GONE);
                    bt_getAnswer.setClickable(true);
                    break;
            }
        }
    };
}
