package com.king.chatview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.king.chatview.layout.KeyboardDetectorRelativeLayout;
import com.king.chatview.widgets.ChatToolBox;
import com.king.chatview.widgets.InputChat;
import com.king.chatview.widgets.NewInputChat;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String INPUT_CHAT_TAG = "input_chat";
//    InputChat inputChat;
    NewInputChat inputChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_view_new);
//        inputChat = new InputChat();

        inputChat = new NewInputChat();

        inputChat.addToolBoxData(new ToolAd());

        getSupportFragmentManager().beginTransaction().replace(R.id.input_container, inputChat, INPUT_CHAT_TAG).commit();
        inputChat.setInputChatListener(new NewInputChat.InputChatListener() {
            @Override
            public void onSendMessage(String msg) {
                Log.d("hehe", "sendMessage:" + msg);
            }

            @Override
            public void onSendVoiceMessage(String path, int duration, String description) {
                Log.d("hehe", "SendVoiceMessage:" + "path:" + path + " duration:" + duration + " description:" + description);
            }

            @Override
            public void onPickPhotoMessage(Intent data) {
                Log.d("hehe", "PickPhotoMessage:" + data.toString());
            }

            @Override
            public void onTakePhotoMessage(Intent data) {
                Log.d("hehe", "TakePhotoMessage:" + data.toString());
            }

            @Override
            public void onPickLocMessage(Intent data) {
                Log.d("hehe", "PickLocMessage:" + data.toString());
            }
        });

        KeyboardDetectorRelativeLayout root = (KeyboardDetectorRelativeLayout) findViewById(R.id.chat_root);
        root.setOnSoftKeyboardListener(new KeyboardDetectorRelativeLayout.OnSoftKeyboardListener() {
            @Override
            public void onShown(int keyboardHeight) {
                inputChat.onKeyboardShow(keyboardHeight);
            }

            @Override
            public void onHidden() {
                inputChat.onKeyboardDismiss();
            }

            @Override
            public void onMeasureFinished() {
//                if (inputChat.isBoxShow()) {
//                } else {
//                }
            }
        });
    }

    private class ToolAd implements ChatToolBox.ChatToolItem {
        @Override
        public int getIcon() {
            return R.drawable.app_panel_ads_selector;
        }

        @Override
        public String getName() {
            return "信息";
        }

        @Override
        public void onItemSelected() {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("请选择")
                    .setItems(new String[]{"已发布信息", "收藏"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }
    }
}
