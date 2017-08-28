package com.example.xposedtest;

import android.app.Application;
import android.content.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

/**
 * 针对5.0.0版本修改
 */
public class HookUtil3 implements IXposedHookLoadPackage {

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

                        final Class<?> BaseResp = XposedHelpers.findClass("com.tencent.mm.opensdk.modelbase.BaseResp", lpparam.classLoader);
//                final Object baseResp =BaseResp.newInstance();

                        XposedHelpers.findAndHookMethod("cn.com.haoluo.www.wxapi.WXPayEntryActivity", lpparam.classLoader, "onResp", BaseResp, new XC_MethodHook() {

                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                XposedBridge.log("param 0 before:" + param.args[0]);

                                if (param.args[0] != null) {
                                    Field publicField = ReflectionUtils.getDeclaredField(param.args[0], "errCode");
                                    XposedBridge.log("----------errCode  name" + publicField.getName());
                                    XposedBridge.log("----------errCode  value" + ReflectionUtils.getFieldValue(param.args[0], "errCode"));
                                    ReflectionUtils.setFieldValue(param.args[0], "errCode", 0);
                                }
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                Class clazz = param.thisObject.getClass();

                            }
                        });

                        final Class<?> e = XposedHelpers.findClass("cn.com.haoluo.www.b.b.e", lpparam.classLoader);
                        final Class<?> a = XposedHelpers.findClass("cn.com.haoluo.www.b.b.e$b", lpparam.classLoader);
                        XposedBridge.log("----------a  " + a);
//                Constructor constructor = a.getConstructor(e);
//                XposedHelpers.callMethod(constructor.newInstance(e),"a");
//                Object object = XposedHelpers.callMethod(a.newInstance(), "a");
//                XposedBridge.log("----------Object  " + object);
                        XposedBridge.hookAllConstructors(a, new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                super.beforeHookedMethod(param);
                                XposedBridge.log("----------param  " + param);
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                super.afterHookedMethod(param);
                                XposedBridge.log("----------param  " + param);
                            }
                        });
                        Constructor<?> constructorExact = XposedHelpers.findConstructorExact("cn.com.haoluo.www.b.b.e$b",
                                lpparam.classLoader, e);
                        XposedBridge.log("----------constructorExact  " + constructorExact);
