package com.kanlulu.aidl_test.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.kanlulu.aidl_test.IBookManager;
import com.kanlulu.aidl_test.bean.Book;

import java.util.ArrayList;
import java.util.List;

public class MyAIDLService extends Service {
    private final String TAG = "MyAIDLService";
    private ArrayList<Book> mBooks;

    /**
     * 根据我们的aidl创建Binder对象
     */
    private IBinder mIBinder = new IBookManager.Stub() {
        /**
         *
         * @param book  客户端传递过来的数据
         * @throws RemoteException
         */
        @Override
        public void addBook(Book book) throws RemoteException {
            mBooks.add(book);
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBooks;
        }
    };

    /**
     * 返回我们创建的Binder对象
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "====== MyAIDLService onBind ======");
        mBooks = new ArrayList<>();
        return mIBinder;
    }
}
