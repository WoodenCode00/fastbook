package com.acme.fastbook.utils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.acme.fastbook.exception.ProcessingException;

import lombok.experimental.UtilityClass;

/**
 * Utility class used for copying
 * 
 * @author Mykhaylo Symulyk
 *
 */
@UtilityClass
public class CopyUtils {

  /**
   * Copies properties from source object to target object for non-null values of
   * source object. Method leverages Spring's
   * {@link BeanUtils#copyProperties(Object, Object, String...)} method.
   * <p>
   * <p>
   * Note, this method will update {@code target} input parameter.
   * 
   * @param source source object
   * @param target target object, that will be updated
   * 
   * @throws ProcessingException if failed to copy properties
   */
  public static void copyNonNullProperties(Object source, Object target) {
    try {
      String[] ignoreProperties = getPropertyNamesWithNullValues(source);
      BeanUtils.copyProperties(source, target, ignoreProperties);
    } catch (IntrospectionException | ProcessingException ex) {
      throw new ProcessingException("Failed to copy properties from source object to target object.", ex);
    }
  }

  /**
   * Gets an array of property names of {@code source} parameter which have NULL
   * values
   * 
   * @param source
   * @return
   * @throws IntrospectionException
   */
  private static String[] getPropertyNamesWithNullValues(final Object source) throws IntrospectionException {

    PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(source.getClass(), Object.class)
        .getPropertyDescriptors();

    // Since methods in lambda throw checked exceptions, we use a custom
    // ThrowingPredicate to convert them to RuntimeExceptions
    ThrowingPredicate<PropertyDescriptor> isPropertyNullPredicate = propertyDescriptor -> (propertyDescriptor
        .getReadMethod() != null && propertyDescriptor.getReadMethod().invoke(source) == null);

    List<String> nullProperties = Arrays.stream(propertyDescriptors).filter(isPropertyNullPredicate)
        .map(PropertyDescriptor::getName).collect(Collectors.toList());

    return nullProperties.toArray(new String[nullProperties.size()]);
  }

}
