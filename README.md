# Model Fragment-View Presenter

## TLDR;

This library provides core logic for MVP architecture with use of Activities+Fragments as view components.
Key points are:
 - **M**odel - provides data
 - passive **V**iew - fulfill display commands and inform Presenter about user/device input
 - **P**resenter - contains application business logic, survives view destruction, cache model data

Provide Dagger component instance and injection will be done automatically.

## How to start

### 1. Add **dependency** to your `build.gradle` file
```groovy
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}

dependencies {
    compile 'com.github.sewerk:mfvp:0.9'
}
```
### 2. Create **presenter** by extending `MvpPresenter` (with view interface)
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
        present(new UIChange<MainView>() {
            @Override
            public void change(MainView view) {
                view.displayData(data)
            }
        });
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
### 3. Create Dagger **component** for Activity, extending `MvpActivityScopeComponent`
```java
@RetainActivityScope
public interface MainActivityComponent
        extends MvpActivityScopeComponent<MainActivity>, // this component is for MainActivity
        MvpFragmentInActivityScopeComponent<ListFragment> { // in addition, ListFragment presenter will live until activity is finishing

}
```
### 4. Create **activity**, by extending `MvpActivity`
```java
public class MainActivity extends MvpActivity<MainActivityComponent>
        implements PresenterOwner, // required when using presenter
        MainViewPresenter.MainView { // fulfill presenter commands

    @Inject MainViewPresenter presenter; // injection will be done in super.onCreate()

    @Override
    public PresenterHandlingDelegate createPresenterDelegate() {
        return new SinglePresenterHandlingDelegate(this, presenter);
    }

    @Override
    public MainActivityComponent prepareComponent() {
        // create MainActivityComponent
    }
}
```
### 5. Create **fragment**, by extending `MvpFragment`:
```java
// living in activity scope
public class ListFragment extends MvpFragment
        implements MvpActivityScopedFragment,
        PresenterOwner, // required when using presenter 
        ListViewPresenter.ListView { // fulfill presenter commands

    @Inject ListViewPresenter presenter; // will live until activity is finishing

    @Override
    public PresenterHandlingDelegate createPresenterDelegate() {
        return new SinglePresenterHandlingDelegate(this, presenter);
    }
}

// or in own(fragment) scope
public class AddFragment extends MvpFragment
        implements MvpFragmentScopedFragment<AddFragmentComponent>, 
        PresenterOwner, // required when using presenter 
        AddViewPresenter.AddView { // fulfill presenter commands

    @Inject AddViewPresenter presenter; // will live until fragment is finishing

    @Override
    public AddFragmentComponent prepareComponent() {
        // create AddFragmentComponent
    }
}

@RetainFragmentScope
@Subcomponent
public interface AddFragmentComponent extends MvpFragmentScopeComponent<AddFragment> {

}
```

More can be found in simple implementation of 'Todo list app' which is located in `app` directory.

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
