package ch.epfl.sweng.eventmanager.test;

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

public class MockTestRunner extends AndroidJUnitRunner {

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {

        // Replace the application by the test equivalent
        return super.newApplication(cl, TestApplication.class.getName(), context);
    }

}