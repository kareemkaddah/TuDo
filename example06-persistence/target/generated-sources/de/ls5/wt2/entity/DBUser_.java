package de.ls5.wt2.entity;

import de.ls5.wt2.AppUserRole;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DBUser.class)
public abstract class DBUser_ extends de.ls5.wt2.entity.DBIdentified_ {

	public static volatile SingularAttribute<DBUser, String> firstName;
	public static volatile SingularAttribute<DBUser, String> lastName;
	public static volatile SingularAttribute<DBUser, String> password;
	public static volatile SingularAttribute<DBUser, AppUserRole> role;
	public static volatile SingularAttribute<DBUser, String> username;

	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String PASSWORD = "password";
	public static final String ROLE = "role";
	public static final String USERNAME = "username";

}

