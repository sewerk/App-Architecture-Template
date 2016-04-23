package pl.srw.template.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import pl.srw.template.R;
import pl.srw.template.TodosApplication;
import pl.srw.template.core.view.BaseFragment;
import pl.srw.template.core.view.delegate.presenter.EachViewNewPresenterOwner;
import pl.srw.template.core.view.delegate.presenter.PresenterHandlingDelegate;
import pl.srw.template.core.view.delegate.presenter.SinglePresenterHandlingDelegate;
import pl.srw.template.presenter.AddViewPresenter;

public class AddFragment extends BaseFragment
        implements EachViewNewPresenterOwner, AddViewPresenter.AddView {

    @Bind(R.id.add_text) EditText textView;
    @Bind(R.id.add_is_done) CheckBox doneView;

    @Inject AddViewPresenter presenter;

    public AddFragment() {
        // Required empty public constructor
    }

    public static AddFragment newInstance() {
        AddFragment fragment = new AddFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void injectDependencies() {
        TodosApplication.getDependencies(getActivity()).getAddFragmentComponent().inject(this);
    }

    @Override
    public void resetDependencies() {
        TodosApplication.getDependencies(getActivity()).releaseAddFragmentComponent();
    }

    @Override
    public PresenterHandlingDelegate createPresenterDelegate() {
        return new SinglePresenterHandlingDelegate(this, presenter);
    }

    @OnClick(R.id.add_add)
    public void onAddBtnClicked() {
        presenter.addClicked(doneView.isChecked(), textView.getText().toString());
    }

    @Override
    public void close() {
        getActivity().onBackPressed();
    }
}
