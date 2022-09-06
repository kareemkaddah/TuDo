package de.ls5.wt2.aal;

import de.ls5.wt2.authentication.TokenBlockList;
import de.ls5.wt2.entity.DBNote;
import de.ls5.wt2.entity.DBUser;
import de.ls5.wt2.security.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Transactional
@RestController
@RequestMapping(path = "rest")
public class LearningRest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private TokenBlockList blockList;

    @GetMapping(path = "reset",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void reset() {
        final CriteriaBuilder builder1 = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBUser> query1 = builder1.createQuery(DBUser.class);
        final Root<DBUser> from1 = query1.from(DBUser.class);
        query1.select(from1);

        final List<DBUser> result = this.entityManager.createQuery(query1).getResultList();
        for (DBUser res : result) {
            if (res.getId() == 1L ){
                Optional<String> salt = PasswordUtils.generateSalt(PasswordUtils.RECOMMENDED_SALT_LENGTH);
                while (!salt.isPresent()){
                    salt = PasswordUtils.generateSalt(PasswordUtils.RECOMMENDED_SALT_LENGTH);
                }
                Optional<String> hashedPass = PasswordUtils.hashThePlainTextPassword("root33",salt.get());
                while (!hashedPass.isPresent()){
                    hashedPass = PasswordUtils.hashThePlainTextPassword("root33",salt.get());
                }
                res.setFirstName("AdminFirstName");
                res.setLastName("AdminLastname");
                res.setPassword(salt.get()+":"+hashedPass.get());
                continue;
            }
            entityManager.remove(res);
        }
        final CriteriaBuilder builder2 = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBNote> query2 = builder2.createQuery(DBNote.class);
        final Root<DBNote> from2 = query2.from(DBNote.class);
        query2.select(from2);
        final List<DBNote> notes = this.entityManager.createQuery(query2).getResultList();
        for (DBNote n : notes){
            entityManager.remove(n);
        }
        blockList.clear();
    }
}
