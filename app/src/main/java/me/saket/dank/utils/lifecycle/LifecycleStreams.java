package me.saket.dank.utils.lifecycle;

import android.support.annotation.CheckResult;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;

public interface LifecycleStreams<EVENT> {

  Object NOTHING = new Object();

  @CheckResult
  Observable<EVENT> events();

  @CheckResult
  Observable<EVENT> onStart();

  @CheckResult
  Observable<EVENT> onResume();

  @CheckResult
  Observable<EVENT> onPause();

  @CheckResult
  Observable<EVENT> onStop();

  @CheckResult
  Observable<EVENT> onDestroy();

  @CheckResult
  default Flowable<EVENT> onDestroyFlowable() {
    return onDestroy().toFlowable(BackpressureStrategy.LATEST);
  }
}
