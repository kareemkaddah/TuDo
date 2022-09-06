package de.ls5.wt2.authentication;

import de.ls5.wt2.AppUserRole;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.Collections;

public class TuDoAuthorizingRealm extends AuthorizingRealm implements Realm {

    final static String REALM = "TuDo";

    @Autowired
    private EntityManager entityManager;


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        final String user = (String) authenticationToken.getPrincipal();
        return new SimpleAccount(user, user.toCharArray(), TuDoAuthorizingRealm.REALM);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return new AuthorizationInfo() {

            @Override
            public Collection<String> getRoles() {
                if ("admin".equals(principals.getPrimaryPrincipal())) {
                    return Collections.singleton(AppUserRole.ADMIN.toString());
                }else {
                    return Collections.singleton(AppUserRole.USER.toString());
                }
            }

            @Override
            public Collection<String> getStringPermissions() {
                return Collections.emptyList();
            }

            @Override
            public Collection<Permission> getObjectPermissions() {
                return Collections.emptyList();
            }
        };
    }


}
