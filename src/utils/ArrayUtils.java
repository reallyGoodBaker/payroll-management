package utils;

public class ArrayUtils {

    public static String join(String[] list, String seperator) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < list.length; i++) {
            if (i > 0) {
                sb.append(seperator);
            }

            sb.append(list[i]);
        }

        return sb.toString();
    }

}
