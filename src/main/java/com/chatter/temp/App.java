package com.chatter.temp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * пример работы логгера
 * Hello world!
 */
public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private static final Marker DEBUG = MarkerFactory.getMarker("DEBUG");
//private static final Logger LOGGER = LogManager.getLogger(Class.class.getName());

    public static void main(String[] args) {
        String string = "Have a nice day!";
        LOGGER.info("a test message");
        LOGGER.error("COLOR ERROR");
        LOGGER.warn("COLOR WARN");
        LOGGER.info("COLOR INFO");
        LOGGER.debug("COLOR DEBUG");
        LOGGER.trace("COLOR TRACE");
        System.out.println("Hello World!");
        LOGGER.info(DEBUG, " getAll() Post List {}");
        LOGGER.debug(DEBUG, " getAll() Post List {}", string);
    }
}
