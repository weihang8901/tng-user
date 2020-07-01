package com.central.user.utils;


import org.apache.commons.io.output.ByteArrayOutputStream;

import javax.crypto.Cipher;
;
import java.io.ObjectInputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import sun.misc.BASE64Decoder;

import java.util.HashMap;
import java.util.Map;

public class RSAutils {
    /**
     * 测试方法
     */
    public static void main(String[] args) throws Exception {
        System.out.println(123);

    }

    //定义加密方式
    public static final String KEY_RSA = "RSA";
    //定义公钥关键词
    public static final String KEY_RSA_PUBLICKEY = "RSAPublicKey";
    //定义私钥关键词
    public static final String KEY_RSA_PRIVATEKEY = "RSAPrivateKey";
    //定义签名算法
    private final static String KEY_RSA_SIGNATURE = "MD5withRSA";
    /**
     * **
     * RSA最大加密大小
     */
    private final static int MAX_ENCRYPT_BLOCK = 128;

    /**
     * **
     * RSA最大解密大小
     */
    private final static int MAX_DECRYPT_BLOCK = 128;
    private static final String ALGORITHM = "RSA";
    public static String decrypt(String cryptograph,String privateKey) throws Exception{ //cryptograph-通过rsa公钥加密得到的参数  privatekey-与公钥对应的私钥
        /** 将文件中的私钥对象读出 */
        byte[] keyBytes = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        Key key = keyFactory.generatePrivate(pKCS8EncodedKeySpec);

        // cryptograph= new String(cryptograph.getBytes(),"gbk");


        /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b1 = decoder.decodeBuffer(cryptograph);
        /** 执行解密操作 */
        byte[] b = null;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i += 100) {
            byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(b1, i, i + 100));
            sb.append(new String(doFinal,"utf-8"));
        }
        return sb.toString();

    }







}
