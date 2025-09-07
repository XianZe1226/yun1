package com.example.adskipper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {

    // 移除权限请求相关代码，简化应用

    private Button btnOpenAccessibility;
    private TextView tvServiceStatus;
    private TextView tvAppDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        btnOpenAccessibility = findViewById(R.id.btn_open_accessibility);
        tvServiceStatus = findViewById(R.id.tv_service_status);
        tvAppDescription = findViewById(R.id.tv_app_description);

        // 设置应用描述
        tvAppDescription.setText("网易云音乐广告跳过助手\n专为网易云音乐开屏广告设计\n自动跳过开屏广告，提升使用体验");

        btnOpenAccessibility.setOnClickListener(v -> {
            // 直接打开无障碍设置，不再请求其他权限
            openAccessibilitySettings();
        });

        updateServiceStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateServiceStatus();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    
    private void openAccessibilitySettings() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
    }

    private void updateServiceStatus() {
        boolean isServiceEnabled = AdSkipAccessibilityService.isServiceEnabled(this);
        if (isServiceEnabled) {
            tvServiceStatus.setText("服务状态：已启用 ✓");
            tvServiceStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            tvServiceStatus.setText("服务状态：未启用");
            tvServiceStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
    }
    
}