/**
 * 
 */
package org.activiti.designer.integration.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Tijs Rademakers
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TaskNames {

  /**
   * The locales.
   */
  TaskName[] value();
}
