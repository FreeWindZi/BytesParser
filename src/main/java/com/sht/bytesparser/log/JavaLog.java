package com.sht.bytesparser.log;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaLog implements LogAdapter {


    private Logger logger = Logger.getLogger(JavaLog.class.getSimpleName());

    @Override
    public void e(String message, Object... args) {
        logger.severe( "error : "+ String.format(message, args));
    }

    @Override
    public void w(String message, Object... args) {
        logger.warning( "warning : "+ String.format(message, args));
    }

    @Override
    public void i(String message, Object... args) {
        logger.info( "info : "+ String.format(message, args));

    }

    @Override
    public void d(String message, Object... args) {
        logger.info( "debug : "+ String.format(message, args));
    }

    @Override
    public void v(String message, Object... args) {
        logger.info( "verbose : "+ String.format(message, args));
    }


}
