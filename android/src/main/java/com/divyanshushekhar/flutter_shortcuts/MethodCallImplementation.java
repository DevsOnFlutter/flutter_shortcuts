package com.divyanshushekhar.flutter_shortcuts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.flutter.FlutterInjector;
import io.flutter.embedding.engine.loader.FlutterLoader;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MethodCallImplementation implements MethodChannel.MethodCallHandler {
    private static final String EXTRA_ACTION = "flutter_shortcuts";
    private static final String TAG = FlutterShortcutsPlugin.getTAG();

    private final Context context;
    private Activity activity;

    private boolean debug;
    private boolean voiceAssistantVisibility;

    void debugPrint(String message) {
        if(debug) {
            Log.d(TAG,message);
        }
    }

    MethodCallImplementation(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    void setActivity(Activity activity) {
        this.activity = activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private ShortcutManager shortcutManager() {
        return (ShortcutManager) context.getSystemService(Context.SHORTCUT_SERVICE);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
            result.success(null);
            return;
        }

        switch (call.method) {
            case "initialize":
                initialize(call);
                break;
            case "getLaunchAction":
                getLaunchAction(result);
                break;
            case "getMaxShortcutLimit":
                result.success(getMaxShortcutLimit());
                break;
            case "getIconProperties":
                result.success(getIconProperties());
                break;
            case "setShortcutItems":
                setShortcutItems(call);
                break;
            case "pushShortcutItem":
                pushShortcutItem(call);
                break;
            case "pushShortcutItems":
                pushShortcutItems(call);
                break;
            case "updateShortcutItems":
                updateShortcutItems(call);
                break;
            case "updateShortcutItem":
                updateShortcutItem(call);
                break;
            case "updateShortLabel":
                updateShortLabel(call);
                break;
            case "changeShortcutItemIcon":
                changeShortcutItemIcon(call);
                break;
            case "clearShortcutItems":
                if(!this.voiceAssistantVisibility) {
                    shortcutManager().removeAllDynamicShortcuts();
                } else {
                    ShortcutManagerCompat.removeAllDynamicShortcuts(context);
                }
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    private void initialize(MethodCall call) {
        List<Map<String, String>> args = call.arguments();
        this.debug = Boolean.parseBoolean(args.get(0).get("debug"));
        this.voiceAssistantVisibility = Boolean.parseBoolean(args.get(0).get("voiceAssistantVisibility"));
        debugPrint("Flutter Shortcuts Initialized");
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void getLaunchAction(MethodChannel.Result result) {
        if (activity == null) {
            result.error(
                    "flutter_shortcuts_no_activity",
                    "There is no activity available when launching action",
                    null);
            return;
        }
        final Intent intent = activity.getIntent();
        final String launchAction = intent.getStringExtra(EXTRA_ACTION);
        if(!this.voiceAssistantVisibility) {
            if (launchAction != null && !launchAction.isEmpty()) {
                shortcutManager().reportShortcutUsed(launchAction);
                intent.removeExtra(EXTRA_ACTION);
            }
        } else {
            if (launchAction != null && !launchAction.isEmpty()) {
                ShortcutManagerCompat.reportShortcutUsed(context,launchAction);
                intent.removeExtra(EXTRA_ACTION);
            }
        }
        result.success(launchAction);
        debugPrint("Launch Action: " + launchAction);
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private int getMaxShortcutLimit() {
        if(!this.voiceAssistantVisibility) {
            return shortcutManager().getMaxShortcutCountPerActivity();
        }
        return ShortcutManagerCompat.getMaxShortcutCountPerActivity(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private Map<String, Integer> getIconProperties() {
        Map<String, Integer> properties = new HashMap<String, Integer>();
        if(!this.voiceAssistantVisibility) {
            properties.put("maxHeight", shortcutManager().getIconMaxHeight());
            properties.put("maxWidth", shortcutManager().getIconMaxWidth());
        } else {
            properties.put("maxHeight", ShortcutManagerCompat.getIconMaxHeight(context));
            properties.put("maxWidth", ShortcutManagerCompat.getIconMaxWidth(context));
        }
        return properties;
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void setShortcutItems(MethodCall call) {
        List<Map<String, String>> args = call.arguments();
        if(!this.voiceAssistantVisibility) {
            List<ShortcutInfo> shortcuts;
            try {
                shortcuts = shortcutInfoList(args);
                shortcutManager().setDynamicShortcuts(shortcuts);
                debugPrint("Shortcuts created");
            } catch (Exception e) {
                Log.e(TAG,e.toString());
            }
        } else {
            List<ShortcutInfoCompat> shortcuts;
            try {
                shortcuts = shortcutInfoCompatList(args);
                ShortcutManagerCompat.setDynamicShortcuts(context,shortcuts);
                debugPrint("Shortcuts created");
            } catch (Exception e) {
                Log.e(TAG,e.toString());
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void pushShortcutItem(MethodCall call) {
        final List<Map<String, String>> args = call.arguments();
        if(!this.voiceAssistantVisibility) {
            List<ShortcutInfo> shortcuts;
            try {
                shortcuts = shortcutInfoList(args);
                shortcutManager().addDynamicShortcuts(shortcuts);
                debugPrint("Shortcut pushed");
            } catch (Exception e) {
                Log.e(TAG,e.toString());
            }
        }else {
            List<ShortcutInfoCompat> shortcuts;
            try {
                shortcuts = shortcutInfoCompatList(args);
                ShortcutManagerCompat.addDynamicShortcuts(context,shortcuts);
                debugPrint("Shortcut pushed");
            } catch (Exception e) {
                Log.e(TAG,e.toString());
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void pushShortcutItems(MethodCall call) {
        final List<Map<String, String>> args = call.arguments();
        if(!this.voiceAssistantVisibility) {
            List<ShortcutInfo> shortcuts;
            try {
                shortcuts = shortcutInfoList(args);
                shortcutManager().addDynamicShortcuts(shortcuts);
                debugPrint("Shortcuts pushed");
            } catch (Exception e) {
                Log.e(TAG,e.toString());
            }
        } else {
            List<ShortcutInfoCompat> shortcuts;
            try {
                shortcuts = shortcutInfoCompatList(args);
                ShortcutManagerCompat.addDynamicShortcuts(context,shortcuts);
                debugPrint("Shortcuts pushed");
            } catch (Exception e) {
                Log.e(TAG,e.toString());
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void updateShortcutItems(MethodCall call) {
        List<Map<String, String>> args = call.arguments();
        boolean updated = false;
        if(!this.voiceAssistantVisibility) {
            try {
                List<ShortcutInfo> updateShortcuts = shortcutInfoList(args);
                updated = shortcutManager().updateShortcuts(updateShortcuts);
            } catch(Exception e) {
                Log.e(TAG, e.toString());
            }
            if(updated) {
                debugPrint("Shortcuts updated");
            } else {
                debugPrint("Unable to update shortcuts");
            }
        } else {
            try {
                List<ShortcutInfoCompat> updateShortcuts = shortcutInfoCompatList(args);
                updated = ShortcutManagerCompat.updateShortcuts(context,updateShortcuts);
            } catch(Exception e) {
                Log.e(TAG, e.toString());
            }
            if(updated) {
                debugPrint("Shortcuts updated");
            } else {
                debugPrint("Unable to update shortcuts");
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void updateShortcutItem(MethodCall call) {
        final List<Map<String, String>> args = call.arguments();
        Map<String, String> info = args.get(0);
        final String refId = info.get("id");
        if(!this.voiceAssistantVisibility) {
            List<ShortcutInfo> dynamicShortcuts = shortcutManager().getDynamicShortcuts();
            final List<ShortcutInfo> shortcutList = new ArrayList<>();
            int flag = 1;
            for(ShortcutInfo si : dynamicShortcuts) {
                if(si.getId().equalsIgnoreCase(refId))  {
                    ShortcutInfo shortcutInfo = buildShortcutInfo(info);
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
                shortcutManager().updateShortcuts(shortcutList);
                debugPrint("Shortcut updated");
            } catch(Exception e) {
                Log.e(TAG,e.toString());
            }
        } else {
            List<ShortcutInfoCompat> dynamicShortcuts = ShortcutManagerCompat.getDynamicShortcuts(context);
            final List<ShortcutInfoCompat> shortcutList = new ArrayList<>();
            int flag = 1;
            for(ShortcutInfoCompat si : dynamicShortcuts) {
                if(si.getId().equalsIgnoreCase(refId))  {
                    ShortcutInfoCompat shortcutInfo = buildShortcutUsingCompat(info);
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
                ShortcutManagerCompat.updateShortcuts(context,shortcutList);
                debugPrint("Shortcut updated");
            } catch(Exception e) {
                Log.e(TAG,e.toString());
            }
        }

    }

    private void updateShortLabel(MethodCall call) {
        final List<String> args = call.arguments();
        final String refId = args.get(0);
        final String title = args.get(1);
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void changeShortcutItemIcon(MethodCall call) {
        try {
            final List<String> args = call.arguments();
            final String refId = args.get(0);
            final String changeIcon = args.get(1);
            Map<String,String> items = deserializeShortcutInfoAtId(refId,changeIcon);
            if(!this.voiceAssistantVisibility) {
                ShortcutInfo shortcutInfo = buildShortcutInfo(items);
                List<ShortcutInfo> dynamicShortcuts = shortcutManager().getDynamicShortcuts();
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
                    shortcutManager().updateShortcuts(shortcutList);
                    debugPrint("Shortcut Icon Changed.");
                } catch(Exception e) {
                    Log.e(TAG,e.toString());
                }
            } else {
                ShortcutInfoCompat shortcutInfo = buildShortcutUsingCompat(items);
                List<ShortcutInfoCompat> dynamicShortcuts = ShortcutManagerCompat.getDynamicShortcuts(context);
                final List<ShortcutInfoCompat> shortcutList = new ArrayList<>();
                int flag = 1;
                for(ShortcutInfoCompat si : dynamicShortcuts) {
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
                    ShortcutManagerCompat.updateShortcuts(context,shortcutList);
                    debugPrint("Shortcut Icon Changed.");
                } catch(Exception e) {
                    Log.e(TAG,e.toString());
                }
            }

        } catch(Exception e) {
            Log.e(TAG,e.toString());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private Map<String,String> deserializeShortcutInfoAtId(String id, String icon) {
        HashMap<String, String> map = new HashMap<String, String>();
        List<ShortcutInfo> dynamicShortcuts = shortcutManager().getDynamicShortcuts();

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
    private List<ShortcutInfo> shortcutInfoList(List<Map<String, String>> shortcuts) {
        final List<ShortcutInfo> shortcutList = new ArrayList<>();

        for (Map<String, String> shortcut : shortcuts) {
            ShortcutInfo shortcutInfo = buildShortcutInfo(shortcut);
            shortcutList.add(shortcutInfo);
        }
        return shortcutList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private List<ShortcutInfoCompat> shortcutInfoCompatList(List<Map<String, String>> shortcuts) {
        final List<ShortcutInfoCompat> shortcutList = new ArrayList<>();

        for (Map<String, String> shortcut : shortcuts) {
            ShortcutInfoCompat shortcutInfoCompat = buildShortcutUsingCompat(shortcut);
            shortcutList.add(shortcutInfoCompat);
        }
        return shortcutList;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private ShortcutInfoCompat buildShortcutUsingCompat(Map<String, String> shortcut) {
        final String id = shortcut.get("id");
        final String icon = shortcut.get("icon");
        final String action = shortcut.get("action");
        final String shortLabel = shortcut.get("shortLabel");
        final String longLabel = shortcut.get("LongLabel");
        final int iconType = Integer.parseInt(Objects.requireNonNull(shortcut.get("shortcutIconType")));


        ShortcutInfoCompat.Builder shortcutInfoCompat = new ShortcutInfoCompat.Builder(context, id);

        final Intent intent = getIntentToOpenMainActivity(action);

        if(longLabel != null) {
            shortcutInfoCompat.setLongLabel(longLabel);
        }
        setIconCompat(iconType, icon,shortcutInfoCompat);

        assert shortLabel!=null;
        return shortcutInfoCompat
                .setShortLabel(shortLabel)
                .setIntent(intent)
                .build();
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private ShortcutInfo buildShortcutInfo(Map<String, String> shortcut) {
        final String id = shortcut.get("id");
        final String icon = shortcut.get("icon");
        final String action = shortcut.get("action");
        final String shortLabel = shortcut.get("shortLabel");
        final String longLabel = shortcut.get("LongLabel");
        final int iconType = Integer.parseInt(Objects.requireNonNull(shortcut.get("shortcutIconType")));

        final ShortcutInfo.Builder shortcutBuilder = new ShortcutInfo.Builder(context, id);
        final Intent intent = getIntentToOpenMainActivity(action);

        if(longLabel != null) {
            shortcutBuilder.setLongLabel(longLabel);
        }

        setIcon(iconType, icon,shortcutBuilder);

        assert shortLabel!=null;
        return shortcutBuilder
                .setShortLabel(shortLabel)
                .setIntent(intent)
                .build();
    }

    private void setIcon(int iconType,String icon,ShortcutInfo.Builder shortcutBuilder) {
        // 0 - ShortcutIconType.androidAsset
        // 1 - ShortcutIconType.flutterAsset
        switch (iconType) {
            case 0:
                setIconFromNative(shortcutBuilder, icon);
                break;
            case 1:
                setIconFromFlutter(shortcutBuilder, icon);
                break;
            default:
                break;
        }
    }

    private void setIconCompat(int iconType,String icon,ShortcutInfoCompat.Builder shortcutBuilderCompat) {
        // 0 - ShortcutIconType.androidAsset
        // 1 - ShortcutIconType.flutterAsset
        switch (iconType) {
            case 0:
                setIconFromNativeCompat(shortcutBuilderCompat, icon);
                break;
            case 1:
                setIconFromFlutterCompat(shortcutBuilderCompat, icon);
                break;
            default:
                break;
        }
    }

    private void setIconFromNative(ShortcutInfo.Builder shortcutBuilder, String icon) {
        final int resourceId = loadResourceId(context, icon);
        if (resourceId > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                shortcutBuilder.setIcon(Icon.createWithResource(context, resourceId));
            }
        }
    }

    private void setIconFromNativeCompat(ShortcutInfoCompat.Builder shortcutBuilder, String icon) {
        final int resourceId = loadResourceId(context, icon);
        if (resourceId > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                shortcutBuilder.setIcon(IconCompat.createFromIcon(context,Icon.createWithResource(context, resourceId)));
            }
        }
    }

    private void setIconFromFlutter(ShortcutInfo.Builder shortcutBuilder, String icon) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            shortcutBuilder.setIcon(getIconFromFlutterAsset(context,icon));
        }
    }

    private void setIconFromFlutterCompat(ShortcutInfoCompat.Builder shortcutBuilder, String icon) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            shortcutBuilder.setIcon(IconCompat.createFromIcon(context,getIconFromFlutterAsset(context,icon)));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Icon getIconFromFlutterAsset(Context context, String path) {
        AssetManager assetManager = context.getAssets();
        FlutterLoader loader = FlutterInjector.instance().flutterLoader();
        String key = loader.getLookupKeyForAsset(path);
        AssetFileDescriptor fd = null;
        try {
            fd = assetManager.openFd(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap image = null;
        try {
            assert fd != null;
            image = BitmapFactory.decodeStream(fd.createInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Icon.createWithAdaptiveBitmap(image);
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
