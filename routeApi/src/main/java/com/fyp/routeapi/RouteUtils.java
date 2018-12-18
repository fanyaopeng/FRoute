package com.fyp.routeapi;


import android.annotation.SuppressLint;
import android.content.Context;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描    述：本类 为路由工具  所有需要模块之间跳转的类   获取class文件
 * 可以调用{@link RouteUtils#getTargetClass(String)} 获取实例 可以调用{@link RouteUtils#getTargetInstance(String)}方法
 * 创 建 人：范要朋
 * 创建日期：2018/10/24 20:53
 * 邮    箱：1094325366@qq.com
 * 修订历史：
 * 修 改 人：
 *
 * @author huisoucw
 */
@SuppressLint("StaticFieldLeak")
public class RouteUtils {
    private Map<String, Class<?>> mPaths;
    private static RouteUtils sInstance;
    private static Context sContext;

    public static RouteUtils getInstance(Context context) {
        if (sInstance == null) {
            synchronized (RouteUtils.class) {
                if (sInstance == null) {
                    sInstance = new RouteUtils(context);
                }
            }
        }
        return sInstance;
    }

    private RouteUtils(Context context) {
        sContext = context.getApplicationContext();
        mPaths = new HashMap<>();
    }

    public void init() {
        ClassUtils utils = new ClassUtils();
        try {
            List<String> path = utils.getPaths(sContext);
            for (String p : path) {
                IRouteLoad load = (IRouteLoad) Class.forName(p).newInstance();
                load.load(mPaths);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getTargetInstance(String path) {
        try {
            return getTargetClass(path).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Class getTargetClass(String path) {
        for (Map.Entry<String, Class<?>> entry : mPaths.entrySet()) {
            if (entry.getKey().equals(path)) {
                return entry.getValue();//找到
            }
        }
        return null;
    }
}
