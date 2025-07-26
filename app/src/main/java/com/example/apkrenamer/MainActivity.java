package com.example.apkrenamer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    
    private EditText suffixEditText;
    private EditText pathEditText;
    private Button renameButton;
    private Button restoreButton;
    private CheckBox autoFolderCheckBox;
    private CheckBox autoRenameCheckBox;
    
    private static final int PERMISSION_REQUEST_CODE = 1001;
    private static final String DEFAULT_PATH = "/sdcard/dcim/";
    private static final String DEFAULT_SUFFIX = ".xaxbxc";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initViews();
        setupListeners();
        checkPermissions();
    }
    
    private void initViews() {
        suffixEditText = findViewById(R.id.suffix_edit_text);
        pathEditText = findViewById(R.id.path_edit_text);
        renameButton = findViewById(R.id.rename_button);
        restoreButton = findViewById(R.id.restore_button);
        autoFolderCheckBox = findViewById(R.id.auto_folder_checkbox);
        autoRenameCheckBox = findViewById(R.id.auto_rename_checkbox);
        
        // 设置默认值
        suffixEditText.setText(DEFAULT_SUFFIX);
        pathEditText.setText(DEFAULT_PATH);
    }
    
    private void setupListeners() {
        // 后缀输入监听器 - 自动去掉xaxbxc
        suffixEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            
            @Override
            public void afterTextChanged(android.text.Editable s) {
                String text = s.toString();
                if (text.contains("xaxbxc")) {
                    String newText = text.replace("xaxbxc", "");
                    if (!text.equals(newText)) {
                        suffixEditText.setText(newText);
                        suffixEditText.setSelection(newText.length());
                    }
                }
            }
        });
        
        // 路径输入监听器 - 自动去掉dcim
        pathEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            
            @Override
            public void afterTextChanged(android.text.Editable s) {
                String text = s.toString();
                if (text.contains("dcim")) {
                    String newText = text.replace("dcim", "");
                    if (!text.equals(newText)) {
                        pathEditText.setText(newText);
                        pathEditText.setSelection(newText.length());
                    }
                }
            }
        });
        
        // 重命名按钮
        renameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRename();
            }
        });
        
        // 还原按钮
        restoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restoreDefaultPath();
            }
        });
        
        // 自动重命名复选框
        autoRenameCheckBox.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    performRename();
                }
            }
        });
    }
    
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 
                    PERMISSION_REQUEST_CODE);
        }
    }
    
    private void performRename() {
        String suffix = suffixEditText.getText().toString();
        String path = pathEditText.getText().toString();
        
        if (suffix.isEmpty() || path.isEmpty()) {
            Toast.makeText(this, "请填写后缀和路径", Toast.LENGTH_SHORT).show();
            return;
        }
        
        File directory = new File(path);
        if (!directory.exists() || !directory.isDirectory()) {
            Toast.makeText(this, "路径不存在或不是目录", Toast.LENGTH_SHORT).show();
            return;
        }
        
        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            Toast.makeText(this, "目录为空", Toast.LENGTH_SHORT).show();
            return;
        }
        
        int renamedCount = 0;
        for (File file : files) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(".apk")) {
                String newName = generateNewName(file, suffix);
                File newFile = new File(directory, newName);
                
                if (file.renameTo(newFile)) {
                    renamedCount++;
                    
                    // 如果启用了自动文件夹功能
                    if (autoFolderCheckBox.isChecked()) {
                        createFolderAndMove(newFile);
                    }
                }
            }
        }
        
        Toast.makeText(this, "成功重命名 " + renamedCount + " 个APK文件", Toast.LENGTH_SHORT).show();
    }
    
    private String generateNewName(File file, String suffix) {
        String originalName = file.getName();
        String nameWithoutExt = originalName.substring(0, originalName.lastIndexOf('.'));
        
        // 生成时间戳
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String timestamp = sdf.format(new Date());
        
        return nameWithoutExt + "_" + timestamp + suffix + ".apk";
    }
    
    private void createFolderAndMove(File apkFile) {
        try {
            // 根据文件大小创建文件夹
            long fileSize = apkFile.length();
            String sizeCategory = getSizeCategory(fileSize);
            
            File categoryFolder = new File(apkFile.getParent(), sizeCategory);
            if (!categoryFolder.exists()) {
                categoryFolder.mkdirs();
            }
            
            // 移动文件到对应文件夹
            File newLocation = new File(categoryFolder, apkFile.getName());
            apkFile.renameTo(newLocation);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String getSizeCategory(long fileSize) {
        if (fileSize < 1024 * 1024) { // 小于1MB
            return "小文件";
        } else if (fileSize < 10 * 1024 * 1024) { // 小于10MB
            return "中等文件";
        } else {
            return "大文件";
        }
    }
    
    private void restoreDefaultPath() {
        pathEditText.setText(DEFAULT_PATH);
        Toast.makeText(this, "路径已还原", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "存储权限已授予", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "需要存储权限才能重命名文件", Toast.LENGTH_SHORT).show();
            }
        }
    }
} 