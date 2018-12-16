package ch.epfl.sweng.eventmanager.test;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.multidex.MultiDex;
import androidx.test.runner.AndroidJUnitRunner;

public class MockTestRunner extends AndroidJUnitRunner {

    @Override
    public void onCreate(Bundle arguments) {
        MultiDex.installInstrumentation(this.getContext(),getTargetContext());
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        super.onCreate(arguments);
    }

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return super.newApplication(cl, TestApplication.class.getName(), context);
    }
}