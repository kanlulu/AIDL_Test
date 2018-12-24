package com.kanlulu.aidl_test.client;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.kanlulu.aidl_test.R;
import com.kanlulu.aidl_test.bean.Book;
import com.kanlulu.aidl_test.constant.Constant;
import com.kanlulu.aidl_test.service.MessengerService;

public class OtherProcessActivity extends AppCompatActivity {
    public Messenger mServiceMessenger;

    @SuppressLint("HandlerLeak")
    Messenger mMessenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SERVICE_TO_CLIENT_WHAT:
                    Bundle bundle = msg.getData();
                    bundle.setClassLoader(getClass().getClassLoader());
                    Log.e("debug", "从服务端来的反馈"+msg.getData().getParcelable("bundle").toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    });

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceMessenger = new Messenger(service);
            Log.e("debug", "连接成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceMessenger = null;
            Log.e("debug", "连接断开");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_process);

        getData();
        bindMessengerService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }

    private void bindMessengerService() {
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        Log.e("debug", "服务已绑定");
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String extrasString = extras.getString("test0");
        Book book = ((Book) extras.getParcelable("parcelable"));
        Log.e("bundle", extrasString + "  ===  " + book.toString());
    }

    public void sendMessage(View view) {
        if (mServiceMessenger == null) return;
        try {
            Message message = Message.obtain();
            message.what = Constant.CLIENT_TO_SERVICE_WHAT;
//            message.obj = new Book(2, "Client-To-Service Book");
            Bundle bundle = new Bundle();
            bundle.putParcelable("book", new Book(2, "Client-To-Service Book"));
            message.setData(bundle);
            message.replyTo = mMessenger;
            mServiceMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
