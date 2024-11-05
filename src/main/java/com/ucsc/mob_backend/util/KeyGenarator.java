package com.ucsc.mob_backend.util;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

@Component
public class KeyGenarator {
//    public static void main(String[] args) {
//        try {
//            KeyGenarator keyGenarator = new KeyGenarator();
//            System.out.println(keyGenarator.generateKey("email", "saffffffflt"));
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//    }
    public String generateKey(String email, String salt) {
        StringBuilder sb = new StringBuilder();
        StringBuilder private_key= new StringBuilder();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(email.getBytes());
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            byte[] hashofkey= digest.digest(pickCharacters(salt,sb.toString()).getBytes());

            for (byte b : hashofkey) {
                private_key.append(String.format("%02x", b));
            }

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return private_key.toString();
    }
    public String pickCharacters(String inputText, String sourceString) throws NoSuchAlgorithmException {

        BitSet binaryMask = generateBinaryMask(inputText);

        StringBuilder selectedCharacters = new StringBuilder();
        for (int position = 0; position < 64; position++) {
            if (binaryMask.get(position)) {
                selectedCharacters.append(sourceString.charAt(position));
            }
        }
        return selectedCharacters.toString();
    }
    private BitSet generateBinaryMask(String inputText) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(inputText.getBytes(StandardCharsets.UTF_8));

        BitSet bitSet = BitSet.valueOf(hash);
        return bitSet.get(0, 64);
    }
}
