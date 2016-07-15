# Model Fragment-View Presenter

This library provides core logic for MVP architecture with use of Activities+Fragments as view components.
Key points are:
 - Model - provides data
 - passive View - fulfill display commands and inform Presenter about user/device input
 - Presenter - contains application business logic, cache model data, can survive view destruction

## Details

### V

View(in MVP) can be Activity or Fragment class. View can contain many presenters, but presenter must
manipulate only single view. This way we can separate different presentation responsibility to individual classes,
but still display all within one screen.

Processing starts when Activity, extending `MvpActivity`, is created. At first steps the dependency
injection take place and, if Activity implements `PresenterOwner`, presenter gets constructed and connected.
Presenter lives until Activity is finishing. Then dependency scope is being reset.

Activity is also responsible for informing Fragment classes when they are finishing.
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

## How to start

1. Add dependency to your `build.gradle` file:
//TODO:
2. Create application class, by extending `MvpApplication`
3. Create Dagger component for Activity, extending `MvpActivityScopeComponent` and optionally `MvpFragmentInActivityScopeComponent`
```java
@RetainActivityScope
public interface MainActivityComponent
        extends MvpActivityScopeComponent<MainActivity>,
        MvpFragmentInActivityScopeComponent<ListFragment> { // ListFragment will get 'end lifecycle' callback when activity is finishing

}
```
4. Create Activity class, by extending `MvpActivity`
```java
public class MainActivity extends MvpActivity<MainActivityComponent>
        implements MainViewPresenter.MainView, PresenterOwner {

    @Inject MainViewPresenter presenter;

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
5. Create presenter class by extending `MvpPresenter`, with view interface
```java
@RetainActivityScope
public class MainViewPresenter extends MvpPresenter<MainViewPresenter.MainView> {

    private Object data; // holding state

    @Inject
    public MainViewPresenter() {
    }

    @Override
    protected void onFirstBind() {
        data = model.getData(); // do work once (asynchronously)

        // when data ready, present result
        present(new UIChange<MainView>() {
            @Override
            public void change(MainView view) {
                // view.displayData(data)
            }
        });
    }

    @Override
    protected void onNewViewRestoreState() {
        // present available data on each next time
    }

    public interface MainView {
        // view change commands:
        void displayData(Object data);
    }
}
```
6. Optionally, create Fragment, by extending `MvpFragment`, living in activity scope:
```java
public class ListFragment extends MvpFragment
        implements MvpActivityScopedFragment, // ListViewPresenter will live until activity is finishing
        PresenterOwner, ListViewPresenter.ListView {

    @Inject ListViewPresenter presenter;

    @Override
    public PresenterHandlingDelegate createPresenterDelegate() {
        return new SinglePresenterHandlingDelegate(this, presenter);
    }
}
```

or in own(fragment) scope:
```java
public class AddFragment extends MvpFragment
        implements MvpFragmentScopedFragment<AddFragmentComponent>, // AddViewPresenter will live until host activity removes this fragment
        PresenterOwner, AddViewPresenter.AddView {

    @Inject AddViewPresenter presenter;

    @Override
    public AddFragmentComponent prepareComponent() {
        // create own component instance
    }
}

@RetainFragmentScope
@Subcomponent
public interface AddFragmentComponent extends MvpFragmentScopeComponent<AddFragment> {

}
```

More can be found in simple implementation of 'Todo list app' which is located in `app` directory.

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
