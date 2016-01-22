package com.wiwide.redpacketworker;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yueguang on 16-1-22.
 */
public class WorkerService extends AccessibilityService {
    private Set<String> mRPListHistory = new HashSet<>();
    private Set<String> mRPHistory = new HashSet<>();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getSource() != null) {
            List<AccessibilityNodeInfo> redListNodes = accessibilityEvent.getSource().findAccessibilityNodeInfosByText("[微信红包]");
            if (redListNodes.size() > 0) {
                AccessibilityNodeInfo redNode = redListNodes.get(0);
                if (!mRPListHistory.contains(redNode.getContentDescription().toString())) {
                    mRPListHistory.add(redNode.getContentDescription().toString());
                    handleRedPacketList(redNode);
                }
            }

            List<AccessibilityNodeInfo> redNodes = accessibilityEvent.getSource().findAccessibilityNodeInfosByText("领取红包");
            if (redNodes.size() > 0) {
                AccessibilityNodeInfo redNode = redNodes.get(0);
                Log.i("xxxxx", "r222edNode:" + redNode);
                if (!mRPHistory.contains(redNode.toString())) {
                    Log.i("xxxxx", "redNode:" + redNode);
                    mRPHistory.add(redNode.toString());
                    handleRedPacket(redNode);
                }
            }

            AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
            if (nodeInfo == null) {
                return;
            }
            List<AccessibilityNodeInfo> list = nodeInfo
                    .findAccessibilityNodeInfosByText("拆红包"); // 获取包含 拆红包
            // 文字的控件，模拟点击事件，拆开红包
            for (AccessibilityNodeInfo n : list) {
                n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }

            back(accessibilityEvent);

            List<AccessibilityNodeInfo> endNodes = accessibilityEvent.getSource().findAccessibilityNodeInfosByText("手慢了");
            if (endNodes.size() > 0) {
                List<AccessibilityNodeInfo> exitNodes = accessibilityEvent.getSource().findAccessibilityNodeInfosByViewId("b2h");
                if (exitNodes.size() > 0) {
                    exitNodes.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }
    }

    private void back(AccessibilityEvent accessibilityEvent) {
        List<AccessibilityNodeInfo> redNodes = accessibilityEvent.getSource().findAccessibilityNodeInfosByText("红包详情");
        if (redNodes.size() > 0) {
            List<AccessibilityNodeInfo> list = accessibilityEvent.getSource()
                    .findAccessibilityNodeInfosByViewId("com.tencent.mm:id/fc"); // 获取包含 拆红包
            if (list.size() > 0) {
                list.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    private void back2(AccessibilityEvent accessibilityEvent) {
        List<AccessibilityNodeInfo> redNodes = accessibilityEvent.getSource().findAccessibilityNodeInfosByText("红包详情");
        if (redNodes.size() > 0) {
            List<AccessibilityNodeInfo> list = accessibilityEvent.getSource()
                    .findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ev"); // 获取包含 拆红包
            if (list.size() > 0) {
                list.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    @Override
    public void onInterrupt() {
        Log.i("xxxxx", "onInterrupt");
    }

    private void handleRedPacketList(final AccessibilityNodeInfo redNodes) {
        redNodes.performAction(AccessibilityNodeInfo.ACTION_CLICK);
    }

    private void handleRedPacket(final AccessibilityNodeInfo redNodes) {
        if (redNodes.getParent() != null) {
            redNodes.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            redNodes.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }
}
