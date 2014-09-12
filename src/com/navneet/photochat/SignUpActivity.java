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

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends Activity {
	protected EditText mUsername;
	protected EditText mPassword;
	protected EditText mEmail;
	protected Button mSignup;
	protected TextView mSignTitle;
	protected Button mCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// To display progress indicator and must be called before set content
		// view
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_sign_up);

		// Hide the action bar
		ActionBar action = getActionBar();
		action.hide();
		mSignTitle = (TextView) findViewById(R.id.title2);
		// Font Path
		String fontPath = "fonts/Fortune.otf";
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
		mSignTitle.setTypeface(tf);

		// Declaring member variables
		mCancel = (Button) findViewById(R.id.cancelButton);
		mUsername = (EditText) findViewById(R.id.userName_signup);
		mPassword = (EditText) findViewById(R.id.passWord_signup);
		mEmail = (EditText) findViewById(R.id.eMail);
		mSignup = (Button) findViewById(R.id.signupButton);
		mCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// Set font face
		String font = "fonts/Roboto-Black.ttf";
		Typeface stf = Typeface.createFromAsset(getAssets(), font);
		mSignup.setTypeface(stf);

		// Button onclicklistener
		mSignup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String username = mUsername.getText().toString();
				String password = mPassword.getText().toString();
				String email = mEmail.getText().toString();

				// For getting rid of white-spaces

				username = username.trim();
				password = password.trim();
				email = email.trim();
				// Checking for empty fields
				if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
					// Alert the user of errors using dialogs

					AlertDialog.Builder builder = new AlertDialog.Builder(
							SignUpActivity.this);
					builder.setMessage("Please enter valid username and password");
					builder.setTitle("Oops!");
					// Button to dismiss the dialog
					builder.setPositiveButton(android.R.string.ok, null);
					// Show the dialog
					AlertDialog dialog = builder.create();
					dialog.show();

				} else {
					// TODO: Create the new user!!
					setProgressBarIndeterminateVisibility(true);
					ParseUser newUser = new ParseUser();
					newUser.setUsername(username);
					newUser.setPassword(password);
					newUser.setEmail(email);
					// Sign up in background helps the app to be responsive evn
					// when its accessing the data from parse.com
					newUser.signUpInBackground(new SignUpCallback() {

						// Code to be executed when signup process is complete
						@Override
						public void done(ParseException e) {
							setProgressBarIndeterminateVisibility(false);
							PhotoChatApplication
									.updateParseInstallation(ParseUser
											.getCurrentUser());

							// TODO Auto-generated method stub
							if (e == null) {
								// success
								Intent intent = new Intent(SignUpActivity.this,
										MainActivity.class);
								// To disallow the user from backing upto the
								// signup page again
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
								// Going to the mailbox
								startActivity(intent);
							} else {
								// Alert the user of errors using dialogs

								AlertDialog.Builder builder = new AlertDialog.Builder(
										SignUpActivity.this);
								builder.setMessage(e.getMessage());
								builder.setTitle("Oops!");
								// Button to dismiss the dialog
								builder.setPositiveButton(android.R.string.ok,
										null);
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
		getMenuInflater().inflate(R.menu.sign_up, menu);
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
