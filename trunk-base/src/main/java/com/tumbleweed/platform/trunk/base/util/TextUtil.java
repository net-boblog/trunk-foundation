package com.tumbleweed.platform.trunk.base.util;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mylover on 1/22/16.
 */
public class TextUtil {
    public static final String[] PHONE_PREFIX = new String[]{"130", "131", "132", "133", "134", "135", "136", "137", "138", "139", "145", "147", "150", "151", "152", "153", "154", "155", "156", "157", "158", "159", "180", "181", "182", "183", "184", "185", "186", "187", "188", "189"};
    public static final Random rnd = new Random();

    public TextUtil() {
    }

    public static int creatRandom(int t) {
        return Math.abs(rnd.nextInt(t));
    }

    public static String getArrayValue(String[] key, String[] value) {
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < value.length; ++i) {
            if(key != null) {
                sb.append(key[i] + ": ");
            }

            sb.append(value[i]);
            if(i < value.length - 1) {
                sb.append(",");
            }
        }

        return sb.toString();
    }

    public static String getArrayValue(String[] value) {
        return getArrayValue((String[])null, value);
    }

    public static String TrimSpace(String str) {
        if(str != null && !str.equals("")) {
            char[] tempChar = (char[])null;

            try {
                char[] e = new char[str.length()];
                int k = 0;

                for(int i = 0; i < str.length(); ++i) {
                    char c = str.charAt(i);
                    if(c != 32) {
                        e[k++] = c;
                    }
                }

                tempChar = new char[k];
                System.arraycopy(e, 0, tempChar, 0, k);
                return new String(tempChar);
            } catch (Exception var6) {
                return str;
            }
        } else {
            return null;
        }
    }

    public static String[] split(String original, String regex, int maxCount) {
        int startIndex = original.indexOf(regex);
        int index = 0;
        if(startIndex < 0) {
            return new String[]{original};
        } else {
            ArrayList v;
            String last;
            for(v = new ArrayList(); startIndex < original.length() && startIndex != -1; startIndex = original.indexOf(regex, startIndex + regex.length())) {
                last = original.substring(index, startIndex);
                v.add(last);
                if(v.size() >= maxCount) {
                    break;
                }

                index = startIndex + regex.length();
            }

            if(v.size() < maxCount && original.indexOf(regex, original.length() - regex.length()) < 0) {
                last = original.substring(index);
                v.add(last);
            }

            return (String[])v.toArray(new String[v.size()]);
        }
    }

    public static String[] split(String original, String regex) {
        return split(original, regex, false);
    }

    public static String[] split(String original, String regex, boolean isTogether) {
        int startIndex = original.indexOf(regex);
        int index = 0;
        if(startIndex < 0) {
            return new String[]{original};
        } else {
            ArrayList v;
            String last;
            for(v = new ArrayList(); startIndex < original.length() && startIndex != -1; startIndex = original.indexOf(regex, startIndex + regex.length())) {
                last = original.substring(index, startIndex);
                v.add(last);
                index = startIndex + regex.length();
            }

            if(original.indexOf(regex, original.length() - regex.length()) < 0) {
                last = original.substring(index);
                if(isTogether) {
                    last = regex + last;
                }

                v.add(last);
            }

            return (String[])v.toArray(new String[v.size()]);
        }
    }

    public static String getLastStringBySplit(String original, String regex) {
        try {
            String[] e = split(original, regex);
            return e[e.length - 1].trim();
        } catch (Exception var3) {
            var3.printStackTrace();
            return original;
        }
    }

    public static String replace(String str, String substr, String restr) {
        try {
            String[] e = split(str, substr);
            String returnstr = null;
            int len = e.length;
            if(e != null && len > 0) {
                returnstr = e[0];

                for(int i = 0; i < len - 1; ++i) {
                    returnstr = returnstr + restr + e[i + 1];
                }
            }

            return returnstr.trim();
        } catch (Exception var7) {
            var7.printStackTrace();
            return str;
        }
    }

    public static void releaseStringArray(String[] array) {
        if(array != null) {
            for(int i = 0; i < array.length; ++i) {
                array[i] = null;
            }

            array = (String[])null;
        }

    }

    public static String xmlTextEncode(String xmlStr) {
        if(xmlStr == null) {
            return "";
        } else {
            xmlStr = xmlStr.replace("&", "&amp;");
            xmlStr = xmlStr.replace("<", "&lt;");
            xmlStr = xmlStr.replace(">", "&gt;");
            xmlStr = xmlStr.replace("\'", "&apos;");
            xmlStr = xmlStr.replace("\"", "&quot;");
            xmlStr = xmlStr.replace("null", "");
            return xmlStr.trim();
        }
    }

    public static String xmlTextDecode(String xmlText) {
        if(xmlText == null) {
            return "";
        } else {
            xmlText = xmlText.replaceAll("&amp;", "&");
            xmlText = xmlText.replaceAll("&lt;", "<");
            xmlText = xmlText.replaceAll("&gt;", ">");
            xmlText = xmlText.replaceAll("&apos;", "\\");
            xmlText = xmlText.replaceAll("&quot;", "\"");
            return xmlText;
        }
    }

    public static String getTextReceiveDate(String time) {
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(time.substring(0, 4) + "-");
        sbBuffer.append(time.substring(4, 6) + "-");
        sbBuffer.append(time.substring(6, 8) + " ");
        sbBuffer.append(time.substring(8, 10) + ":");
        sbBuffer.append(time.substring(10, 12) + ":");
        sbBuffer.append(time.substring(12, time.length()));
        return sbBuffer.toString();
    }

    public static String getURLPrefix(String uri) {
        byte startIndex = 0;
        String str = uri.substring(startIndex + 1);
        int endIndex = str.indexOf("/") + 1;
        if(endIndex == -1) {
            return null;
        } else {
            String prefix = uri.substring(startIndex + 1, endIndex);
            System.out.println(prefix);
            return prefix;
        }
    }

    public static String checkJson(String json) {
        return json.startsWith("[") && json.endsWith("]")?json:(json.startsWith("[") && !json.endsWith("]")?json + "]":(!json.startsWith("[") && json.endsWith("]")?"[" + json:"[" + json + "]"));
    }

    public static boolean checkMDN(String mdn, boolean checkLen) {
        if(mdn != null && !mdn.equals("")) {
            if(mdn.startsWith("+86")) {
                mdn = mdn.substring(3);
            }

            if(checkLen && mdn.length() != 11) {
                return false;
            } else {
                boolean flag = false;
                String p = mdn.length() > 3?mdn.substring(0, 3):mdn;

                for(int i = 0; i < PHONE_PREFIX.length; ++i) {
                    if(p.equals(PHONE_PREFIX[i])) {
                        flag = true;
                        break;
                    }
                }

                return flag;
            }
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        String s = "330";
        String[] array = split(s, ",", 3);
        System.out.println(array.length);
    }

    public static char[] toCharArray(ArrayList<Character> charList) {
        Character[] cc = (Character[])charList.toArray(new Character[charList.size()]);
        char[] c = new char[cc.length];

        for(int i = 0; i < cc.length; ++i) {
            c[i] = cc[i].charValue();
        }

        return c;
    }
}
