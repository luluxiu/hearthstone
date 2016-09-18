package com.tigercel.hearthstone.config;

import lombok.Data;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.Ssl;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by somebody on 2016/8/10.
 */
/*
@Configuration
@PropertySource("classpath:/tomcat.https.properties")
@ConfigurationProperties(prefix = "custom.tomcat.https")
@EnableConfigurationProperties(HttpsConfiguration.TomcatSslConnectorProperties.class)
@Data
*/
public class HttpsConfiguration {
    private Integer httpPort;
    private Integer port;
    private String  keystore;
    private String  keystorePassword;

    @Configuration
    public class TomcatSslConnectorProperties implements EmbeddedServletContainerCustomizer {

        @Override
        public void customize(ConfigurableEmbeddedServletContainer container) {
            String path;
            Ssl ssl = new Ssl();

            ssl.setKeyStorePassword(keystorePassword);
            if(HttpsConfiguration.class.getResource(keystore) == null) {
                return;
            }
            path = HttpsConfiguration.class.getResource(keystore).getPath();
            ssl.setKeyStore(path);
            container.setSsl(ssl);

            if(port != null) {
                container.setPort(port);
            }
        }
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainerFactory() {
        TomcatEmbeddedServletContainerFactory factory =
                new TomcatEmbeddedServletContainerFactory() {
                    @Override
                    protected void postProcessContext(Context context) {
                        SecurityConstraint securityConstraint = new SecurityConstraint();
                        securityConstraint.setUserConstraint("CONFIDENTIAL");
                        SecurityCollection collection = new SecurityCollection();
                        collection.addPattern("/*");
                        securityConstraint.addCollection(collection);
                        context.addConstraint(securityConstraint);
                    }
                };
        factory.addAdditionalTomcatConnectors(createHttpConnector());
        return factory;
    }

    private Connector createHttpConnector() {
        Connector connector = new Connector();
        connector.setScheme("http");
        connector.setSecure(false);
        connector.setPort(httpPort);
        connector.setRedirectPort(port);
        return connector;
    }
}
