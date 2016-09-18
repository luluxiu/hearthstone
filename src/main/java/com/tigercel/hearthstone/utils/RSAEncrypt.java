package com.tigercel.hearthstone.utils;

import org.springframework.context.annotation.Configuration;
import sun.misc.BASE64Decoder;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by somebody on 2016/8/10.
 */
public class RSAEncrypt {

    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;
    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public void loadPublicKey(InputStream in) {
        try {
            BufferedReader br= new BufferedReader(new InputStreamReader(in));
            String readLine= null;
            StringBuilder sb= new StringBuilder();
            while((readLine= br.readLine())!=null){
                if(readLine.charAt(0)=='-'){
                    continue;
                }else{
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPublicKey(sb.toString());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPublicKey(String publicKeyStr) {
        try {
            BASE64Decoder base64Decoder= new BASE64Decoder();
            byte[] buffer= base64Decoder.decodeBuffer(publicKeyStr);
            KeyFactory keyFactory= KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec= new X509EncodedKeySpec(buffer);
            this.publicKey= (RSAPublicKey) keyFactory.generatePublic(keySpec);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void loadPrivateKey(InputStream in) {
        try {
            BufferedReader br= new BufferedReader(new InputStreamReader(in));
            String readLine= null;
            StringBuilder sb= new StringBuilder();
            while((readLine= br.readLine())!=null){
                if(readLine.charAt(0)=='-'){
                    continue;
                }else{
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPrivateKey(sb.toString());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPrivateKey(String privateKeyStr) {
        try {
            BASE64Decoder base64Decoder= new BASE64Decoder();
            byte[] buffer= base64Decoder.decodeBuffer(privateKeyStr);
            PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory= KeyFactory.getInstance("RSA");
            this.privateKey= (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


    public byte[] encrypt(byte[] plainTextData) {

        return encrypt(getPublicKey(), plainTextData);
    }


    public byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) {
        if(publicKey== null){
            return null;
        }
        Cipher cipher= null;
        try {
            cipher= Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] output= cipher.doFinal(plainTextData);
            return output;
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) {
        if (privateKey== null){
            return null;
        }
        Cipher cipher= null;
        try {
            cipher= Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] output= cipher.doFinal(cipherData);
            return output;
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String byteArrayToString(byte[] data){
        StringBuilder stringBuilder= new StringBuilder();
        for (int i=0; i<data.length; i++){
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0)>>> 4]);
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
            if (i<data.length-1){
                stringBuilder.append(' ');
            }
        }
        return stringBuilder.toString();
    }

/*
    public static void main(String[] args){
        RSAEncrypt rsaEncrypt= new RSAEncrypt();

        rsaEncrypt.loadPrivateKey(RSAEncrypt.class.getResourceAsStream("/rsa/id_rsa"));
        rsaEncrypt.loadPublicKey(RSAEncrypt.class.getResourceAsStream("/rsa/id_rsa.pub"));


        //测试字符串
        String encryptStr= "Test String chaijunkun";

        try {
            //加密
            byte[] cipher = rsaEncrypt.encrypt(rsaEncrypt.getPublicKey(), encryptStr.getBytes());
            //解密
            byte[] plainText = rsaEncrypt.decrypt(rsaEncrypt.getPrivateKey(), cipher);
            System.out.println("密文长度:"+ cipher.length);
            System.out.println(RSAEncrypt.byteArrayToString(cipher));
            //System.out.println(new String(cipher));
            System.out.println("明文长度:"+ plainText.length);
            System.out.println(RSAEncrypt.byteArrayToString(plainText));
            System.out.println(new String(plainText));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
*/

}
