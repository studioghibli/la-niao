package com.laniao.di;

import com.laniao.util.Clock;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class UtilModule_ProvideClockFactory implements Factory<Clock> {
  @Override
  public Clock get() {
    return provideClock();
  }

  public static UtilModule_ProvideClockFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static Clock provideClock() {
    return Preconditions.checkNotNullFromProvides(UtilModule.INSTANCE.provideClock());
  }

  private static final class InstanceHolder {
    static final UtilModule_ProvideClockFactory INSTANCE = new UtilModule_ProvideClockFactory();
  }
}
