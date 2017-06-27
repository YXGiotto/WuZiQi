package com.example.administrator.wuziqi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
   private FivePanel fiveziqi;
    private Button  btnRestart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fiveziqi= (FivePanel) findViewById(R.id.wuziqi);
        btnRestart= (Button) findViewById(R.id.again);
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fiveziqi.restart();
            }
        });
    }



}
