package com.codebyte.sunuso.Tokenizer;

import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encoder {
    public static ArrayList<ObjectAttrib> StartEncodeVersion2(String Key, ArrayList<ObjectAttrib> lista) {
        try {
            ArrayList<ObjectAttrib> Encdata = lista;
            Charset charset = Charset.forName("UTF-8");
            IvParameterSpec IvParam = new IvParameterSpec(Key.getBytes(charset));
            SecretKey aesKey = new SecretKeySpec(Key.getBytes(charset), "AES");
            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(1, aesKey, IvParam);
            for (ObjectAttrib ObjAtr : Encdata) {
                byte[] encrypted = cipher.doFinal(ObjAtr.getValue().getBytes(charset));
                ObjAtr.setValue(Base64.getEncoder().encodeToString(encrypted));
            }
            return Encdata;
        } catch (NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException|java.security.InvalidKeyException|javax.crypto.IllegalBlockSizeException|javax.crypto.BadPaddingException|java.security.InvalidAlgorithmParameterException ex) {
            Logger.getLogger(Encoder.class.getName()).log(Level.SEVERE, (String)null, ex);
            return null;
        }
    }

    public static ArrayList<ObjectAttrib> StartDecode(String Key, ArrayList<ObjectAttrib> lista) {
        try {
            ArrayList<ObjectAttrib> Encdata = lista;
            Charset charset = Charset.forName("UTF-8");
            IvParameterSpec IvParam = new IvParameterSpec(Key.getBytes(charset));
            SecretKey aesKey = new SecretKeySpec(Key.getBytes(charset), "AES");
            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            for (ObjectAttrib objAtt : Encdata) {
                String Text = objAtt.getValue();
                byte[] decodedBytes = Base64.getDecoder().decode(Text);
                cipher.init(2, aesKey, IvParam);
                String decrypted = (new String(cipher.doFinal(decodedBytes), charset)).trim();
                objAtt.setValue(decrypted);
            }
            return Encdata;
        } catch (NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException|java.security.InvalidKeyException|javax.crypto.IllegalBlockSizeException|javax.crypto.BadPaddingException|java.security.InvalidAlgorithmParameterException ex) {
            Logger.getLogger(Encoder.class.getName()).log(Level.SEVERE, (String)null, ex);
            return null;
        }
    }

    public static String StartEncodesingle(String Key, String Single) {
        try {
            Charset charset = Charset.forName("UTF-8");
            IvParameterSpec IvParam = new IvParameterSpec(Key.getBytes(charset));
            SecretKey aesKey = new SecretKeySpec(Key.getBytes(charset), "AES");
            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(1, aesKey, IvParam);
            byte[] encrypted = cipher.doFinal(Single.getBytes(charset));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException|java.security.InvalidKeyException|javax.crypto.IllegalBlockSizeException|javax.crypto.BadPaddingException|java.security.InvalidAlgorithmParameterException ex) {
            Logger.getLogger(Encoder.class.getName()).log(Level.SEVERE, (String)null, ex);
            return null;
        }
    }

    public static String StartDecodeSingle(String Key, String Text) {
        try {
            Charset charset = Charset.forName("UTF-8");
            IvParameterSpec IvParam = new IvParameterSpec(Key.getBytes(charset));
            SecretKey aesKey = new SecretKeySpec(Key.getBytes(charset), "AES");
            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            byte[] decodedBytes = Base64.getDecoder().decode(Text);
            cipher.init(2, aesKey, IvParam);
            String decrypted = (new String(cipher.doFinal(decodedBytes), charset)).trim();
            return decrypted;
        } catch (NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException|java.security.InvalidKeyException|javax.crypto.IllegalBlockSizeException|javax.crypto.BadPaddingException|java.security.InvalidAlgorithmParameterException ex) {
            Logger.getLogger(Encoder.class.getName()).log(Level.SEVERE, (String)null, ex);
            return null;
        }
    }
}
