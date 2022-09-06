package de.ls5.wt2.rest;


import com.nimbusds.jose.JOSEException;
import de.ls5.wt2.authentication.JWTLoginData;
import de.ls5.wt2.authentication.JWTUtil;
import de.ls5.wt2.authentication.TokenBlockList;
import de.ls5.wt2.entity.DBUser_;
import de.ls5.wt2.security.PasswordUtils;
import de.ls5.wt2.entity.DBUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Transactional
@RestController
@RequestMapping(path = "rest/authentication")
public class JWTRest {

    @Autowired
    private EntityManager entityManager;
    @Autowired

    private TokenBlockList tokenBlockList;

    private final static String AUTHORIZATION = "Authorization";
    private int status = 0;
    private final int AUTHORIZED_USER = 0;
    private final int USER_NOT_FOUND = 1;
    private final int USER_AND_PASSWORD_DONT_MATCH = 2;

    private final int PASSWORD_IS_NOT_STORED_CORRECTLY = 3;



    @PostMapping(path = "/authenticate",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DBUser> createJWToken(@RequestBody JWTLoginData credentials) throws JOSEException {

        // do some proper lookup
        final String user = credentials.getUsername();
        final String pwd = credentials.getPassword();

        DBUser dbUser = checkUsernameAndPassword(user,pwd);
        if (status != this.AUTHORIZED_USER || dbUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        dbUser.setToken(JWTUtil.createJWToken(credentials));
        return ResponseEntity.ok(dbUser);
    }

    @PostMapping(path = "/logout",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> logout(@RequestHeader(JWTRest.AUTHORIZATION) final String token){

        final Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated() || token == null|| !token.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        else {
            tokenBlockList.add(token.split(" ")[1]);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    private DBUser checkUsernameAndPassword(String username, String password){
        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBUser> query = builder.createQuery(DBUser.class);
        final Root<DBUser> from = query.from(DBUser.class);
        final Predicate predicate = builder.equal(from.get(DBUser_.username), username);

        query.select(from).where(predicate);
        List<DBUser> users = this.entityManager.createQuery(query).getResultList();

        if (users == null || users.size() == 0) {
            status = this.USER_NOT_FOUND;
            return null;
        }
        String [] hashed =users.get(0).getPassword().split(":", 2);
        if (hashed.length == 2){
            if(PasswordUtils.verifyThePlainTextPassword(password,hashed[1],hashed[0])){
                status = this.AUTHORIZED_USER;
                return users.get(0);
            }
            else {
                status = this.USER_AND_PASSWORD_DONT_MATCH;
                return null;
            }
        }else{
            status = this.PASSWORD_IS_NOT_STORED_CORRECTLY;
            return null;
        }

    }


}
