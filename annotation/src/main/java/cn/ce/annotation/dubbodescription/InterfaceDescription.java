package cn.ce.annotation.dubbodescription;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ FIELD, METHOD })
public @interface InterfaceDescription {

	public abstract String name();

	public abstract String des();

	public abstract String version();
}
