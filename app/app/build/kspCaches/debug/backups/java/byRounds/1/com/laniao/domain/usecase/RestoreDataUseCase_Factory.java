package com.laniao.domain.usecase;

import com.laniao.data.local.LaNiaoDatabase;
import com.laniao.data.local.dao.AppSettingsDao;
import com.laniao.data.local.dao.DrinkEntryDao;
import com.laniao.data.local.dao.ExerciseCompletionDao;
import com.laniao.data.local.dao.ExerciseScheduleDao;
import com.laniao.data.local.dao.PeeEntryDao;
import com.laniao.data.local.dao.VoidScheduleDao;
import com.laniao.data.local.dao.WaterIntakeDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class RestoreDataUseCase_Factory implements Factory<RestoreDataUseCase> {
  private final Provider<LaNiaoDatabase> databaseProvider;

  private final Provider<PeeEntryDao> peeEntryDaoProvider;

  private final Provider<VoidScheduleDao> voidScheduleDaoProvider;

  private final Provider<WaterIntakeDao> waterIntakeDaoProvider;

  private final Provider<DrinkEntryDao> drinkEntryDaoProvider;

  private final Provider<ExerciseScheduleDao> exerciseScheduleDaoProvider;

  private final Provider<ExerciseCompletionDao> exerciseCompletionDaoProvider;

  private final Provider<AppSettingsDao> appSettingsDaoProvider;

  public RestoreDataUseCase_Factory(Provider<LaNiaoDatabase> databaseProvider,
      Provider<PeeEntryDao> peeEntryDaoProvider, Provider<VoidScheduleDao> voidScheduleDaoProvider,
      Provider<WaterIntakeDao> waterIntakeDaoProvider,
      Provider<DrinkEntryDao> drinkEntryDaoProvider,
      Provider<ExerciseScheduleDao> exerciseScheduleDaoProvider,
      Provider<ExerciseCompletionDao> exerciseCompletionDaoProvider,
      Provider<AppSettingsDao> appSettingsDaoProvider) {
    this.databaseProvider = databaseProvider;
    this.peeEntryDaoProvider = peeEntryDaoProvider;
    this.voidScheduleDaoProvider = voidScheduleDaoProvider;
    this.waterIntakeDaoProvider = waterIntakeDaoProvider;
    this.drinkEntryDaoProvider = drinkEntryDaoProvider;
    this.exerciseScheduleDaoProvider = exerciseScheduleDaoProvider;
    this.exerciseCompletionDaoProvider = exerciseCompletionDaoProvider;
    this.appSettingsDaoProvider = appSettingsDaoProvider;
  }

  @Override
  public RestoreDataUseCase get() {
    return newInstance(databaseProvider.get(), peeEntryDaoProvider.get(), voidScheduleDaoProvider.get(), waterIntakeDaoProvider.get(), drinkEntryDaoProvider.get(), exerciseScheduleDaoProvider.get(), exerciseCompletionDaoProvider.get(), appSettingsDaoProvider.get());
  }

  public static RestoreDataUseCase_Factory create(
      javax.inject.Provider<LaNiaoDatabase> databaseProvider,
      javax.inject.Provider<PeeEntryDao> peeEntryDaoProvider,
      javax.inject.Provider<VoidScheduleDao> voidScheduleDaoProvider,
      javax.inject.Provider<WaterIntakeDao> waterIntakeDaoProvider,
      javax.inject.Provider<DrinkEntryDao> drinkEntryDaoProvider,
      javax.inject.Provider<ExerciseScheduleDao> exerciseScheduleDaoProvider,
      javax.inject.Provider<ExerciseCompletionDao> exerciseCompletionDaoProvider,
      javax.inject.Provider<AppSettingsDao> appSettingsDaoProvider) {
    return new RestoreDataUseCase_Factory(Providers.asDaggerProvider(databaseProvider), Providers.asDaggerProvider(peeEntryDaoProvider), Providers.asDaggerProvider(voidScheduleDaoProvider), Providers.asDaggerProvider(waterIntakeDaoProvider), Providers.asDaggerProvider(drinkEntryDaoProvider), Providers.asDaggerProvider(exerciseScheduleDaoProvider), Providers.asDaggerProvider(exerciseCompletionDaoProvider), Providers.asDaggerProvider(appSettingsDaoProvider));
  }

  public static RestoreDataUseCase_Factory create(Provider<LaNiaoDatabase> databaseProvider,
      Provider<PeeEntryDao> peeEntryDaoProvider, Provider<VoidScheduleDao> voidScheduleDaoProvider,
      Provider<WaterIntakeDao> waterIntakeDaoProvider,
      Provider<DrinkEntryDao> drinkEntryDaoProvider,
      Provider<ExerciseScheduleDao> exerciseScheduleDaoProvider,
      Provider<ExerciseCompletionDao> exerciseCompletionDaoProvider,
      Provider<AppSettingsDao> appSettingsDaoProvider) {
    return new RestoreDataUseCase_Factory(databaseProvider, peeEntryDaoProvider, voidScheduleDaoProvider, waterIntakeDaoProvider, drinkEntryDaoProvider, exerciseScheduleDaoProvider, exerciseCompletionDaoProvider, appSettingsDaoProvider);
  }

  public static RestoreDataUseCase newInstance(LaNiaoDatabase database, PeeEntryDao peeEntryDao,
      VoidScheduleDao voidScheduleDao, WaterIntakeDao waterIntakeDao, DrinkEntryDao drinkEntryDao,
      ExerciseScheduleDao exerciseScheduleDao, ExerciseCompletionDao exerciseCompletionDao,
      AppSettingsDao appSettingsDao) {
    return new RestoreDataUseCase(database, peeEntryDao, voidScheduleDao, waterIntakeDao, drinkEntryDao, exerciseScheduleDao, exerciseCompletionDao, appSettingsDao);
  }
}
