package pl.srw.todos.view;

import android.os.Bundle;
import android.view.View;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.srw.mfvp.MvpActivity;
import pl.srw.todos.R;
import pl.srw.todos.TodosApplication;
import pl.srw.mfvp.presenter.PresenterHandlingDelegate;
import pl.srw.mfvp.presenter.PresenterOwner;
import pl.srw.mfvp.presenter.SinglePresenterHandlingDelegate;
import pl.srw.todos.di.component.MainActivityComponent;
import pl.srw.todos.presenter.MainViewPresenter;

public class MainActivity extends MvpActivity<MainActivityComponent>
        implements MainViewPresenter.MainView, PresenterOwner {

    @Bind(R.id.add) View addView;

    @Inject MainViewPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

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
        changeFragmentWithStack(R.id.fragment, AddFragment.newInstance(), null);
    }

    @Override
    public void showListView() {
        changeFragment(R.id.fragment, ListFragment.newInstance(), null);
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
