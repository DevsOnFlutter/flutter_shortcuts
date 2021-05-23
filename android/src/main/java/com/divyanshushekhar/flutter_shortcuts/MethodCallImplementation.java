package com.divyanshushekhar.flutter_shortcuts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MethodCallImplementation implements MethodChannel.MethodCallHandler {
    private static final String EXTRA_ACTION = "flutter_shortcuts";
    private static final String TAG = FlutterShortcutsPlugin.getTAG();

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
            case "getMaxShortcutLimit":
                final int maxLimit = getMaxShortcutLimit();
                result.success(maxLimit);
                break;
            case "getIconProperties":
                Map<String,Integer> properties =  getIconProperties();
                result.success(properties);
                break;
            case "setShortcutItems":
                setShortcutItems(call,shortcutManager);
                break;
            case "pushShortcutItem":
                pushShortcutItem(call,shortcutManager);
                break;
            case "pushShortcutItems":
                pushShortcutItems(call,shortcutManager);
                break;
            case "updateShortcutItems":
                updateShortcutItems(call,shortcutManager);
                break;
            case "updateShortcutItem":
                updateShortcutItem(call,shortcutManager);
                break;
            case "updateShortLabel":
                updateShortLabel(call,shortcutManager);
                break;
            case "changeShortcutItemIcon":
                changeShortcutItemIcon(call,shortcutManager);
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
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private int getMaxShortcutLimit() {
        ShortcutManager shortcutManager =
                (ShortcutManager) context.getSystemService(Context.SHORTCUT_SERVICE);
        return shortcutManager.getMaxShortcutCountPerActivity();
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private Map<String, Integer> getIconProperties() {
        ShortcutManager shortcutManager =
                (ShortcutManager) context.getSystemService(Context.SHORTCUT_SERVICE);
        Map<String, Integer> properties = new HashMap<String, Integer>();
        properties.put("maxHeight", shortcutManager.getIconMaxHeight());
        properties.put("maxWidth", shortcutManager.getIconMaxWidth());
        return properties;
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void setShortcutItems(MethodCall call,ShortcutManager shortcutManager) {
        List<Map<String, String>> setShortcutItemsArgs = call.arguments();
        List<ShortcutInfo> shortcuts;
        try {
            shortcuts = processShortcuts(setShortcutItemsArgs);
            shortcutManager.setDynamicShortcuts(shortcuts);
        } catch (Exception e) {
            Log.e(TAG,e.toString());
        }
        Toast.makeText(context, "Shortcut Created", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void pushShortcutItem(MethodCall call, ShortcutManager shortcutManager) {
        final List<Map<String, String>> args = call.arguments();
        List<ShortcutInfo> shortcuts;
        try {
            shortcuts = processShortcuts(args);
            shortcutManager.addDynamicShortcuts(shortcuts);
        } catch (Exception e) {
            Log.e(TAG,e.toString());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void pushShortcutItems(MethodCall call, ShortcutManager shortcutManager) {
        final List<Map<String, String>> args = call.arguments();
        List<ShortcutInfo> shortcuts;
        try {
            shortcuts = processShortcuts(args);
            shortcutManager.addDynamicShortcuts(shortcuts);
        } catch (Exception e) {
            Log.e(TAG,e.toString());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void updateShortcutItems(MethodCall call, ShortcutManager shortcutManager) {
        List<Map<String, String>> updateAllShortcutArgs = call.arguments();
        boolean updated = false;
        try {
            List<ShortcutInfo> updateShortcuts = processShortcuts(updateAllShortcutArgs);
            updated = shortcutManager.updateShortcuts(updateShortcuts);
        } catch(Exception e) {
            Log.e(TAG, e.toString());
        }
        if(updated) {
            Log.d(TAG,"All Shortcuts updated");
        } else {
            Log.d(TAG,"Unable to update all shortcuts");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void updateShortcutItem(MethodCall call, ShortcutManager shortcutManager) {
        final List<Map<String, String>> args = call.arguments();
        Map<String, String> info = args.get(0);
        final String refId = info.get("id");
        List<ShortcutInfo> dynamicShortcuts = shortcutManager.getDynamicShortcuts();
        final List<ShortcutInfo> shortcutList = new ArrayList<>();
        int flag = 1;
        for(ShortcutInfo si : dynamicShortcuts) {
            if(si.getId().equalsIgnoreCase(refId))  {
                ShortcutInfo shortcutInfo = createShortcutInfo(info);
                shortcutList.add(shortcutInfo);
                flag = 0;
                continue;
            }
            shortcutList.add(si);
        }
        if (flag == 1) {
            Log.e(TAG, "ID did not match any shortcut");
            return;
        }
        try {
            shortcutManager.updateShortcuts(shortcutList);
        } catch(Exception e) {
            Log.e(TAG,e.toString());
        }
    }

    private void updateShortLabel(MethodCall call, ShortcutManager shortcutManager) {
        final List<String> args = call.arguments();
        final String refId = args.get(0);
        final String title = args.get(1);

    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void changeShortcutItemIcon(MethodCall call, ShortcutManager shortcutManager) {
        try {
            final List<String> args = call.arguments();
            final String refId = args.get(0);
            final String changeIcon = args.get(1);
            Map<String,String> items = deserializeShortcutInfoAtId(refId,changeIcon,shortcutManager);
            ShortcutInfo shortcutInfo = createShortcutInfo(items);
            List<ShortcutInfo> dynamicShortcuts = shortcutManager.getDynamicShortcuts();
            final List<ShortcutInfo> shortcutList = new ArrayList<>();
            int flag = 1;
            for(ShortcutInfo si : dynamicShortcuts) {
                if(si.getId().equalsIgnoreCase(refId))  {
                    shortcutList.add(shortcutInfo);
                    flag = 0;
                    continue;
                }
                shortcutList.add(si);
            }
            if (flag == 1) {
                Log.e(TAG, "ID did not match any shortcut");
                return;
            }
            try {
                shortcutManager.updateShortcuts(shortcutList);
            } catch(Exception e) {
                Log.e(TAG,e.toString());
            }
        } catch(Exception e) {
            Log.e(TAG,e.toString());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private Map<String,String> deserializeShortcutInfoAtId(String id, String icon, ShortcutManager shortcutManager) {
        HashMap<String, String> map = new HashMap<String, String>();
        List<ShortcutInfo> dynamicShortcuts = shortcutManager.getDynamicShortcuts();

        for(ShortcutInfo si : dynamicShortcuts) {
            if(si.getId().equalsIgnoreCase(id))  {
                map.put("id", si.getId());
                map.put("shortLabel", String.valueOf(si.getShortLabel()));
                map.put("icon", icon);
                map.put("action",si.getIntent().getStringExtra(EXTRA_ACTION));
            }
        }
        return map;
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private List<ShortcutInfo> processShortcuts(List<Map<String, String>> shortcuts) {
        final List<ShortcutInfo> shortcutList = new ArrayList<>();

        for (Map<String, String> shortcut : shortcuts) {
            ShortcutInfo shortcutInfo = createShortcutInfo(shortcut);
            shortcutList.add(shortcutInfo);
        }
        return shortcutList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private ShortcutInfo createShortcutInfo(Map<String, String> shortcut) {
        final String id = shortcut.get("id");
        final String icon = shortcut.get("icon");
        final String action = shortcut.get("action");
        final String shortLabel = shortcut.get("shortLabel");
        final String longLabel = shortcut.get("LongLabel");
        final ShortcutInfo.Builder shortcutBuilder;
        shortcutBuilder = new ShortcutInfo.Builder(context, id);

        final int resourceId = loadResourceId(context, icon);
        final Intent intent = getIntentToOpenMainActivity(action);

        if (resourceId > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                shortcutBuilder.setIcon(Icon.createWithResource(context, resourceId));
            }
        }

        if(longLabel != null) {
            shortcutBuilder.setLongLabel(longLabel);
        }

        assert shortLabel!=null;
        return shortcutBuilder
                .setShortLabel(shortLabel)
                .setIntent(intent)
                .build();
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
