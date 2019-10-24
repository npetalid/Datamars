/*        Copyright 2019 Nikolaos Petalidis
 *
 *        Licensed under the Apache License, Version 2.0 (the "License");
 *        you may not use this file except in compliance with the License.
 *        You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *        Unless required by applicable law or agreed to in writing, software
 *        distributed under the License is distributed on an "AS IS" BASIS,
 *        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *        See the License for the specific language governing permissions and
 *        limitations under the License.
 */
package gr.petalidis.datamars;

import org.apache.log4j.Level;

import java.io.File;
import java.util.Objects;

import de.mindpipe.android.logging.log4j.LogConfigurator;

public class Log4jHelper {
    private final static LogConfigurator mLogConfigrator = new LogConfigurator();

    static {
        configureLog4j();
    }

    private static void configureLog4j() {
        if (Moo.getAppContext()!=null) {
            File dataDirectory = Moo.getAppContext().getExternalFilesDir("");

            String fileName = Objects.requireNonNull(dataDirectory).getAbsolutePath() + File.separator + "log4j.log";

            String filePattern = "%d - [%c] - %p : %m%n";
            int maxBackupSize = 10;
            long maxFileSize = 1024 * 1024;

            configure(fileName, filePattern, maxBackupSize, maxFileSize);
        }
    }

    private static void configure( String fileName, String filePattern, int maxBackupSize, long maxFileSize ) {
        mLogConfigrator.setFileName( fileName );
        mLogConfigrator.setMaxFileSize( maxFileSize );
        mLogConfigrator.setFilePattern(filePattern);
        mLogConfigrator.setMaxBackupSize(maxBackupSize);
        mLogConfigrator.setUseLogCatAppender(true);
        mLogConfigrator.setRootLevel(Level.ALL);
        mLogConfigrator.setUseFileAppender(true);
        mLogConfigrator.setImmediateFlush(true);
        mLogConfigrator.configure();
    }

    public static org.apache.log4j.Logger getLogger( String name ) {
        return org.apache.log4j.Logger.getLogger( name );
    }
}