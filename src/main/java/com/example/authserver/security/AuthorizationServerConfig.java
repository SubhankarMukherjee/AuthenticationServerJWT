package com.example.authserver.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    public static final String RESOURCE_ID = "couponservice";
    @Autowired
    AuthenticationManager manager;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    DataSource dataSource;
    @Value("${keyFile}")
    String keyFile;
    @Value("${password}")
    String password;
    @Value("${alias}")
     String alias;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//         We need to tell Authentication Server about
//        1) token store
//                2) AuthenticationManager
//                3)UserDetailsService

        //IN memory
       // endpoints.tokenStore(new InMemoryTokenStore()).authenticationManager(manager).userDetailsService(userDetailsService);
        //JDBC
       // endpoints.tokenStore(new JdbcTokenStore(dataSource)).authenticationManager(manager).userDetailsService(userDetailsService);
        //JWT Token store and  converter
        endpoints.tokenStore(jwtTokenStore()).
                accessTokenConverter(jwtAccessTokenConverter())
                .authenticationManager(manager).userDetailsService(userDetailsService);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //clients.inMemory().withClient("couponClientapp").secret(encoder.encode("9999"))
              //  .authorizedGrantTypes("password","refresh_token")
               // .scopes("read","write").resourceIds(RESOURCE_ID);
        clients.inMemory().withClient("couponClientapp").secret(encoder.encode("9999"))
          .authorizedGrantTypes("password","refresh_token")
         .scopes("read","write").resourceIds(RESOURCE_ID);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll");
    }

    @Bean
    public JwtTokenStore jwtTokenStore()
    {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter()
    {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();


        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(new ClassPathResource(keyFile), password.toCharArray());
        KeyPair keyPair = factory.getKeyPair(alias);
        converter.setKeyPair(keyPair);
        return converter;
    }

    // For Symmetrci KEY
//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter()
//    {
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//    converter.setSigningKey("hello");
//        return converter;
//    }


}
