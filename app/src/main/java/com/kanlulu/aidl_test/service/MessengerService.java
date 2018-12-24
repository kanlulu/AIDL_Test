package com.kanlulu.aidl_test.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kanlulu.aidl_test.bean.Book;
import com.kanlulu.aidl_test.constant.Constant;

/**
 * Created by kanlulu
 * DATE: 2018/12/6 16:32
 */
public class MessengerService extends Service {

    @SuppressLint("HandlerLeak")
    private Messenger mMessenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //创建一个Message用于存放发送回Client的消息
            Message sendToClientMsg = Message.obtain(msg);
            switch (msg.what) {
                case Constant.CLIENT_TO_SERVICE_WHAT:
                    try {
                        Bundle bundle = msg.getData();
                        bundle.setClassLoader(getClass().getClassLoader());
                        Log.e("debug", "从客户端来的消息："+bundle.getParcelable("book").toString());
                        sendToClientMsg.what = Constant.SERVICE_TO_CLIENT_WHAT;
                        Bundle data = new Bundle();
                        data.putParcelable("bundle",new Book(1,"Service To Client book"));
                        sendToClientMsg.setData(data);
                        msg.replyTo.send(sendToClientMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    });

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger != null ? mMessenger.getBinder() : null;
    }

}
