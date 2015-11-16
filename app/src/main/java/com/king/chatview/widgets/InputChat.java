package com.king.chatview.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.king.chatview.R;
import com.king.chatview.fragment.BaseFragment;
import com.king.chatview.tools.DImenUtil;
import com.king.chatview.utils.BundleArguments;
import com.king.chatview.utils.ResourceUtil;
import com.king.chatview.widgets.emotion.EmotionView;
import com.king.chatview.widgets.emotion.data.CustomEmoji;
import com.king.chatview.widgets.emotion.data.Emoji;
import com.king.chatview.widgets.emotion.data.Emoticon;
import com.king.chatview.widgets.emotion.data.EmotionData;
import com.king.chatview.widgets.emotion.data.UniqueEmoji;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/11.
 */
public class InputChat extends BaseFragment {
    private static final int REQUEST_CODE_TAKE_PHOTO = 1;
    private static final int REQUEST_CODE_PICK_PHOTO = 2;
    private static final int REQUEST_CODE_CHOOSE_LOCATION = 3;

    protected LinearLayout rootView;
    protected LinearLayout buttonView;
    private OnlyView onlyView4;
    private EditText editText;
    private OnlyView onlyView3;
    private Button sendBtn;
    private Button send2toolBtn;
    private Button send2toolBtnOn;
    private OnlyView onlyView1;
    private Button voiceBtn;
    private Button voice2chatBtn;
    private OnlyView onlyView2;
    private Button emojiBtn;
    private Button emoji2chatBtn;
    protected OnlyView boxView;
    protected ChatToolBox toolBox;
    protected VoicePress voicePress;
    protected View voiceView;

    private EmotionView emotionView;

    protected InputChatListener inputChatListener = null;
    private List<ChatToolBox.ChatToolItem> items = new ArrayList<ChatToolBox.ChatToolItem>();

    private boolean keyBoardShow = false;

    private int boxViewHeight;

    public void clearInput() {
        editText.setText("");
    }

    public interface InputChatListener {
        void onSendMessage(String msg);

        void onSendVoiceMessage(String path, int duration, String description);

        void onPickPhotoMessage(Intent data);

        void onTakePhotoMessage(Intent data);

        void onPickLocMessage(Intent data);
    }

    //this will be called at first, so we must make initial data there to avoid initial data being reset. !!!
    public InputChat() {
        inputChatListener = null;
        items = new ArrayList<>();
        items.add(new ToolPhoto());
        items.add(new ToolCamera());
        items.add(new ToolPosition());
    }

    public void setInputChatListener(InputChatListener listener) {
        this.inputChatListener = listener;
    }

    private int getBoxViewHeight() {
        if (0 == boxViewHeight) {
            // 保存键盘高度，隐藏toolbox
            SharedPreferences pref = getActivity().getSharedPreferences("sys_variable", Context.MODE_PRIVATE);
            boxViewHeight = pref.getInt("virtual_keyboard_height", 0);
            if (0 == boxViewHeight) {
                if (null != getActivity()) {
                    boxViewHeight = DImenUtil.dip2px(getActivity(), 230);
                } else {
                    boxViewHeight = 2 * 230;
                }
            }
        }
        return boxViewHeight;
    }

    //this will be called when fragment called to show the view, this will be called after creatView().
    @Override
    protected View onInitializeView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (LinearLayout) inflater.inflate(R.layout.inputchat_new, container, false);
        buttonView = (LinearLayout) rootView.findViewById(R.id.chat_button);
        toolBox = (ChatToolBox) rootView.findViewById(R.id.chat_tool_box);
        boxView = (OnlyView) rootView.findViewById(R.id.box_view);

