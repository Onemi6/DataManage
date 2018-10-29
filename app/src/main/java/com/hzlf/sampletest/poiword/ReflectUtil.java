package com.hzlf.sampletest.poiword;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射的工具类
 *
 * @author wumi
 * @version $Id: ReflectUtil.java, v 0.1 Jul 5, 2013 11:33:24 AM wumi Exp $
 */
public class ReflectUtil {

    /**
     * 得到某个对象的公共属性
     *
     * @param owner , fieldName
     * @return 该属性对象
     * @throws Exception
     */
    public static Object getProperty(Object owner, String fieldName)
            throws Exception {
        Class<?> ownerClass = owner.getClass();
        Field field = ownerClass.getField(fieldName);
        Object property = field.get(owner);
        return property;
    }

    /**
     * 得到某类的静态公共属性
     *
     * @param className 类名
     * @param fieldName 属性名
     * @return 该属性对象
     * @throws Exception
     */
    public static Object getStaticProperty(String className, String fieldName)
            throws Exception {
        Class<?> ownerClass = Class.forName(className);
        Field field = ownerClass.getField(fieldName);
        Object property = field.get(ownerClass);
        return property;
    }

    /**
     * 执行某对象方法
     *
     * @param owner      对象
     * @param methodName 方法名
     * @param args       参数
     * @return 方法返回值
     * @throws Exception
     */
    public static Object invokeMethod(Object owner, String methodName,
                                      Object[] args) throws Exception {
        Class<?> ownerClass = owner.getClass();
        Class<?>[] argsClass = new Class[args.length];
        for (int i = 0, j = args.length; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }
        Method method = ownerClass.getMethod(methodName, argsClass);
        return method.invoke(owner, args);
    }

    /**
     * 执行某类的静态方法
     *
     * @param className  类名
     * @param methodName 方法名
     * @param args       参数数组
     * @return 执行方法返回的结果
     * @throws Exception
     */
    public static Object invokeStaticMethod(String className,
                                            String methodName, Object[] args) throws Exception {
        Class<?> ownerClass = Class.forName(className);
        Class<?>[] argsClass = new Class[args.length];
        for (int i = 0, j = args.length; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }
        Method method = ownerClass.getMethod(methodName, argsClass);
        return method.invoke(null, args);
    }

    /**
     * 执行某类的静态方法
     *
     * @param className  类名
     * @param methodName 方法名
     * @param args       参数数组
     * @return 执行方法返回的结果
     * @throws Exception
     */
    public static Object invokeStaticMethod(String className,
                                            String methodName, Class<?>[] argsClass, Object[] args)
            throws Exception {
        Class<?> ownerClass = Class.forName(className);
        Method method = ownerClass.getMethod(methodName, argsClass);
        return method.invoke(null, args);
    }

    /**
     * 新建实例
     *
     * @param className 类名
     * @param args      构造函数的参数
     * @return 新建的实例
     * @throws Exception
     */
    public static Object newInstance(String className, Object[] args)
            throws Exception {
        Class<?> newoneClass = Class.forName(className);
        Class<?>[] argsClass = new Class[args.length];
        for (int i = 0, j = args.length; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }
        Constructor<?> cons = newoneClass.getConstructor(argsClass);
        return cons.newInstance(args);

    }

    /**
     * 是不是某个类的实例
     *
     * @param obj 实例
     * @param cls 类
     * @return 如果 obj 是此类的实例，则返回 true
     */
    public static boolean isInstance(Object obj, Class<?> cls) {
        return cls.isInstance(obj);
    }

    /**
     * 得到数组中的某个元素
     *
     * @param array 数组
     * @param index 索引
     * @return 返回指定数组对象中索引组件的值
     */
    public static Object getByArray(Object array, int index) {
        return Array.get(array, index);
    }

    /**
     * 根据类的实例和要读取的层次得到值 例如 给的第二个参数是 dept.name 就会读出传来的实例的属性dept的属性name
     *
     * @param owner
     * @param fieldPath
     * @return
     * @throws Exception
     */
    public static Object getValue(Object owner, String fieldPath)
            throws Exception {

        Object temp = owner;
        String[] fieldArray = fieldPath.split("\\.");

        for (String field : fieldArray) {
            temp = invokeMethod(temp, getGetFunction(field), new Object[]{});

        }

        return temp;
    }

    public static String getGetFunction(String field) {
        return "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
    }

    public static String getSetFunction(String field) {
        return "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
    }
}