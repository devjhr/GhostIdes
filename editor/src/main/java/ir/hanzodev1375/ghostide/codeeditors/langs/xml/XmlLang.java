package ir.hanzodev1375.ghostide.codeeditors.langs.xml;

import android.os.Bundle;
import io.github.rosemoe.sora.lang.EmptyLanguage;
import io.github.rosemoe.sora.text.CharPosition;
import io.github.rosemoe.sora.lang.completion.CompletionPublisher;
import io.github.rosemoe.sora.text.ContentReference;
import io.github.rosemoe.sora.lang.completion.CompletionCancelledException;
import io.github.rosemoe.sora.lang.format.Formatter;
import io.github.rosemoe.sora.lang.analysis.AnalyzeManager;
import io.github.rosemoe.sora.lang.Language;
import io.github.rosemoe.sora.widget.SymbolPairMatch;
import io.github.rosemoe.sora.lang.smartEnter.NewlineHandler;
import ir.hanzodev1375.ghostide.codeeditors.util.CustomFormatter;

public class XmlLang implements Language {
  private CustomFormatter format = new CustomFormatter();
  private XmlAnalyzer an;
  public XmlLang() {
    var xmlformat= new XmlFormatter();
    format.setFormatAction(xmlformat::formatxml);
    an = new XmlAnalyzer();
  }

  @Override
  public AnalyzeManager getAnalyzeManager() {
    return an;
  }

  @Override
  public int getInterruptionLevel() {
    return INTERRUPTION_LEVEL_STRONG;
  }

  @Override
  public void requireAutoComplete(
      ContentReference arg0, CharPosition arg1, CompletionPublisher arg2, Bundle arg3)
      throws CompletionCancelledException {}

  @Override
  public int getIndentAdvance(ContentReference arg0, int arg1, int arg2) {
    return 0;
  }

  @Override
  public boolean useTab() {
    return true;
  }

  @Override
  public Formatter getFormatter() {
    return format;
  }

  @Override
  public SymbolPairMatch getSymbolPairs() {
    return new SymbolPairMatch.DefaultSymbolPairs();
  }

  @Override
  public NewlineHandler[] getNewlineHandlers() {
    return null;
  }

  @Override
  public void destroy() {}
}
