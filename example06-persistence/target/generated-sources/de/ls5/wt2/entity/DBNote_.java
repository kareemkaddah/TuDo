package de.ls5.wt2.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DBNote.class)
public abstract class DBNote_ extends de.ls5.wt2.entity.DBIdentified_ {

	public static volatile SingularAttribute<DBNote, Long> owner;
	public static volatile SingularAttribute<DBNote, Integer> priority;
	public static volatile SingularAttribute<DBNote, Date> deadline;
	public static volatile SingularAttribute<DBNote, Date> createdOn;
	public static volatile SingularAttribute<DBNote, String> content;

	public static final String OWNER = "owner";
	public static final String PRIORITY = "priority";
	public static final String DEADLINE = "deadline";
	public static final String CREATED_ON = "createdOn";
	public static final String CONTENT = "content";

}

