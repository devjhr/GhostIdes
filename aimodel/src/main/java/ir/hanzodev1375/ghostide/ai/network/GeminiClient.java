package ir.hanzodev1375.ghostide.ai.network;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import ir.hanzodev1375.ghostide.ai.model.ChatMessage;
import ir.hanzodev1375.ghostide.ai.utils.AiConstants;

public class GeminiClient implements AiClient {

    private static final String TAG = "GeminiClient";

    private final String apiKey;

    private final String model;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public GeminiClient(String apiKey, String model) {
        this.apiKey = apiKey;
        this.model = model;
    }

    @Override
    public void sendMessage(List<ChatMessage> history, String userMessage, Callback callback) {
        executor.execute(() -> {
            try {
                JSONArray contents = new JSONArray();
                for (ChatMessage msg : history) {
                    if (msg.getType() == ChatMessage.TYPE_USER || msg.getType() == ChatMessage.TYPE_AI) {
                        JSONObject part = new JSONObject();
                        part.put("text", msg.getContent());
                        JSONArray parts = new JSONArray();
                        parts.put(part);
                        JSONObject content = new JSONObject();
                        content.put("role", msg.getType() == ChatMessage.TYPE_USER ? "user" : "model");
                        content.put("parts", parts);
                        contents.put(content);
                    }
                }
                // Current user message
                JSONObject userPart = new JSONObject();
                userPart.put("text", userMessage);
                JSONArray userParts = new JSONArray();
                userParts.put(userPart);
                JSONObject userContent = new JSONObject();
                userContent.put("role", "user");
                userContent.put("parts", userParts);
                contents.put(userContent);
                JSONObject body = new JSONObject();
                body.put("contents", contents);
                String endpoint = AiConstants.ApiEndpoints.GEMINI_BASE_URL + model + ":generateContent?key=" + apiKey;
                URL url = new URL(endpoint);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(60000);
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(body.toString().getBytes(StandardCharsets.UTF_8));
                }
                int responseCode = conn.getResponseCode();
                BufferedReader reader;
                if (responseCode == 200) {
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                reader.close();
                if (responseCode == 200) {
                    JSONObject resp = new JSONObject(sb.toString());
                    String text = resp.getJSONArray("candidates").getJSONObject(0).getJSONObject("content").getJSONArray("parts").getJSONObject(0).getString("text");
                    mainHandler.post(() -> callback.onSuccess(text));
                } else {
                    String err = "Error " + responseCode + ": " + sb;
                    mainHandler.post(() -> callback.onError(err));
                }
            } catch (Exception e) {
                Log.e(TAG, "Gemini API error", e);
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    @Override
    public String getProviderName() {
        return AiConstants.AiProvider.GEMINI;
    }
}
