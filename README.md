# Model Fragment-View Presenter

Small and powerful combination for your Android app architecture: **MVP+Fragments+DI**
 - Activity+Fragment as a view 
 - retain-over-configuration-change presenter
 - automatic dependency injection and scope management
 - base classes for view, presenter and DI component

[![BuddyBuild](https://dashboard.buddybuild.com/api/statusImage?appID=57ab933e40aec601003836de&branch=master&build=latest)](https://dashboard.buddybuild.com/apps/57ab933e40aec601003836de/build/latest)
Get it from [![](https://jitpack.io/v/sewerk/mfvp.svg)](https://jitpack.io/#sewerk/mfvp)

### Changes in progress

This API might change until stable version is released. The documentation below might be outdated.
I recommend looking into `sample/` module for correct setup

## How to start

### 0. Configure dependency
```groovy
    allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }
    dependencies {
        ...
        compile 'com.github.sewerk:mfvp:1.0-beta1'
    }
```

### 1. Create **presenter** by extending `MvpPresenter` (with view interface)
```java
@RetainActivityScope
public class MainViewPresenter extends MvpPresenter<MainViewPresenter.MainView> {

    private Object data; // holding state

    @Inject
    public MainViewPresenter() {
    }

    @Override
    protected void onFirstBind() {
        data = model.getData(); // do work once and cache the result in field

        // when data ready, present result
        present(view -> view.displayData(data));
    }

    @Override
    protected void onNewViewRestoreState() {
        // present available data on each next time
        present(...);
    }

    public interface MainView {
        // view change commands:
        void displayData(Object data);
    }
}
```
### 2. Create DI **component** for Activity, extending `MvpActivityScopeComponent`
I recommend [Dagger 2](https://google.github.io/dagger/) as DI framework
```java
@RetainActivityScope
public interface MainActivityComponent
        extends MvpActivityScopeComponent<MainActivity> { // this component is for MainActivity

    // add methods for injecting fragments in activity scope
}
```
### 3. Create **activity**, by extending `MvpActivity`
```java
public class MainActivity extends MvpActivity<MainActivityComponent>
        implements MainViewPresenter.MainView { // fulfill presenter commands

    @Inject MainViewPresenter presenter; // injection will be done in super.onCreate()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        attachPresenter(presenter);
    }

    @Override
    public MainActivityComponent prepareComponent() {
        // create MainActivityComponent
    }
    
    public void changeScreen(Fragment fragment) {
        changeFragment(R.id.fragmentResId, fragment, "optional tag"); // use API from MvpActivity class for proper Fragment scope management
    }
}
```
### 4. Create **fragment**, by extending `MvpFragment`:
```java
// living in activity scope
public class ListFragment extends MvpFragment
        implements MvpActivityScopedFragment<MainActivityComponent>,
        ListViewPresenter.ListView { // fulfill presenter commands

    @Inject ListViewPresenter presenter; // will live until activity is finishing

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachPresenter(presenter);
    }

    @Override
    public void injectDependencies(MainActivityComponent activityComponent) {
        activityComponent.inject(this);
    }
}
```

More can be found in sample implementation of 'Todo list' app.

## Details

### V

View(in MVP) can be Activity or Fragment class. View can contain many presenters, but presenter must
manipulate only single view. This way we can separate different presentation responsibility to individual classes,
but still display all within one screen.

Processing starts when Activity, extending `MvpActivity`, is created. At first steps the dependency
injection take place and, if Activity implements `PresenterOwner`, presenter gets constructed and connected.
Presenter lives until Activity is finishing. Then dependency scope is being reset.

Activity is also responsible for informing Fragment classes when they are finishing.
To make it work use helper methods from `MvpActivity` to change visible Fragments.
Similar flow applies to Fragment, which should extend `MvpFragment`, might implement `PresenterOwner`
and one of `MvpActivityScopedFragment` or `MvpFragmentScopedFragment`.

Simple Fragment without those extensions will also work.
You can also connect your own delegate by implementing `LifeCycleListener` and adding it to view component.

### P

Binding view to presenter take place by `PresenterHandlingDelegate`, which is created when view implements `PresenterOwner`.
All presenters should extend `MvpPresenter`. Initialization of state should be implemented in `onFirstBind`
method, after view is bind for the first time, whereas restoring state, after every next bind,
in `onNewViewRestoreState` method. When scope of presenter comes to end, `onFinish` callback is executed.

View changes are done through `MvpPresenter.UIChange`. In case the view is missing when
data processing has finished, the presenter will hold the change request and execute as soon as new view
is bind.

Presenter holds current state and lives in scope of the "screen".
This means it is created with associated view component but stay alive as long as it is required,
especially retains over configuration changes(like screen rotation).
In particular:
- it can be new for each screen, like forms, where you don't want the same data that were
previously entered, to be restored in new view (Fragment-view will implement `MvpFragmentScopedFragment`),
- or every time the same, to not perform expensive data processing many times,
when going back and forth between screens (Fragment-view will implement `MvpActivityScopedFragment`).

This is accomplished by annotating presenters with Dagger2 scopes: `RetainActivityScope` and `RetainFragmentScope`.

### M

It's all up to you how you want the Model to work.

## License

    Copyright 2016 Kamil Seweryn

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
