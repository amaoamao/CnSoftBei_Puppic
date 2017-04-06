package com.gouder.cnsoftbei.UI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gouder.cnsoftbei.R;

import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity {


    @InjectView(R.id.login_progress)
    ProgressBar mProgressView;
    @InjectView(R.id.et_email)
    EditText etEmail;
    @InjectView(R.id.login_form)
    ScrollView mLoginFormView;
    @InjectView(R.id.til_email)
    TextInputLayout tilEmail;
    @InjectView(R.id.et_password)
    EditText etPassword;
    @InjectView(R.id.til_password)
    TextInputLayout tilPassword;
    @InjectView(R.id.btn_sign_in_or_register)
    Button btnSignInOrRegister;
    @InjectView(R.id.btn_sign_in)
    Button btnSignIn;

    private UserLoginTask mAuthTask = null;

    private UserIsSignedUpTask isSignedUpTask = null;

    private Boolean userIsSignedUp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBars();
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        etEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                LoginActivity.this.registerOrLogIn();
                return true;
            }
        });
        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                LoginActivity.this.attemptLogin();
                return true;
            }
        });

        animate();
    }

    private void hideKeyboard(View textView) {
        InputMethodManager inputMethodManager = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(textView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void registerOrLogIn() {
        hideKeyboard(etEmail);
        if (isSignedUpTask != null) {
            return;
        }
        etEmail.setError(null);
        String email = etEmail.getText().toString();
        boolean cancel = false;
        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getString(R.string.error_field_required));
            cancel = true;
        } else if (!isEmailValid(email)) {
            etEmail.setError(getString(R.string.error_invalid_email));
            cancel = true;
        }
        if (cancel) {
            etEmail.requestFocus();
        } else {
            showProgress(true);
            isSignedUpTask = new UserIsSignedUpTask(email);
            isSignedUpTask.execute((Void) null);
        }
    }

    private void animate() {
        mLoginFormView.startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.slid_up));
    }

    private void initBars() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);
    }

    @OnClick(R.id.btn_sign_in_or_register)
    public void onEmailSignInButtonClicked(View v) {
        registerOrLogIn();
    }

    @OnClick(R.id.btn_sign_in)
    public void onSignInButtonClicked(View v) {
        attemptLogin();
    }

    private void attemptLogin() {
        hideKeyboard(etEmail);
        if (mAuthTask != null) {
            return;
        }
        etPassword.setError(null);
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        boolean cancel = false;
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            etPassword.setError(getString(R.string.error_invalid_password));
            cancel = true;
        }
        if (cancel) {
            etPassword.requestFocus();
        } else {
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*").matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

        if (userIsSignedUp != null && !show) {
            tilEmail.setVisibility(userIsSignedUp ? View.GONE : View.VISIBLE);
            btnSignInOrRegister.setVisibility(userIsSignedUp ? View.GONE : View.VISIBLE);
            tilPassword.setVisibility(userIsSignedUp ? View.VISIBLE : View.GONE);
            btnSignIn.setVisibility(userIsSignedUp ? View.VISIBLE : View.GONE);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
//            showProgress(false);

            finish();
//            if (success) {
//                finish();
//            } else {
////                mPasswordView.setError(getString(R.string.error_incorrect_password));
////                mPasswordView.requestFocus();
//            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


    private class UserIsSignedUpTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;

        UserIsSignedUpTask(String email) {
            mEmail = email;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            isSignedUpTask = null;
            userIsSignedUp = success;
            showProgress(false);

        }


        @Override
        protected void onCancelled() {
            isSignedUpTask = null;
            userIsSignedUp = null;
            showProgress(false);
        }
    }

}

