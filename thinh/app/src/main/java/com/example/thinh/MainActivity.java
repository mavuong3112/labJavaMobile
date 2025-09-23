package com.example.thinh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText etName = findViewById(R.id.etName);
        Button btnOpen = findViewById(R.id.btnOpen);

        btnOpen.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            Intent intent = new Intent(MainActivity.this, StopwatchActivity.class);
            intent.putExtra("USER_NAME", name);
            startActivity(intent);
        });
    }
}
