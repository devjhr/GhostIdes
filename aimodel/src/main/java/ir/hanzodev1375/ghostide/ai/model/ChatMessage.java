package ir.hanzodev1375.ghostide.ai.model;

public class ChatMessage {

    public static final int TYPE_USER = 0;

    public static final int TYPE_AI = 1;

    public static final int TYPE_LOADING = 2;

    public static final int TYPE_ERROR = 3;

    private String content;

    private int type;

    private String provider;

    private long timestamp;

    public ChatMessage(String content, int type, String provider) {
        this.content = content;
        this.type = type;
        this.provider = provider;
        this.timestamp = System.currentTimeMillis();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
