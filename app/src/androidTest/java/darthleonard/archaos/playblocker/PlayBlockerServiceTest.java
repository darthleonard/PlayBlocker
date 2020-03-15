package darthleonard.archaos.playblocker;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import darthleonard.archaos.playblocker.locker.PlayBlockerService;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PlayBlockerServiceTest {
    @Test
    public void canRunService() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        appContext.startService(new Intent(appContext, PlayBlockerService.class));
        assertTrue(IsServiceRunning(appContext));
    }

    private boolean IsServiceRunning(Context appContext) {
        ActivityManager activityManager = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (PlayBlockerService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
