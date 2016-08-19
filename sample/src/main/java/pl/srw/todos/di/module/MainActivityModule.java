package pl.srw.todos.di.module;

import dagger.Module;
import dagger.Provides;
import pl.srw.mfvp.di.scope.RetainActivityScope;
import pl.srw.todos.model.FakeRemoteRepo;
import pl.srw.todos.model.Repository;
import pl.srw.todos.view.MainActivity;

/**
 * Dependencies for {@link MainActivity} scope
 */
@Module
public class MainActivityModule {

    // dependency provided on activity level to prove same instance is used in fragments
    @Provides
    @RetainActivityScope
    Repository provideRepository() {
        return new FakeRemoteRepo();
    }
}
