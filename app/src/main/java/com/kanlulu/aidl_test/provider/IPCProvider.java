package com.kanlulu.aidl_test.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kanlulu.aidl_test.database.DbHelper;


/**
 * Created by kanlulu
 * DATE: 2018/12/12 10:24
 */
public class IPCProvider extends ContentProvider {
    //ContentProvider 的授权字符串
    public static final String AUTHORITY = "com.kanlulu.aidl_test.provider.IPCProvider";
    /**
     * 内容 URI 用于在 ContentProvider 中标识数据的 URI，可以使用 content:// + authority 作为 ContentProvider 的 URI
     */
    public static final Uri IPC_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");
    public String mTableName;
    public Context mContext;
    public SQLiteDatabase mDatabase;

    /**
     * 在 ContentProvider 中可以通过 UriMatcher 来为不同的 URI 关联不同的 code，便于后续根据 URI 找到对应的表
     */
    private static UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int TABLE_CODE_PERSON = 2;

    static {
        //关联不同的 URI 和 code，便于后续 getType
        mUriMatcher.addURI(AUTHORITY, "book", TABLE_CODE_PERSON);
    }

    @Override
    public boolean onCreate() {
        initProvider();
        return false;
    }

    private void initProvider() {
        mTableName = DbHelper.TABLE_NAME;
        mContext = getContext();
        mDatabase = new DbHelper(mContext).getWritableDatabase();

        new Thread(new Runnable() {
            @Override
            public void run() {
                mDatabase.execSQL("delete from " + mTableName);
                mDatabase.execSQL("insert into " + mTableName + " values(1,'test_book_name','test_book_desc')");
            }
        }).start();
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String tableName = getTableName(uri);
        Log.e("debug", tableName + " 查询数据");
        return mDatabase.query(tableName, projection, selection, selectionArgs, null, sortOrder, null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String tableName = getTableName(uri);
        Log.e("debug", tableName + " 插入数据");
        mDatabase.insert(tableName, null, values);
        mContext.getContentResolver().notifyChange(uri, null);
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getTableName(uri);
        Log.e("debug", tableName + " 删除数据");
        int deleteCount = mDatabase.delete(tableName, selection, selectionArgs);
        if (deleteCount > 0) {
            mContext.getContentResolver().notifyChange(uri, null);
        }
        return deleteCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getTableName(uri);
        int updateCount = mDatabase.update(tableName, values, selection, selectionArgs);
        if (updateCount > 0) {
            mContext.getContentResolver().notifyChange(uri, null);
        }
        return updateCount;
    }

    private String getTableName(final Uri uri) {
        String tableName = "";
        int match = mUriMatcher.match(uri);
        switch (match) {
            case TABLE_CODE_PERSON:
                tableName = DbHelper.TABLE_NAME;
        }
        return tableName;
    }
}
