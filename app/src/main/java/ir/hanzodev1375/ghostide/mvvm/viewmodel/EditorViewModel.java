package ir.hanzodev1375.ghostide.mvvm.viewmodel;

import android.app.Application;
import android.content.Context;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import io.github.rosemoe.sora.text.Content;
import io.github.rosemoe.sora.text.ContentIO;

public class EditorViewModel extends AndroidViewModel {
  private MutableLiveData<Content> text = new MutableLiveData<>();
  private MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
  private MutableLiveData<String> currentPath = new MutableLiveData<>();

  public EditorViewModel(Application app) {
    super(app);
  }

  public LiveData<Content> getText() {
    return text;
  }

  public LiveData<Boolean> getLoading() {
    return loading;
  }

  public LiveData<String> getCurrentPath() {
    return currentPath;
  }

  public void loadFile(String filePath) {
    loading.setValue(true);
    currentPath.setValue(filePath);

    new Thread(
            () -> {
              try {
                File file = new File(filePath);
                if (!file.exists()) {
                  
                  Content emptyContent = new Content();
                  text.postValue(emptyContent);
                  loading.postValue(false);
                  return;
                }

                try (FileInputStream fis = new FileInputStream(file)) {
                  Content content = ContentIO.createFrom(fis);
                  text.postValue(content);
                }
              } catch (Exception e) {
                e.printStackTrace();
                text.postValue(null);
              } finally {
                loading.postValue(false);
              }
            })
        .start();
  }

  public void saveFile(Content content) {
    String path = currentPath.getValue();
    if (path == null || path.isEmpty()) return;

    new Thread(
            () -> {
              try {
                File file = new File(path);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                  ContentIO.writeTo(content, fos,false);
                }
              } catch (Exception e) {
                e.printStackTrace();
              }
            })
        .start();
  }
}
