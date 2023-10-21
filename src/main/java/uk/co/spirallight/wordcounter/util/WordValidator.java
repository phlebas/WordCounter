package uk.co.spirallight.wordcounter.util;

import org.apache.commons.lang3.StringUtils;

public interface WordValidator {

  static boolean isWordValid(String word) {
    return StringUtils.isAlpha(word);
  }

}
