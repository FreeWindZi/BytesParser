package com.sht.bytesparser.parser;



public class ParsedResult {
    Object value;
    int bitsUsed;

    public ParsedResult(Object value, int bitsUsed) {
        this.value = value;
        this.bitsUsed = bitsUsed;
    }

    public Object getValue() {
        return value;
    }

    public int getBitsUsed() {
        return bitsUsed;
    }

    public int getBytesUsed() {
        return bitsUsed / 8;
    }
}
