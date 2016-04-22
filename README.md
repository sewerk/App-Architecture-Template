# App-Architecture-Template

This template provides core logic for MVP architecture. Key points are:
 - Model - provides data
 - passive View - fulfill display commands and inform Presenter about user/device input
 - Presenter - contains application business logic, can survive view destruction

## Details

Presenter connects model with view.
It hold current state and lives in scope of view.
This means it is created with associated view but stays alive as long as it is required.
In particular:
- it can be new for each view instance, like forms, where you don't want the same data that were
previously entered to be restored in new view (see `EachViewNewPresenterOwner`),
- or every time the same, to not perform expensive data processing many times,
when going back and forth between screens (see `PresenterOwner`).

When the view is being recreated by configuration change, the presenter instance is being retained
to bind again with new view. This is accomplished with use of Dagger2 scopes (see `RetainActivityScope`
and `DependencyComponentManager`).

View can be Activity, Fragment or View class. View can contain many presenters, but presenter must
manipulate only single view. This way we can separate different presentation responsibility to individual classes,
but still display all within one screen.

Processing starts when Activity, extending `BaseActivity`, is created. At first steps the dependency
injection take place and presenter is created. When activity is destroyed and finishing
then dependency scope is being reset. Activity is also responsible for informing Fragment and View
classes when they are finishing.

Binding view to presenter take place by delegate implementing `LifeCycleDelegating`.
All presenters should extend `BasePresenter`. Initialization of state should happen in `onFirstBind`
method, after view is bind for the first time, whereas restoring state, after every next bind,
in `onNewViewRestoreState` method.

View changes are done through `BasePresenter.UIChange`. In case the view is missing when
data processing has finished, the presenter will hold the change request and execute when new view
is bind.

## Setup

Implementation uses [retrolambda](https://github.com/orfjackal/retrolambda)
so you need to set env. variable `JAVA8_HOME=path_to_java8`

## TODO
1. remove retrolambda
2. separate core to package

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
