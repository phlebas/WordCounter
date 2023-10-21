package uk.co.spirallight.wordcounter.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import uk.co.spirallight.wordcounter.exception.InvalidWordException;
import uk.co.spirallight.wordcounter.repository.WordStore;
import uk.co.spirallight.wordcounter.util.Translator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class SimpleWordCounterTest {
  public static final String VALID_WORD = "validWord";
  private final WordStore wordStore = mock(WordStore.class);

  private final Translator translator = new Translator() {
    @Override
    public String translate(String word) {
      return Translator.super.translate(word);
    }
  };

  private final WordCounter wordCounter = new SimpleWordCounter(wordStore, translator);

  @Test
  @SneakyThrows
  void validWordIsAdded() {
    wordCounter.addWords(VALID_WORD);
    verify(wordStore).addWord(VALID_WORD.toLowerCase());
  }

  @Test
  @SneakyThrows
  void allValidWordsAreAdded() {
    wordCounter.addWords(VALID_WORD, VALID_WORD.toLowerCase(), VALID_WORD.toLowerCase());
    verify(wordStore, times(3)).addWord(VALID_WORD.toLowerCase());
  }

  @Test
  void emptyWordIsNotAdded() {
    assertThatThrownBy(() ->
        wordCounter.addWords(""))
          .isInstanceOf(InvalidWordException.class)
          .hasMessage("The following words are invalid and were not added to the word counter: ['']");
  }

  @Test
  void invalidWordsAreNotAdded() {
    assertThatThrownBy(() ->
        wordCounter.addWords("123", "{abc}", "This!Is!Not!A!Valid!Word"))
          .isInstanceOf(InvalidWordException.class)
          .hasMessage("The following words are invalid and were not added to the word counter: ['{abc}', 'This!Is!Not!A!Valid!Word', '123']");
  }

  @Test
  void validWordsAreAddedAndInvalidWordsAreNot() {
    assertThatThrownBy(() -> wordCounter.addWords(VALID_WORD, VALID_WORD.toLowerCase(), VALID_WORD.toLowerCase(), "123", "{abc}", "This!Is!Not!A!Valid!Word"))
        .isInstanceOf(InvalidWordException.class)
        .hasMessage("The following words are invalid and were not added to the word counter: ['123', '{abc}', 'This!Is!Not!A!Valid!Word']");

    verify(wordStore, times(3)).addWord(VALID_WORD.toLowerCase());
  }
}