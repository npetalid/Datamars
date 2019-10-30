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

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Properties;

public class Moo extends Application {

    private static WeakReference<Context> weakReference;
    final static private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    final static Properties properties = new Properties();
    public void onCreate() {
        super.onCreate();
        Moo.weakReference = new WeakReference<>(getApplicationContext());
        readProperties(getApplicationContext());
    }

    public static Context getAppContext()
    {
        if (weakReference==null) {
            return null;
        }
        Context context = Objects.requireNonNull(weakReference.get());
        return context;
    }

    public static String getProperty(String key)
    {
        return properties.getProperty(key);
    }

    public static void setTestProperties(Context context)
    {
        readProperties(context);
    }

    public static SimpleDateFormat getFormatter() {
        return formatter;
    }

    private static void readProperties(Context context) {
        AssetManager assetManager = context.getAssets();
        try (
                InputStream inputStream = assetManager.open("application.properties")
        ) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }
}
