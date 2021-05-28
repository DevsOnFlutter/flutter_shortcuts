package com.divyanshushekhar.flutter_shortcuts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
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
            case "changeShortcutItemShortLabel":
                changeShortcutItemShortLabel(call);
                break;
            case "changeShortcutItemIcon":
                changeShortcutItemIcon(call);
                break;
            case "clearShortcutItems":
                ShortcutManagerCompat.removeAllDynamicShortcuts(context);
                debugPrint("Removed all shortcuts.");
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    private void initialize(MethodCall call) {
        List<Map<String, String>> args = call.arguments();
        this.debug = Boolean.parseBoolean(args.get(0).get("debug"));
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

        if (launchAction != null && !launchAction.isEmpty()) {
            ShortcutManagerCompat.reportShortcutUsed(context,launchAction);
            intent.removeExtra(EXTRA_ACTION);
        }

        result.success(launchAction);
        debugPrint("Launch Action: " + launchAction);
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private int getMaxShortcutLimit() {
        return ShortcutManagerCompat.getMaxShortcutCountPerActivity(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private Map<String, Integer> getIconProperties() {
        Map<String, Integer> properties = new HashMap<String, Integer>();
        properties.put("maxHeight", ShortcutManagerCompat.getIconMaxHeight(context));
        properties.put("maxWidth", ShortcutManagerCompat.getIconMaxWidth(context));
        return properties;
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void setShortcutItems(MethodCall call) {
        List<Map<String, String>> args = call.arguments();
        List<ShortcutInfoCompat> shortcuts;
        try {
            shortcuts = shortcutInfoCompatList(args);
            ShortcutManagerCompat.setDynamicShortcuts(context,shortcuts);
            debugPrint("Shortcuts created");
        } catch (Exception e) {
            Log.e(TAG,e.toString());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void pushShortcutItem(MethodCall call) {
        final List<Map<String, String>> args = call.arguments();
        List<ShortcutInfoCompat> shortcuts;
        try {
            shortcuts = shortcutInfoCompatList(args);
            ShortcutManagerCompat.addDynamicShortcuts(context,shortcuts);
            debugPrint("Shortcut pushed");
        } catch (Exception e) {
            Log.e(TAG,e.toString());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void pushShortcutItems(MethodCall call) {
        final List<Map<String, String>> args = call.arguments();
        List<ShortcutInfoCompat> shortcuts;
        try {
            shortcuts = shortcutInfoCompatList(args);
            ShortcutManagerCompat.addDynamicShortcuts(context,shortcuts);
            debugPrint("Shortcuts pushed");
        } catch (Exception e) {
            Log.e(TAG,e.toString());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void updateShortcutItems(MethodCall call) {
        List<Map<String, String>> args = call.arguments();
        boolean updated = false;
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

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void updateShortcutItem(MethodCall call) {
        final List<Map<String, String>> args = call.arguments();
        Map<String, String> info = args.get(0);
        final String id = info.get("id");
        final String icon = info.get("icon");
        final String action = info.get("action");
        final String shortLabel = info.get("shortLabel");
        final String longLabel = info.get("LongLabel");
        final int iconType = Integer.parseInt(Objects.requireNonNull(info.get("shortcutIconType")));

        List<ShortcutInfoCompat> dynamicShortcuts = ShortcutManagerCompat.getDynamicShortcuts(context);
        final List<ShortcutInfoCompat> shortcutList = new ArrayList<>();
        int flag = 1;
        for(ShortcutInfoCompat si : dynamicShortcuts) {
            if(si.getId().equals(id))  {
                ShortcutInfoCompat.Builder shortcutInfo = buildShortcutUsingCompat(id,icon,action,shortLabel,longLabel,iconType);
                shortcutList.add(shortcutInfo.build());
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("RestrictedApi")
    private void changeShortcutItemShortLabel(MethodCall call) {
        final List<String> args = call.arguments();
        final String refId = args.get(0);
        final String shortLabel = args.get(1);

        List<ShortcutInfoCompat> dynamicShortcuts = ShortcutManagerCompat.getDynamicShortcuts(context);
        final List<ShortcutInfoCompat> shortcutList = new ArrayList<>();

        int flag = 1;
        for(ShortcutInfoCompat si : dynamicShortcuts) {
            String id = si.getId();
            if(id.equals(refId))  {
                String longLabel = (String) si.getLongLabel();
                ShortcutInfoCompat.Builder shortcutInfoCompat = buildShortcutUsingCompat(id,null,null,shortLabel,longLabel,0);
                shortcutInfoCompat.setIcon(si.getIcon()).setIntent(si.getIntent());
                shortcutList.add(shortcutInfoCompat.build());
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
            debugPrint("Shortcut Short Label Changed.");
        } catch(Exception e) {
            Log.e(TAG,e.toString());
        }
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void changeShortcutItemIcon(MethodCall call) {
        try {
            final List<String> args = call.arguments();
            final String refId = args.get(0);
            final String changeIcon = args.get(1);
            Icon icon = getIconFromFlutterAsset(context,changeIcon);
            IconCompat iconCompat = IconCompat.createFromIcon(context,icon);
            List<ShortcutInfoCompat> dynamicShortcuts = ShortcutManagerCompat.getDynamicShortcuts(context);

            final List<ShortcutInfoCompat> shortcutList = new ArrayList<>();
            int flag = 1;
            for(ShortcutInfoCompat si : dynamicShortcuts) {
                String id = si.getId();
                if(id.equals(refId))  {
                    String shortLabel = (String) si.getShortLabel();
                    String longLabel = (String) si.getLongLabel();

                    ShortcutInfoCompat.Builder shortcutInfo = buildShortcutUsingCompat(id,null,null,shortLabel,longLabel,0);
                    shortcutInfo.setIcon(iconCompat).setIntent(si.getIntent());
                    shortcutList.add(shortcutInfo.build());
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
        } catch(Exception e) {
            Log.e(TAG,e.toString());
        }
    }


    /* ********************   Utility Functions   ********************* */

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private List<ShortcutInfoCompat> shortcutInfoCompatList(List<Map<String, String>> shortcuts) {
        final List<ShortcutInfoCompat> shortcutList = new ArrayList<>();

        for (Map<String, String> shortcut : shortcuts) {
            final String id = shortcut.get("id");
            final String icon = shortcut.get("icon");
            final String action = shortcut.get("action");
            final String shortLabel = shortcut.get("shortLabel");
            final String longLabel = shortcut.get("LongLabel");
            final int iconType = Integer.parseInt(Objects.requireNonNull(shortcut.get("shortcutIconType")));
            ShortcutInfoCompat.Builder shortcutInfoCompat = buildShortcutUsingCompat(id,icon,action,shortLabel,longLabel,iconType);
            shortcutList.add(shortcutInfoCompat.build());
        }
        return shortcutList;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private ShortcutInfoCompat.Builder buildShortcutUsingCompat(String id, String icon, String action, String shortLabel, String longLabel, int iconType) {

        assert id != null;
        ShortcutInfoCompat.Builder shortcutInfoCompat = new ShortcutInfoCompat.Builder(context, id);

        Intent intent = null;
        if(action != null) {
            intent = getIntentToOpenMainActivity(action);
            shortcutInfoCompat.setIntent(intent);
        }

        if(longLabel != null) {
            shortcutInfoCompat.setLongLabel(longLabel);
        }

        if(icon != null) {
            setIconCompat(iconType, icon, shortcutInfoCompat);
        }

        if(shortLabel != null) {
            shortcutInfoCompat.setShortLabel(shortLabel);
        }

        return shortcutInfoCompat;
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

    private void setIconFromNativeCompat(ShortcutInfoCompat.Builder shortcutBuilder, String icon) {
        final int resourceId = loadResourceId(context, icon);
        if (resourceId > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                shortcutBuilder.setIcon(IconCompat.createFromIcon(context,Icon.createWithResource(context, resourceId)));
            }
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
