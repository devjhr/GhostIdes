package ir.hanzodev1375.ghostide.ai.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import java.util.List;
import ir.hanzodev1375.ghostide.ai.R;
import ir.hanzodev1375.ghostide.ai.model.ChatMessage;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final List<ChatMessage> messages;

  public ChatAdapter(List<ChatMessage> messages) {
    this.messages = messages;
  }

  @Override
  public int getItemViewType(int position) {
    return messages.get(position).getType();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    switch (viewType) {
      case ChatMessage.TYPE_USER:
        return new UserViewHolder(inflater.inflate(R.layout.item_chat_user, parent, false));
      case ChatMessage.TYPE_AI:
        return new AiViewHolder(inflater.inflate(R.layout.item_chat_ai, parent, false));
      case ChatMessage.TYPE_LOADING:
        return new LoadingViewHolder(inflater.inflate(R.layout.item_chat_loading, parent, false));
      default:
        return new ErrorViewHolder(inflater.inflate(R.layout.item_chat_error, parent, false));
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    ChatMessage msg = messages.get(position);
    if (holder instanceof UserViewHolder) {
      ((UserViewHolder) holder).tvMessage.setText(msg.getContent());
    } else if (holder instanceof AiViewHolder) {
      ((AiViewHolder) holder).tvMessage.setText(msg.getContent());
      ((AiViewHolder) holder).tvProvider.setText(msg.getProvider().toUpperCase());
    } else if (holder instanceof ErrorViewHolder) {
      ((ErrorViewHolder) holder).tvError.setText(msg.getContent());
    }
    // LoadingViewHolder نیازی به bind ندارد، ProgressBar به صورت خودکار چرخش دارد
  }

  @Override
  public int getItemCount() {
    return messages.size();
  }

  static class UserViewHolder extends RecyclerView.ViewHolder {
    TextView tvMessage;

    UserViewHolder(@NonNull View itemView) {
      super(itemView);
      tvMessage = itemView.findViewById(R.id.tv_message_user);
    }
  }

  static class AiViewHolder extends RecyclerView.ViewHolder {
    TextView tvMessage;
    TextView tvProvider;

    AiViewHolder(@NonNull View itemView) {
      super(itemView);
      tvMessage = itemView.findViewById(R.id.tv_message_ai);
      tvProvider = itemView.findViewById(R.id.tv_provider_label);
    }
  }

  static class LoadingViewHolder extends RecyclerView.ViewHolder {
    CircularProgressIndicator progressBar;

    LoadingViewHolder(@NonNull View itemView) {
      super(itemView);
      progressBar = itemView.findViewById(R.id.progress_loading);
    }
  }

  static class ErrorViewHolder extends RecyclerView.ViewHolder {
    TextView tvError;

    ErrorViewHolder(@NonNull View itemView) {
      super(itemView);
      tvError = itemView.findViewById(R.id.tv_error);
    }
  }
}
