package uk.co.spirallight.wordcounter.repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConcurrentWordStore implements WordStore {

  private final ConcurrentMap<String, Integer> storage = new ConcurrentHashMap<>();

  @Override
  public void addWord(String word) {
    if (storage.containsKey(word)) {
      storage.put(word, storage.get(word) + 1);
    } else {
      storage.put(word, 1);
    }
  }

  @Override
  public Integer getWordCount(String word) {
    return storage.getOrDefault(word, 0);
  }
}
