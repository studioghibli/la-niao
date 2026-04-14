package com.laniao;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.laniao.data.local.LaNiaoDatabase;
import com.laniao.data.local.dao.AppSettingsDao;
import com.laniao.data.local.dao.DrinkEntryDao;
import com.laniao.data.local.dao.ExerciseCompletionDao;
import com.laniao.data.local.dao.ExerciseScheduleDao;
import com.laniao.data.local.dao.ManuallyMissedTimeDao;
import com.laniao.data.local.dao.PeeEntryDao;
import com.laniao.data.local.dao.VoidScheduleDao;
import com.laniao.data.local.dao.WaterIntakeDao;
import com.laniao.data.repository.DrinkEntryRepositoryImpl;
import com.laniao.data.repository.ExerciseRepositoryImpl;
import com.laniao.data.repository.ManuallyMissedTimeRepositoryImpl;
import com.laniao.data.repository.PeeEntryRepositoryImpl;
import com.laniao.data.repository.VoidScheduleRepositoryImpl;
import com.laniao.di.DatabaseModule_ProvideAppSettingsDaoFactory;
import com.laniao.di.DatabaseModule_ProvideDatabaseFactory;
import com.laniao.di.DatabaseModule_ProvideDrinkEntryDaoFactory;
import com.laniao.di.DatabaseModule_ProvideExerciseCompletionDaoFactory;
import com.laniao.di.DatabaseModule_ProvideExerciseScheduleDaoFactory;
import com.laniao.di.DatabaseModule_ProvideManuallyMissedTimeDaoFactory;
import com.laniao.di.DatabaseModule_ProvidePeeEntryDaoFactory;
import com.laniao.di.DatabaseModule_ProvideVoidScheduleDaoFactory;
import com.laniao.di.DatabaseModule_ProvideWaterIntakeDaoFactory;
import com.laniao.di.UtilModule_ProvideClockFactory;
import com.laniao.di.UtilModule_ProvideDispatcherProviderFactory;
import com.laniao.domain.repository.ManuallyMissedTimeRepository;
import com.laniao.domain.usecase.AddDrinkEntryUseCase;
import com.laniao.domain.usecase.AddPeeEntryUseCase;
import com.laniao.domain.usecase.BackupDataUseCase;
import com.laniao.domain.usecase.CheckScheduleOverlapUseCase;
import com.laniao.domain.usecase.ClearAllDataUseCase;
import com.laniao.domain.usecase.CompleteExerciseUseCase;
import com.laniao.domain.usecase.CreateVoidScheduleUseCase;
import com.laniao.domain.usecase.DeleteDrinkEntryUseCase;
import com.laniao.domain.usecase.DeletePeeEntryUseCase;
import com.laniao.domain.usecase.DeleteVoidScheduleUseCase;
import com.laniao.domain.usecase.ExportCsvUseCase;
import com.laniao.domain.usecase.GetActiveSchedulesUseCase;
import com.laniao.domain.usecase.GetAllSchedulesUseCase;
import com.laniao.domain.usecase.GetAverageVoidGapUseCase;
import com.laniao.domain.usecase.GetDayTimelineUseCase;
import com.laniao.domain.usecase.GetDaysWithEntriesUseCase;
import com.laniao.domain.usecase.GetExerciseCompletionUseCase;
import com.laniao.domain.usecase.GetExerciseStreakUseCase;
import com.laniao.domain.usecase.GetHydrationStreakUseCase;
import com.laniao.domain.usecase.GetLiquidIntakeUseCase;
import com.laniao.domain.usecase.GetMonthDayStatsUseCase;
import com.laniao.domain.usecase.GetPeeEntryByIdUseCase;
import com.laniao.domain.usecase.GetRecentEntriesUseCase;
import com.laniao.domain.usecase.GetScheduleAdherenceUseCase;
import com.laniao.domain.usecase.GetScheduleProgressUseCase;
import com.laniao.domain.usecase.GetTodayDrinksUseCase;
import com.laniao.domain.usecase.GetTodayExerciseStatusUseCase;
import com.laniao.domain.usecase.GetTodaySummaryUseCase;
import com.laniao.domain.usecase.GetUnclaimedScheduledTimesUseCase;
import com.laniao.domain.usecase.GetVoidFrequencyUseCase;
import com.laniao.domain.usecase.ManageExerciseScheduleUseCase;
import com.laniao.domain.usecase.RecalculateScheduledTimesUseCase;
import com.laniao.domain.usecase.RedoUseCase;
import com.laniao.domain.usecase.RestoreDataUseCase;
import com.laniao.domain.usecase.UndoRedoManager;
import com.laniao.domain.usecase.UndoUseCase;
import com.laniao.domain.usecase.UpdatePeeEntryUseCase;
import com.laniao.domain.usecase.UpdateVoidScheduleExpiryUseCase;
import com.laniao.presentation.viewmodel.AddEntryViewModel;
import com.laniao.presentation.viewmodel.AddEntryViewModel_HiltModules;
import com.laniao.presentation.viewmodel.AddEntryViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.laniao.presentation.viewmodel.AddEntryViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.laniao.presentation.viewmodel.CalendarViewModel;
import com.laniao.presentation.viewmodel.CalendarViewModel_HiltModules;
import com.laniao.presentation.viewmodel.CalendarViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.laniao.presentation.viewmodel.CalendarViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.laniao.presentation.viewmodel.HomeViewModel;
import com.laniao.presentation.viewmodel.HomeViewModel_HiltModules;
import com.laniao.presentation.viewmodel.HomeViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.laniao.presentation.viewmodel.HomeViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.laniao.presentation.viewmodel.ScheduleViewModel;
import com.laniao.presentation.viewmodel.ScheduleViewModel_HiltModules;
import com.laniao.presentation.viewmodel.ScheduleViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.laniao.presentation.viewmodel.ScheduleViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.laniao.presentation.viewmodel.SettingsViewModel;
import com.laniao.presentation.viewmodel.SettingsViewModel_HiltModules;
import com.laniao.presentation.viewmodel.SettingsViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.laniao.presentation.viewmodel.SettingsViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.laniao.presentation.viewmodel.StatisticsViewModel;
import com.laniao.presentation.viewmodel.StatisticsViewModel_HiltModules;
import com.laniao.presentation.viewmodel.StatisticsViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.laniao.presentation.viewmodel.StatisticsViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.laniao.util.Clock;
import com.laniao.util.DispatcherProvider;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerLaNiaoApplication_HiltComponents_SingletonC {
  private DaggerLaNiaoApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public LaNiaoApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements LaNiaoApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public LaNiaoApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements LaNiaoApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public LaNiaoApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements LaNiaoApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public LaNiaoApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements LaNiaoApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public LaNiaoApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements LaNiaoApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public LaNiaoApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements LaNiaoApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public LaNiaoApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements LaNiaoApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public LaNiaoApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends LaNiaoApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends LaNiaoApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends LaNiaoApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends LaNiaoApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(6).put(AddEntryViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, AddEntryViewModel_HiltModules.KeyModule.provide()).put(CalendarViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, CalendarViewModel_HiltModules.KeyModule.provide()).put(HomeViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, HomeViewModel_HiltModules.KeyModule.provide()).put(ScheduleViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, ScheduleViewModel_HiltModules.KeyModule.provide()).put(SettingsViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, SettingsViewModel_HiltModules.KeyModule.provide()).put(StatisticsViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, StatisticsViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }
  }

  private static final class ViewModelCImpl extends LaNiaoApplication_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AddEntryViewModel> addEntryViewModelProvider;

    private Provider<CalendarViewModel> calendarViewModelProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private Provider<ScheduleViewModel> scheduleViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private Provider<StatisticsViewModel> statisticsViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    private AddPeeEntryUseCase addPeeEntryUseCase() {
      return new AddPeeEntryUseCase(singletonCImpl.peeEntryRepositoryImplProvider.get(), singletonCImpl.provideClockProvider.get());
    }

    private RecalculateScheduledTimesUseCase recalculateScheduledTimesUseCase() {
      return new RecalculateScheduledTimesUseCase(singletonCImpl.peeEntryRepositoryImplProvider.get(), singletonCImpl.voidScheduleRepositoryImplProvider.get());
    }

    private UpdatePeeEntryUseCase updatePeeEntryUseCase() {
      return new UpdatePeeEntryUseCase(singletonCImpl.peeEntryRepositoryImplProvider.get(), recalculateScheduledTimesUseCase());
    }

    private DeletePeeEntryUseCase deletePeeEntryUseCase() {
      return new DeletePeeEntryUseCase(singletonCImpl.peeEntryRepositoryImplProvider.get(), recalculateScheduledTimesUseCase());
    }

    private GetPeeEntryByIdUseCase getPeeEntryByIdUseCase() {
      return new GetPeeEntryByIdUseCase(singletonCImpl.peeEntryRepositoryImplProvider.get());
    }

    private GetUnclaimedScheduledTimesUseCase getUnclaimedScheduledTimesUseCase() {
      return new GetUnclaimedScheduledTimesUseCase(singletonCImpl.voidScheduleRepositoryImplProvider.get(), singletonCImpl.peeEntryRepositoryImplProvider.get());
    }

    private GetDaysWithEntriesUseCase getDaysWithEntriesUseCase() {
      return new GetDaysWithEntriesUseCase(singletonCImpl.peeEntryRepositoryImplProvider.get());
    }

    private GetDayTimelineUseCase getDayTimelineUseCase() {
      return new GetDayTimelineUseCase(singletonCImpl.peeEntryRepositoryImplProvider.get(), singletonCImpl.drinkEntryRepositoryImplProvider.get(), singletonCImpl.exerciseRepositoryImplProvider.get(), singletonCImpl.voidScheduleRepositoryImplProvider.get(), singletonCImpl.bindManuallyMissedTimeRepositoryProvider.get());
    }

    private GetMonthDayStatsUseCase getMonthDayStatsUseCase() {
      return new GetMonthDayStatsUseCase(singletonCImpl.peeEntryRepositoryImplProvider.get(), singletonCImpl.drinkEntryRepositoryImplProvider.get(), singletonCImpl.exerciseRepositoryImplProvider.get(), singletonCImpl.voidScheduleRepositoryImplProvider.get(), singletonCImpl.appSettingsDao());
    }

    private GetTodaySummaryUseCase getTodaySummaryUseCase() {
      return new GetTodaySummaryUseCase(singletonCImpl.peeEntryRepositoryImplProvider.get(), singletonCImpl.voidScheduleRepositoryImplProvider.get(), singletonCImpl.provideClockProvider.get());
    }

    private GetRecentEntriesUseCase getRecentEntriesUseCase() {
      return new GetRecentEntriesUseCase(singletonCImpl.peeEntryRepositoryImplProvider.get(), singletonCImpl.provideClockProvider.get());
    }

    private AddDrinkEntryUseCase addDrinkEntryUseCase() {
      return new AddDrinkEntryUseCase(singletonCImpl.drinkEntryRepositoryImplProvider.get());
    }

    private DeleteDrinkEntryUseCase deleteDrinkEntryUseCase() {
      return new DeleteDrinkEntryUseCase(singletonCImpl.drinkEntryRepositoryImplProvider.get());
    }

    private GetTodayDrinksUseCase getTodayDrinksUseCase() {
      return new GetTodayDrinksUseCase(singletonCImpl.drinkEntryRepositoryImplProvider.get(), singletonCImpl.provideClockProvider.get());
    }

    private GetTodayExerciseStatusUseCase getTodayExerciseStatusUseCase() {
      return new GetTodayExerciseStatusUseCase(singletonCImpl.exerciseRepositoryImplProvider.get());
    }

    private CompleteExerciseUseCase completeExerciseUseCase() {
      return new CompleteExerciseUseCase(singletonCImpl.exerciseRepositoryImplProvider.get(), singletonCImpl.provideClockProvider.get());
    }

    private GetExerciseStreakUseCase getExerciseStreakUseCase() {
      return new GetExerciseStreakUseCase(singletonCImpl.exerciseRepositoryImplProvider.get());
    }

    private GetHydrationStreakUseCase getHydrationStreakUseCase() {
      return new GetHydrationStreakUseCase(singletonCImpl.drinkEntryRepositoryImplProvider.get(), singletonCImpl.appSettingsDao());
    }

    private GetScheduleProgressUseCase getScheduleProgressUseCase() {
      return new GetScheduleProgressUseCase(singletonCImpl.voidScheduleRepositoryImplProvider.get(), singletonCImpl.peeEntryRepositoryImplProvider.get(), singletonCImpl.bindManuallyMissedTimeRepositoryProvider.get(), singletonCImpl.provideClockProvider.get());
    }

    private GetActiveSchedulesUseCase getActiveSchedulesUseCase() {
      return new GetActiveSchedulesUseCase(singletonCImpl.voidScheduleRepositoryImplProvider.get());
    }

    private GetAllSchedulesUseCase getAllSchedulesUseCase() {
      return new GetAllSchedulesUseCase(singletonCImpl.voidScheduleRepositoryImplProvider.get());
    }

    private CreateVoidScheduleUseCase createVoidScheduleUseCase() {
      return new CreateVoidScheduleUseCase(singletonCImpl.voidScheduleRepositoryImplProvider.get(), singletonCImpl.peeEntryRepositoryImplProvider.get(), singletonCImpl.provideClockProvider.get());
    }

    private UpdateVoidScheduleExpiryUseCase updateVoidScheduleExpiryUseCase() {
      return new UpdateVoidScheduleExpiryUseCase(singletonCImpl.voidScheduleRepositoryImplProvider.get(), singletonCImpl.peeEntryRepositoryImplProvider.get());
    }

    private DeleteVoidScheduleUseCase deleteVoidScheduleUseCase() {
      return new DeleteVoidScheduleUseCase(singletonCImpl.voidScheduleRepositoryImplProvider.get(), singletonCImpl.peeEntryRepositoryImplProvider.get());
    }

    private CheckScheduleOverlapUseCase checkScheduleOverlapUseCase() {
      return new CheckScheduleOverlapUseCase(singletonCImpl.voidScheduleRepositoryImplProvider.get());
    }

    private ManageExerciseScheduleUseCase manageExerciseScheduleUseCase() {
      return new ManageExerciseScheduleUseCase(singletonCImpl.exerciseRepositoryImplProvider.get());
    }

    private ExportCsvUseCase exportCsvUseCase() {
      return new ExportCsvUseCase(singletonCImpl.peeEntryRepositoryImplProvider.get(), singletonCImpl.drinkEntryRepositoryImplProvider.get(), singletonCImpl.exerciseRepositoryImplProvider.get());
    }

    private BackupDataUseCase backupDataUseCase() {
      return new BackupDataUseCase(singletonCImpl.peeEntryDao(), singletonCImpl.voidScheduleDao(), singletonCImpl.waterIntakeDao(), singletonCImpl.drinkEntryDao(), singletonCImpl.exerciseScheduleDao(), singletonCImpl.exerciseCompletionDao(), singletonCImpl.appSettingsDao());
    }

    private RestoreDataUseCase restoreDataUseCase() {
      return new RestoreDataUseCase(singletonCImpl.provideDatabaseProvider.get(), singletonCImpl.peeEntryDao(), singletonCImpl.voidScheduleDao(), singletonCImpl.waterIntakeDao(), singletonCImpl.drinkEntryDao(), singletonCImpl.exerciseScheduleDao(), singletonCImpl.exerciseCompletionDao(), singletonCImpl.appSettingsDao());
    }

    private ClearAllDataUseCase clearAllDataUseCase() {
      return new ClearAllDataUseCase(singletonCImpl.provideDatabaseProvider.get(), singletonCImpl.peeEntryDao(), singletonCImpl.voidScheduleDao(), singletonCImpl.waterIntakeDao(), singletonCImpl.drinkEntryDao(), singletonCImpl.exerciseScheduleDao(), singletonCImpl.exerciseCompletionDao(), singletonCImpl.appSettingsDao());
    }

    private UndoUseCase undoUseCase() {
      return new UndoUseCase(singletonCImpl.undoRedoManagerProvider.get(), singletonCImpl.peeEntryRepositoryImplProvider.get(), singletonCImpl.drinkEntryRepositoryImplProvider.get(), singletonCImpl.exerciseRepositoryImplProvider.get(), singletonCImpl.voidScheduleRepositoryImplProvider.get(), singletonCImpl.bindManuallyMissedTimeRepositoryProvider.get());
    }

    private RedoUseCase redoUseCase() {
      return new RedoUseCase(singletonCImpl.undoRedoManagerProvider.get(), singletonCImpl.peeEntryRepositoryImplProvider.get(), singletonCImpl.drinkEntryRepositoryImplProvider.get(), singletonCImpl.exerciseRepositoryImplProvider.get(), singletonCImpl.voidScheduleRepositoryImplProvider.get(), singletonCImpl.bindManuallyMissedTimeRepositoryProvider.get());
    }

    private GetVoidFrequencyUseCase getVoidFrequencyUseCase() {
      return new GetVoidFrequencyUseCase(singletonCImpl.peeEntryRepositoryImplProvider.get());
    }

    private GetScheduleAdherenceUseCase getScheduleAdherenceUseCase() {
      return new GetScheduleAdherenceUseCase(singletonCImpl.voidScheduleRepositoryImplProvider.get(), singletonCImpl.peeEntryRepositoryImplProvider.get());
    }

    private GetLiquidIntakeUseCase getLiquidIntakeUseCase() {
      return new GetLiquidIntakeUseCase(singletonCImpl.drinkEntryRepositoryImplProvider.get());
    }

    private GetExerciseCompletionUseCase getExerciseCompletionUseCase() {
      return new GetExerciseCompletionUseCase(singletonCImpl.exerciseRepositoryImplProvider.get());
    }

    private GetAverageVoidGapUseCase getAverageVoidGapUseCase() {
      return new GetAverageVoidGapUseCase(singletonCImpl.peeEntryRepositoryImplProvider.get(), singletonCImpl.voidScheduleRepositoryImplProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.addEntryViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.calendarViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.scheduleViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.statisticsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(6).put(AddEntryViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) addEntryViewModelProvider)).put(CalendarViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) calendarViewModelProvider)).put(HomeViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) homeViewModelProvider)).put(ScheduleViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) scheduleViewModelProvider)).put(SettingsViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) settingsViewModelProvider)).put(StatisticsViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) statisticsViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.laniao.presentation.viewmodel.AddEntryViewModel 
          return (T) new AddEntryViewModel(viewModelCImpl.addPeeEntryUseCase(), viewModelCImpl.updatePeeEntryUseCase(), viewModelCImpl.deletePeeEntryUseCase(), viewModelCImpl.getPeeEntryByIdUseCase(), viewModelCImpl.getUnclaimedScheduledTimesUseCase(), singletonCImpl.provideClockProvider.get(), singletonCImpl.provideDispatcherProvider.get(), viewModelCImpl.savedStateHandle);

          case 1: // com.laniao.presentation.viewmodel.CalendarViewModel 
          return (T) new CalendarViewModel(viewModelCImpl.getDaysWithEntriesUseCase(), viewModelCImpl.getDayTimelineUseCase(), viewModelCImpl.getMonthDayStatsUseCase(), viewModelCImpl.deletePeeEntryUseCase(), viewModelCImpl.addPeeEntryUseCase(), viewModelCImpl.updatePeeEntryUseCase(), singletonCImpl.peeEntryRepositoryImplProvider.get(), singletonCImpl.drinkEntryRepositoryImplProvider.get(), singletonCImpl.exerciseRepositoryImplProvider.get(), singletonCImpl.voidScheduleRepositoryImplProvider.get(), singletonCImpl.bindManuallyMissedTimeRepositoryProvider.get(), singletonCImpl.undoRedoManagerProvider.get(), singletonCImpl.provideClockProvider.get(), singletonCImpl.provideDispatcherProvider.get());

          case 2: // com.laniao.presentation.viewmodel.HomeViewModel 
          return (T) new HomeViewModel(viewModelCImpl.addPeeEntryUseCase(), viewModelCImpl.updatePeeEntryUseCase(), viewModelCImpl.deletePeeEntryUseCase(), viewModelCImpl.getTodaySummaryUseCase(), viewModelCImpl.getRecentEntriesUseCase(), viewModelCImpl.addDrinkEntryUseCase(), viewModelCImpl.deleteDrinkEntryUseCase(), viewModelCImpl.getTodayDrinksUseCase(), singletonCImpl.drinkEntryRepositoryImplProvider.get(), singletonCImpl.peeEntryRepositoryImplProvider.get(), singletonCImpl.exerciseRepositoryImplProvider.get(), viewModelCImpl.getTodayExerciseStatusUseCase(), viewModelCImpl.completeExerciseUseCase(), viewModelCImpl.getExerciseStreakUseCase(), viewModelCImpl.getHydrationStreakUseCase(), viewModelCImpl.getScheduleProgressUseCase(), singletonCImpl.undoRedoManagerProvider.get(), singletonCImpl.provideClockProvider.get(), singletonCImpl.provideDispatcherProvider.get());

          case 3: // com.laniao.presentation.viewmodel.ScheduleViewModel 
          return (T) new ScheduleViewModel(viewModelCImpl.getActiveSchedulesUseCase(), viewModelCImpl.getAllSchedulesUseCase(), viewModelCImpl.getScheduleProgressUseCase(), viewModelCImpl.createVoidScheduleUseCase(), viewModelCImpl.updateVoidScheduleExpiryUseCase(), viewModelCImpl.deleteVoidScheduleUseCase(), viewModelCImpl.checkScheduleOverlapUseCase(), viewModelCImpl.manageExerciseScheduleUseCase(), viewModelCImpl.getTodayExerciseStatusUseCase(), singletonCImpl.exerciseRepositoryImplProvider.get(), singletonCImpl.undoRedoManagerProvider.get(), singletonCImpl.provideDispatcherProvider.get());

          case 4: // com.laniao.presentation.viewmodel.SettingsViewModel 
          return (T) new SettingsViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.appSettingsDao(), viewModelCImpl.exportCsvUseCase(), viewModelCImpl.backupDataUseCase(), viewModelCImpl.restoreDataUseCase(), viewModelCImpl.clearAllDataUseCase(), singletonCImpl.undoRedoManagerProvider.get(), viewModelCImpl.undoUseCase(), viewModelCImpl.redoUseCase(), singletonCImpl.provideDispatcherProvider.get());

          case 5: // com.laniao.presentation.viewmodel.StatisticsViewModel 
          return (T) new StatisticsViewModel(viewModelCImpl.getVoidFrequencyUseCase(), viewModelCImpl.getScheduleAdherenceUseCase(), viewModelCImpl.getLiquidIntakeUseCase(), viewModelCImpl.getExerciseCompletionUseCase(), viewModelCImpl.getAverageVoidGapUseCase(), singletonCImpl.appSettingsDao(), singletonCImpl.provideClockProvider.get(), singletonCImpl.provideDispatcherProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends LaNiaoApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends LaNiaoApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends LaNiaoApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<LaNiaoDatabase> provideDatabaseProvider;

    private Provider<PeeEntryRepositoryImpl> peeEntryRepositoryImplProvider;

    private Provider<Clock> provideClockProvider;

    private Provider<VoidScheduleRepositoryImpl> voidScheduleRepositoryImplProvider;

    private Provider<DispatcherProvider> provideDispatcherProvider;

    private Provider<DrinkEntryRepositoryImpl> drinkEntryRepositoryImplProvider;

    private Provider<ExerciseRepositoryImpl> exerciseRepositoryImplProvider;

    private Provider<ManuallyMissedTimeRepositoryImpl> manuallyMissedTimeRepositoryImplProvider;

    private Provider<ManuallyMissedTimeRepository> bindManuallyMissedTimeRepositoryProvider;

    private Provider<UndoRedoManager> undoRedoManagerProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private PeeEntryDao peeEntryDao() {
      return DatabaseModule_ProvidePeeEntryDaoFactory.providePeeEntryDao(provideDatabaseProvider.get());
    }

    private VoidScheduleDao voidScheduleDao() {
      return DatabaseModule_ProvideVoidScheduleDaoFactory.provideVoidScheduleDao(provideDatabaseProvider.get());
    }

    private DrinkEntryDao drinkEntryDao() {
      return DatabaseModule_ProvideDrinkEntryDaoFactory.provideDrinkEntryDao(provideDatabaseProvider.get());
    }

    private ExerciseScheduleDao exerciseScheduleDao() {
      return DatabaseModule_ProvideExerciseScheduleDaoFactory.provideExerciseScheduleDao(provideDatabaseProvider.get());
    }

    private ExerciseCompletionDao exerciseCompletionDao() {
      return DatabaseModule_ProvideExerciseCompletionDaoFactory.provideExerciseCompletionDao(provideDatabaseProvider.get());
    }

    private ManuallyMissedTimeDao manuallyMissedTimeDao() {
      return DatabaseModule_ProvideManuallyMissedTimeDaoFactory.provideManuallyMissedTimeDao(provideDatabaseProvider.get());
    }

    private AppSettingsDao appSettingsDao() {
      return DatabaseModule_ProvideAppSettingsDaoFactory.provideAppSettingsDao(provideDatabaseProvider.get());
    }

    private WaterIntakeDao waterIntakeDao() {
      return DatabaseModule_ProvideWaterIntakeDaoFactory.provideWaterIntakeDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<LaNiaoDatabase>(singletonCImpl, 1));
      this.peeEntryRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<PeeEntryRepositoryImpl>(singletonCImpl, 0));
      this.provideClockProvider = DoubleCheck.provider(new SwitchingProvider<Clock>(singletonCImpl, 2));
      this.voidScheduleRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<VoidScheduleRepositoryImpl>(singletonCImpl, 3));
      this.provideDispatcherProvider = DoubleCheck.provider(new SwitchingProvider<DispatcherProvider>(singletonCImpl, 4));
      this.drinkEntryRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<DrinkEntryRepositoryImpl>(singletonCImpl, 5));
      this.exerciseRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<ExerciseRepositoryImpl>(singletonCImpl, 6));
      this.manuallyMissedTimeRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 7);
      this.bindManuallyMissedTimeRepositoryProvider = DoubleCheck.provider((Provider) manuallyMissedTimeRepositoryImplProvider);
      this.undoRedoManagerProvider = DoubleCheck.provider(new SwitchingProvider<UndoRedoManager>(singletonCImpl, 8));
    }

    @Override
    public void injectLaNiaoApplication(LaNiaoApplication laNiaoApplication) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.laniao.data.repository.PeeEntryRepositoryImpl 
          return (T) new PeeEntryRepositoryImpl(singletonCImpl.peeEntryDao());

          case 1: // com.laniao.data.local.LaNiaoDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 2: // com.laniao.util.Clock 
          return (T) UtilModule_ProvideClockFactory.provideClock();

          case 3: // com.laniao.data.repository.VoidScheduleRepositoryImpl 
          return (T) new VoidScheduleRepositoryImpl(singletonCImpl.voidScheduleDao());

          case 4: // com.laniao.util.DispatcherProvider 
          return (T) UtilModule_ProvideDispatcherProviderFactory.provideDispatcherProvider();

          case 5: // com.laniao.data.repository.DrinkEntryRepositoryImpl 
          return (T) new DrinkEntryRepositoryImpl(singletonCImpl.drinkEntryDao());

          case 6: // com.laniao.data.repository.ExerciseRepositoryImpl 
          return (T) new ExerciseRepositoryImpl(singletonCImpl.exerciseScheduleDao(), singletonCImpl.exerciseCompletionDao());

          case 7: // com.laniao.data.repository.ManuallyMissedTimeRepositoryImpl 
          return (T) new ManuallyMissedTimeRepositoryImpl(singletonCImpl.manuallyMissedTimeDao());

          case 8: // com.laniao.domain.usecase.UndoRedoManager 
          return (T) new UndoRedoManager();

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
