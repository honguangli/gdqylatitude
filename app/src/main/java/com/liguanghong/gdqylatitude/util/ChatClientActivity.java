package com.liguanghong.gdqylatitude.util;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.liguanghong.gdqylatitude.R;

import org.java_websocket.WebSocketImpl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author wlf(Andy)
 * @datetime 2016-02-16 09:28 GMT+8
 * @email 411086563@qq.com
 */
public class ChatClientActivity extends AppCompatActivity implements OnClickListener {

    private ScrollView svChat;
    private Spinner spDraft;
    private EditText etAddress;
    private Spinner spAddress;
    private Button btnConnect;
    private Button btnClose;
    private EditText etDetails;

    private EditText etName;
    private EditText etMessage;
    private Button btnSend;
    private WebSocketClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_client);
        initView();

        initData();

    }

    private void initView(){
        svChat = (ScrollView) findViewById(R.id.svChat);
        spDraft = (Spinner) findViewById(R.id.spDraft);
        etAddress = (EditText) findViewById(R.id.etAddress);
        spAddress = (Spinner) findViewById(R.id.spAddress);
        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnClose = (Button) findViewById(R.id.btnClose);
        etDetails = (EditText) findViewById(R.id.etDetails);

        etName = (EditText) findViewById(R.id.etName);
        etMessage = (EditText) findViewById(R.id.etMessage);
        btnSend = (Button) findViewById(R.id.btnSend);


        btnConnect.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        btnSend.setOnClickListener(this);
    }

    private void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnConnect:
                String address = "ws://172.17.144.57:8080/GdqyLatitude/websocket/4";
                try{
                    client = new WebSocketClient(new URI(address)) {
                        @Override
                        public void onOpen(ServerHandshake handshakedata) {
                            Log.i("websocket", "open");
                        }

                        @Override
                        public void onMessage(String message) {
                            Log.i("websocket", "message" + "["+message+"]");
                        }

                        @Override
                        public void onClose(int code, String reason, boolean remote) {
                            Log.i("websocket", "close");
                        }

                        @Override
                        public void onError(Exception ex) {
                            Log.i("websocket", "error");
                        }
                    };
                    client.connect();
                } catch (URISyntaxException e){
                    e.printStackTrace();
                }
                break;
            case R.id.btnClose:
                client.close();
                break;
            case R.id.btnSend:
                client.send("你好");
                break;
        }
    }
}