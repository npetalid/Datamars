/*
 * Copyright 2017-2019 Nikolaos Petalidis
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package gr.petalidis.datamars;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Objects;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.android.BasicLogcatConfigurator;
import ch.qos.logback.classic.android.LogcatAppender;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;


public class Log4jHelper {

    static {
        configureLog4j();
    }

    private static void configureLog4j() {
        if (Moo.getAppContext()!=null) {
            BasicLogcatConfigurator.configureDefaultContext();
            File dataDirectory = Moo.getAppContext().getExternalFilesDir("");

            String fileName = Objects.requireNonNull(dataDirectory).getAbsolutePath() + File.separator + "log4j.log";

            String filePattern = "%d - [%c] - %p : %m%n";
            int maxBackupSize = 10;
            long maxFileSize = 1024 * 1024;

            configure(fileName, filePattern, maxBackupSize, maxFileSize);
        }
    }

    private static void configure( String fileName, String filePattern, int maxBackupSize, long maxFileSize ) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.stop();

        // setup FileAppender
        PatternLayoutEncoder encoder1 = new PatternLayoutEncoder();
        encoder1.setContext(lc);
        encoder1.setPattern(filePattern);
        encoder1.start();

        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        fileAppender.setContext(lc);
        fileAppender.setFile(fileName);
        fileAppender.setEncoder(encoder1);
        //fileAppender.setBufferSize(maxFileSize);
        fileAppender.start();

        // setup LogcatAppender
        PatternLayoutEncoder encoder2 = new PatternLayoutEncoder();
        encoder2.setContext(lc);
        encoder2.setPattern("[%thread] %msg%n");
        encoder2.start();

        LogcatAppender logcatAppender = new LogcatAppender();
        logcatAppender.setContext(lc);
        logcatAppender.setEncoder(encoder2);
        logcatAppender.start();

        // add the newly created appenders to the root logger;
        // qualify Logger to disambiguate from org.slf4j.Logger
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.addAppender(fileAppender);
        root.addAppender(logcatAppender);
    }

    public static  org.slf4j.Logger getLogger( String name ) {
        return LoggerFactory.getLogger(name);
    }
}