package gr.petalidis.datamars;

import android.app.Application;
import android.content.Context;

public class Moo extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();;
        Moo.context = getApplicationContext();
    }

    public static Context getAppContext()
    {
        return Moo.context;
    }
}
