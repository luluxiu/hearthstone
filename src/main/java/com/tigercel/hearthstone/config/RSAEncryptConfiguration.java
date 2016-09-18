package com.tigercel.hearthstone.config;

import com.tigercel.hearthstone.utils.RSAEncrypt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * not in use yet
 */

//@Configuration
public class RSAEncryptConfiguration {

    //@Bean
    public RSAEncrypt RSAEncryptInit() {
        RSAEncrypt rsaEncrypt= new RSAEncrypt();

        rsaEncrypt.loadPrivateKey(RSAEncrypt.class.getResourceAsStream("/rsa/id_rsa"));
        rsaEncrypt.loadPublicKey(RSAEncrypt.class.getResourceAsStream("/rsa/id_rsa.pub"));

        return rsaEncrypt;
    }
}
