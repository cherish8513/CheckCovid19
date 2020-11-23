package com.project.checkcovid19;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.project.checkcovid19.constants.Constants;
import com.project.checkcovid19.service.Rankings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AndroidServiceTest {
    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
    }
    @Test
    public void fileWriter() {
        System.out.println("1");
        Rankings rankings;
        rankings = new Rankings(new File(context.getFilesDir(), Constants.file_name));
        Log.d("file",context.getFilesDir().getAbsolutePath());
    }
}