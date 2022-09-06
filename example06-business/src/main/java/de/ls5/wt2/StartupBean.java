package de.ls5.wt2;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import de.ls5.wt2.entity.DBUser;
import de.ls5.wt2.security.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class StartupBean implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        final DBUser firstAdmin = this.entityManager.find(DBUser.class, 1L);
        if (firstAdmin == null) {
            Optional<String> salt = PasswordUtils.generateSalt(PasswordUtils.RECOMMENDED_SALT_LENGTH);
            while (!salt.isPresent()){
                salt = PasswordUtils.generateSalt(PasswordUtils.RECOMMENDED_SALT_LENGTH);
            }
            Optional<String> hashedPass = PasswordUtils.hashThePlainTextPassword("root33",salt.get());
            while (!hashedPass.isPresent()){
                hashedPass = PasswordUtils.hashThePlainTextPassword("root33",salt.get());
            }
            final DBUser admin = new DBUser("admin",AppUserRole.ADMIN,"AdminFirstName","AdminLastname",
                    salt.get()+":"+hashedPass.get(),"");

            this.entityManager.persist(admin);
        }

    }


}
