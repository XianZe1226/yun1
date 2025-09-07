package com.example.adskipper;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

public class AdSkipAccessibilityService extends AccessibilityService {

    private static final String TARGET_PACKAGE = "com.netease.cloudmusic";
    private static final List<String> SKIP_BUTTON_TEXTS = new ArrayList<>();

    static {
        // 网易云音乐专用跳过按钮文本关键词
        SKIP_BUTTON_TEXTS.add("跳过");
        SKIP_BUTTON_TEXTS.add("跳过广告");
        SKIP_BUTTON_TEXTS.add("skip");
        SKIP_BUTTON_TEXTS.add("Skip");
        SKIP_BUTTON_TEXTS.add("跳過");
        SKIP_BUTTON_TEXTS.add("跳过推广");
        SKIP_BUTTON_TEXTS.add("跳过视频");
        SKIP_BUTTON_TEXTS.add("立即跳过");
        SKIP_BUTTON_TEXTS.add("点击跳过");
        SKIP_BUTTON_TEXTS.add("关闭广告");
        SKIP_BUTTON_TEXTS.add("关闭");
        SKIP_BUTTON_TEXTS.add("×");
        SKIP_BUTTON_TEXTS.add("X");
    }

    private long lastSkipTime = 0;
    private static final long SKIP_COOLDOWN = 500; // 减少到500ms冷却时间，提高响应速度
    private String lastPackageName = "";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        String packageName = event.getPackageName() != null ? event.getPackageName().toString() : "";
        String className = event.getClassName() != null ? event.getClassName().toString() : "";

        // 只检查网易云音乐
        if (TARGET_PACKAGE.equals(packageName)) {
            long currentTime = System.currentTimeMillis();

            // 添加冷却时间，防止重复触发，但减少冷却时间以提高响应速度
            if (currentTime - lastSkipTime < SKIP_COOLDOWN && packageName.equals(lastPackageName)) {
                return;
            }

            // 优化事件处理：只在窗口状态变化时立即触发，其他事件类型延迟处理以避免重复
            if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                lastPackageName = packageName;
                performSkipAction();
                lastSkipTime = currentTime;
            }
            // 对于内容变化和滚动事件，使用延迟检查以避免频繁触发
            else if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED ||
                     eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
                lastPackageName = packageName;
                // 延迟50ms执行，让UI有足够时间加载，但减少不必要的立即检查
                android.os.Handler handler = new android.os.Handler();
                handler.postDelayed(this::performSkipAction, 50);
                lastSkipTime = currentTime;
            }
        }
    }

    private void performSkipAction() {
        try {
            AccessibilityNodeInfo rootNode = getRootInActiveWindow();
            if (rootNode != null) {
                boolean found = handleNeteaseSpecific(rootNode);

                // 释放资源
                rootNode.recycle();
            }
        } catch (Exception e) {
            // 捕获所有异常，确保服务不会崩溃
            e.printStackTrace();
        }
    }

    @Override
    public void onInterrupt() {
        // 服务中断时的处理
    }

    public static boolean isServiceEnabled(Context context) {
        // 检查服务是否已启用
        String enabledServices = android.provider.Settings.Secure.getString(
                context.getContentResolver(),
                android.provider.Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);

        if (enabledServices != null) {
            String serviceName = context.getPackageName() + "/" + AdSkipAccessibilityService.class.getName();
            return enabledServices.contains(serviceName);
        }
        return false;
    }

    private boolean isNodeVisible(AccessibilityNodeInfo node) {
        // 简单的可见性检查 - 确保节点在屏幕上
        return node != null && node.isVisibleToUser();
    }


    private boolean handleNeteaseSpecific(AccessibilityNodeInfo rootNode) {
        // 网易云音乐专用处理 - 优化识别速度
        // 优先使用ID查找，因为更快更可靠
        
        String[] neteaseSkipIds = {
                "com.netease.cloudmusic:id/updateVersionTitle", // 李跳跳项目规则
                "com.netease.cloudmusic:id/md_dialog_cm_close_btn", // 李跳跳项目规则对应的action
                "com.netease.cloudmusic:id/skip_text", // 最常见的跳过文本
                "com.netease.cloudmusic:id/btn_skip", // 跳过按钮
                "com.netease.cloudmusic:id/skip_ad", // 跳过广告
                "com.netease.cloudmusic:id/tv_count_down", // 倒计时文本
                "com.netease.cloudmusic:id/close_btn", // 关闭按钮
                "com.netease.cloudmusic:id/iv_close" // 关闭图标
        };

        // 优化ID查找：直接点击已知的跳过按钮ID，无需额外文本检查
        for (String id : neteaseSkipIds) {
            try {
                List<AccessibilityNodeInfo> nodes = rootNode.findAccessibilityNodeInfosByViewId(id);
                for (AccessibilityNodeInfo node : nodes) {
                    if (node != null && node.isClickable() && node.isEnabled() && isNodeVisible(node)) {
                        // 对于已知的跳过按钮ID，直接点击，减少验证步骤
                        node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        return true;
                    }
                }
            } catch (Exception e) {
                // 捕获异常，防止闪退
                e.printStackTrace();
            }
        }

        // 如果ID检测没找到，再尝试优化的文本检测
        return findSkipButtonByTextOptimized(rootNode);
    }

    private boolean findSkipButtonByTextOptimized(AccessibilityNodeInfo rootNode) {
        // 优化的文本查找：使用更高效的匹配方式
        for (String text : SKIP_BUTTON_TEXTS) {
            List<AccessibilityNodeInfo> nodes = rootNode.findAccessibilityNodeInfosByText(text);
            for (AccessibilityNodeInfo node : nodes) {
                if (node != null && node.isClickable() && node.isEnabled() && isNodeVisible(node)) {
                    // 立即点击跳过按钮
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    return true;
                }
            }
        }
        return false;
    }

}