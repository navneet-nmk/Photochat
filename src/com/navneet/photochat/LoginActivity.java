package com.navneet.photochat;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends Activity {

	protected TextView mSignUpText;
	protected EditText mUsername;
	protected EditText mPassword;
	protected Button mLogin;
	protected TextView mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// To display progress indicator and must be called before set content
		// view
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_login);
		// Hide the action bar
		ActionBar action = getActionBar();
		action.hide();

		mTitle = (TextView) findViewById(R.id.title);
		// Font Path
		String fontPath = "fonts/Fortune.otf";
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
		mTitle.setTypeface(tf);

		mUsername = (EditText) findViewById(R.id.userName);
		mPassword = (EditText) findViewById(R.id.passWord);
		mLogin = (Button) findViewById(R.id.loginButton);
		mSignUpText = (TextView) findViewById(R.id.signUp);
		mSignUpText.setOnClickListener(new View.OnClickListener() {
			// Onclicklistener for the signup text
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(LoginActivity.this,
						SignUpActivity.class);
				startActivity(intent);

			}
		});

		// Login Button onclicklistener
		mLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String username = mUsername.getText().toString();
				String password = mPassword.getText().toString();

				// For getting rid of white-spaces

				username = username.trim();
				password = password.trim();

				// Checking for empty fields

				if (username.isEmpty() || password.isEmpty()) {
					// Alert the user of errors using dialogs

					AlertDialog.Builder builder = new AlertDialog.Builder(
							LoginActivity.this);
					builder.setMessage("Please enter valid username and password");
					builder.setTitle("Oops!");
					// Button to dismiss the dialog
					builder.setPositiveButton(android.R.string.ok, null);
					// Show the dialog
					AlertDialog dialog = builder.create();
					dialog.show();

				} else {
					// TODO: Login the user!!
					// Progress bar indicator
					setProgressBarIndeterminateVisibility(true);
					ParseUser.logInInBackground(username, password,
							new LogInCallback() {

								@Override
								public void done(ParseUser user,
										ParseException e) {
									// TODO Auto-generated method stub
									setProgressBarIndeterminateVisibility(false);
									// To check successful login
									if (e == null) {
										// Success
										// updateParseInstallation
										// Refer to RecipientsActivity
										// Method declared in Photochat
										// application class
										// We need to mention the class since
										// the method used is static
										PhotoChatApplication
												.updateParseInstallation(user);
										Intent intent = new Intent(
												LoginActivity.this,
												MainActivity.class);
										// To disallow the user from backing
										// upto the
										// signup page again
										intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
										// Going to the mailbox
										startActivity(intent);
									} else {
										// Error display

										// Alert the user of errors using
										// dialogs

										AlertDialog.Builder builder = new AlertDialog.Builder(
												LoginActivity.this);
										builder.setMessage(e.getMessage());
										builder.setTitle("Oops!");
										// Button to dismiss the dialog
										builder.setPositiveButton(
												android.R.string.ok, null);
										// Show the dialog
										AlertDialog dialog = builder.create();
										dialog.show();
									}

								}
							});

				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
