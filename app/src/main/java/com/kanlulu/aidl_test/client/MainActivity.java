package com.kanlulu.aidl_test.client;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.kanlulu.aidl_test.IBookManager;
import com.kanlulu.aidl_test.R;
import com.kanlulu.aidl_test.provider.IPCProvider;
import com.kanlulu.aidl_test.service.MyAIDLService;
import com.kanlulu.aidl_test.widget.CircleProgress;


/**
 * AIDL：Android接口定义语言(Android Interface definition Language)，是Android中的进程间通信机制。
 * 在Android中一个进程是无法访问另一个进程的内存，但是如果我们能够将对象分解成操作系统能够识别的原语，并将对象编组成跨越边界的对象,
 * 就能够实现进程间的通信。
 * Binder继承自IBinder接口(IBinder接口代表了跨进程传输的能力)；
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private IBookManager mAidl;
    private ServiceConnection aidlServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接后拿到 Binder，转换成 AIDL，在不同进程会返回个代理
            mAidl = IBookManager.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mAidl = null;
        }
    };

    IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mAidl == null) return;
            mAidl.asBinder().unlinkToDeath(deathRecipient, 0);
            mAidl = null;
            //TODO 重新绑定服务
//            bindService()
        }
    };
    public TextView tvBooks;
    public CircleProgress mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startAIDLService();
        tvBooks = (TextView) findViewById(R.id.tv_books);
        mProgressView = (CircleProgress) findViewById(R.id.progress);
        mProgressView.setProgress(15);
    }

    private void startAIDLService() {
        Intent intent = new Intent(this, MyAIDLService.class);
        bindService(intent, aidlServiceConnection, BIND_AUTO_CREATE);
    }

    public void btnAdd(View view) {
        queryBook();

        /** ================================== */
//        Intent intent = new Intent(this, OtherProcessActivity.class);
//        Bundle extras = new Bundle();
//        extras.putString("test0", "string");
//        extras.putParcelable("parcelable", new Book(0, "bookName 0"));
//        intent.putExtras(extras);
//        startActivity(intent);

        /** ============================ */
//        mProgressView.setProgress(80);

        //TODO AIDL
//        Book bookName = new Book(10, "bookName");
//
//        try {
//            mAidl.addBook(bookName);
//            List<Book> bookList = mAidl.getBookList();
//            tvBooks.setText(bookList.toString());
//        } catch (RemoteException e) {
//            Log.e(TAG,e.getMessage());
//        }finally {
//            Log.d(TAG,"添加一本书...");
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(aidlServiceConnection);
    }

    public void btnContentProvider(View view) {
        addBook();
    }

    private void addBook() {
        Uri uri = IPCProvider.IPC_CONTENT_URI;
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", (int) ((Math.random() * 9 + 1) * 100));
        contentValues.put("name", "ipc_provider_book_name");
        contentValues.put("description", "ipc_provider_book_description");
        ContentResolver contentResolver = getContentResolver();
        contentResolver.insert(uri, contentValues);//插入数据
    }

    private void queryBook() {
        Uri uri = IPCProvider.IPC_CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(uri, new String[]{"name", "description"}, null, null, null);
        if (cursor == null) return;
        while (cursor.moveToNext()) {
            String result = cursor.getString(0) + "  *****  " + cursor.getString(1);
            Log.e("debug", result);
        }

        cursor.close();
    }
}
