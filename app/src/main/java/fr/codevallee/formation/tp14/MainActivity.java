package fr.codevallee.formation.tp14;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button buttonGo;
    private Runnable progressThread;
    private int progression;

    private final int MAX_PROGRESSION = 100;
    private final int LOADING_STEP = 10;
    private final int TIME_STEP = 300;

    private AtomicBoolean isThreadRunnning = new AtomicBoolean();
    private AtomicBoolean isThreadPausing = new AtomicBoolean(); //Inutile

    //Création du Handler:
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(isThreadRunnning.get()) {
                progressBar.incrementProgressBy(LOADING_STEP);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Interface:
        this.buttonGo = (Button) findViewById(R.id.button_go);
        this.progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        //Création du thread de progression
        progressThread = new Runnable() {
            public void run() {
                try {
                    while (progression <= MAX_PROGRESSION) {
                        progression+=LOADING_STEP;
                        Thread.sleep(TIME_STEP);
                        Message msg = handler.obtainMessage(0, progression, 0);
                        handler.sendMessage(msg);
                    }
                    isThreadRunnning.set(false);
                    Log.d("Test","Je met le threadRunning à false");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        //Création du click listener pour lancer le thread
        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isThreadRunnning.get()) {
                    progression = 0;
                    progressBar.setProgress(0);
                    isThreadRunnning.set(true);
                    new Thread(progressThread).start();
                } else {
                    Log.d("Test", "Non je relance pas le thread! Fuck!");
                }
            }
        });
    }
}