        // EmotionView 开始入口位置
        emotionView = (EmotionView) rootView.findViewById(R.id.emotion_view);
        // 1 构建需要显示的表情结构
        List<EmotionData> emotionList = new ArrayList<>();
        // 1.1 emoji emotion
        TypedArray icons = getContext().getResources().obtainTypedArray(R.array.emotion_array);
        List<Emoticon> emojiList = new ArrayList<>();
        int[] intStringArray = getContext().getResources().getIntArray(R.array.emotion_dec_int);
        for (int i = 0; i < icons.length(); ++i) {
            Emoticon emoji = new Emoji(icons.getResourceId(i, 0), intStringArray[i]);
            emojiList.add(emoji);
        }
        EmotionData data = new EmotionData(emojiList,
                ResourceUtil.getResourceUriString(getContext(), R.drawable.u1f004),
                EmotionData.EmotionCategory.emoji,
                new UniqueEmoji(R.drawable.bx_emotion_delete), 3, 7);
//                3, 7);
        emotionList.add(data);
        // 1.2 custom emotion
        String temp = ResourceUtil.getResourceUriString(getContext(), R.mipmap.ic_launcher);
        List<Emoticon> customList = new ArrayList<>();
        customList.add(new CustomEmoji(temp));
        customList.add(new CustomEmoji(temp));
        customList.add(new CustomEmoji(temp));

        data = new EmotionData(customList,
                ResourceUtil.getResourceUriString(getContext(), R.mipmap.ic_launcher),
                EmotionData.EmotionCategory.image,
                new UniqueEmoji(temp), 2, 4);
//                EmotionData.EmotionCategory.image, 2, 4);
        emotionList.add(data);
        // 以上即在填充emotionView前所需要完成的结构内容
        emotionView.setEmotionDataList(emotionList);
        // 2 设置相应的监听器
        emotionView.setEmotionClickListener(new EmotionView.EmotionClickListener() {
            @Override
            public void OnEmotionClick(Emoticon emotionData, View v, EmotionData.EmotionCategory category) {
                switch (category) {
                    case emoji:
                        Toast.makeText(getContext(),
                                "decInt:" + emotionData.getDesc() + " drawableid" + emotionData.getResourceId(),
                                Toast.LENGTH_SHORT).show();
                        break;
                    case image:
                        Toast.makeText(getContext(),
                                "path:" + emotionData.getDesc(),
                                Toast.LENGTH_SHORT).show();
                    default:
                }
            }

            @Override
            public void OnUniqueEmotionClick(Emoticon uniqueItem, View v, EmotionData.EmotionCategory category) {
                switch (category) {
                    case emoji:
                        Toast.makeText(getContext(), "uniqueItem: " +
                                        "decInt:" + uniqueItem.getDesc() + " drawableid" + uniqueItem.getResourceId(),
                                Toast.LENGTH_SHORT).show();
                        break;
                    case image:
                        Toast.makeText(getContext(), "uniqueItem: " + "path:" + uniqueItem.getDesc(),
                                Toast.LENGTH_SHORT).show();

                        String temp = ResourceUtil.getResourceUriString(getContext(), R.mipmap.ic_launcher);
                        List<Emoticon> customList = emotionView.getEmotionDataList().get(1).getEmotionList();
                        customList.add(new CustomEmoji(temp));
                        EmotionData data = new EmotionData(customList,
                                ResourceUtil.getResourceUriString(getContext(), R.mipmap.ic_launcher),
                                EmotionData.EmotionCategory.image, new CustomEmoji(temp), 2, 4);

                        emotionView.modifyEmotionDataList(data, 1);
                    default:
                }
            }
        });

