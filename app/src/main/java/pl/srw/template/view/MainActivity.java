package pl.srw.template.view;

import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import pl.srw.template.R;
import pl.srw.template.TemplateApplication;
import pl.srw.template.core.view.BaseActivity;
import pl.srw.template.core.view.delegate.presenter.PresenterHandlingDelegate;
import pl.srw.template.core.view.delegate.presenter.PresenterOwner;
import pl.srw.template.core.view.delegate.presenter.SinglePresenterHandlingDelegate;
import pl.srw.template.presenter.MainView;
import pl.srw.template.presenter.MainViewPresenter;

public class MainActivity extends BaseActivity implements MainView, PresenterOwner {

    @Bind(R.id.hello) TextView helloView;

    @Inject MainViewPresenter presenter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public PresenterHandlingDelegate createPresenterDelegate() {
        return new SinglePresenterHandlingDelegate(this, presenter);
    }

    @Override
    public void injectDependencies() {
        TemplateApplication.getDependencies(this).getMainActivityComponent().inject(this);
    }

    @Override
    protected void resetDependencies() {
        TemplateApplication.getDependencies(this).releaseMainActivityComponent();
    }

    @Override
    public void showHelloWorld() {
        helloView.setText(R.string.hello_world);
    }

    @Override
    public void showHelloWorldAgain() {
        helloView.setText(R.string.hello_again);
    }
}
