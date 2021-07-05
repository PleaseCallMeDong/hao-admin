package com.example.common.utils;

import cn.hutool.core.util.StrUtil;

/**
 * @author: dj
 * @create: 2020-11-06 11:25
 * @description:
 */
public class MyStrUtil {

    public static final String EMPTY = "";

    public static boolean isBlank(CharSequence str) {

        if ((str == null) || (str.length() == 0)) {
            return true;
        }

        return "".contentEquals(str);
    }

    /**
     * 字符串是否为空，空的定义如下:<br>
     * 1、为null <br>
     * 2、为""<br>
     *
     * @param str 被检测的字符串
     * @return 是否为空
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * 字符串是否为非空白 空白的定义如下： <br>
     * 1、不为null <br>
     * 2、不为不可见字符（如空格）<br>
     * 3、不为""<br>
     *
     * @param str 被检测的字符串
     * @return 是否为非空
     */
    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }

    /**
     * 补充字符串以满足最小长度
     *
     * <pre>
     * StrUtil.padPre(null, *, *);//null
     * StrUtil.padPre("1", 3, "ABC");//"AB1"
     * StrUtil.padPre("123", 2, "ABC");//"12"
     * </pre>
     *
     * @param str       字符串
     * @param minLength 最小长度
     * @param padStr    补充的字符
     * @return 补充后的字符串
     */
    public static String padPre(CharSequence str, int minLength, CharSequence padStr) {
        if (null == str) {
            return null;
        }
        final int strLen = str.length();
        if (strLen == minLength) {
            return str.toString();
        } else if (strLen > minLength) {
            return subPre(str, minLength);
        }

        return repeatByLength(padStr, minLength - strLen).concat(str.toString());
    }

    /**
     * 重复某个字符串到指定长度
     *
     * @param str    被重复的字符
     * @param padLen 指定长度
     * @return 重复字符字符串
     * @since 4.3.2
     */
    public static String repeatByLength(CharSequence str, int padLen) {
        if (null == str) {
            return null;
        }
        if (padLen <= 0) {
            return EMPTY;
        }
        final int strLen = str.length();
        if (strLen == padLen) {
            return str.toString();
        } else if (strLen > padLen) {
            return subPre(str, padLen);
        }

        // 重复，直到达到指定长度
        final char[] padding = new char[padLen];
        for (int i = 0; i < padLen; i++) {
            padding[i] = str.charAt(i % strLen);
        }
        return new String(padding);
    }

    /**
     * 切割指定位置之前部分的字符串
     *
     * @param string  字符串
     * @param toIndex 切割到的位置（不包括）
     * @return 切割后的剩余的前半部分字符串
     */
    public static String subPre(CharSequence string, int toIndex) {
        return sub(string, 0, toIndex);
    }

    /**
     * 改进JDK subString<br>
     * index从0开始计算，最后一个字符为-1<br>
     * 如果from和to位置一样，返回 "" <br>
     * 如果from或to为负数，则按照length从后向前数位置，如果绝对值大于字符串长度，则from归到0，to归到length<br>
     * 如果经过修正的index中from大于to，则互换from和to example: <br>
     * abcdefgh 2 3 =》 c <br>
     * abcdefgh 2 -3 =》 cde <br>
     *
     * @param str       String
     * @param fromIndex 开始的index（包括）
     * @param toIndex   结束的index（不包括）
     * @return 字串
     */
    public static String sub(CharSequence str, int fromIndex, int toIndex) {
        if (isEmpty(str)) {
            return str(str);
        }
        int len = str.length();

        if (fromIndex < 0) {
            fromIndex = len + fromIndex;
            if (fromIndex < 0) {
                fromIndex = 0;
            }
        } else if (fromIndex > len) {
            fromIndex = len;
        }

        if (toIndex < 0) {
            toIndex = len + toIndex;
            if (toIndex < 0) {
                toIndex = len;
            }
        } else if (toIndex > len) {
            toIndex = len;
        }

        if (toIndex < fromIndex) {
            int tmp = fromIndex;
            fromIndex = toIndex;
            toIndex = tmp;
        }

        if (fromIndex == toIndex) {
            return EMPTY;
        }

        return str.toString().substring(fromIndex, toIndex);
    }

    /**
     * {@link CharSequence} 转为字符串，null安全
     *
     * @param cs {@link CharSequence}
     * @return 字符串
     */
    public static String str(CharSequence cs) {
        return null == cs ? null : cs.toString();
    }

    /**
     * 根据给定长度，将给定字符串截取为多个部分
     *
     * @param str 字符串
     * @param len 每一个小节的长度
     * @return 截取后的字符串数组
     */
    public static String[] split(CharSequence str, int len) {
        if (null == str) {
            return new String[]{};
        }
        return splitByLength(str.toString(), len);
    }

    /**
     * 根据给定长度，将给定字符串截取为多个部分
     *
     * @param str 字符串
     * @param len 每一个小节的长度
     * @return 截取后的字符串数组
     */
    public static String[] splitByLength(String str, int len) {
        int partCount = str.length() / len;
        int lastPartCount = str.length() % len;
        int fixPart = 0;
        if (lastPartCount != 0) {
            fixPart = 1;
        }

        final String[] strs = new String[partCount + fixPart];
        for (int i = 0; i < partCount + fixPart; i++) {
            if (i == partCount + fixPart - 1 && lastPartCount != 0) {
                strs[i] = str.substring(i * len, i * len + lastPartCount);
            } else {
                strs[i] = str.substring(i * len, i * len + len);
            }
        }
        return strs;
    }

    public static String strReplenishZero(String str, int num) {
        str = str == null ? "" : str;
        int strLength = str.length();
        if (num != strLength) {
            while (true) {
                strLength = str.length();
                if (num > strLength) {
                    str = "0" + str;
                } else {
                    break;
                }
            }
        }
        return str;
    }

    public static String upperFirst(CharSequence str) {
        if (null == str) {
            return null;
        }
        if (str.length() > 0) {
            char firstChar = str.charAt(0);
            if (Character.isLowerCase(firstChar)) {
                return Character.toUpperCase(firstChar) + subSuf(str, 1);
            }
        }
        return str.toString();
    }

    public static String subSuf(CharSequence string, int fromIndex) {
        if (isEmpty(string)) {
            return null;
        }
        return sub(string, fromIndex, string.length());
    }

    /**
     * 除法10
     *
     * @return String
     */
    public static String strDiv10(String num) {
        if (1 == num.length()) {
            num = "0." + num;
        } else {
            StringBuilder sb = new StringBuilder(num);
            sb.insert(num.length() - 1, ".");
            num = sb.toString();
        }
        return num;
    }


    /**
     * 反转字符串<br>
     * 例如：abcd =》dcba
     *
     * @param str 被反转的字符串
     * @return 反转后的字符串
     * @since 3.0.9
     */
    public static String reverse(String str) {
        return new String(MyArrayUtil.reverse(str.toCharArray()));
    }

    /**
     * 电话匿名
     *
     * @param str
     * @return
     */
    public static String anonymousPhone(String str) {
        if (StrUtil.isNotBlank(str)) {
            return StrUtil.replace(str, 3, 7, '*');
        }
        return null;
    }

    public static String anonymousUserName(String str) {
        if (StrUtil.isNotBlank(str)) {
            return StrUtil.replace(str, 2, str.length(), '*');
        }
        return null;
    }

    /**
     * 身份证匿名
     *
     * @param str
     * @return
     */
    public static String anonymousIdCard(String str) {
        if (StrUtil.isNotBlank(str)) {
            return StrUtil.replace(str, 6, 16, '*');
        }
        return null;
    }

}