        init();
        return rootView;
    }

    //we should replace the view with fragment after views created.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 获取FragmentManager对象
        FragmentManager fm = getChildFragmentManager();
        // 开启事务
        FragmentTransaction ft = fm.beginTransaction();
        voicePress = new VoicePress();
        voicePress.setListener(new VoicePress.voiceMessageListener() {
            @Override
            public void onSendVoiceMessage(String path, int duration, String description) {
                if (null != inputChatListener) {
                    inputChatListener.onSendVoiceMessage(path, duration, description);
                }
            }
        });
        ft.replace(R.id.voice_press_view, voicePress);
        ft.commit();
        voiceView = rootView.findViewById(R.id.voice_press_view);
    }

    public void init() {
        initInputLayout();
        initToolBox();
    }

    private void initToolBox() {
        toolBox.setData(items);
    }

    private void initInputLayout() {
        onlyView1 = (OnlyView) buttonView.findViewById(R.id.button_1);
        voiceBtn = (Button) buttonView.findViewById(R.id.voice_button);
        voice2chatBtn = (Button) buttonView.findViewById(R.id.voice2chat_button);
        onlyView2 = (OnlyView) buttonView.findViewById(R.id.button_2);
        emojiBtn = (Button) buttonView.findViewById(R.id.emoji_button);
        emoji2chatBtn = (Button) buttonView.findViewById(R.id.emoji2chat_button);
        onlyView3 = (OnlyView) buttonView.findViewById(R.id.button_3);
        sendBtn = (Button) buttonView.findViewById(R.id.send_button);
        send2toolBtn = (Button) buttonView.findViewById(R.id.send2tool_button);
        send2toolBtnOn = (Button) buttonView.findViewById(R.id.send2toolOn_button);
        onlyView4 = (OnlyView) buttonView.findViewById(R.id.voice_view);
        editText = (EditText) buttonView.findViewById(R.id.id_edit);
        editText.setCursorVisible(true);

        initButtonListener();
    }

    private void initButtonListener() {
        voiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAll();
                onlyView1.setChildView(voice2chatBtn);
                onlyView2.setChildView(emojiBtn);
                onlyView3.setChildView(send2toolBtn);
                onlyView4.setChildView(voiceView);
            }
        });
        voice2chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSoftInput();
                onlyView1.setChildView(voiceBtn);
                onlyView2.setChildView(emojiBtn);
                onlyView4.setChildView(editText);
                editText.requestFocus();
            }
        });
        emojiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput(new Runnable() {
                    @Override
                    public void run() {
                        showBoxView();
                    }
                });
                onlyView1.setChildView(voiceBtn);
                onlyView2.setChildView(emoji2chatBtn);
                if (send2toolBtnOn.getVisibility() == View.VISIBLE) {
                    onlyView3.setChildView(send2toolBtn);
                }
                onlyView4.setChildView(editText);
                boxView.setChildView(emotionView);
            }
        });
        emoji2chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSoftInput();
                onlyView1.setChildView(voiceBtn);
                onlyView2.setChildView(emojiBtn);
                onlyView4.setChildView(editText);
                editText.requestFocus();
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != inputChatListener) {
                    inputChatListener.onSendMessage(editText.getEditableText().toString());
                }
                clearInput();
            }
        });
        send2toolBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput(new Runnable() {
                    @Override
                    public void run() {
                        showBoxView();
                    }
                });
                onlyView1.setChildView(voiceBtn);
                onlyView2.setChildView(emojiBtn);
                onlyView3.setChildView(send2toolBtnOn);
                onlyView4.setChildView(editText);
                boxView.setChildView(toolBox);
            }
        });
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    onlyView3.setChildView(send2toolBtn);
                } else {
                    onlyView3.setChildView(sendBtn);
                }
                buttonView.postInvalidate();
            }
        });
    }

    private void showBoxView() {
        if (null == boxView) {
            return;
        }
        if (isBoxViewShow()) {
            return;
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) boxView.getLayoutParams();
        params.height = getBoxViewHeight();
        boxView.setLayoutParams(params);
        boxView.setVisibility(View.VISIBLE);
    }

    public void addToolBoxData(ChatToolBox.ChatToolItem item) {
        this.items.add(item);
        if (null != toolBox) {
            toolBox.setData(items);
        }
    }

    public void addToolBoxData(List<ChatToolBox.ChatToolItem> items) {
        this.items.addAll(items);
        if (null != toolBox) {
            toolBox.setData(items);
        }
    }

    Runnable runOnKeyboardDismiss;

    protected boolean hideSoftInput(Runnable runnable) {
        if (keyBoardShow) {
            runOnKeyboardDismiss = runnable;
            super.hideSoftKeyboard();
            return true;
        }
        if (null != runnable) {
            runnable.run();
        }
        runOnKeyboardDismiss = null;
        return false;
    }

    public void onKeyboardShow(int height) {
        //  保存键盘高度，隐藏toolbox
        SharedPreferences pref = getActivity().getSharedPreferences("sys_variable", Context.MODE_PRIVATE);
        pref.edit().putInt("virtual_keyboard_height", height).apply();
        boxViewHeight = height;
        keyBoardShow = true;
        hideBoxView();
        onlyView1.setChildView(voiceBtn);
        onlyView2.setChildView(emojiBtn);

        if (!editText.getText().toString().equals("")) {
            onlyView3.setChildView(sendBtn);
        } else {
            onlyView3.setChildView(send2toolBtn);
        }
    }

    public void onKeyboardDismiss() {
        keyBoardShow = false;
        if (null != runOnKeyboardDismiss) {
            runOnKeyboardDismiss.run();
            runOnKeyboardDismiss = null;
        }
    }

    public void hideBoxViewMode() {
        if (null == rootView) {
            return;
        }
        this.hideAll();
        onlyView1.setChildView(voiceBtn);
        onlyView2.setChildView(emojiBtn);
        onlyView3.setChildView(send2toolBtn);
        onlyView4.setChildView(editText);
    }

    @Override
    public boolean handleBack() {
        return hideAll();
    }

    public boolean hideAll() {
        return hideBoxView() || hideSoftInput(null);
    }

    public boolean hideBoxView() {
        if (isBoxViewShow()) {
            boxView.setVisibility(View.GONE);
            return true;
        } else {
            return false;
        }
    }

    private void showSoftInput() {
        if (keyBoardShow) {
            return;
        }
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.toggleSoftInput(0, 0);
    }

    private boolean isBoxViewShow() {
        if (null == boxView) {
            return false;
        }
        return View.VISIBLE == boxView.getVisibility();
    }

    public boolean isBoxShow() {
        return isBoxViewShow() || keyBoardShow;
    }

    private class ToolPhoto implements ChatToolBox.ChatToolItem {
        @Override
        public int getIcon() {
            return R.drawable.app_panel_gallery_selector;//app_panel_pic_icon
        }

        @Override
        public String getName() {
            return "照片";
        }

        @Override
        public void onItemSelected() {
            startPhotoPicker();
        }
    }

    private class ToolCamera implements ChatToolBox.ChatToolItem {
        @Override
        public int getIcon() {
            return R.drawable.app_panel_video_selector;
        }

        @Override
        public String getName() {
            return "拍摄";
        }

        @Override
        public void onItemSelected() {
            startCameraDlg();
        }
    }

    private class ToolPosition implements ChatToolBox.ChatToolItem {
        @Override
        public int getIcon() {
            return R.drawable.app_panel_location_selector;
        }

        @Override
        public String getName() {
            return "位置";
        }

        @Override
        public void onItemSelected() {
            startLocationChooser();
        }
    }

    protected void startCameraDlg() {
        Map<String, Serializable> map = new HashMap<>();
        map.put(BundleArguments.NEED_PHOTO_UPLOAD, false);
        map.put(BundleArguments.ARG_COUNT, 12);
    }

    protected void startPhotoPicker() {
    }

    protected void startLocationChooser() {
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == Activity.RESULT_OK || null != data) {
            if (REQUEST_CODE_TAKE_PHOTO == requestCode) {
                if (null != inputChatListener) {
                    inputChatListener.onTakePhotoMessage(data);
                }
            } else if (REQUEST_CODE_PICK_PHOTO == requestCode) {
                if (null != inputChatListener) {
                    inputChatListener.onPickPhotoMessage(data);
                }
            } else if (REQUEST_CODE_CHOOSE_LOCATION == requestCode) {
                if (null != inputChatListener) {
                    inputChatListener.onPickLocMessage(data);
                }
            }
        }
        this.hideBoxViewMode();
    }
}
