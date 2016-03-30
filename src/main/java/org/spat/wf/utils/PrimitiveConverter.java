package org.spat.wf.utils;

import java.util.HashMap;
import java.util.Map;

public class PrimitiveConverter {

    private Map<Class<?>, Converter<?>> map = new HashMap<Class<?>, Converter<?>>();

    public PrimitiveConverter() {
        init();
    }

    void init() {

        Converter<?> c = new StringConverter();
        map.put(String.class, c);

        c = new BooleanConverter();
        map.put(boolean.class, c);
        map.put(Boolean.class, c);

        c = new CharacterConverter();
        map.put(char.class, c);
        map.put(Character.class, c);

        c = new ByteConverter();
        map.put(byte.class, c);
        map.put(Byte.class, c);

        c = new ShortConverter();
        map.put(short.class, c);
        map.put(Short.class, c);

        c = new IntegerConverter();
        map.put(int.class, c);
        map.put(Integer.class, c);

        c = new LongConverter();
        map.put(long.class, c);
        map.put(Long.class, c);

        c = new FloatConverter();
        map.put(float.class, c);
        map.put(Float.class, c);

        c = new DoubleConverter();
        map.put(double.class, c);
        map.put(Double.class, c);
    }


    public boolean canConvert(Class<?> clazz) {
        return clazz.equals(String.class) || map.containsKey(clazz);
    }

    public Object convert(Class<?> clazz, String s) {
        Converter<?> c = map.get(clazz);
        return c.convert(s);
    }
}


interface Converter<T> {

    /**
     * Convert a not-null String to specified object.
     */
    T convert(String s);

}

/**
 * Convert String to Boolean.
 * 
 */
class BooleanConverter implements Converter<Boolean> {

    public Boolean convert(String s) {
        return Boolean.parseBoolean(s);
    }
}

/**
 * Convert String to Byte.
 * 
 */
class ByteConverter implements Converter<Byte> {

    public Byte convert(String s) {
        return Byte.parseByte(s);
    }
}

/**
 * Convert String to Character.
 * 
 */
class CharacterConverter implements Converter<Character> {

    public Character convert(String s) {
        if (s.length()==0)
            throw new IllegalArgumentException("Cannot convert empty string to char.");
        return s.charAt(0);
    }
}

/**
 * Convert String to Double.
 * 
 */
class DoubleConverter implements Converter<Double> {

    public Double convert(String s) {
        return Double.parseDouble(s);
    }
}

/**
 * Convert String to Float.
 * 
 */
class FloatConverter implements Converter<Float> {

    public Float convert(String s) {
        return Float.parseFloat(s);
    }
}

/**
 * Convert String to Integer.
 * 
 */
class IntegerConverter implements Converter<Integer> {

    public Integer convert(String s) {
        return Integer.parseInt(s);
    }
}

/**
 * Convert String to Long.
 * 
 */
class LongConverter implements Converter<Long> {

    public Long convert(String s) {
        return Long.parseLong(s);
    }
}

/**
 * Convert String to Short.
 * 
 */
class ShortConverter implements Converter<Short> {

    public Short convert(String s) {
        return Short.parseShort(s);
    }
}

/**
 * Convert String to Integer.
 * 
 */
class StringConverter implements Converter<String> {

    public String convert(String s) {
        return s;
    }
}
