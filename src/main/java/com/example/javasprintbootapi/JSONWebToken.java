package com.example.javasprintbootapi;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class JSONWebToken {
    private static String SECRET_KEY = "";

    public static void setSecretKey(String secretKey){
        SECRET_KEY = secretKey;
    }

    public static String getSecretKey(){
        return SECRET_KEY;
    }

    private static String HmacSHA256(String data, String secret) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(),"HmacSHA256");
        mac.init(secretKeySpec);

        byte[] hmacBytes = mac.doFinal(data.getBytes());
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hmacBytes);
    }

    public static boolean VerifyJWT(String jwt) throws NoSuchAlgorithmException, InvalidKeyException {
        String[] parts = jwt.split("\\.");
        if (parts.length != 3){
            return false;
        }

        String headerPayload = parts[0] + "." + parts[1];
        String signatureRecieved = parts[2];

        String computedSignature = HmacSHA256(headerPayload,SECRET_KEY);

        return computedSignature.equals(signatureRecieved);
    }

    public static String SecureKeyGenerator(){
        byte[] key = new byte[new Random().nextInt(32,65)];
        new SecureRandom().nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }


}
