package uk.co.spirallight.wordcounter.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import uk.co.spirallight.wordcounter.exception.InvalidWordException;
import uk.co.spirallight.wordcounter.repository.ConcurrentWordStore;
import uk.co.spirallight.wordcounter.repository.WordStore;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class WordCounterTest {
  public static final String NOT_VALID_WORD = "NotValidWord!";
  public static final String VALID_WORD = "ValidWord";
  private WordStore wordStore;
  private WordCounter wordCounter;

  @BeforeEach
  void setup() {
    wordStore = new ConcurrentWordStore();
    wordCounter = new SimpleWordCounter(wordStore);
  }

  @Test
  void thatEmptyStoreReturnsZeroForValidWord() {
    assertThat(wordCounter.getWordCount(VALID_WORD)).isZero();
  }

  @Test
  void thatEmptyStoreReturnsZeroForInvalidWord() {
    assertThat(wordStore.getWordCount(NOT_VALID_WORD)).isZero();
    assertThatThrownBy(() ->
        wordCounter.addWords(NOT_VALID_WORD))
        .isInstanceOf(InvalidWordException.class);
    assertThat(wordCounter.getWordCount(NOT_VALID_WORD)).isZero();
  }

  @Test
  @SneakyThrows
  void thatCountIsCorrectAfterAddingValidWords() {
    wordCounter.addWords(VALID_WORD, VALID_WORD.toLowerCase(), VALID_WORD.toUpperCase());
    assertThat(wordCounter.getWordCount(VALID_WORD.toLowerCase())).isEqualTo(3);
  }

  @Test
  @SneakyThrows
  void thatCountIsCorrectAfterAddingValidAndInvalidWords() {
    assertThatThrownBy(() ->
        wordCounter.addWords(NOT_VALID_WORD, VALID_WORD, VALID_WORD.toLowerCase(), VALID_WORD.toUpperCase()))
        .isInstanceOf(InvalidWordException.class);
    assertThat(wordCounter.getWordCount(VALID_WORD.toLowerCase())).isEqualTo(3);
  }
}
