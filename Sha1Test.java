/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sha.pkg1test;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author sheidbri
 */
public class Sha1Test {

    /**
     * @param args the 
     * command line arguments
     */
    public static void main(String[] args) throws Exception {
        int numOfTries = 50;
        PrintWriter preImage = new PrintWriter("pre-imageNum.txt", "UTF-8");
        PrintWriter preImageMessages = new PrintWriter("pre-imageMessages.txt", "UTF-8");
        
        PrintWriter collision = new PrintWriter("collisionNum.txt", "UTF-8");
        PrintWriter collisionMessages = new PrintWriter("collisionMessages.txt", "UTF-8");
        System.out.println("Start");
        while(numOfTries-- > 0)
        {
            String message = randomMessage();
            byte[] messageDigest = encrypt(message);
            String randomMessage = randomMessage();
            int numTries = 0;

            //Pre-Image Attack

            while(!Arrays.equals(messageDigest, encrypt(randomMessage)))
            {
                numTries++;
                do
                {
                    randomMessage = randomMessage();
                }while(randomMessage.compareTo(message) == 0);
            }
            preImage.println(numTries);
            preImageMessages.println(message + ":" + bytesToHex(encrypt(message)) + ";" + 
                                        randomMessage + ":" + bytesToHex(encrypt(randomMessage)));
            
            // Collision Attack
            Map digestMap = new HashMap();
            randomMessage = randomMessage();
            String digest = bytesToHex(encrypt(randomMessage));
            numTries = 0;
            while(!digestMap.containsKey(digest))
            {
                digestMap.put(digest, randomMessage);
                do
                {
                    randomMessage = randomMessage();
                }while(randomMessage.compareTo(message) == 0);
                digest = bytesToHex(encrypt(randomMessage));
                numTries++;
            }
            collision.println(numTries);
            collisionMessages.println(digestMap.get(digest) + ":" + digest + ";" +
                                        randomMessage + ":" + digest);
            System.out.println(numOfTries + " left.");
        }
        System.out.println("End");
        preImage.close();
        preImageMessages.close();
        collision.close();
        collisionMessages.close();
    }
    
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static Random rnd = new Random();

     public static String randomMessage() 
     {
        return randomString (rnd.nextInt(100) + 10);
    }
    static String randomString( int len ) 
    {
       StringBuilder sb = new StringBuilder( len );
       for( int i = 0; i < len; i++ ) 
          sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
       return sb.toString();
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    public static byte[] encrypt(String x) throws Exception {
        java.security.MessageDigest d = null;
        d = java.security.MessageDigest.getInstance("Sha-1");
        d.reset();
        d.update(x.getBytes());
        return Arrays.copyOfRange(d.digest(), 0, 3);
    }
    
}
