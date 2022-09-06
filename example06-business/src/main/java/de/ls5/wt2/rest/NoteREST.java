package de.ls5.wt2.rest;



import de.ls5.wt2.AppUserRole;
import de.ls5.wt2.entity.DBNote;
import de.ls5.wt2.entity.DBNote_;
import de.ls5.wt2.entity.DBUser;
import de.ls5.wt2.entity.DBUser_;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;

@Transactional
@RestController
@RequestMapping(path = "rest/notes")
public class NoteREST {

    @Autowired
    private EntityManager entityManager;



    @PostMapping(path = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<DBNote> create(@RequestBody final DBNote param) {
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
            final DBNote note = new DBNote(users.get(0).getId(), param.getContent(), param.getPriority(), new Date(), param.getDeadline());
            this.entityManager.persist(note);
            return new ResponseEntity<>(note,HttpStatus.CREATED);
        }
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DBNote>> readAllAsJSON() {
        final Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        else if (!subject.hasRole(AppUserRole.ADMIN.toString())){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBNote> query = builder.createQuery(DBNote.class);
        final Root<DBNote> from = query.from(DBNote.class);
        query.select(from);
        return ResponseEntity.ok(this.entityManager.createQuery(query).getResultList());
    }


    @GetMapping(path = "me",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DBNote>> readAllMyNotes() {
        final Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String username = (String) subject.getPrincipal();
        List<DBUser> users = getUsersWithUsername(username);
        if (users == null || users.size() == 0) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBNote> query = builder.createQuery(DBNote.class);
        final Root<DBNote> from = query.from(DBNote.class);

        final Predicate predicate = builder.equal(from.get(DBNote_.owner), users.get(0).getId());
        final Order order = builder.asc(from.get(DBNote_.deadline));

        query.select(from).where(predicate).orderBy(order);
        List<DBNote> result = this.entityManager.createQuery(query).getResultList();

        return ResponseEntity.ok(result);
    }
    @PutMapping(path = "update/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DBNote> update(@PathVariable("id") final Long id,
                                         @RequestBody final DBNote param){
        final Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String username = (String) subject.getPrincipal();
        List<DBUser> users = getUsersWithUsername(username);
        if (users == null || users.size() == 0) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        DBNote note = this.entityManager.find(DBNote.class,id);
        if (note == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (note.getOwner() != users.get(0).getId() && !subject.hasRole(AppUserRole.ADMIN.toString())) {
            return  new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        note.setPriority(param.getPriority());
        note.setContent(param.getContent());
        note.setDeadline(param.getDeadline());
        this.entityManager.persist(note);
        return ResponseEntity.ok(note);
    }
    @DeleteMapping(path = "delete/{id}")
    public ResponseEntity<DBNote> delete(@PathVariable("id") final Long id){

        final Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String username = (String) subject.getPrincipal();
        List<DBUser> users = getUsersWithUsername(username);
        if (users == null || users.size() == 0) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        DBNote note = this.entityManager.find(DBNote.class,id);
        if (note == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (note.getOwner() != users.get(0).getId() && !subject.hasRole(AppUserRole.ADMIN.toString())) {
            return  new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        int isSuccessful = this.entityManager.createQuery("DELETE FROM DBNote n WHERE n.id = :id ")
                .setParameter("id",id)
                .executeUpdate();
        if (isSuccessful == 0){
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }else {
            return ResponseEntity.ok(note);
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
