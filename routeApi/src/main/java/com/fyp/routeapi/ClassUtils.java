package com.fyp.routeapi;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import dalvik.system.DexFile;

/**
 * 获得分包
 */
public class ClassUtils {
    private String basePath = "com.fyp.generate_route";
    private String TAG = "ClassUtils";

    public List<String> getPaths(Context context) throws Exception {
        final List<String> result = new ArrayList<>();
        long start = System.currentTimeMillis();
        List<String> apk = getSourcePath(context);
        if (BuildConfig.DEBUG) {
            apk.addAll(getDebugSourcePath(context));
        }
        final CountDownLatch countDownLatch = new CountDownLatch(apk.size());
        for (final String path : apk) {
            //对于每一个解析 都放入任务
            DefaultThreadExecutor.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    DexFile dexFile = null;
                    try {
                        dexFile = new DexFile(path);
                        Log.e(TAG, "获得分包  " + path);
                        Enumeration<String> enumeration = dexFile.entries();
                        while (enumeration.hasMoreElements()) {
                            String element = enumeration.nextElement();
                            if (element.startsWith(basePath)) {
                                result.add(element);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (dexFile != null) {
                            try {
                                dexFile.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        countDownLatch.countDown();
                    }
                }
            });
        }
        countDownLatch.await();
        Log.e(TAG, "result " + result);
        Log.e(TAG, "耗时 " + (System.currentTimeMillis() - start));
        return result;
    }

    private List<String> getSourcePath(Context context) throws PackageManager.NameNotFoundException {
        List<String> paths = new ArrayList<>();
        ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
        paths.add(info.sourceDir);
        return paths;
    }

    private List<String> getDebugSourcePath(Context context) throws PackageManager.NameNotFoundException {
        List<String> instantRunSourcePaths = new ArrayList<>();
        ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && info.splitSourceDirs != null) {
            //这里主要是instant run的问题
            instantRunSourcePaths.addAll(Arrays.asList(info.splitSourceDirs));
        }
        return instantRunSourcePaths;
    }
}
