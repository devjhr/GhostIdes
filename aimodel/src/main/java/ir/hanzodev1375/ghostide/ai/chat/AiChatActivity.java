package ir.hanzodev1375.ghostide.ai.chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;
import ir.hanzodev1375.ghostide.ai.R;
import ir.hanzodev1375.ghostide.ai.model.ChatMessage;
import ir.hanzodev1375.ghostide.ai.network.AiClient;
import ir.hanzodev1375.ghostide.ai.network.AiClientFactory;
import ir.hanzodev1375.ghostide.ai.utils.AiConstants;
import ir.hanzodev1375.ghostide.ai.utils.AiPreferencesUtils;

public class AiChatActivity extends AppCompatActivity {

  private RecyclerView recyclerView;
  private TextInputEditText etInput;
  private MaterialButton btnSend;
  private AutoCompleteTextView actvProvider;

  private ChatAdapter adapter;
  private List<ChatMessage> messages = new ArrayList<>();
  private AiPreferencesUtils prefs;
  private AiClient currentClient;

  private static final String[] PROVIDERS = {
    AiConstants.AiProvider.CLAUDE,
    AiConstants.AiProvider.CHATGPT,
    AiConstants.AiProvider.DEEPSEEK,
    AiConstants.AiProvider.GEMINI
  };

  private static final String[] PROVIDER_LABELS = {
    "Claude (Anthropic)", "ChatGPT (OpenAI)", "DeepSeek", "Gemini (Google)"
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    EdgeToEdge.enable(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ai_chat);

    prefs = new AiPreferencesUtils(this);

    Toolbar toolbar = findViewById(R.id.toolbar_ai_chat);
    setSupportActionBar(toolbar);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setTitle("Ghost AI");
    }

    recyclerView = findViewById(R.id.rv_chat);
    etInput = findViewById(R.id.et_input);
    btnSend = findViewById(R.id.btn_send);
    actvProvider = findViewById(R.id.actv_provider);

    setupProviderDropdown();
    setupRecyclerView();
    setupSendButton();
    refreshClient();
  }

  

  private void setupProviderDropdown() {
    ArrayAdapter<String> adapter =
        new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, PROVIDER_LABELS);
    actvProvider.setAdapter(adapter);

    String currentProvider = prefs.getSelectedProvider();
    for (int i = 0; i < PROVIDERS.length; i++) {
      if (PROVIDERS[i].equals(currentProvider)) {
        actvProvider.setText(PROVIDER_LABELS[i], false);
        break;
      }
    }

    actvProvider.setOnItemClickListener(
        (parent, view, position, id) -> {
          String selectedProvider = PROVIDERS[position];
          prefs.setSelectedProvider(selectedProvider);
          if (!prefs.hasApiKeyForProvider(selectedProvider)) {
            Toast.makeText(
                    AiChatActivity.this,
                    "API key not set. Go to Settings → AI Settings.",
                    Toast.LENGTH_LONG)
                .show();
          }
          refreshClient();
        });
  }

  private void setupRecyclerView() {
    adapter = new ChatAdapter(messages);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    layoutManager.setStackFromEnd(true);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);
  }

  private void setupSendButton() {
    btnSend.setOnClickListener(v -> sendMessage());
    etInput.setOnEditorActionListener(
        (v, actionId, event) -> {
          sendMessage();
          return true;
        });
  }

  private void sendMessage() {
    String input = etInput.getText().toString().trim();
    if (TextUtils.isEmpty(input)) return;

    String provider = prefs.getSelectedProvider();
    if (!prefs.hasApiKeyForProvider(provider)) {
      Toast.makeText(this, "Please set the API key in Settings → AI Settings.", Toast.LENGTH_LONG)
          .show();
      return;
    }

    etInput.setText("");
    etInput.setEnabled(false);
    btnSend.setEnabled(false);

    // Add user message
    addMessage(new ChatMessage(input, ChatMessage.TYPE_USER, provider));

    // Add loading indicator
    ChatMessage loadingMsg = new ChatMessage("", ChatMessage.TYPE_LOADING, provider);
    addMessage(loadingMsg);

    currentClient.sendMessage(
        messages,
        input,
        new AiClient.Callback() {
          @Override
          public void onSuccess(String response) {
            removeLastMessage(); // remove loading
            addMessage(new ChatMessage(response, ChatMessage.TYPE_AI, provider));
            enableInput(true);
          }

          @Override
          public void onError(String errorMessage) {
            removeLastMessage(); // remove loading
            addMessage(new ChatMessage("Error: " + errorMessage, ChatMessage.TYPE_ERROR, provider));
            enableInput(true);
          }
        });
  }

  private void enableInput(boolean enable) {
    runOnUiThread(
        () -> {
          etInput.setEnabled(enable);
          btnSend.setEnabled(enable);
          if (enable) etInput.requestFocus();
        });
  }

  private void addMessage(ChatMessage message) {
    runOnUiThread(
        () -> {
          messages.add(message);
          adapter.notifyItemInserted(messages.size() - 1);
          recyclerView.smoothScrollToPosition(messages.size() - 1);
        });
  }

  private void removeLastMessage() {
    runOnUiThread(
        () -> {
          if (!messages.isEmpty()) {
            int lastIndex = messages.size() - 1;
            messages.remove(lastIndex);
            adapter.notifyItemRemoved(lastIndex);
          }
        });
  }

  private void refreshClient() {
    currentClient = AiClientFactory.create(this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_ai_chat, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
      return true;
    } else if (item.getItemId() == R.id.action_clear_chat) {
      messages.clear();
      adapter.notifyDataSetChanged();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
