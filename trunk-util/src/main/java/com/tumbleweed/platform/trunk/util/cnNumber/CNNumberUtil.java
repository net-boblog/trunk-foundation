package com.tumbleweed.platform.trunk.util.cnNumber;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CNNumberUtil {

    private static final char[] CN_NUMBERS = {
            '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'
    };

    private static final char[] CN_UNITS = {
            '厘', '分', '角', '元', '拾', '佰', '仟', '万', '拾', '佰', '仟', '亿', '拾', '佰', '仟'
    };

    private static final char[] ARABIC_NUMBERS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    public static String format(String number) {
        return transform(number);
    }

    public static String format(BigDecimal number) {
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        String original = String.valueOf(decimalFormat.format(number));
        return transform(original);
    }

    public static String format(double number) {
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        String original = String.valueOf(decimalFormat.format(number));
        return transform(original);
    }

    public static String format(long number) {
        return transform(String.valueOf(number));
    }

    private static String transform(String original) {
        String integerPart = "";
        String floatPart = "";

        if (original.indexOf(".") > -1) {
            int dotIndex = original.indexOf(".");
            integerPart = original.substring(0, dotIndex);
            floatPart = original.substring(dotIndex + 1);
        } else {
            integerPart = original;
        }
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < integerPart.length(); i++) {
            int number = Integer
                    .parseInt(String.valueOf(integerPart.charAt(i)));

            if (number == 0 && i == integerPart.length() - 1) {
                stringBuilder.append(CN_UNITS[integerPart.length() + 2 - i]);
                continue;
            }

            if (number == 0) {
                continue;
            }

            stringBuilder.append(CN_NUMBERS[number]);

            stringBuilder.append(CN_UNITS[integerPart.length() + 2 - i]);
        }

        if (floatPart.length() >= 1) {
            for (int i = 0; i < floatPart.length(); i++) {
                int number = Integer.parseInt(String.valueOf(floatPart
                        .charAt(i)));
                stringBuilder.append(CN_NUMBERS[number]);
                if (i < 3) {
                    stringBuilder.append(CN_UNITS[2 - i]);
                }
            }
        } else {
            stringBuilder.append('整');
        }

        return stringBuilder.toString();

    }

    public static BigDecimal parse(String cnNumberString) {
        if (null == cnNumberString || "".equals(cnNumberString.trim())) {
            return null;
        }
        cnNumberString = cnNumberString.replaceAll("整", "");
        if (!cnNumberString.endsWith("元")) {
            cnNumberString = cnNumberString.replaceAll("元", ".");
        }
        for (int i = 0; i < CN_NUMBERS.length; i++) {
            cnNumberString = cnNumberString.replace(CN_NUMBERS[i], ARABIC_NUMBERS[i]);
        }
        for (int j = 0; j < CN_UNITS.length; j++) {
            cnNumberString = cnNumberString.replaceAll(CN_UNITS[j] + "", "");
        }

        BigDecimal b = new BigDecimal(cnNumberString);

        return b;
    }

    public static void main(String[] args) {
        System.out.println(CNNumberUtil.format(123456789000.12345));
        System.out.println(CNNumberUtil.format(123456789));
        System.out.println(CNNumberUtil.format(.123456789));
        System.out.println(CNNumberUtil.format(0.1234));
        System.out.println(CNNumberUtil.format(1));
        System.out.println(CNNumberUtil.format(12));
        System.out.println(CNNumberUtil.format(123));
        System.out.println(CNNumberUtil.format(1234));
        System.out.println(CNNumberUtil.format(12345));
        System.out.println(CNNumberUtil.format(123456));
        System.out.println(CNNumberUtil.format(1234567));
        System.out.println(CNNumberUtil.format(12345678));
        System.out.println(CNNumberUtil.format(123456789));
        System.out.println(CNNumberUtil.parse("壹亿贰仟叁佰肆拾伍万陆仟柒佰捌拾玖元壹角贰分叁厘"));
    }
}
