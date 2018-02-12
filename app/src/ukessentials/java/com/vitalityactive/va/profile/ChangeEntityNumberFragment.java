package com.vitalityactive.va.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.BaseUkePresentedFragment;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.uicomponents.LoadingIndicatorFragment;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.ViewUtilities;

import javax.inject.Inject;

import static com.vitalityactive.va.register.view.RegisterActivity.LOADING_INDICATOR;

public class ChangeEntityNumberFragment extends BaseUkePresentedFragment<ChangeEntityNumberPresenter.UI, ChangeEntityNumberPresenter>
        implements ChangeEntityNumberPresenter.UI {

    private static final String ERROR_DIALOG_TAG = "INCORRECT_ENTITY_NUMBER";
    private static final String ERROR_CONNECTION_TAG = "CONNECTION_ERROR";
    private static final String ERROR_UNKNOWN = "UNKNOWN_ERROR";
    private TextView entityNumberView;
    private ImageView entityNumberIcon;
    private MenuItem doneItem;

    @Inject
    ChangeEntityNumberPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_change_entity_number, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.done_menu, menu);
        doneItem = menu.findItem(R.id.action_menu);

        doneItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                presenter.onUserEntersEntityNumber(entityNumberView.getText().toString());
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    protected void activityCreated(@Nullable Bundle savedInstanceState) {
        View parentView = getView();
        if (parentView == null) {
            return;
        }
        setUpChangeEmailView(parentView);
        addFocusListener();
        addEmailTouchOutsideListener();
    }

    private void setUpChangeEmailView(@NonNull View parentView) {
        entityNumberView = parentView.findViewById(R.id.new_entity_number);
        entityNumberIcon = parentView.findViewById(R.id.new_entity_number_icon);
        entityNumberView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
    }

    @Override
    public void showEntityNumber(String currentEntity){
        if(currentEntity.length() > 0){
            entityNumberView.setText(currentEntity);
            entityNumberView.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_disabled_38));
            entityNumberIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.card_inactive));
        }

    }

    @Override
    public void showIncorrectEntityNumber() {
        AlertDialogFragment alert = AlertDialogFragment.create(
                ERROR_DIALOG_TAG,
                getResources().getString( R.string.uke_alert_incorrect_number_title_372 ),
                getResources().getString( R.string.uke_alert_incorrect_number_message_373),
                null,
                null,
                getResources().getString( R.string.profile_change_email_ok ));
        alert.show(getFragmentManager(), ERROR_DIALOG_TAG);
    }

    @Override
    public void showENConnectionError() {
        AlertDialogFragment alert = AlertDialogFragment.create(
                ERROR_CONNECTION_TAG,
                getResources().getString( R.string.alert_connection_error_title_1139 ),
                getResources().getString( R.string.connectivity_error_alert_message_45),
                null,
                null,
                getResources().getString( R.string.try_again_button_title_43 ));
        alert.show(getFragmentManager(), ERROR_DIALOG_TAG);
    }

    @Override
    public void showUnknownError(){
        AlertDialogFragment alert = AlertDialogFragment.create(
                ERROR_UNKNOWN,
                getResources().getString(R.string.alert_unknown_title_266),
                getResources().getString(R.string.alert_unknown_message_267),
                null,
                null,
                getResources().getString(R.string.try_again_button_title_43)
        );
        alert.show(getFragmentManager(), ERROR_UNKNOWN);
    }

    @Override
    public void showChangeEntityConfirmation() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((PersonalDetailsActivity)getActivity()).changeActionBarHomeIconToBackArrow();
                getActivity().onBackPressed();
            }
        });
    }

    private void addEmailTouchOutsideListener() {
        if (getView() != null) {
            getView().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        int touchX = (int) (event.getX());
                        int touchY = (int) (event.getY());
                        boolean isOutsideEmailView = ViewUtilities.isPointOutsideViewHitRect(touchX, touchY, entityNumberView);

                        if (isOutsideEmailView) {
                            ViewUtilities.hideKeyboard(getActivity(), getView());
                        }
                        return isOutsideEmailView;
                    }
                    return false;
                }
            });
        }
    }

    private void addFocusListener() {
        entityNumberView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(v.getId() == R.id.new_entity_number) {
                    CharSequence text = ((TextView) v).getText();
                    if (hasFocus || !TextUtilities.isNullOrWhitespace(text)) {
                        entityNumberIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.card_active));
                        entityNumberView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
                            if(entityNumberView.getText().length() > 0){
                                doneItem.setEnabled(true);
                            }
                    } else {
                        entityNumberIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.card_inactive));
                        doneItem.setEnabled(false);
                    }
                }
            }
        });
    }



    @Override
    public void showLoadingIndicator() {
        if (getView() != null) {
            if (getFragmentManager().findFragmentByTag(LOADING_INDICATOR) != null) {
                return;
            }
            ViewUtilities.hideKeyboard(getActivity(), getView());
            new LoadingIndicatorFragment().show(getFragmentManager(), LOADING_INDICATOR);
        }
    }

    @Override
    public void hideLoadingIndicator() {
        if (getView() != null) {
            LoadingIndicatorFragment loadingIndicatorFragment = (LoadingIndicatorFragment) getFragmentManager().findFragmentByTag(LOADING_INDICATOR);
            if (loadingIndicatorFragment != null) {
                loadingIndicatorFragment.dismiss();
            }
        }
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected ChangeEntityNumberPresenter.UI getUserInterface() {
        return this;
    }

    @Override
    protected ChangeEntityNumberPresenter getPresenter() {
        return presenter;
    }
}
