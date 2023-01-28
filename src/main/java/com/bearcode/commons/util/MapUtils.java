//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.bearcode.commons.util;

import java.util.Map;

public class MapUtils {
    public MapUtils() {
    }

    private static String getValueString(Map map, String name) {
        if (map.containsKey(name)) {
            Object value = map.get(name);
            if (value == null) {
                return null;
            }

            if (value instanceof String) {
                return (String)value;
            }

            if (value instanceof String[]) {
                String[] array = (String[])((String[])value);
                if (array.length != 1) {
                    throw new AmbiguousParameterException(name);
                }

                if (array[0].equals("")) {
                    return null;
                }

                return array[0];
            }
        }

        return null;
    }

    public static String getString(Map map, String name, String defaultValue) {
        String value = getValueString(map, name);
        return value == null ? defaultValue : value;
    }

    public static boolean getBoolean(Map map, String name, boolean defaultValue) {
        String value = getValueString(map, name);
        if (value == null) {
            return defaultValue;
        } else {
            try {
                return Boolean.valueOf(value);
            } catch (NumberFormatException var5) {
                return defaultValue;
            }
        }
    }

    public static int getInteger(Map map, String name, int defaultValue) {
        String value = getValueString(map, name);
        if (value == null) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException var5) {
                return defaultValue;
            }
        }
    }

    public static long getLong(Map map, String name, long defaultValue) {
        String value = getValueString(map, name);
        if (value == null) {
            return defaultValue;
        } else {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException var6) {
                return defaultValue;
            }
        }
    }

    public static float getFloat(Map map, String name, float defaultValue) {
        String value = getValueString(map, name);
        if (value == null) {
            return defaultValue;
        } else {
            try {
                return Float.parseFloat(value);
            } catch (NumberFormatException var5) {
                return defaultValue;
            }
        }
    }

    public static double getDouble(Map map, String name, double defaultValue) {
        String value = getValueString(map, name);
        if (value == null) {
            return defaultValue;
        } else {
            try {
                return (double)Float.parseFloat(value);
            } catch (NumberFormatException var6) {
                return defaultValue;
            }
        }
    }

    public static short getShort(Map map, String name, short defaultValue) {
        String value = getValueString(map, name);
        if (value == null) {
            return defaultValue;
        } else {
            try {
                return Short.parseShort(value);
            } catch (NumberFormatException var5) {
                return defaultValue;
            }
        }
    }

    public static byte getByte(Map map, String name, byte defaultValue) {
        String value = getValueString(map, name);
        if (value == null) {
            return defaultValue;
        } else {
            try {
                return Byte.parseByte(value);
            } catch (NumberFormatException var5) {
                return defaultValue;
            }
        }
    }

    public static String[] getStrings(Map map, String name, String[] defaultValues) {
        if (map.containsKey(name)) {
            String[] value = (String[])((String[])map.get(name));
            return value != null ? value : defaultValues;
        } else {
            return defaultValues;
        }
    }

    /** @deprecated */
    public static boolean getFirstBoolean(Map map, String name, boolean defaultValue) {
        if (map.containsKey(name)) {
            Object value = map.get(name);
            if (value == null) {
                return defaultValue;
            }

            if (value instanceof String[]) {
                String[] array = (String[])((String[])value);
                if (array.length > 0) {
                    return Boolean.valueOf(array[0]);
                }
            }
        }

        return defaultValue;
    }

    /** @deprecated */
    public static int getFirstInteger(Map map, String name, int defaultValue) {
        if (map.containsKey(name)) {
            Object value = map.get(name);
            if (value == null) {
                return defaultValue;
            }

            if (value instanceof String[]) {
                String[] array = (String[])((String[])value);
                if (array.length > 0) {
                    return Integer.parseInt(array[0]);
                }
            }
        }

        return defaultValue;
    }

    /** @deprecated */
    public static long getFirstLong(Map map, String name, long defaultValue) {
        if (map.containsKey(name)) {
            Object value = map.get(name);
            if (value == null) {
                return defaultValue;
            }

            if (value instanceof String[]) {
                String[] array = (String[])((String[])value);
                if (array.length > 0) {
                    return Long.parseLong(array[0]);
                }
            }
        }

        return defaultValue;
    }

    /** @deprecated */
    public static short getFirstShort(Map map, String name, short defaultValue) {
        if (map.containsKey(name)) {
            Object value = map.get(name);
            if (value == null) {
                return defaultValue;
            }

            if (value instanceof String[]) {
                String[] array = (String[])((String[])value);
                if (array.length > 0) {
                    return Short.parseShort(array[0]);
                }
            }
        }

        return defaultValue;
    }

    /** @deprecated */
    public static byte getFirstByte(Map map, String name, byte defaultValue) {
        if (map.containsKey(name)) {
            Object value = map.get(name);
            if (value == null) {
                return defaultValue;
            }

            if (value instanceof String[]) {
                String[] array = (String[])((String[])value);
                if (array.length > 0) {
                    return Byte.parseByte(array[0]);
                }
            }
        }

        return defaultValue;
    }

    /** @deprecated */
    public static double getFirstDouble(Map map, String name, double defaultValue) {
        if (map.containsKey(name)) {
            Object value = map.get(name);
            if (value == null) {
                return defaultValue;
            }

            if (value instanceof String[]) {
                String[] array = (String[])((String[])value);
                if (array.length > 0) {
                    return Double.parseDouble(array[0]);
                }
            }
        }

        return defaultValue;
    }

    /** @deprecated */
    public static float getFirstFloat(Map map, String name, float defaultValue) {
        if (map.containsKey(name)) {
            Object value = map.get(name);
            if (value == null) {
                return defaultValue;
            }

            if (value instanceof String[]) {
                String[] array = (String[])((String[])value);
                if (array.length > 0) {
                    return Float.parseFloat(array[0]);
                }
            }
        }

        return defaultValue;
    }

    public static boolean[] getBooleans(Map map, String name, boolean[] defaultValues) {
        if (map.containsKey(name)) {
            Object value = map.get(name);
            if (value == null) {
                return defaultValues;
            }

            boolean[] result;
            int i;
            if (value instanceof String[]) {
                String[] array = (String[])((String[])value);
                if (array.length > 0) {
                    result = new boolean[array.length];

                    for(i = 0; i < array.length; ++i) {
                        result[i] = Boolean.valueOf(array[i]);
                    }

                    return result;
                }
            } else if (value instanceof Boolean[]) {
                Boolean[] array = (Boolean[])((Boolean[])value);
                if (array.length > 0) {
                    result = new boolean[array.length];

                    for(i = 0; i < array.length; ++i) {
                        result[i] = array[i];
                    }

                    return result;
                }
            }
        }

        return defaultValues;
    }

    public static long[] getLongs(Map map, String name, long[] defaultValues) {
        if (map.containsKey(name)) {
            Object value = map.get(name);
            if (value == null) {
                return defaultValues;
            }

            long[] result;
            int i;
            if (value instanceof String[]) {
                String[] array = (String[])((String[])value);
                if (array.length > 0) {
                    result = new long[array.length];

                    for(i = 0; i < array.length; ++i) {
                        result[i] = Long.parseLong(array[i]);
                    }

                    return result;
                }
            } else if (value instanceof Long[]) {
                Long[] array = (Long[])((Long[])value);
                if (array.length > 0) {
                    result = new long[array.length];

                    for(i = 0; i < array.length; ++i) {
                        result[i] = array[i];
                    }

                    return result;
                }
            }
        }

        return defaultValues;
    }

    public static short[] getShorts(Map map, String name, short[] defaultValues) {
        if (map.containsKey(name)) {
            Object value = map.get(name);
            if (value == null) {
                return defaultValues;
            }

            short[] result;
            int i;
            if (value instanceof String[]) {
                String[] array = (String[])((String[])value);
                if (array.length > 0) {
                    result = new short[array.length];

                    for(i = 0; i < array.length; ++i) {
                        result[i] = Short.parseShort(array[i]);
                    }

                    return result;
                }
            } else if (value instanceof Short[]) {
                Short[] array = (Short[])((Short[])value);
                if (array.length > 0) {
                    result = new short[array.length];

                    for(i = 0; i < array.length; ++i) {
                        result[i] = array[i];
                    }

                    return result;
                }
            }
        }

        return defaultValues;
    }

    public static byte[] getBytes(Map map, String name, byte[] defaultValues) {
        if (map.containsKey(name)) {
            Object value = map.get(name);
            if (value == null) {
                return defaultValues;
            }

            byte[] result;
            int i;
            if (value instanceof String[]) {
                String[] array = (String[])((String[])value);
                if (array.length > 0) {
                    result = new byte[array.length];

                    for(i = 0; i < array.length; ++i) {
                        result[i] = Byte.parseByte(array[i]);
                    }

                    return result;
                }
            } else if (value instanceof Byte[]) {
                Byte[] array = (Byte[])((Byte[])value);
                if (array.length > 0) {
                    result = new byte[array.length];

                    for(i = 0; i < array.length; ++i) {
                        result[i] = array[i];
                    }

                    return result;
                }
            }
        }

        return defaultValues;
    }

    public static double[] getDoubles(Map map, String name, double[] defaultValues) {
        if (map.containsKey(name)) {
            Object value = map.get(name);
            if (value == null) {
                return defaultValues;
            }

            double[] result;
            int i;
            if (value instanceof String[]) {
                String[] array = (String[])((String[])value);
                if (array.length > 0) {
                    result = new double[array.length];

                    for(i = 0; i < array.length; ++i) {
                        result[i] = Double.parseDouble(array[i]);
                    }

                    return result;
                }
            } else if (value instanceof Double[]) {
                Double[] array = (Double[])((Double[])value);
                if (array.length > 0) {
                    result = new double[array.length];

                    for(i = 0; i < array.length; ++i) {
                        result[i] = array[i];
                    }

                    return result;
                }
            }
        }

        return defaultValues;
    }

    public static float[] getFloats(Map map, String name, float[] defaultValues) {
        if (map.containsKey(name)) {
            Object value = map.get(name);
            if (value == null) {
                return defaultValues;
            }

            float[] result;
            int i;
            if (value instanceof String[]) {
                String[] array = (String[])((String[])value);
                if (array.length > 0) {
                    result = new float[array.length];

                    for(i = 0; i < array.length; ++i) {
                        result[i] = Float.parseFloat(array[i]);
                    }

                    return result;
                }
            } else if (value instanceof Float[]) {
                Float[] array = (Float[])((Float[])value);
                if (array.length > 0) {
                    result = new float[array.length];

                    for(i = 0; i < array.length; ++i) {
                        result[i] = array[i];
                    }

                    return result;
                }
            }
        }

        return defaultValues;
    }

    public static int[] getInts(Map map, String name, int[] defaultValues) {
        if (map.containsKey(name)) {
            Object value = map.get(name);
            if (value == null) {
                return defaultValues;
            }

            int[] result;
            int i;
            if (value instanceof String[]) {
                String[] array = (String[])((String[])value);
                if (array.length > 0) {
                    result = new int[array.length];

                    for(i = 0; i < array.length; ++i) {
                        result[i] = Integer.parseInt(array[i]);
                    }

                    return result;
                }
            } else if (value instanceof Integer[]) {
                Integer[] array = (Integer[])((Integer[])value);
                if (array.length > 0) {
                    result = new int[array.length];

                    for(i = 0; i < array.length; ++i) {
                        result[i] = array[i];
                    }

                    return result;
                }
            }
        }

        return defaultValues;
    }
}
