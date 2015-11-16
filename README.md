# AndroidEmotionView
仿照微信的输入框表情view(从 百姓网 android 端 剥离出来并重构)

对外的接口从 InputChat 类的 onInitializeView 开始

在外层通过继承 Emoticon 定义好表情元素并根据这些表情元素组合成EmotionData,再把多个EmotionData 组合成 List 后方可传入 EmotionView

在设置好EmotionDataList后可以设置Listener监听事件
