package com.fyp.routecompiler;

import com.fyp.routeannotation.Route;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * 描    述：编译
 * 创 建 人：范要朋
 * 创建日期：2018/12/15 15:43
 * 邮    箱：1094325366@qq.com
 * 修订历史：
 * 修 改 人：
 *
 * @author 范要朋
 */

@AutoService(Processor.class)
public class RouteCompile extends AbstractProcessor {
    private Filer filer;
    private String moduleName;
    private String packageBase = "com.fyp.generate_route.";

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        collect(roundEnvironment);
        return true;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        System.out.println("init ");
        filer = processingEnvironment.getFiler();
        moduleName = processingEnvironment.getOptions().get("route_name");
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> result = new LinkedHashSet<>();
        result.add(Route.class.getCanonicalName());
        return result;
    }

    @Override
    public Set<String> getSupportedOptions() {
        Set<String> options = new HashSet<>();
        options.add("route_name");
        return options;
    }

    private void collect(RoundEnvironment environment) {
        List<AnnotationInfo> infos = new ArrayList<>();
        Set<? extends Element> elements = environment.getElementsAnnotatedWith(Route.class);
        for (Element element : elements) {
            if (element.getKind() == ElementKind.CLASS) {
                TypeElement typeElement = (TypeElement) element;
                AnnotationInfo info = new AnnotationInfo(typeElement.getAnnotation(Route.class).value(), typeElement.getQualifiedName().toString());
                infos.add(info);
            }
        }
        writeJava(infos);
    }

    private void writeJava(List<AnnotationInfo> infos) {
        System.out.println("开始写入  " + moduleName);
        ClassName map = ClassName.get("java.util", "Map");
        ClassName string = ClassName.get("java.lang", "String");
        ClassName cls = ClassName.get("java.lang", "Class");
        ParameterizedTypeName clsParam = ParameterizedTypeName.get(cls, TypeVariableName.get("?"));
        ParameterizedTypeName mapParam = ParameterizedTypeName.get(map, string, clsParam);

        MethodSpec.Builder methodBuild = MethodSpec.methodBuilder("load").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class)
                .addParameter(ParameterSpec.builder(mapParam, "paths").build())
                .returns(void.class);
        for (AnnotationInfo info : infos) {
            System.out.println("name " + info.getName() + "  path" + info.getPath());
            methodBuild.addStatement("paths.put($S," + info.getPath() + ".class)", info.getName());
        }
        TypeSpec AppRoute = TypeSpec.classBuilder("AppRoute")
                .addModifiers(Modifier.PUBLIC).addSuperinterface(ClassName.get("com.fyp.routeapi", "IRouteLoad"))
                .addMethod(methodBuild.build()).build();
        JavaFile file = JavaFile.builder(packageBase + moduleName, AppRoute).build();
        try {
            System.out.println("写入成功 ");
            file.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("写入失败  " + e.getMessage());
        }
    }
}
