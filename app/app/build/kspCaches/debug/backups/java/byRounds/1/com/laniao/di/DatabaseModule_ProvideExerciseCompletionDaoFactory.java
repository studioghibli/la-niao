package com.laniao.di;

import com.laniao.data.local.LaNiaoDatabase;
import com.laniao.data.local.dao.ExerciseCompletionDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class DatabaseModule_ProvideExerciseCompletionDaoFactory implements Factory<ExerciseCompletionDao> {
  private final Provider<LaNiaoDatabase> databaseProvider;

  public DatabaseModule_ProvideExerciseCompletionDaoFactory(
      Provider<LaNiaoDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ExerciseCompletionDao get() {
    return provideExerciseCompletionDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideExerciseCompletionDaoFactory create(
      javax.inject.Provider<LaNiaoDatabase> databaseProvider) {
    return new DatabaseModule_ProvideExerciseCompletionDaoFactory(Providers.asDaggerProvider(databaseProvider));
  }

  public static DatabaseModule_ProvideExerciseCompletionDaoFactory create(
      Provider<LaNiaoDatabase> databaseProvider) {
    return new DatabaseModule_ProvideExerciseCompletionDaoFactory(databaseProvider);
  }

  public static ExerciseCompletionDao provideExerciseCompletionDao(LaNiaoDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideExerciseCompletionDao(database));
  }
}
