package com.laniao.di;

import com.laniao.util.DispatcherProvider;
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
public final class UtilModule_ProvideDispatcherProviderFactory implements Factory<DispatcherProvider> {
  @Override
  public DispatcherProvider get() {
    return provideDispatcherProvider();
  }

  public static UtilModule_ProvideDispatcherProviderFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static DispatcherProvider provideDispatcherProvider() {
    return Preconditions.checkNotNullFromProvides(UtilModule.INSTANCE.provideDispatcherProvider());
  }

  private static final class InstanceHolder {
    static final UtilModule_ProvideDispatcherProviderFactory INSTANCE = new UtilModule_ProvideDispatcherProviderFactory();
  }
}
