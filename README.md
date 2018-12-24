### Android进程间通信机制之AIDL

 AIDL的全称为:*Android Interface Definition Language*，即安卓接口定义语言。我们可以使用它定义客户端和服务端通信时都认可的编程接口。

在Android中如果我们自己编写这个过程的代码，无疑是极其繁琐的。因此我们可以利用AIDL来处理，我们只需要编写aidl接口文件，然后再重新编译一下项目系统会帮我们生产Binder接口。

#### 在Android Studio中编写AIDL

利用AIDL进行进程间通信的步骤如下：

- 创建AIDL文件

  > 创建实体对象，实现Parcelable接口;
  >
  > 创建aidl文件夹，创建aidl接口文件和实体类的映射aidl文件(*aidl文件包名路径需要和实体对象包名路径一致*)；
  >
  > 重新build一下项目。

- 实现Server端

  > 创建service，创建Binder对象，实现接口方法；
  >
  > 在onBind中返回创建的Binder对象。

- 实现Client端

  > 创建ServiceConnection对象，实现其方法，需要再其方法中拿到AIDL对象；
  >
  > 绑定服务bindService()；
  >
  > 通过调用AIDL类中的方法向服务端发起请求。

***

#### 一、创建AIDL文件

1.创建Book实体类，实现Parcelable接口；

```java
package com.kanlulu.aidl_test.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kanlulu
 */
public class Book implements Parcelable {
    private int bookId;
    private String bookName;

    public Book(int bookId, String bookName) {
        this.bookId = bookId;
        this.bookName = bookName;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                '}';
    }

    protected Book(Parcel in) {
        bookId = in.readInt();
        bookName = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bookId);
        dest.writeString(bookName);
    }
}
```

2.创建aidl文件夹和aidl文件

在和src/main目录下创建aidl文件夹，需要保持和java在统计目录。在Android Studio中我们自然不需要一步一步手动创建aidl文件夹和aidl文件。如图所示：
![创建AIDL](https://img-blog.csdnimg.cn/20181206133443532.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM2MDQ2MzA1,size_16,color_FFFFFF,t_70)

这一步完成即完成了aidl接口文件的创建，我们可以根据自己的需要写一些接口方法：

```java
// IBookManager.aidl
package com.kanlulu.aidl_test;

// Declare any non-default types here with import statements
import com.kanlulu.aidl_test.bean.Book;

interface IBookManager {
     void addBook(in Book book);
     List<Book> getBookList();
}

```

需要注意的是:

> 还要import我们用到的实体类*import com.kanlulu.aidl_test.bean.Book;* 因为Book不是aidl支持的基本数据类型（java的基本数据类型、List和Map、其他AIDL接口和实现Parcelable的实体类*后3种不是基本数据类型*）。

> `void addBook(in Book book);`中的"in" 表示输入，还有其他的：out表示输出，inout表示输入输出。

接下来我们要创建实体类的映射adil文件，路径需要与实体类保持一致，同时需要使用parcelable声明我们的实体类对象；

```java
// Book.aidl
package com.kanlulu.aidl_test.bean;

// Declare any non-default types here with import statements
parcelable Book;

```

最后我们只需要Make Project，系统就会帮我们自动生成Binder的java文件。我们可以再`build/generated/source/aidl/...`路径下查看。
![查看生成的java代码](https://img-blog.csdnimg.cn/20181206133523457.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM2MDQ2MzA1,size_16,color_FFFFFF,t_70)
***

#### 二、创建Server

创建Service类，在类中创建Binder对象并实现接口方法，然后在onBind（）总返回我们的Binder对象。

```java
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

```

*不要忘了在AndroidManifest.xml文件中注册Service组件*

```xml
<service
         android:name=".service.MyAIDLService"
         android:enabled="true"
         android:exported="true"
         android:process=":aidl_test" />
```

***

#### 三、创建Client进行通信

创建ServiceConnection对象，实现接口方法并拿到Binder对象然后转换成AIDL。通过bindService()建立连接，之后就可以进行对Server端的访问。

```java
package com.kanlulu.aidl_test.client;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.kanlulu.aidl_test.IBookManager;
import com.kanlulu.aidl_test.R;
import com.kanlulu.aidl_test.bean.Book;
import com.kanlulu.aidl_test.service.MyAIDLService;

import java.util.List;

/**
 * AIDL：Android接口定义语言(Android Interface definition Language)，是Android中的进程间通信机制。
 * 在Android中一个进程是无法访问另一个进程的内存，但是如果我们能够将对象分解成操作系统能够识别的原语，并将对象编组成跨越边界的对象,
 * 就能够实现进程间的通信。
 * Binder继承自IBinder接口(IBinder接口代表了跨进程传输的能力)；
 *
 *
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
    public TextView tvBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startAIDLService();
        tvBooks = (TextView) findViewById(R.id.tv_books);
    }

    private void startAIDLService() {
        Intent intent = new Intent(this, MyAIDLService.class);
        bindService(intent, aidlServiceConnection, BIND_AUTO_CREATE);
    }

    public void btnAdd(View view) {
        Book bookName = new Book(10, "bookName");

        try {
            mAidl.addBook(bookName);
            List<Book> bookList = mAidl.getBookList();
            tvBooks.setText(bookList.toString());
        } catch (RemoteException e) {
            Log.e(TAG,e.getMessage());
        }finally {
            Log.d(TAG,"添加一本书...");
        }
    }
}

```

至此，在Android中利用AIDL进行进程间通信就完成了。
