package com.leandog.gametel.driver.commands;

import java.lang.reflect.Method;
import java.util.*;

import com.leandog.gametel.driver.TestRunInformation;

public class CommandRunner {

    private Object theLastResult;

    public void execute(final Command command) {
        try {
            final Method method = findMethodFor(command);
            theLastResult = method.invoke(theTarget(), command.getArguments());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object theLastResult() {
        return theLastResult;
    }

    private Object theTarget() {
        return (theLastResult == null) ? TestRunInformation.getSolo() : theLastResult;
    }

    private Method findMethodFor(final Command command) throws NoSuchMethodException {
        return theTarget().getClass().getMethod(command.getName(), argumentTypesFor(command));
    }

    private static Map<Class<?>, Class<?>> primitiveMap = new HashMap<Class<?>, Class<?>>();
    static {
        primitiveMap.put(Integer.class, int.class);
        primitiveMap.put(Boolean.class, boolean.class);
        primitiveMap.put(Float.class, float.class);
    }

    private Class<? extends Object> typeFor(final Object argument) {
        Class<?> argumentType = argument.getClass();
        Class<?> primitiveType = primitiveMap.get(argumentType);
        return (primitiveType == null) ? argumentType : primitiveType;
    }

    private Class<?>[] argumentTypesFor(final Command command) {
        Class<?>[] types = new Class<?>[command.getArguments().length];

        int index = 0;
        for (final Object argument : command.getArguments()) {
            types[index++] = typeFor(argument);
        }

        return types;
    }

}
