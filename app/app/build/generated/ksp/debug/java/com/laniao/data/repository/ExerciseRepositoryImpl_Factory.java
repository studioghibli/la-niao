package com.laniao.data.repository;

import com.laniao.data.local.dao.ExerciseCompletionDao;
import com.laniao.data.local.dao.ExerciseScheduleDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
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
public final class ExerciseRepositoryImpl_Factory implements Factory<ExerciseRepositoryImpl> {
  private final Provider<ExerciseScheduleDao> scheduleDaoProvider;

  private final Provider<ExerciseCompletionDao> completionDaoProvider;

  public ExerciseRepositoryImpl_Factory(Provider<ExerciseScheduleDao> scheduleDaoProvider,
      Provider<ExerciseCompletionDao> completionDaoProvider) {
    this.scheduleDaoProvider = scheduleDaoProvider;
    this.completionDaoProvider = completionDaoProvider;
  }

  @Override
  public ExerciseRepositoryImpl get() {
    return newInstance(scheduleDaoProvider.get(), completionDaoProvider.get());
  }

  public static ExerciseRepositoryImpl_Factory create(
      javax.inject.Provider<ExerciseScheduleDao> scheduleDaoProvider,
      javax.inject.Provider<ExerciseCompletionDao> completionDaoProvider) {
    return new ExerciseRepositoryImpl_Factory(Providers.asDaggerProvider(scheduleDaoProvider), Providers.asDaggerProvider(completionDaoProvider));
  }

  public static ExerciseRepositoryImpl_Factory create(
      Provider<ExerciseScheduleDao> scheduleDaoProvider,
      Provider<ExerciseCompletionDao> completionDaoProvider) {
    return new ExerciseRepositoryImpl_Factory(scheduleDaoProvider, completionDaoProvider);
  }

  public static ExerciseRepositoryImpl newInstance(ExerciseScheduleDao scheduleDao,
      ExerciseCompletionDao completionDao) {
    return new ExerciseRepositoryImpl(scheduleDao, completionDao);
  }
}
