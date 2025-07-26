@echo off
echo 正在构建APK文件...

REM 检查Java是否安装
java -version >nul 2>&1
if errorlevel 1 (
    echo 错误：未找到Java，请先安装Java JDK
    pause
    exit /b 1
)

REM 创建临时目录
if not exist "temp" mkdir temp
if not exist "temp\classes" mkdir temp\classes
if not exist "temp\resources" mkdir temp\resources

REM 复制资源文件
xcopy /E /I /Y "app\src\main\res" "temp\resources\res"

REM 编译Java文件
javac -cp "android.jar;androidx-appcompat-1.6.1.jar" -d temp\classes app\src\main\java\com\example\apkrenamer\*.java

if errorlevel 1 (
    echo 编译失败
    pause
    exit /b 1
)

echo 编译成功！
echo APK文件将在 temp 目录中生成
pause 