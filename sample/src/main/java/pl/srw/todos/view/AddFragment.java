package pl.srw.todos.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.srw.mfvp.MvpFragment;
import pl.srw.mfvp.view.fragment.MvpFragmentScopedFragment;
import pl.srw.todos.R;
import pl.srw.todos.di.component.AddFragmentComponent;
import pl.srw.todos.di.component.MainActivityComponent;
import pl.srw.todos.presenter.AddViewPresenter;

public class AddFragment extends MvpFragment<AddViewPresenter>
        implements AddViewPresenter.AddView,
        MvpFragmentScopedFragment<AddFragmentComponent, MainActivityComponent> {

    @Bind(R.id.add_text) EditText textView;
    @Bind(R.id.add_is_done) CheckBox doneView;

    @Inject AddViewPresenter presenter;

    public AddFragment() {
        // Required empty public constructor
    }

    public static AddFragment newInstance() {
        return new AddFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public AddFragmentComponent prepareComponent(MainActivityComponent activityComponent) {
        return activityComponent.getAddFragmentComponent();
    }

    @Override
    public AddViewPresenter getPresenter() {
        return presenter;
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
