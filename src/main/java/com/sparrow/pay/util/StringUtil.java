//package com.sparrow.pay.util;
//
//import javax.crypto.Cipher;
//import java.util.Random;
//
//public class StringUtil {
//
//    public static String randomStr(int length) {
//        Random random = new Random();
//        StringBuilder str = new StringBuilder();
//        for (int i = 0; i < length; i++) {
//            int choice = random.nextInt(3);
//            switch (choice) {
//                case 0:
//                    str.append((char) (random.nextInt(25) + 97));
//                    break;
//                case 1:
//                    str.append((char) (random.nextInt(25) + 65));
//                    break;
//                case 2:
//                    str.append((char) (random.nextInt(10) + 48));
//                    break;
//                default:
//                    break;
//            }
//        }
//        return str.toString();
//    }
//
//}
//
//    public String encrypt(String data) {
//
//        try {
//            Cipher cipher = Cipher.getInstance(ALGORITHM);
//            cipher.init(Cipher.ENCRYPT_MODE, createKeySpec(), createIvSpec());
//            byte[] encryptData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
//            return iv + Base64Utils.encodeToString(encryptData);
//        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
//            throw new RuntimeException("encrypt fail : " + e.getMessage());
//        }
//    }
//
//    public String decrypt(String data) {
//        String ivStr = data.substring(0, 16);
//        String content = data.substring(16);
//        byte[] dataBytes = Base64Utils.decodeFromString(content);
//
//        try {
//            Cipher cipher = Cipher.getInstance(ALGORITHM);
//            cipher.init(Cipher.DECRYPT_MODE, createKeySpec(), new IvParameterSpec(ivStr.getBytes(StandardCharsets.UTF_8)));
//            byte[] original = cipher.doFinal(dataBytes);
//            return new String(original, StandardCharsets.UTF_8);
//        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
//            throw new RuntimeException("decrypt fail : " + e.getMessage());
//        }
//    }
//}
