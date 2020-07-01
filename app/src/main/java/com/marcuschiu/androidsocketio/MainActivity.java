package com.marcuschiu.androidsocketio;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    Socket mSocket;
    EditText editText;
    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.edit_text);
        textView = findViewById(R.id.text_view);
        button = findViewById(R.id.button);
        button.setOnClickListener((view) -> {
            createOrJoinRoom();
        });

        try {
            mSocket = IO.socket("http://localhost:8081");
            mSocket.on("created-room", this::onCreatedRoom);
            mSocket.connect();
        } catch (URISyntaxException e) {
            Log.e("MainActivity", "ERROR SOCKET.IO CONNECTION");
        }
    }

    public void onCreatedRoom(Object[] room) {
        String response = (String) room[0];
        runOnUiThread(() -> {
            Toast.makeText(getApplicationContext(), "onCreatedRoom: " + response, Toast.LENGTH_SHORT).show();
            String textBody = textView.getText().toString();
            textBody += "\n" + response;
            textView.setText(textBody);
        });
    }

    private void createOrJoinRoom() {
        String room = editText.getText().toString().trim();
        if (!TextUtils.isEmpty(room)) {
            editText.setText("");
            mSocket.emit("create or join", room);
        }
    }
}