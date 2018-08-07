package com.github.matvapps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.matvapps.piggy.listeners.OnGetMoneyListener;
import com.github.matvapps.piggy.PiggyView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private PiggyView piggyView;
    
    private Button addMoneyBtn;
    private Button getMoneyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        piggyView = findViewById(R.id.piggy);
        addMoneyBtn = findViewById(R.id.add_money);
        getMoneyBtn = findViewById(R.id.get_money);
        
        addMoneyBtn.setOnClickListener(this);
        getMoneyBtn.setOnClickListener(this);
        
        piggyView.setOnGetMoneyListener(new OnGetMoneyListener() {
            @Override
            public void onStartWaitForDeviceRotate() {
                Toast.makeText(MainActivity.this, "Invert your screen", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeviceRotateNotCompleted() {
                Toast.makeText(MainActivity.this, "Invert not completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationStart() {
                Toast.makeText(MainActivity.this, "Beginning of empty the piggy", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationStop() {
                Toast.makeText(MainActivity.this, "The piggy is empty", Toast.LENGTH_SHORT).show();
            }
        });
        
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_money: {
                piggyView.startAddingMoney();
                break;
            }
            case R.id.get_money: {
                piggyView.startGetMoney();
                break;
            }
        }
    }
}
