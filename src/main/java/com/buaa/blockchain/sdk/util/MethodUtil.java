package com.buaa.blockchain.sdk.util;

import java.lang.reflect.Method;

/**
 * xxxx
 *
 * @author <a href="http://github.com/hackdapp">hackdapp</a>
 * @date 2020/12/23
 * @since JDK1.8
 */
public class MethodUtil {

    public static Method getMethod(Class<?> controller, String methodName, int paramLen) throws NoSuchMethodException {
        Method[] methods = controller.getDeclaredMethods();
        for(Method method : methods){
            if(method.getName().equals(methodName) && method.getParameters().length == paramLen){
                return method;
            }
        }
        throw new NoSuchMethodException();
    }
}
