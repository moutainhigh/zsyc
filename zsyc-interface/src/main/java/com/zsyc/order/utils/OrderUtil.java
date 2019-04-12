package com.zsyc.order.utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: zsyc-parent
 * @description: 订单工具类
 * @author: Mr.Ning
 * @create: 2019-02-26 10:23
 **/

public class OrderUtil {

    /**
     * 生成订单号
     * @return
     */
    public static String getOrderNo(){
        int machineId = 1;//最大支持1-9个集群机器部署
        int r1=(int)(Math.random()*(10));//产生2个0-9的随机数
        int r2=(int)(Math.random()*(10));
        long now = System.currentTimeMillis();//一个13位的时间戳
        return machineId + String.valueOf(r1) + String.valueOf(r2) + String.valueOf(now);// 订单ID
    }


    /**
     * 枚举转map结合value作为map的key,description作为map的value
     * @param enumT
     * @return enum mapcolloction
     */
    public static <T> Map<Object, String> EnumToMap(Class<T> enumT,
                                                    String... methodNames) {
        Map<Object, String> enummap = new HashMap<Object, String>();
        if (!enumT.isEnum()) {
            return enummap;
        }
        T[] enums = enumT.getEnumConstants();
        if (enums == null || enums.length <= 0) {
            return enummap;
        }
        int count = methodNames.length;
        String valueMathod = "val"; //更改为自己的获取value的方法名
        String desMathod = "desc";//更改为自己的获取des的方法名
        if (count >= 1 && !"".equals(methodNames[0])) { //扩展方法
            valueMathod = methodNames[0];
        }
        if (count == 2 && !"".equals(methodNames[1])) {
            desMathod = methodNames[1];
        }
        for (int i = 0, len = enums.length; i < len; i++) {
            T tobj = enums[i];
            try {
                Object resultValue = getMethodValue(valueMathod, tobj); //获取value值
                if ("".equals(resultValue)) {
                    continue;
                }
                Object resultDes = getMethodValue(desMathod, tobj); //获取description描述值
                if ("".equals(resultDes)) { //如果描述不存在获取属性值
                    resultDes = tobj;
                }
                enummap.put(resultValue, resultDes + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return enummap;
    }


    /**
     * 根据反射，通过方法名称获取方法值，忽略大小写的
     * @param methodName
     * @param obj
     * @param args
     * @return return value
     */
    private static <T> Object getMethodValue(String methodName, T obj,
                                             Object... args) {
        Object resut = "";
        // boolean isHas = false;
        try {
            /********************************* start *****************************************/
            Method[] methods = obj.getClass().getMethods(); //获取方法数组，这里只要共有的方法
            if (methods.length <= 0) {
                return resut;
            }
            // String methodstr=Arrays.toString(obj.getClass().getMethods());
            // if(methodstr.indexOf(methodName)<0){ //只能粗略判断如果同时存在 getValue和getValues可能判断错误
            // return resut;
            // }
            // List<Method> methodNamelist=Arrays.asList(methods); //这样似乎还要循环取出名称
            Method method = null;
            for (int i = 0, len = methods.length; i < len; i++) {
                if (methods[i].getName().equalsIgnoreCase(methodName)) { //忽略大小写取方法
                    // isHas = true;
                    methodName = methods[i].getName(); //如果存在，则取出正确的方法名称
                    method = methods[i];
                    break;
                }
            }
            // if(!isHas){
            // return resut;
            // }
            /*************************** end ***********************************************/
            // Method method = obj.getClass().getDeclaredMethod(methodName); ///确定方法
            if (method == null) {
                return resut;
            }
            resut = method.invoke(obj, args); //方法执行
            if (resut == null) {
                resut = "";
            }
            return resut; //返回结果
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resut;
    }
}
