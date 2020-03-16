package cn.com.felix.core.utils.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {
    public static String md5(String source) {
        return md5(source, false);
    }

    public static String md5(String source, boolean shortLength) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();

            messageDigest.update(source.getBytes("UTF-8"));

        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();

        String hexString = hexEncode(byteArray);
        if (shortLength) {
            return hexString.substring(8, 24);
        }
        return hexString;
    }

    public static String hexEncode(byte[] bytes) {
        String result = "";
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(aByte & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            result = result.concat(hex.toUpperCase());
        }
        return result;
    }
}
