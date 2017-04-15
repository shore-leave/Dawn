package me.saket.dank.utils;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Utility methods related to Rx.
 */
public class RxUtils {

    /**
     * A transformer that makes an Observable execute its computation (or emit items) inside an IO thread and the
     * operators that follow this method call (including the Subscriber) execute on the main thread. This should ideally
     * be called right before (or as close as possible) to the subscribe() call to ensure any other operator doesn't
     * accidentally get executed on the main thread.
     */
    public static <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * A transformer that makes an Observable execute its computation (or emit items) inside an IO thread and the
     * operators that follow this method call (including the Subscriber) execute on the main thread. This should ideally
     * be called right before (or as close as possible) to the subscribe() call to ensure any other operator doesn't
     * accidentally get executed on the main thread.
     */
    public static <T> Single.Transformer<T, T> applySchedulersSingle() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> Action1<T> doNothing() {
        return t -> {
        };
    }

    public static Action1<Throwable> logError(String errorMessage) {
        return error -> Timber.e(error, errorMessage);
    }

    public static <T> Observable.Transformer<T, T> doOnStartAndEnd(Action1<Boolean> action) {
        return observable -> observable
                .doOnSubscribe(() -> action.call(true))
                .doOnUnsubscribe(() -> action.call(false));
    }

    public static <T> Observable.Transformer<T, T> doOnStartAndNext(Action1<Boolean> action) {
        return observable -> observable
                .doOnSubscribe(() -> action.call(true))
                .doOnNext(o -> action.call(false))
                .doOnError(o -> action.call(false));
    }

    public static <T> Single.Transformer<T, T> doOnStartAndFinishSingle(Action1<Boolean> isOngoingAction) {
        return observable -> observable
                .doOnSubscribe(() -> isOngoingAction.call(true))
                .doOnUnsubscribe(() -> isOngoingAction.call(false));
    }

    @NonNull
    public static <T> Observable.Transformer<T, T> flatMapIf(boolean predicate, Observable<T> flatMapObservable) {
        return observable -> observable.flatMap(value -> {
            if (predicate) {
                return flatMapObservable;

            } else {
                return Observable.just(value);
            }
        });
    }

}
