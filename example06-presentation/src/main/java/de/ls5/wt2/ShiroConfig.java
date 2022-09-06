package de.ls5.wt2;

import java.util.*;

import javax.servlet.Filter;

import de.ls5.wt2.authentication.*;


import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {


    @Bean

    public Realm realm() {
        return new TuDoAuthorizingRealm();
    }
    @Bean
    public Realm jwtRealm() {
        return new JWTTuDoRealm();
    }

    @Bean
    public DefaultWebSecurityManager securityManager(Realm realm,  Realm jwtRealm) {
        return new DefaultWebSecurityManager(Arrays.asList(jwtRealm,realm));
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();

        filters.put("jwtFilter", new JWTAuthenticationFilter());


        final Map<String, String> chainDefinition = new LinkedHashMap<>();


        // make login and sign up possible
        chainDefinition.put("/rest/users/add","anon");
        chainDefinition.put("/rest/authentication/authenticate","anon");

        chainDefinition.put("/rest/reset","anon");
        chainDefinition.put("/rest/**","noSessionCreation, jwtFilter");
        // make static Angular resources globally available
        chainDefinition.put("/**", "anon");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(chainDefinition);

        return shiroFilterFactoryBean;
    }


    @Bean
    public TokenBlockList tokenBlockList() {
        return new TokenBlockList(new ArrayList<String>());
    }

}
