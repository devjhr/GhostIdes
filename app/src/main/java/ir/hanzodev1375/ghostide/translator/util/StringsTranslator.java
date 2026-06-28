package ir.hanzodev1375.ghostide.translator.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONArray;
import ir.hanzodev1375.ghostide.translator.model.AndroidLanguage;
import ir.hanzodev1375.ghostide.translator.model.StringEntry;
import ir.hanzodev1375.ghostide.translator.model.TranslationProgress;

public class StringsTranslator {
  private static final int DELAY_MS = 120;

  public interface Callback {
    void onProgress(TranslationProgress progress);

    void onLanguageDone(String folder);

    void onComplete();

    void onError(String message);
  }

  public void translate(
      File sourceXml,
      String projectResDir,
      List<AndroidLanguage> languages,
      AtomicBoolean cancelled,
      Callback callback) {
    try {
      List<StringEntry> allEntries = StringsXmlParser.parse(sourceXml);
      List<StringEntry> translatableEntries = new ArrayList<>();
      List<StringEntry> nonTranslatableEntries = new ArrayList<>();
      for (StringEntry e : allEntries) {
        if (e.translatable) translatableEntries.add(e);
        else nonTranslatableEntries.add(e);
      }
      int totalSteps = languages.size() * translatableEntries.size();
      int[] step = {0};
      for (AndroidLanguage lang : languages) {
        if (cancelled.get()) return;
        List<StringEntry> translated = new ArrayList<>(nonTranslatableEntries);
        for (StringEntry entry : translatableEntries) {
          if (cancelled.get()) return;
          step[0]++;
          callback.onProgress(
              new TranslationProgress(step[0], totalSteps, entry.name, lang.androidFolder));
          String translatedValue = translateText(entry.value, lang.googleCode);
          translated.add(new StringEntry(entry.name, translatedValue, true));
          Thread.sleep(DELAY_MS);
        }
        File outFile = new File(projectResDir + "/" + lang.androidFolder + "/strings.xml");
        StringsXmlParser.write(outFile, translated);
        callback.onLanguageDone(lang.androidFolder);
      }
      callback.onComplete();
    } catch (InterruptedException ignored) {
    } catch (Exception e) {
      callback.onError(e.getMessage() != null ? e.getMessage() : "Unknown error");
    }
  }

  private String translateText(String text, String targetLang) throws Exception {
    if (text == null || text.trim().isEmpty()) return text;
    String encodedText = URLEncoder.encode(text, "UTF-8");
    String urlStr =
        "https://translate.googleapis.com/translate_a/single"
            + "?client=gtx&sl=en&tl="
            + targetLang
            + "&dt=t&q="
            + encodedText;
    URL url = new URL(urlStr);
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestProperty("User-Agent", "Mozilla/5.0");
    con.setConnectTimeout(10000);
    con.setReadTimeout(10000);
    StringBuilder sb = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
      String line;
      while ((line = reader.readLine()) != null) sb.append(line);
    }
    JSONArray main = new JSONArray(sb.toString());
    JSONArray parts = main.getJSONArray(0);
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < parts.length(); i++) {
      Object chunk = parts.getJSONArray(i).get(0);
      if (chunk != null && !"null".equals(chunk.toString())) result.append(chunk);
    }
    return result.toString();
  }
}
