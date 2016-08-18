package pl.srw.todos.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collection;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.srw.todos.R;
import pl.srw.mfvp.view.fragment.MvpActivityScopedFragment;
import pl.srw.mfvp.MvpFragment;
import pl.srw.mfvp.view.delegate.presenter.PresenterHandlingDelegate;
import pl.srw.mfvp.view.delegate.presenter.PresenterOwner;
import pl.srw.mfvp.view.delegate.presenter.SinglePresenterHandlingDelegate;
import pl.srw.todos.model.Todo;
import pl.srw.todos.presenter.ListViewPresenter;

public class ListFragment extends MvpFragment
        implements ListViewPresenter.ListView, PresenterOwner, MvpActivityScopedFragment {

    @Inject ListViewPresenter presenter;

    @Bind(R.id.list)
    LinearLayout listView;

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
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
    public PresenterHandlingDelegate createPresenterDelegate() {
        return new SinglePresenterHandlingDelegate(this, presenter);
    }

    @Override
    public void showEntries(Collection<Todo> entries) {
        listView.removeAllViews();
        for (Todo t : entries) {
            final LinearLayout row = createRow(t);
            listView.addView(row);
        }
    }

    @NonNull
    private LinearLayout createRow(final Todo t) {
        final LinearLayout row = new LinearLayout(getContext());
        row.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        row.addView(createCheckBox(t));
        row.addView(createTextView(t));
        return row;
    }

    @NonNull
    private CheckBox createCheckBox(final Todo t) {
        final CheckBox checkBox = new CheckBox(getContext());
        checkBox.setChecked(t.isDone());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.checkboxClickedFor(t);
            }
        });
        return checkBox;
    }

    @NonNull
    private TextView createTextView(Todo t) {
        final TextView textView = new TextView(getContext());
        textView.setText(t.getText());
        return textView;
    }
}
