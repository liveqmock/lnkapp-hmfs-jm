package utils;

/**
 * 字符串工具类
 */
public class StringHelper {

    public static String rightPad4ChineseToByteLength(String srcStr, int totalByteLength, String padStr) {
        if (srcStr == null) {
            return null;
        }
        int srcByteLength = srcStr.getBytes().length;

        if (padStr == null || "".equals(padStr)) {
            padStr = " ";
        } else if (padStr.getBytes().length > 1 || totalByteLength <= 0) {
            throw new RuntimeException("参数错误");
        }
        StringBuilder rtnStrBuilder = new StringBuilder();
        if (totalByteLength >= srcByteLength) {
            rtnStrBuilder.append(srcStr);
            for (int i = 0; i < totalByteLength - srcByteLength; i++) {
                rtnStrBuilder.append(padStr);
            }
        } else {
            byte[] rtnBytes = new byte[totalByteLength];
            System.arraycopy(srcStr.getBytes(), 0, rtnBytes, 0, totalByteLength);
            rtnStrBuilder.append(rtnBytes);
        }
        return rtnStrBuilder.toString();
    }

    public static String leftPad4ChineseToByteLength(String srcStr, int totalByteLength, String padStr) {
        if (srcStr == null) {
            return null;
        }
        int srcByteLength = srcStr.getBytes().length;

        if (padStr == null || "".equals(padStr)) {
            padStr = " ";
        } else if (padStr.getBytes().length > 1 || totalByteLength <= 0) {
            throw new RuntimeException("参数错误");
        }
        StringBuilder rtnStrBuilder = new StringBuilder();
        if (totalByteLength >= srcByteLength) {
            for (int i = 0; i < totalByteLength - srcByteLength; i++) {
                rtnStrBuilder.append(padStr);
            }
            rtnStrBuilder.append(srcStr);
        } else {
            byte[] rtnBytes = new byte[totalByteLength];
            System.arraycopy(srcStr.getBytes(), 0, rtnBytes, 0, totalByteLength);
            rtnStrBuilder.append(rtnBytes);
        }
        return rtnStrBuilder.toString();
    }

    public static String appendStrToLength(String srcStr, String appendStr, int length) {
        int appendLength = length - srcStr.getBytes().length;
        StringBuilder strBuilder = new StringBuilder(srcStr);
        for (int i = 1; i <= appendLength; i++) {
            strBuilder.append(appendStr);
        }
        return strBuilder.toString();
    }
}
