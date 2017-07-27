package com.mooncascade.weathertestapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.test.InstrumentationTestCase;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ListView;

import static junit.framework.Assert.assertNotNull;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

import com.mooncascade.weathertestapp.activities.MainActivity;
import com.mooncascade.weathertestapp.activities.SettingsActivity;
import com.mooncascade.weathertestapp.common.Utility;
import com.mooncascade.weathertestapp.fragments.MainFragment;
import com.mooncascade.weathertestapp.rest.RestClient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;
import org.robolectric.util.FragmentTestUtil;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class ExampleUnitTest {
    private Activity mainActivity;
    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        mainActivity = Robolectric.setupActivity(MainActivity.class);

        server = new MockWebServer();
        server.start();
        RestClient.BASE_URL = server.url("/").toString();

    }

    @Test
    public void menuItemOpensSettingsScreen() {

        // Get shadow
        ShadowActivity shadowActivity = Shadows.shadowOf(mainActivity);

        // Click menu
        shadowActivity.clickMenuItem(R.id.settings);

        // Get intent
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(startedIntent);

        // Make your assertion
        assertEquals(SettingsActivity.class, shadowIntent.getIntentClass());

    }

    @Test
    public void mainFragmentHandlesDataCorrectly() {

        InputStream XmlFileInputStream = RuntimeEnvironment.application.getResources().openRawResource(R.raw.service_box_response);
        String sxml = Utility.readTextFile(XmlFileInputStream);

        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(sxml));

        AppCompatActivity mainActivity = Robolectric.setupActivity(MainActivity.class);

        MainFragment mainFragment = new MainFragment();

        FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(mainFragment, null);
        fragmentTransaction.commit();

        assertNotNull(mainFragment);

        //assertEquals(2, mainFragment.data.size());

        RecyclerView recyclerView = mainFragment.getView().findViewById(R.id.list);
        assertEquals(2, recyclerView.getAdapter().getItemCount());

    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }
}
