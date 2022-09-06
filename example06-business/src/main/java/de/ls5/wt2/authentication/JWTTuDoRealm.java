package de.ls5.wt2.authentication;


import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.springframework.beans.factory.annotation.Autowired;


public class JWTTuDoRealm extends AuthenticatingRealm {

    @Autowired
    private TokenBlockList tokenBlockList;
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTShiroToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        final JWTShiroToken jwToken = (JWTShiroToken) token;

        if (JWTUtil.validateToken(jwToken.getToken()) && !tokenBlockList.isBlockedToken((jwToken.getToken()))) {
            return new SimpleAccount(jwToken.getPrincipal(), jwToken.getCredentials(), getName());
        }

        throw new AuthenticationException();
    }
}
