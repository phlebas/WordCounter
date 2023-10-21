package uk.co.spirallight.wordcounter.util;

public interface Translator {

  default String translate(String word) {
      return word.toLowerCase();
  }

}
