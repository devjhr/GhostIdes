package ir.hanzodev1375.components.ftp.views;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import ir.hanzodev1375.components.ftp.impl.FtpClientImpl;
import ir.hanzodev1375.components.ftp.impl.SftpClientImpl;
import ir.hanzodev1375.components.ftp.interfaces.RemoteClient;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ir.hanzodev1375.components.R;

public class FtpConnectSheet extends BottomSheetDialogFragment {

  public static final String TAG = "FtpConnectSheet";

  private TextInputEditText etHost, etPort, etUsername, etPassword;
  private TextInputLayout tilHost, tilPort, tilUsername, tilPassword;
  private MaterialButtonToggleGroup toggleProtocol;
  private MaterialButton btnConnect;
  private MaterialButton btnFtp, btnSftp;

  private final ExecutorService executor = Executors.newSingleThreadExecutor();

  public interface OnConnectedListener {
    void onConnected(RemoteClient client, String host, boolean isSftp);
  }

  private OnConnectedListener listener;

  public void setOnConnectedListener(OnConnectedListener l) {
    listener = l;
  }

  public static FtpConnectSheet newInstance() {
    return new FtpConnectSheet();
  }

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.bottom_sheet_ftp_connect, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    etHost = view.findViewById(R.id.etHost);
    etPort = view.findViewById(R.id.etPort);
    etUsername = view.findViewById(R.id.etUsername);
    etPassword = view.findViewById(R.id.etPassword);
    tilHost = view.findViewById(R.id.tilHost);
    tilPort = view.findViewById(R.id.tilPort);
    tilUsername = view.findViewById(R.id.tilUsername);
    tilPassword = view.findViewById(R.id.tilPassword);
    toggleProtocol = view.findViewById(R.id.toggleProtocol);
    btnConnect = view.findViewById(R.id.btnConnect);
    btnFtp = view.findViewById(R.id.btnFtp);
    btnSftp = view.findViewById(R.id.btnSftp);
    toggleProtocol.check(R.id.btnFtp);
    toggleProtocol.addOnButtonCheckedListener(
        (group, checkedId, isChecked) -> {
          if (!isChecked) return;
          if (checkedId == R.id.btnSftp) {
            etPort.setText("22");
          } else {
            etPort.setText("21");
          }
        });

    btnConnect.setOnClickListener(v -> onConnectClick());
  }

  private void onConnectClick() {
    String host = getText(etHost);
    String portStr = getText(etPort);
    String user = getText(etUsername);
    String password = getText(etPassword);

    // validation
    if (TextUtils.isEmpty(host)) {
      tilHost.setError(getString(R.string.ftp_error_host));
      return;
    }
    tilHost.setError(null);

    int port;
    try {
      port = Integer.parseInt(portStr);
    } catch (NumberFormatException e) {
      tilPort.setError(getString(R.string.ftp_error_port));
      return;
    }
    tilPort.setError(null);

    boolean isSftp = toggleProtocol.getCheckedButtonId() == R.id.btnSftp;

    btnConnect.setEnabled(false);
    btnConnect.setText(R.string.ftp_connecting);

    final String finalHost = host;
    final int finalPort = port;
    final String finalUser = user;
    final String finalPassword = password;

    executor.execute(
        () -> {
          RemoteClient client = isSftp ? new SftpClientImpl() : new FtpClientImpl();
          try {
            client.connect(finalHost, finalPort, finalUser, finalPassword);
            if (getActivity() == null) return;
            getActivity()
                .runOnUiThread(
                    () -> {
                      if (!isAdded()) return;
                      dismiss();
                      if (listener != null) listener.onConnected(client, finalHost, isSftp);
                    });
          } catch (Exception e) {
            if (getActivity() == null) return;
            getActivity()
                .runOnUiThread(
                    () -> {
                      if (!isAdded()) return;
                      btnConnect.setEnabled(true);
                      btnConnect.setText(R.string.ftp_btn_connect);
                      Toast.makeText(
                              requireContext(),
                              getString(R.string.ftp_error_connect, e.getMessage()),
                              Toast.LENGTH_LONG)
                          .show();
                    });
          }
        });
  }

  private String getText(TextInputEditText et) {
    return et.getText() != null ? et.getText().toString().trim() : "";
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    executor.shutdownNow();
  }

  public void show(FragmentManager manager) {
    show(manager, TAG);
  }
}
