package uk.co.spirallight.wordcounter.service;

import uk.co.spirallight.wordcounter.exception.InvalidWordException;

import java.util.List;

public interface WordCounter {
  List<String> addWords(String word, String... additionalWords) throws InvalidWordException;
  Integer getWordCount(String word);
}
