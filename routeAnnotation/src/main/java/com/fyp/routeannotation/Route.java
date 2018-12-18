package com.fyp.routeannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
  * 描    述：
  * 创 建 人：范要朋
  * 创建日期：2018/12/15 15:43
  * 邮    箱：1094325366@qq.com
  * 修订历史：
  * 修 改 人：
  * @author 范要朋
  */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Route {
    String value();
}
