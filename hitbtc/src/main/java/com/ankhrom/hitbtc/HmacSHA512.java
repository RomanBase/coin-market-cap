package com.ankhrom.hitbtc;


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacSHA512 {

    public static String digest(String url, String secretKey) {

        String digest = null;

        String algo = "HmacSHA512";

        try {

            SecretKeySpec key = new SecretKeySpec((secretKey).getBytes("UTF-8"), algo);

            Mac mac = Mac.getInstance(algo);

            mac.init(key);

            byte[] bytes = mac.doFinal(url.getBytes("UTF-8"));

            StringBuilder hash = new StringBuilder();

            for (int i = 0; i < bytes.length; i++) {

                String hex = Integer.toHexString(0xFF & bytes[i]);

                if (hex.length() == 1) {

                    hash.append('0');

                }

                hash.append(hex);

            }

            digest = hash.toString();

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        } catch (InvalidKeyException e) {

            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();

        }

        return digest;
    }
}
