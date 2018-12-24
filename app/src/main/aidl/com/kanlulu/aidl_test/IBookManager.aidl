// IBookManager.aidl
package com.kanlulu.aidl_test;

// Declare any non-default types here with import statements
import com.kanlulu.aidl_test.bean.Book;

interface IBookManager {
     void addBook(in Book book);
     List<Book> getBookList();
}
