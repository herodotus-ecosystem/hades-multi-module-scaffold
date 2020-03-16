package cn.com.felix;

import cn.com.felix.core.utils.encrypt.AesEncryptUtils;
import org.apache.shiro.codec.Base64;
import org.bouncycastle.util.encoders.Hex;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HadesWebApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    StringEncryptor stringEncryptor;

    @Test
    public void devPassord() {

        System.out.println("----------- dev -----------");

        String postgresqlUsername = stringEncryptor.encrypt("framework");
        String postgresqlPassword = stringEncryptor.encrypt("framework");

        System.out.println("Username - pg : " + postgresqlUsername);
        System.out.println("Password - pg : " + postgresqlPassword);

        Assert.assertTrue(postgresqlUsername.length() > 0);
        Assert.assertTrue(postgresqlPassword.length() > 0);
    }

    @Test
    public void prodPassord() {

        System.out.println("----------- prod -----------");

        String postgresqlUsername = stringEncryptor.encrypt("framework");
        String postgresqlPassword = stringEncryptor.encrypt("framework");
        String redisPassword = stringEncryptor.encrypt("framework");


        System.out.println("Username - pg : " + postgresqlUsername);
        System.out.println("Password - pg : " + postgresqlPassword);
        System.out.println("Password - redis : " + redisPassword);

        Assert.assertTrue(postgresqlUsername.length() > 0);
        Assert.assertTrue(postgresqlPassword.length() > 0);
        Assert.assertTrue(redisPassword.length() > 0);
    }

    @Test
    public void cipherkeyGenerate() throws NoSuchAlgorithmException {

        int length = 128;

        KeyGenerator keygen = KeyGenerator.getInstance("AES");;
//        keygen.init(length);
        SecretKey secretKey = keygen.generateKey();

        byte[] encodeKey = secretKey.getEncoded();
        String key = Base64.encodeToString(encodeKey);
        System.out.println(key);
    }

}

