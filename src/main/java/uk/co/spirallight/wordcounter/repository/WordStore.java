package uk.co.spirallight.wordcounter.repository;

public interface WordStore {
  void addWord(String word);
  Integer getWordCount(String word);
}
