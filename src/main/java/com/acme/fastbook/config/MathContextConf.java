package com.acme.fastbook.config;

import java.math.MathContext;
import java.math.RoundingMode;

import lombok.experimental.UtilityClass;

/**
 * Class holds {@link MathContext} objects used in the application
 * 
 * @author Mykhaylo Symulyk
 *
 */
@UtilityClass
public class MathContextConf {
  
  /** MathContext: precision - 2, rounding - down */
  public static final MathContext TWO_DOWN = new MathContext(2, RoundingMode.DOWN);

}