//                Object o = a.getConstructor(e).newInstance(e.newInstance());
//                XposedBridge.log("----------constructorExact  " + o);
//                Object method = XposedHelpers.callMethod(o, "a");
//                XposedBridge.log("----------method  " + method);
                        XposedHelpers.findAndHookConstructor("cn.com.haoluo.www.b.b.e$b",
                                lpparam.classLoader, e, new XC_MethodHook() {

                                    @Override
                                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                        XposedBridge.log("----------param  " + param);
                                        XposedBridge.log("----------param.args[0]  " + param.args[0]);
                                        Field[] fs = param.args[0].getClass().getDeclaredFields();
                                        final Class<?> Contract = XposedHelpers.findClass("cn.com.haoluo.www.data.model.ShuttleLineBean", lpparam.classLoader);
                                        final Class ShuttleTicketContract = XposedHelpers.findClass("cn.com.haoluo.www.data.model.ShuttleTicket", lpparam.classLoader);
                                        final Object ShuttleLineBean = Contract.newInstance();
                                        final Object ShuttleTicket = ShuttleTicketContract.newInstance();

                                        List<Object> ShuttleTicketList = new ArrayList();


                                        for (int i = 0; i < fs.length; i++) {
                                            Field f = fs[i];
                                            f.setAccessible(true); //设置些属性是可以访问的
                                            Object val = f.get(param.args[0]);//得到此属性的值
                                            if ("g".equals(f.getName())) {
//                                    f.set(contract, contract);
                                                List<Object> list = (List<Object>) val;
                                                //伪造一个val 然后给他赋值 假如到新的list中，然后将新的list赋值给param
                                                Calendar instance = Calendar.getInstance();
                                                ReflectionUtils.setFieldValue(ShuttleTicket, "checkTime", System.currentTimeMillis() / 1000);
                                                ReflectionUtils.setFieldValue(ShuttleTicket, "createTime", 1503898724);
                                                ReflectionUtils.setFieldValue(ShuttleTicket, "cursorId", "1503898724");
                                                SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
                                                ReflectionUtils.setFieldValue(ShuttleTicket, "deptDate", format.format(Calendar.getInstance().getTime()));
                                                int hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                                                if (hourOfDay < 12) {
                                                    ReflectionUtils.setFieldValue(ShuttleTicket, "desc", " 票价1元，仅限乘坐K003");
                                                    ReflectionUtils.setFieldValue(ShuttleTicket, "title", "K003 乘车凭证");
                                                    ReflectionUtils.setFieldValue(ShuttleLineBean, "businessHour", "06:30-09:30");
                                                    ReflectionUtils.setFieldValue(ShuttleLineBean, "lineCode", "K003");
                                                } else {
                                                    ReflectionUtils.setFieldValue(ShuttleTicket, "desc", " 票价1元，仅限乘坐K004");
                                                    ReflectionUtils.setFieldValue(ShuttleTicket, "title", "K004 乘车凭证");
                                                    ReflectionUtils.setFieldValue(ShuttleLineBean, "businessHour", "17:30-21:00");
                                                    ReflectionUtils.setFieldValue(ShuttleLineBean, "lineCode", "K004");
                                                }
                                                ReflectionUtils.setFieldValue(ShuttleTicket, "isUploaded", false);
                                                ReflectionUtils.setFieldValue(ShuttleTicket, "showColorAheadInSeconds", 0);
                                                ReflectionUtils.setFieldValue(ShuttleTicket, "status", 2);
                                                ReflectionUtils.setFieldValue(ShuttleTicket, "tickerColor", "0x30a694");
                                                ReflectionUtils.setFieldValue(ShuttleTicket, "ticketId", "59a3ac73531d4267882b879e");


                                                ReflectionUtils.setFieldValue(ShuttleLineBean, "businessEnd", "19:00");

                                                ReflectionUtils.setFieldValue(ShuttleLineBean, "businessStart", "17:00");
                                                ReflectionUtils.setFieldValue(ShuttleLineBean, "lindId", "58b948e443388a5f5a06a98f");
                                                ReflectionUtils.setFieldValue(ShuttleLineBean, "lineName", "软件园一期-软件园二期-西北旺地铁");
                                                //判断是否是周五来设置价格

                                                int dayOfWeek = instance.get(Calendar.DAY_OF_WEEK);
                                                if (dayOfWeek == 6) {
                                                    ReflectionUtils.setFieldValue(ShuttleLineBean, "price", 1.0f);
                                                } else {
                                                    ReflectionUtils.setFieldValue(ShuttleLineBean, "price", 2.0f);
                                                }
                                                ReflectionUtils.setFieldValue(ShuttleTicket, "shuttleLine", ShuttleLineBean);

                                                ShuttleTicketList.add(ShuttleTicket);
                                                XposedBridge.log("----------val  " + val);
                                                XposedBridge.log("----------ShuttleTicketList  " + ShuttleTicketList);
                                                for (int j = 0; j < list.size(); j++) {
                                                    Object o = list.get(j);
                                                    Field[] ofs = o.getClass().getDeclaredFields();
                                                    for (int k = 0; k < ofs.length; k++) {
                                                        Field field = ofs[k];
                                                        field.setAccessible(true); //设置些属性是可以访问的
                                                        Object newVal = field.get(o);//得到此属性的值
                                                        String type = field.getType().toString();//得到此属性的类型
                                                        XposedBridge.log("----------type  " + type);
                                                        XposedBridge.log("----------name  " + field.getName());
                                                        XposedBridge.log("----------val  " + newVal);
                                                        if ("shuttleLine".equals(field.getName())) {
                                                            Object o2 = newVal;
                                                            Field[] ofs2 = o2.getClass().getDeclaredFields();
                                                            for (int n = 0; n < ofs2.length; n++) {
                                                                Field field2 = ofs2[n];
                                                                field2.setAccessible(true); //设置些属性是可以访问的
                                                                Object newVal2 = field2.get(o2);//得到此属性的值
                                                                String type2 = field2.getType().toString();//得到此属性的类型
                                                                XposedBridge.log("----------shuttleLine type  " + type2);
                                                                XposedBridge.log("----------shuttleLine name  " + field2.getName());
                                                                XposedBridge.log("----------shuttleLine val  " + newVal2);

                                                            }
                                                        }
                                                    }
//                                        ReflectionUtils.setFieldValue(ShuttleLineBean, "lindId", 0);

                                                }
                                            }
                                        }
                                        setNotAccessibleProperty(param.args[0], "g", ShuttleTicketList);
                                    }

                                    @Override
                                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                        Class clazz = param.thisObject.getClass();

                                    }
                                }

                        );
                        final Class<?> TicketDisplayActivity = XposedHelpers.findClass("cn.com.haoluo.www.ui.common.activitys.TicketDisplayActivity", lpparam.classLoader);
                        XposedBridge.hookAllMethods(TicketDisplayActivity, "b", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                super.beforeHookedMethod(param);
                                XposedBridge.log("----------beforeHookedMethod param  " + param);
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                super.afterHookedMethod(param);
                                XposedBridge.log("----------afterHookedMethod param  " + param);
                            }
                        });
                    }
                }

        );
    }

    /**
     * 对给定对象obj的propertyName指定的成员变量进行赋值
     * 赋值为value所指定的值
     * <p>
     * 该方法可以访问私有成员
     */
    public static void setNotAccessibleProperty(Object obj, String propertyName, Object value) throws Exception {
        Class<?> clazz = obj.getClass();
        Field field = clazz.getDeclaredField(propertyName);
        //赋值前将该成员变量的访问权限打开
        field.setAccessible(true);
        field.set(obj, value);
        //赋值后将该成员变量的访问权限关闭
        field.setAccessible(false);
    }

}
