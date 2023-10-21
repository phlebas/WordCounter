package uk.co.spirallight.wordcounter.service;

import uk.co.spirallight.wordcounter.exception.InvalidWordException;

public interface WordCounter {
  void addWords(String word, String... additionalWords) throws InvalidWordException;
  Integer getWordCount(String word);
}
