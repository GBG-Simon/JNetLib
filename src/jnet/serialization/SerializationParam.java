package jnet.serialization;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;

@Documented
@Retention(RUNTIME)
@Repeatable(Serializable.class)
public @interface SerializationParam
{
	String key();
	
	String value();
}