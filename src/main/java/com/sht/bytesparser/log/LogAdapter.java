package com.sht.bytesparser.log;



public interface LogAdapter {


    public  void e(String message, Object... args);

    public  void w(String message, Object... args);

    public  void i(String message, Object... args) ;

    public  void d(String message, Object... args);


    public  void v(String message, Object... args) ;



}
