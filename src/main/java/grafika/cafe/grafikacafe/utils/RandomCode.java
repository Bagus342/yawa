package grafika.cafe.grafikacafe.utils;

public class RandomCode {
    public String getCode() {
        int length = 10;
        String setup = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = (int)(setup.length() * Math.random());
            stringBuilder.append(setup.charAt(index));
        }
        return stringBuilder.toString();
    }

}
