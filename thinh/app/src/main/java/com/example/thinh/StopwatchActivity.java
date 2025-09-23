package com.example.thinh;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class StopwatchActivity extends AppCompatActivity {

    private TextView tvHour, tvMinute, tvSecond, tvTextTime, tvName;
    private Button btnStartStop, btnReset;
    private ImageView imgState;

    private final Handler handler = new Handler();
    private boolean running = false;
    private long elapsedSec = 0; // tổng giây

    private final Runnable ticker = new Runnable() {
        @Override public void run() {
            if (running) {
                elapsedSec++;
                updateClock();                 // cập nhật 3 dòng: giờ/phút/giây
                handler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

        tvName = findViewById(R.id.tvName);
        tvHour = findViewById(R.id.tvHour);
        tvMinute = findViewById(R.id.tvMinute);
        tvSecond = findViewById(R.id.tvSecond);
        tvTextTime = findViewById(R.id.tvTextTime);
        btnStartStop = findViewById(R.id.btnStartStop);
        btnReset = findViewById(R.id.btnReset);
        imgState = findViewById(R.id.imgState);

        String name = getIntent().getStringExtra("USER_NAME");
        if (name != null && !name.isEmpty()) {
            tvName.setText("Xin chào, " + name);
        } else {
            tvName.setText("Xin chào!");
        }
        updateClock(); // 00/00/00

        btnStartStop.setOnClickListener(v -> {
            if (!running) start();             // đổi thành STOP & chạy
            else stop();                       // dừng & in text
        });

        btnReset.setOnClickListener(v -> reset());
    }

    private void start() {
        running = true;
        btnStartStop.setText("STOP");
        imgState.setImageResource(R.drawable.ic_run);
        tvTextTime.setText("");                // xoá text đang đọc
        handler.post(ticker);
    }

    private void stop() {
        running = false;
        btnStartStop.setText("START");
        imgState.setImageResource(R.drawable.ic_stop);
        tvTextTime.setText(buildVietnameseTime(elapsedSec)); // in câu thời gian
        handler.removeCallbacks(ticker);
    }

    private void reset() {
        running = false;
        elapsedSec = 0;
        updateClock();
        btnStartStop.setText("START");
        imgState.setImageResource(R.drawable.ic_stop);
        tvTextTime.setText("");
        handler.removeCallbacks(ticker);
    }

    private void updateClock() {
        long h = (elapsedSec / 3600);
        long m = (elapsedSec % 3600) / 60;
        long s = (elapsedSec % 60);
        tvHour.setText(String.format(Locale.getDefault(), "%02d", h));
        tvMinute.setText(String.format(Locale.getDefault(), "%02d", m));
        tvSecond.setText(String.format(Locale.getDefault(), "%02d", s));
    }

    // "Một giờ hai phút bốn mươi lăm giây"
    private String buildVietnameseTime(long totalSeconds) {
        long h = totalSeconds / 3600;
        long m = (totalSeconds % 3600) / 60;
        long s = totalSeconds % 60;

        String res = numberToWords((int) h) + " giờ " +
                numberToWords((int) m) + " phút " +
                numberToWords((int) s) + " giây";
        return res.substring(0,1).toUpperCase(Locale.getDefault()) + res.substring(1);
    }

    // 0..59 → chữ Việt tự nhiên
    private String numberToWords(int n) {
        if (n == 0) return "không";
        String[] u = {"", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín"};
        if (n < 10) return u[n];
        if (n < 20) {
            if (n == 10) return "mười";
            if (n == 15) return "mười lăm";
            return "mười " + numberToWords(n - 10);
        }
        int chuc = n / 10, donvi = n % 10;
        String[] tens = {"", "", "hai mươi", "ba mươi", "bốn mươi", "năm mươi",
                "sáu mươi", "bảy mươi", "tám mươi", "chín mươi"};
        String tail;
        if (donvi == 0) tail = "";
        else if (donvi == 1 && chuc >= 2) tail = " mốt";
        else if (donvi == 4 && chuc >= 2) tail = " bốn";
        else if (donvi == 5 && chuc >= 2) tail = " lăm";
        else tail = " " + u[donvi];
        return tens[chuc] + tail;
    }

    @Override protected void onPause() {
        super.onPause();
        if (running) handler.removeCallbacks(ticker);
    }
    @Override protected void onResume() {
        super.onResume();
        if (running) handler.post(ticker);
    }
}
