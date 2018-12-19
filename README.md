# FRoute
### 一个简单的组件跳转框架 该框架返回组件的class 文件或者其实例  以注解的形式 标注你可能被跳转的组件 
### 具体使用 
### 1.在公共基础组件中 声明
  `<api 'com.github.fanyaopeng.FRoute:routeAnnotation:1.0.2'  api 'com.github.fanyaopeng.FRoute:routeApi:1.0.2'>`
### 2.在各个组件中添加
   `<annotationProcessor 'com.github.fanyaopeng.FRoute:routeCompiler:1.0.2'  javaCompileOptions {
            annotationProcessorOptions {
                arguments=["route_name":project.getName()]
            }
        }`>
### 3.在application中
 `<RouteUtils routeUtils = RouteUtils.getInstance(getApplicationContext());
  routeUtils.setDebug(BuildConfig.DEBUG);
  routeUtils.init()>`
### 4. 跳转
    声明 `<@Route("test")>`
    跳转 `<startActivity(new Intent(this,RouteUtils.getInstance(context).getTargetClass("test"));>`
    
      
  
 
