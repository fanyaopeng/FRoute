package com.fyp.routecompiler;

/**
 * 描    述：存储被注解的信息
 * 创 建 人：范要朋
 * 创建日期：2018/12/15 15:43
 * 邮    箱：1094325366@qq.com
 * 修订历史：
 * 修 改 人：
 *
 * @author 范要朋
 */

public class AnnotationInfo {
    private String name;
    private String path;

    public AnnotationInfo(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
