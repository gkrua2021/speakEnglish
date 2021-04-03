package com.example.tsjif;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tsjif.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    Button bt1, bt2;
    SpeechRecognizer mRecognizer;
    TextView tv1,tv2;
    final int PERMISSION = 1;
    String[] questions = {"이웃", "리더", "반 친구", "냉장고","동전", "벽","날다","나이","충분한","바구니"

    };
    String[] answers = {
            "neighbor", "leader", "classmate", "refrigerator","coin", "wall", "fly" ,"age","enough","basket"
    };
    int num_questions = questions.length;
    int index = 0;

    void test(){
        if(num_questions>0){
            index = (int)Math.floor(Math.random()*num_questions);

            tv1 = (TextView)findViewById(R.id.tv1);
            tv1.setText(questions[index]+"에 해당하는 영어 단어은?");
        }
        else {
            Toast.makeText(this,"문제가 다 떨어졌습니다.",
                    Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
    }

    void use(){

        for (int i =index +1; i<num_questions;i++){
            questions[i - 1]=questions[i];
            answers[i -1]=answers[i];
        }
        num_questions--;
    }

    void speak() {
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO}, PERMISSION);
        }
        tv2 = (TextView) findViewById(R.id.tv2);
        bt2 = (Button) findViewById(R.id.bt2);
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en_EN");
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);
                mRecognizer.setRecognitionListener(listener);
                mRecognizer.startListening(intent);
            }
        });
    }
    void cheak(){

        bt1 = (Button)findViewById(R.id.bt1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv2 = (TextView)findViewById(R.id.tv2);

                String text = tv2.getText().toString();

                if(text.equals(answers[index])){
                    Toast.makeText(getApplicationContext(), "정답!", Toast.LENGTH_SHORT).show();
                    tv2.setText(null);
                    use();
                    test();
                }
                else {
                    Toast.makeText(getApplicationContext(), "오답...", Toast.LENGTH_SHORT).show();
                    tv2.setText(null);
                }
            }
        });
        test();
    }
    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Toast.makeText(getApplicationContext(),"음성인식을 시작합니다.",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() {}

        @Override
        public void onRmsChanged(float rmsdB) {}

        @Override
        public void onBufferReceived(byte[] buffer) {}

        @Override
        public void onEndOfSpeech() {}

        @Override
        public void onError(int error) {
            String message;

            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상함";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    break;
                default:
                    message = "알 수 없는 오류임";
                    break;
            }
            Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> matches =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            for(int i = 0; i < matches.size() ; i++){
                tv2.setText(matches.get(i));
            }
        }
        @Override
        public void onPartialResults(Bundle partialResults) {}

        @Override
        public void onEvent(int eventType, Bundle params) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speak();
        cheak();
    }
}