package com.fyp.routeapi;


import android.annotation.SuppressLint;
import android.content.Context;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描    述：本类 为路由工具  所有需要模块之间跳转的类   获取class文件
 * 可以调用{@link RouteUtils#getTargetClass(String)} 获取实例 可以调用{@link RouteUtils#getTargetInstance(String, Object[], Class[])}方法
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
    private Context mContext;
    boolean isDebug;

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

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
        mContext = context.getApplicationContext();
        mPaths = new HashMap<>();
    }

    public void init() {
        ClassUtils utils = new ClassUtils();
        try {
            List<String> path = utils.getPaths(mContext);
            for (String p : path) {
                IRouteLoad load = (IRouteLoad) Class.forName(p).newInstance();
                load.load(mPaths);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param path       路径
     * @param params     参数
     * @param paramsType 参数类型
     * @return
     */
    public Object getTargetInstance(String path, Object[] params, Class[] paramsType) {
        if (params.length != paramsType.length) {
            throw new IllegalArgumentException("type and params must  match");
        }
        try {
            Class targetCls = getTargetClass(path);
            Constructor constructor = targetCls.getConstructor(paramsType);
            return constructor.newInstance(params);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
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
