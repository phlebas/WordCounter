package uk.co.spirallight.wordcounter.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.spirallight.wordcounter.exception.InvalidWordException;
import uk.co.spirallight.wordcounter.repository.ConcurrentWordStore;
import uk.co.spirallight.wordcounter.repository.WordStore;
import uk.co.spirallight.wordcounter.util.Translator;
import uk.co.spirallight.wordcounter.util.WordValidator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.partitioningBy;

@AllArgsConstructor
@Slf4j
public class SimpleWordCounter implements WordCounter {
  private final WordStore wordStore;
  private Translator translator;

  public SimpleWordCounter() {
    this.wordStore = new ConcurrentWordStore();
    this.translator = new SimpleTranslator();
  }

  public SimpleWordCounter(WordStore wordStore) {
    this.wordStore = wordStore;
    this.translator = new SimpleTranslator();
  }

  @Override
  public List<String> addWords(String word, String... additionalWords) throws InvalidWordException {
    List<String> words = getWordList(word, additionalWords);
    Map<Boolean, List<String>> wordLists =
        words.stream().collect(partitioningBy(WordValidator::isWordValid));

    List<String> validWords = wordLists.get(true);
    validWords.forEach(wordToAdd -> wordStore.addWord(translator.translate(wordToAdd)));

    List<String> inValidWords = wordLists.get(false);
    if (!inValidWords.isEmpty()) {
      String message = "The following words are invalid and were not added to the word counter: " + inValidWords.stream().collect(joining("', '", "['", "']"));
      log.warn(message);
      throw new InvalidWordException(message);
    }
    return wordLists.get(true);
  }

  private List<String> getWordList(String word, String[] additionalWords) {
    if (additionalWords.length == 0) {
      return List.of(word);
    } else {
      List<String> result = new java.util.ArrayList<>(Arrays.stream(additionalWords).toList());
      result.add(word);
      return result;
    }
  }

  @Override
  public Integer getWordCount(String word) {
    return wordStore.getWordCount(translator.translate(word));
  }

  private static class SimpleTranslator implements Translator {

  }
}
