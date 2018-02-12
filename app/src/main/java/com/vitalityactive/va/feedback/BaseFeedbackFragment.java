package com.vitalityactive.va.feedback;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.BasePresentedFragment;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.feedback.presenter.FeedbackPresenter;

import javax.inject.Inject;

public class BaseFeedbackFragment extends BasePresentedFragment<FeedbackPresenter.UserInterface, FeedbackPresenter> implements FeedbackPresenter.UserInterface {

    @Inject
    FeedbackPresenter presenter;

    private TextView description;
    protected boolean isCMS = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feedback, container, false);
    }


    @Override
    public void activityCreated(@Nullable Bundle savedInstanceState) {
        View parentView = getView();
        if (parentView == null) {
            return;
        }

        setToolbarDrawerIconColourToWhite();
        setUpDetailsView(parentView);

        if (isCMS())
            presenter.fetchFeedbacks();
        else
            description.setText(getString(R.string.settings_feedback_message_1107));
    }

    protected boolean isCMS() {
        return false;
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected FeedbackPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected FeedbackPresenter getPresenter() {
        return presenter;
    }

    public void setUpDetailsView(View parentView) {
        description = parentView.findViewById(R.id.feedback_description);
    }

    @Override
    public void showFeedbacks(@NonNull String feedbacks) {
        if (!TextUtils.isEmpty(feedbacks)) description.setText(Html.fromHtml(feedbacks));
    }

    @Override
    public void showLoadingIndicator() {
        if (getActivity() instanceof BasePresentedActivity)
            ((BasePresentedActivity) getActivity()).showLoadingIndicator();
    }

    @Override
    public void hideLoadingIndicator() {
        if (getActivity() instanceof BasePresentedActivity)
            ((BasePresentedActivity) getActivity()).hideLoadingIndicator();
    }

    @Override
    public void activityDestroyed() {
    }


}
