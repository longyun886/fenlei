# APK文件分类重命名工具

这是一个Android应用，用于批量重命名APK文件并根据特定参数进行分类。

## 功能特性

### 主要功能
- **后缀设置**：自定义重命名后的文件后缀（自动去掉xaxbxc字符）
- **路径设置**：设置要处理的APK文件路径（自动去掉dcim字符）
- **批量重命名**：自动重命名指定路径下的所有APK文件
- **自动分类**：根据文件大小自动创建文件夹并移动文件

### 智能输入处理
- 后缀输入框：输入时自动去掉"xaxbxc"字符
- 路径输入框：输入时自动去掉"dcim"字符
- 还原按钮：快速恢复默认路径设置

### 自动化选项
- **自动文件夹创建**：根据文件大小创建分类文件夹
  - 小文件（< 1MB）
  - 中等文件（1MB - 10MB）
  - 大文件（> 10MB）
- **自动重命名**：设置完成后自动执行重命名操作

## 文件命名规则

重命名后的文件格式：`原文件名_时间戳_后缀.apk`

示例：
- 原文件：`test.apk`
- 重命名后：`test_20241201_143022_自定义后缀.apk`

## 构建说明

### 方法一：使用Android Studio（推荐）

1. 下载并安装 [Android Studio](https://developer.android.com/studio)
2. 打开Android Studio，选择"Open an existing Android Studio project"
3. 选择本项目的根目录（ApkRenamer文件夹）
4. 等待项目同步完成
5. 点击菜单 Build → Build Bundle(s) / APK(s) → Build APK(s)
6. APK文件将生成在 `app/build/outputs/apk/debug/` 目录中

### 方法二：使用命令行

如果您已安装Android SDK和Gradle：

```bash
cd ApkRenamer
./gradlew assembleRelease
```

APK文件将生成在 `app/build/outputs/apk/release/` 目录中。

## 项目结构

```
ApkRenamer/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/apkrenamer/
│   │   │   └── MainActivity.java          # 主活动类
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   └── activity_main.xml      # 主界面布局
│   │   │   ├── drawable/                  # 图标和背景资源
│   │   │   ├── values/
│   │   │   │   ├── colors.xml            # 颜色定义
│   │   │   │   └── styles.xml            # 样式定义
│   │   │   └── mipmap-*/                 # 应用图标
│   │   └── AndroidManifest.xml           # 应用清单文件
│   ├── build.gradle                      # 应用级构建配置
│   └── proguard-rules.pro               # 代码混淆规则
├── build.gradle                         # 项目级构建配置
├── settings.gradle                      # 项目设置
├── gradle.properties                    # Gradle属性配置
└── README.md                           # 项目说明文档
```

## 权限要求

应用需要以下权限：
- `READ_EXTERNAL_STORAGE`：读取外部存储
- `WRITE_EXTERNAL_STORAGE`：写入外部存储

## 使用说明

1. **启动应用**：安装并打开APK重命名工具
2. **设置后缀**：在后缀框中输入自定义后缀（会自动去掉xaxbxc）
3. **设置路径**：在路径框中输入APK文件所在目录（会自动去掉dcim）
4. **选择功能**：
   - 勾选"自动创建文件夹并移动文件"以启用分类功能
   - 勾选"设置完成后自动执行重命名"以启用自动重命名
5. **执行重命名**：点击"开始重命名"按钮或等待自动执行

## 注意事项

- 确保目标路径存在且包含APK文件
- 重命名操作不可逆，建议先备份重要文件
- 应用需要存储权限才能正常工作
- 支持Android 5.0（API 21）及以上版本

## 技术实现

- **开发语言**：Java
- **最低SDK版本**：API 21 (Android 5.0)
- **目标SDK版本**：API 33 (Android 13)
- **主要依赖**：
  - androidx.appcompat:appcompat:1.6.1
  - com.google.android.material:material:1.9.0

## 许可证

本项目仅供学习和个人使用。 