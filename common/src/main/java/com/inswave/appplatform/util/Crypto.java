package com.inswave.appplatform.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
public class Crypto {

    /**
     * BASE64 Encoder
     *
     * @param str
     * @return
     */
    public static String base64Encode(String str) throws java.io.IOException {
        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        byte[] strByte = str.getBytes();
        String result = encoder.encode(strByte);
        return result;
    }

    public static String base64Encode(byte[] value) throws java.io.IOException {
        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        String result = encoder.encode(value);
        return result;
    }

    /**
     * BASE64 Decoder
     *
     * @param str
     * @return
     */
    public static byte[] base64Decode(String str) throws java.io.IOException {
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        return decoder.decodeBuffer(str);
        //        byte[] strByte = decoder.decodeBuffer(str);
        //        String result = new String(strByte);
        //        return result ;
    }

    public String getSHA256(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(text.getBytes("UTF-8"));
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * @param algorithm (MessageDigest also supports MD2, MD5, SHA, SHA-1, SHA-256, SHA-384, SHA-512)
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public static byte[] getEncrypt(String algorithm, String text) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        final byte[] theTextToDigestAsBytes = text.getBytes("8859_1");

        // MessageDigest also supports MD2, MD5, SHA-1, SHA-256, SHA-384, SHA-512
        final MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(theTextToDigestAsBytes);
        // md.update( int ) processes only the low order 8-bits. It actually expects an unsigned byte.
        final byte[] digest = md.digest();
        // will print SHA
        //System.out.println( "Algorithm used: " + md.getAlgorithm() );
        // should be 20 bytes, 160 bits long
        //System.out.println( "Digest is " + digest.length * 8 + " bits, " + digest.length + " bytes long." );
        // dump out the hash
        //System.out.print( "Digest: " );
        for (byte b : digest) {
            // print byte as 2 unsigned hex digits with lead 0. Separate pairs of digits with space
            //System.out.printf( "%02X ", b & 0xff );
        }
        //System.out.println();

        //        System.out.println("encodeBase64 ->"+encodeBase64(digest));
        //
        //        System.out.println("convertToHex->"+convertToHex(digest) );
        //        System.out.println("convertToHex and encodeBase64 ->"+encodeBase64(convertToHex(digest)) );
        //
        //        // Find out what other digests we can compute:
        //        final Set<String> algorithms = Security.getAlgorithms( "MessageDigest" );
        //        for ( String algorithm : algorithms )
        //        	System.out.println( algorithm );

        return digest;
    }

    public static String encryptSHA256(String text) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes());
            return byteToHex(md.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String byteToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    public static String encryptAES256(String text, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(key.substring(0, 16).getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

        byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
        String cipherText = Base64.getEncoder().encodeToString(encrypted);
        log.info("encrypted cipherText encodeTarget : {}", cipherText);
        return cipherText;
        //        return new String(encrypted, "UTF-8");
    }

    public static String decryptAES256(String cipherText, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(key.substring(0, 16).getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

        log.info("encrypted cipherText decodeTarget : {}", cipherText);
        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decrypted = cipher.doFinal(decodedBytes);
        //        byte[] decrypted = cipher.doFinal(cipherText.getBytes(StandardCharsets.UTF_8));
        return new String(decrypted, "UTF-8");
    }
}
