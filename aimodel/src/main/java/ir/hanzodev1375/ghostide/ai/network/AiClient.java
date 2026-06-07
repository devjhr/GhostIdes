package ir.hanzodev1375.ghostide.ai.network;

import java.util.List;
import ir.hanzodev1375.ghostide.ai.model.ChatMessage;

public interface AiClient {

    interface Callback {

        void onSuccess(String response);

        void onError(String errorMessage);
    }

    void sendMessage(List<ChatMessage> history, String userMessage, Callback callback);

    String getProviderName();
}
