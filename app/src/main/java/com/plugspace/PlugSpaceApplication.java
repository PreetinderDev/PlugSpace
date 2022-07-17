package com.plugspace;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import androidx.multidex.MultiDex;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class PlugSpaceApplication extends Application {
//  private static final String TAG = PlugSpaceApplication.class.getSimpleName();
  public static PlugSpaceApplication application;

  public PlugSpaceApplication() {
    application = this;
  }

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

  @Override
  public void onCreate() {
    super.onCreate();
    application = this;
//    FirebaseDatabase.getInstance().setPersistenceEnabled(true); // firebase offline data store FirebaseDatabase.getInstance().setPersistenceEnabled(true);


//    MobileAds.initialize(this, new OnInitializationCompleteListener() {
//      @Override
//      public void onInitializationComplete(@NotNull InitializationStatus initializationStatus) {
//      }
//    });
//    AudienceNetworkAds.initialize(application);
//    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//    StrictMode.setVmPolicy(builder.build());

  }


  @Override
  public void onTerminate() {
    super.onTerminate();
  }

  public static Context getAppContext() {
    if (application == null) {
      application = new PlugSpaceApplication();
    }
    return application;
  }

  public static PlugSpaceApplication getApp() {
    if (application == null) {
      application = new PlugSpaceApplication();
    }
    return application;
  }

}
