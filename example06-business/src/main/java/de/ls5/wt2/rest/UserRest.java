package de.ls5.wt2.rest;

import de.ls5.wt2.AppUserRole;
import de.ls5.wt2.entity.DBUser;
import de.ls5.wt2.entity.DBUser_;
import de.ls5.wt2.security.PasswordUtils;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@RequestMapping(path = "rest/users")
public class UserRest {
    @Autowired
    private EntityManager entityManager;


    @PostMapping(path = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DBUser> create(@RequestBody final DBUser param) {

        if (this.isUsedUsername(param.getUsername())){
            return new ResponseEntity<>(null,HttpStatus.CONFLICT);
        }
        try {
            String hash = PasswordUtils.getHashedPassword(param.getPassword(),PasswordUtils.RECOMMENDED_SALT_LENGTH);
            final DBUser user = new DBUser(param.getUsername(), AppUserRole.USER,param.getFirstName(),param.getLastName(),
                    hash, "");

            this.entityManager.persist(user);

            return new ResponseEntity<>(user, HttpStatus.CREATED);

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<DBUser> getUserById(@PathVariable("id") final Long id){
        final Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (!subject.hasRole(AppUserRole.ADMIN.toString())){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(this.entityManager.find(DBUser.class,id),HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DBUser>> readAllAsJSON() {

        final Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        // just the Admin is allowed to get all information about Users
        else if (!subject.hasRole(AppUserRole.ADMIN.toString())){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBUser> query = builder.createQuery(DBUser.class);

        final Root<DBUser> from = query.from(DBUser.class);

        query.select(from);

        return new ResponseEntity<>(this.entityManager.createQuery(query).getResultList(), HttpStatus.OK);
    }

    @PutMapping(
            path = "update",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<DBUser> editUser(@RequestBody final DBUser param){
        final Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String username = (String) subject.getPrincipal();
        List<DBUser> users = getUsersWithUsername(username);
        if (users == null || users.size() == 0) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        else {
            if (param.getPassword() != null) {

                try {
                    users.get(0).setPassword(PasswordUtils.getHashedPassword(param.getPassword(),PasswordUtils.RECOMMENDED_SALT_LENGTH));
                }catch (Exception e){
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            if (param.getLastName() != null) {
                users.get(0).setLastName(param.getLastName());
            }
            if (param.getFirstName() != null) {
                users.get(0).setFirstName(param.getFirstName());
            }
            entityManager.persist(users.get(0));
            return ResponseEntity.ok(users.get(0));
        }
    }

    @GetMapping(path = "/me",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<DBUser>readUser() {
        final Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String username = (String) subject.getPrincipal();
        List<DBUser> users = getUsersWithUsername(username);
        if (users == null || users.size() == 0) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(users.get(0),HttpStatus.OK);
    }

    private boolean isUsedUsername(String username){
        List<DBUser> users = getUsersWithUsername(username);
        if (users == null || users.size() == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    private List<DBUser> getUsersWithUsername(String username){
        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBUser> query = builder.createQuery(DBUser.class);
        final Root<DBUser> from = query.from(DBUser.class);
        final Predicate predicate = builder.equal(from.get(DBUser_.username), username);

        query.select(from).where(predicate);
        List<DBUser> result = this.entityManager.createQuery(query).getResultList();
        return result;
    }

}
