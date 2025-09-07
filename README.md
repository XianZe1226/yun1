# 网易云音乐广告跳过助手

一款专为网易云音乐设计的Android无障碍服务应用，自动跳过开屏广告，提升用户体验。

## 功能特点

- 🚀 **快速跳过**：优化算法，减少冷却时间，提高广告跳过速度
- 🎯 **精准识别**：支持多种跳过按钮文本和ID识别
- 🔒 **隐私安全**：无需网络权限，所有处理在本地完成
- ⚡ **轻量高效**：低资源占用，不影响设备性能
- 📱 **易用性**：简洁界面，一键启用无障碍服务

## 技术实现

基于Android无障碍服务(AccessibilityService)实现，通过监控窗口状态变化和内容变化，自动查找并点击跳过按钮。

### 支持的跳过按钮识别
- **文本匹配**：跳过、跳过广告、skip、Skip、跳過、关闭广告、关闭、×、X等
- **ID匹配**：com.netease.cloudmusic:id/updateVersionTitle、com.netease.cloudmusic:id/md_dialog_cm_close_btn等

## 安装和使用

### 前提条件
- Android 5.0 (API 21) 或更高版本
- 需要启用无障碍服务权限

### 安装步骤
1. 下载并安装APK文件
2. 打开应用，点击"打开无障碍设置"
3. 在系统设置中找到"网易云音乐广告跳过助手"
4. 启用服务开关
5. 返回应用，确认服务状态显示"已启用 ✓"

### 构建项目
```bash
# 克隆仓库
git clone https://github.com/XianZe1226/yun1.git

# 打开项目 in Android Studio
# 或使用命令行构建
./gradlew assembleDebug
```

## 项目结构

```
yun1/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/adskipper/
│   │   │   ├── AdSkipAccessibilityService.java  # 主要服务逻辑
│   │   │   └── MainActivity.java                 # 主界面
│   │   ├── res/                                  # 资源文件
│   │   └── AndroidManifest.xml                   # 应用清单
│   └── build.gradle                             # 模块配置
├── build.gradle                                 # 项目配置
├── settings.gradle                             # 项目设置
└── README.md                                   # 项目说明
```

## 版本历史

- **v1.0.0** (当前版本)
  - 初始发布
  - 优化广告跳过算法
  - 减少冷却时间至500ms
  - 完善.gitignore配置

## 注意事项

- 本应用仅用于学习和研究目的，请遵守相关法律法规
- 使用前请确保已获得网易云音乐应用的使用授权
- 应用需要无障碍服务权限，请谨慎授权

## 贡献

欢迎提交Issue和Pull Request来改进这个项目。

## 许可证

本项目采用MIT许可证。详见LICENSE文件。

## 支持

如果您遇到任何问题或有建议，请通过GitHub Issues联系我们。
