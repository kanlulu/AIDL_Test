package com.kanlulu.aidl_test.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by kanlulu
 * DATE: 2018/11/30 15:31
 */
public class SerialUtils {
    private static final String TAG = "SerialUtils";

    /**
     * 序列化
     *
     * @param object 序列化对象
     * @param path   序列化对象存储路径
     * @return
     */
    public synchronized static boolean saveObject(Object object, String path) {
        if (object == null || TextUtils.isEmpty(path)) {
            return false;
        }
        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(path));
            outputStream.writeObject(object);
            outputStream.close();
            return true;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * @param path
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public synchronized static <T> T readObject(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        ObjectInputStream inputStream = null;
        Object object = null;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(path));
            object = inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return (T) object;
    }
}
