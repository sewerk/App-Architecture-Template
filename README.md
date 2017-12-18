# Model Fragment-View Presenter

**DEPRECATED** - this code is no longer under development

Small and powerful combination for your Android app architecture: **MVP+Fragments+DI**
 - Activity+Fragment as a view 
 - retain-over-configuration-change presenter
 - automatic dependency injection and scope management
 - base classes for view, presenter and DI component

[![BuddyBuild](https://dashboard.buddybuild.com/api/statusImage?appID=57ab933e40aec601003836de&branch=master&build=latest)](https://dashboard.buddybuild.com/apps/57ab933e40aec601003836de/build/latest)
Get it from [![](https://jitpack.io/v/sewerk/mfvp.svg)](https://jitpack.io/#sewerk/mfvp)

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

    public interface MainView {
        void displayData(Object data);
    }
}
```
### 2. Create DI **component** for Activity, extending `MvpComponent`
I recommend [Dagger 2](https://google.github.io/dagger/) as DI framework
```java
@RetainActivityScope
public interface MainActivityComponent
        extends MvpComponent<MainActivity> { // this component is for MainActivity
}
```
### 3. Create **activity**, by extending `MvpActivity`
```java
public class MainActivity extends MvpActivity<MainActivityComponent>
        implements MainViewPresenter.MainView {

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
    
    @Override
    public void displayData(Object data) {
        // change UI
    }
    
    @Override
    public void changeScreen(Fragment fragment) {
        changeFragment(R.id.fragmentResId, fragment, "optional tag"); // use API from MvpActivity class for proper Fragment scope management
    }
}
```

More can be found in [sample](https://github.com/sewerk/mfvp/tree/master/sample) implementation of 'Todo list' app.

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
