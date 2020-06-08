//package de.ginisolutions.trader.trading.config;
//
//import com.bol.crypt.CryptVault;
//import com.bol.secure.CachedEncryptionEventListener;
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
//
//import java.util.Base64;
//
///**
// * https://github.com/bolcom/spring-data-mongodb-encrypt
// */
//@Configuration
//public class MongoDBConfiguration extends AbstractMongoClientConfiguration {
//
//    // TODO normally you would use @Value to wire a property here
//    private static final byte[] secretKey = Base64.getDecoder().decode("hqHKBLV83LpCqzKpf8OvutbCs+O5wX5BPu3btWpEvXA=");
//    private static final byte[] oldKey = Base64.getDecoder().decode("cUzurmCcL+K252XDJhhWI/A/+wxYXLgIm678bwsE2QM=");
//
//    @Override
//    protected String getDatabaseName() {
//        return "test";
//    }
//
//    @Override
//    public MongoClient mongoClient() {
//        return MongoClients.create();
//    }
//
//    @Bean
//    public CryptVault cryptVault() {
//        return new CryptVault()
//            .with256BitAesCbcPkcs5PaddingAnd128BitSaltKey(0, oldKey)
//            .with256BitAesCbcPkcs5PaddingAnd128BitSaltKey(1, secretKey)
//            // can be omitted if it's the highest version
//            .withDefaultKeyVersion(1);
//    }
//
//    @Bean
//    public CachedEncryptionEventListener encryptionEventListener(CryptVault cryptVault) {
//        return new CachedEncryptionEventListener(cryptVault);
//    }
//}
