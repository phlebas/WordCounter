package uk.co.spirallight.wordcounter.service;

import uk.co.spirallight.wordcounter.exception.InvalidWordException;

import java.util.List;
import java.util.Map;

public interface WordCounter {
  Map<Boolean, List<String>> addWords(String word, String... additionalWords) throws InvalidWordException;
  Integer getWordCount(String word);
}
