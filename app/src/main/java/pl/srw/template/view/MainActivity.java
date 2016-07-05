package pl.srw.template.view;

import android.view.View;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import pl.srw.template.R;
import pl.srw.template.TodosApplication;
import pl.srw.template.core.MvpActivity;
import pl.srw.template.core.view.delegate.presenter.PresenterHandlingDelegate;
import pl.srw.template.core.view.delegate.presenter.PresenterOwner;
import pl.srw.template.core.view.delegate.presenter.SinglePresenterHandlingDelegate;
import pl.srw.template.di.component.MainActivityComponent;
import pl.srw.template.presenter.MainViewPresenter;

public class MainActivity extends MvpActivity<MainActivityComponent>
        implements MainViewPresenter.MainView, PresenterOwner {

    @Bind(R.id.add) View addView;

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
    public MainActivityComponent prepareComponent() {
        return TodosApplication.get(this)
                .getApplicationComponent()
                .getMainActivityComponent();
    }

    @OnClick(R.id.add)
    public void onAddViewClicked() {
        presenter.addClicked();
    }

    @Override
    public void showAddView() {
        changeFragmentWithStack(AddFragment.newInstance(), null);
    }

    @Override
    public void showListView() {
        changeFragment(ListFragment.newInstance(), null);
    }

    @Override
    public void onBackPressed() {
        presenter.backPressed();
        super.onBackPressed();
    }

    @Override
    public void hideAddButton() {
        addView.setVisibility(View.GONE);
    }

    @Override
    public void showAddButton() {
        addView.setVisibility(View.VISIBLE);
    }
}
