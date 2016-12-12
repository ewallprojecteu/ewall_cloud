package eu.ewall.platform.idss.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation can be added to a class to indicate that it can be defined
 * as an application-wide component in {@link AppComponents AppComponents}.
 * 
 * @author Dennis Hofs (RRD)
 */
@Retention(value=RetentionPolicy.SOURCE)
@Documented
public @interface AppComponent {
}
