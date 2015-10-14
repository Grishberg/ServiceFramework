package info.goodline.framework.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Random;

import info.goodline.framework.R;
import info.goodline.framework.activities.BaseBinderActivity;

public class MainActivity extends BaseDrawerActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SCREEN_TAG = "";
    private int mFirstTaskId = -1;

    /**
     * start tasks
     */
    @Override
    protected void onFirstBound() {
        super.onFirstBound();
        final Random random = new Random();
        TestService service = (TestService) mService;
        for (int i = 0; i < 1000; i++) {
            int priority = (int) (Math.random() * 5) + 5;
            int taskId = service.startThread(SCREEN_TAG, priority, i);
            if (mFirstTaskId < 0) {
                mFirstTaskId = taskId;
            }
        }
    }

    @Override
    protected Intent getServiceIntent() {
        return new Intent(this, TestService.class);
    }

    @Override
    protected void onFabClicked() {
        super.onFabClicked();
        TestService service = (TestService) mService;
        int taskId = service.startThread("newTask", 1, 555);
        delaylTask(SCREEN_TAG);
    }

    @Override
    protected void onTaskDone(String tag, int taskId, int code) {
        Log.d(TAG, "        on task done tag=" + tag + " id=" + code);
    }
}
