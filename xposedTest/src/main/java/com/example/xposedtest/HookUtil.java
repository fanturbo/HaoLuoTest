package com.example.xposedtest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class HookUtil implements IXposedHookLoadPackage{

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        // 标记目标app包名
        if (!lpparam.packageName.equals("cn.com.haoluo.www"))
            return;
        XposedBridge.log("Loaded app: " + lpparam.packageName);

        // Hook MainActivity中的isCorrectInfo(String,String)方法
        // findAndHookMethod(hook方法的类名，classLoader，hook方法名，hook方法参数...，XC_MethodHook)
//        XposedHelpers.findAndHookMethod("com.example.logintest.MainActivity", lpparam.classLoader, "isCorrectInfo", String.class,
//                String.class, new XC_MethodHook() {
//
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                        XposedBridge.log("开始hook");
//                        XposedBridge.log("参数1 = " + param.args[0]);
//                        XposedBridge.log("参数2 = " + param.args[1]);
//                    }
//
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        XposedBridge.log("结束hook");
//                        XposedBridge.log("参数1 = " + param.args[0]);
//                        XposedBridge.log("参数2 = " + param.args[1]);
//
//                    }
//                });

        // Hook MainActivity中的onClick(View v)方法 cn.com.haoluo.www.fragment.ShuttleBusListFragment$ViewHolder

        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("success!!");
                Class<?> ShuttleLine = XposedHelpers.findClass("cn.com.haoluo.www.model.ShuttleLine", lpparam.classLoader);
                final Object shuttleLine =ShuttleLine.newInstance();

                final Class<?> Contract = XposedHelpers.findClass("cn.com.haoluo.www.model.ShuttleLine.Contract", lpparam.classLoader);
                final Object contract =Contract.newInstance();

                XposedHelpers.findAndHookMethod("cn.com.haoluo.www.fragment.ShuttleMainFragment$1", lpparam.classLoader, "onAction", int.class, ShuttleLine, new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Class clazz = param.thisObject.getClass();
                        XposedBridge.log("param 0 before:"+ param.args[0]);
                        param.args[0] = 4;
                        //给ShuttleLine参数设置Contract
//                         param.args[1] = contract;
                        XposedBridge.log("param 0 after:"+ param.args[0]);
                        XposedBridge.log("param 1:"+ param.args[1]);

                        if(param.args[1]!=null){
                            Field[] fs = param.args[1].getClass().getDeclaredFields();
                            for(int i = 0 ; i < fs.length; i++){
                                Field f = fs[i];
                                f.setAccessible(true); //设置些属性是可以访问的
                                Object val = f.get(param.args[1]);//得到此属性的值
                                String type = f.getType().toString();//得到此属性的类型
                                XposedBridge.log("----------name  "+f.getName());
                                XposedBridge.log("----------type  "+type);
                                XposedBridge.log("----------val  "+val);
//                                Log.i("99999",contract+"");
                                if(Contract.equals(f.getType())){
//                                    f.set(contract, contract);
//                                    setNotAccessibleProperty(param.args[1], "contract", contract);
                                }
                            }
                        }else{
                            param.args[1] = shuttleLine;
                            Field[] fs = param.args[1].getClass().getDeclaredFields();
                            for(int i = 0 ; i < fs.length; i++){
                                Field f = fs[i];
                                f.setAccessible(true); //设置些属性是可以访问的
                                Object val = f.get(param.args[1]);//得到此属性的值
                                String type = f.getType().toString();//得到此属性的类型
                                XposedBridge.log("----------name  "+f.getName());
                                XposedBridge.log("----------type  "+type);
                                XposedBridge.log("----------val  "+val);
//                                Log.i("99999",contract+"");
                                if(Contract.equals(f.getType())){
//                                    f.set(contract, contract);
                                    setNotAccessibleProperty(param.args[1], "contract", contract);
                                    XposedBridge.log("----------Contract name  "+f.getName());
                                    XposedBridge.log("----------Contract type  "+type);
                                    XposedBridge.log("----------Contract val  "+val);
                                }
                            }
                        }
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Class clazz = param.thisObject.getClass();

                    }
                });
                Class<?> ShuttleTicket = XposedHelpers.findClass("cn.com.haoluo.www.model.ShuttleTicket", lpparam.classLoader);
                Class<?> DisplayShuttleTicket = XposedHelpers.findClass("cn.com.haoluo.www.features.DisplayShuttleTicket", lpparam.classLoader);
                XposedHelpers.findAndHookMethod(DisplayShuttleTicket, "display", ShuttleTicket, new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Class clazz = param.thisObject.getClass();
                        XposedBridge.log("display ShuttleTicket:"+ param.args[0]);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Class clazz = param.thisObject.getClass();

                    }
                });



                //注意，这儿generateShuttleTicket 是静态方法
                Method m = XposedHelpers.findMethodExact(ShuttleLine, "generateShuttleTicket", ShuttleLine);
                XposedBridge.hookMethod(m, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("generateShuttleTicket method:"+ param.args[0]);
                        if(param.args[0]!=null){
                            Field[] fs = param.args[0].getClass().getDeclaredFields();
                            for(int i = 0 ; i < fs.length; i++){
                                Field f = fs[i];
                                f.setAccessible(true); //设置些属性是可以访问的
                                Object val = f.get(param.args[0]);//得到此属性的值
                                String type = f.getType().toString();//得到此属性的类型
                            }
                        }
                        super.beforeHookedMethod(param);
                    }
                });
//            	 XposedHelpers.findAndHookMethod(DisplayShuttleTicket, "generateShuttleTicket", ShuttleLine, new XC_MethodHook() {
//
//                     @Override
//                     protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                    	 Class clazz = param.thisObject.getClass();
//                         XposedBridge.log("display shuttleLine:"+ param.args[0]);
//                         //给ShuttleLine参数设置Contract
////                         param.args[0] = contract;
//                     }
//
//                     @Override
//                     protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                         Class clazz = param.thisObject.getClass();
//                         
//                     }
//                 });
            }

        });


    }
    /**
     * 对给定对象obj的propertyName指定的成员变量进行赋值
     * 赋值为value所指定的值
     *
     * 该方法可以访问私有成员
     */
    public static void setNotAccessibleProperty(Object obj, String propertyName, Object value) throws Exception
    {
        Class<?> clazz = obj.getClass();
        Field field = clazz.getDeclaredField(propertyName);
        //赋值前将该成员变量的访问权限打开
        field.setAccessible(true);
        field.set(obj, value);
        //赋值后将该成员变量的访问权限关闭
        field.setAccessible(false);
    }

}
