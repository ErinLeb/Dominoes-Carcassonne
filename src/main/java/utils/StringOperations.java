package utils;

public class StringOperations {

    private StringOperations() {
    }

    public static String repeat(String s, int number) {
        String res = "";

        for (int i = 0; i < number; i++) {
            res += s;
        }

        return res;
    }

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}