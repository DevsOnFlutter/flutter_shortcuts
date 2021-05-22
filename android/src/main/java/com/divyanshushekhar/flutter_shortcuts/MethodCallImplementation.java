package com.divyanshushekhar.flutter_shortcuts;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.res.Resources;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MethodCallImplementation implements MethodChannel.MethodCallHandler {
    private static final String EXTRA_ACTION = "flutter_shortcuts";

    private final Context context;
    private Activity activity;

    MethodCallImplementation(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
            result.success(null);
            return;
        }
        ShortcutManager shortcutManager =
                (ShortcutManager) context.getSystemService(Context.SHORTCUT_SERVICE);
        switch (call.method) {
            case "setShortcutItems":
                List<Map<String, String>> arg = call.arguments();
                List<ShortcutInfo> shortcuts = processShortcuts(arg);
                shortcutManager.setDynamicShortcuts(shortcuts);
                Toast.makeText(context, "Shortcut Created", Toast.LENGTH_SHORT).show();
                break;
            case "updateShortcutItems":
                List<Map<String, String>> updateShortcutArgs = call.arguments();
                List<ShortcutInfo> updateShortcuts = processShortcuts(updateShortcutArgs);
                boolean updated = shortcutManager.updateShortcuts(updateShortcuts);
                Toast.makeText(context, "Shortcut Updated: " + updated, Toast.LENGTH_SHORT).show();
                break;
            case "clearShortcutItems":
                shortcutManager.removeAllDynamicShortcuts();
                break;
            case "getLaunchAction":
                if (activity == null) {
                    result.error(
                            "flutter_shortcuts_no_activity",
                            "There is no activity available when launching action",
                            null);
                    return;
                }
                final Intent intent = activity.getIntent();
                final String launchAction = intent.getStringExtra(EXTRA_ACTION);
                if (launchAction != null && !launchAction.isEmpty()) {
                    shortcutManager.reportShortcutUsed(launchAction);
                    intent.removeExtra(EXTRA_ACTION);
                }
                result.success(launchAction);
                return;
            default:
                result.notImplemented();
                return;
        }
        result.success(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    @TargetApi(Build.VERSION_CODES.N_MR1)
    private List<ShortcutInfo> processShortcuts(List<Map<String, String>> shortcuts) {
        final List<ShortcutInfo> shortcutList = new ArrayList<>();

        for (Map<String, String> shortcut : shortcuts) {
            final String id = shortcut.get("id");
            final String icon = shortcut.get("icon");
            final String action = shortcut.get("action");
            final String title = shortcut.get("title");
            final ShortcutInfo.Builder shortcutBuilder = new ShortcutInfo.Builder(context, id);

            final int resourceId = loadResourceId(context, icon);
            final Intent intent = getIntentToOpenMainActivity(action);

            if (resourceId > 0) {
                shortcutBuilder.setIcon(Icon.createWithResource(context, resourceId));
            }

            final ShortcutInfo shortcutInfo = shortcutBuilder
                    .setLongLabel(title)
                    .setShortLabel(title)
                    .setIntent(intent)
                    .build();
            shortcutList.add(shortcutInfo);
        }
        return shortcutList;
    }

    private int loadResourceId(Context context, String icon) {
        if (icon == null) {
            return 0;
        }
        final String packageName = context.getPackageName();
        final Resources res = context.getResources();
        final int resourceId = res.getIdentifier(icon, "drawable", packageName);

        if (resourceId == 0) {
            return res.getIdentifier(icon, "mipmap", packageName);
        } else {
            return resourceId;
        }
    }

    private Intent getIntentToOpenMainActivity(String type) {
        final String packageName = context.getPackageName();

        return context
                .getPackageManager()
                .getLaunchIntentForPackage(packageName)
                .setAction(Intent.ACTION_RUN)
                .putExtra(EXTRA_ACTION, type)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }
}
