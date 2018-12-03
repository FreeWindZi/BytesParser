package com.sht.bytesparser.exception;

public class BytesParserException  extends RuntimeException{

    BytesParserException(String s) {
        super(s);
    }

    BytesParserException(Throwable throwable) {
        super(throwable);
    }

}
