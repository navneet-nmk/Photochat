package com.navneet.photochat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseUser;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	// Declaration of variables
	SectionsPagerAdapter mSectionsPagerAdapter;
	Context mcontext;
	String[] cameraoptions = { "Take Photo", "Take Video", "Choose Photo",
			"Choose Video" };

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	// Setting a TAG value for logging in logcat
	public static final String TAG = MainActivity.class.getSimpleName();
	// Declaring variables to be used for request codes
	public static final int TAKE_PHOTO_REQUESt = 0;
	public static final int TAKE_VIDEO_REQUESt = 1;
	public static final int CHOOSE_PHOTO_REQUESt = 2;
	public static final int CHOOSE_VIDEO_REQUESt = 3;

	// Decalring camera photos storage variables
	public static final int MEDIA_TYPE_IMAGE = 4;
	public static final int MEDIA_TYPE_VIDEO = 5;

	// Constant declaration for file size type conversions
	public static final int FILE_SIZE = 1024 * 1024 * 10;// 10 MB

	// Declaring Uri variable
	// Uri and URI classes are different
	// Uri stands for Universal resource identifier

	protected Uri mMediaUri;



	// Declaration of the member variable for setting the onclick listener for
	// dialog options

	protected DialogInterface.OnClickListener mDialogOptionsSelected = new DialogInterface.OnClickListener() {
		// Which parameter defines the index of the parameter clicked
		@Override
		public void onClick(DialogInterface dialog, int which) {

			// Switch statement to decide our course of action

			switch (which) {
			case 0:// Take photo
					// Declaring an intent
				Intent takePhotoIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				// Using mMediaUri
				mMediaUri = getOutputMediaUri(MEDIA_TYPE_IMAGE);

				if (mMediaUri == null) {
					// Display error
					Toast.makeText(MainActivity.this,
							"Problem in accessing the device external storage",
							Toast.LENGTH_LONG).show();
				} else {
					takePhotoIntent
							.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);

					// Starting the activity for obtaining the result(ie the
					// image)

					startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUESt);
				}

				break;

			case 1:// Take Video
				Intent takeVideoIntent = new Intent(
						MediaStore.ACTION_VIDEO_CAPTURE);
				// Using mMediaUri
				mMediaUri = getOutputMediaUri(MEDIA_TYPE_VIDEO);
				if (mMediaUri == null) {
					// Display error
					Toast.makeText(MainActivity.this,
							"Problem in accessing the device external storage",
							Toast.LENGTH_LONG).show();
				} else {
					takeVideoIntent
							.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
					// Limit the video duration to 10s
					takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,
							10);
					// Set the quality of the video
					// 0=Low quality
					// 1=High quality
					takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);

					// Starting activity for obtaining the result (ie the video)
					startActivityForResult(takeVideoIntent, TAKE_VIDEO_REQUESt);

				}
				break;

			case 2:// Choose Photo
				Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
				// Set type of file to choose ie only photos
				choosePhotoIntent.setType("image/*");
				startActivityForResult(choosePhotoIntent, CHOOSE_PHOTO_REQUESt);

				break;

			case 3:// Choose Video
				Intent chooseVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);
				// Set type of file to choose ie only videos
				chooseVideoIntent.setType("video/*");
				// Warn users to not attach videos of size more than 10mb
				Toast.makeText(MainActivity.this,
						"Attach Videos of size less than or equal to 10MB",
						Toast.LENGTH_LONG).show();
				startActivityForResult(chooseVideoIntent, CHOOSE_VIDEO_REQUESt);
				break;
			}

		}

		// Method Definition of getOutputMediaUri
		private Uri getOutputMediaUri(int mediaType) {
			// To be safe, you should check that the SDCard is mounted
			// using Environment.getExternalStorageState() before doing this.
			if (isExternalStorageAvailable()) {
				// Return Uri
				// 1.Get the external storage directory
				// Declare the napplication name
				String appname = MainActivity.this.getString(R.string.app_name);
				File MediaStorageDir = new File(
						Environment
								.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
						appname);
				// 2.Create our subdirectory
				// Check for existence of MediaStorageDir
				if (!MediaStorageDir.exists()) {
					// mkdirs() returns a boolean value

					if (!MediaStorageDir.mkdirs()) {
						// Logging exception
						Log.e(TAG, "Failed to create directory");
						// Sets the Uri = null
						return null;
					}
				}
				// 3.Create file-name
				// 4.Create file
				File mediaFile;
				// Date declaration
				Date now = new Date();
				// Declaring the timestamp
				String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
						Locale.US).format(now);
				// Declaring the path and initializing it to the path of the
				// directory
				// Separator for defining the default file types
				String path = MediaStorageDir.getPath() + File.separator;
				// We need diff. file names and extensions for img and vid ,
				// hence if condition
				if (mediaType == MEDIA_TYPE_IMAGE) {
					mediaFile = new File(path + "IMG_" + timestamp + ".jpg");
				} else if (mediaType == MEDIA_TYPE_VIDEO) {
					mediaFile = new File(path + "VID_" + timestamp + ".mp4");
				} else {
					return null;
				}
				// For debugging purposes, storing the file Uri
				Log.d(TAG, "File" + Uri.fromFile(mediaFile));
				// 5.Return the file's Uri
				return Uri.fromFile(mediaFile);
			} else {
				// No mounted storage
				return null;
			}
		}

		// To check whther external storage is available or not
		private boolean isExternalStorageAvailable() {
			String state = Environment.getExternalStorageState();
			// Defining an if-block
			if (state.equals(Environment.MEDIA_MOUNTED)) {
				return true;
			} else {
				return false;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// For use of progress bar indicator in fragments
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_main);

		// Backend analytics

		ParseAnalytics.trackAppOpened(getIntent());
		// Returns the current user or session logged in

		ParseUser currentuser = ParseUser.getCurrentUser();
		// When user is logged in
		if (currentuser == null) {
			// For jumping straight into loginactivity class
			Intent intent = new Intent(this, LoginActivity.class);
			// To prevent back button going to mainactivity
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
		} else {
			// Log the username in logcat for debugging purposes
			Log.i(TAG, currentuser.getUsername());

		}
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		FragmentManager fragmentmanager = getSupportFragmentManager();
		mSectionsPagerAdapter = new SectionsPagerAdapter(this, fragmentmanager);

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setIcon(mSectionsPagerAdapter.getIcon(i))
					.setTabListener(this));
		}
	}

	// Defining onActivityResult listener
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		// If condition to check for various values of resultCode
		if (resultCode == RESULT_OK) {
			if (requestCode == CHOOSE_PHOTO_REQUESt
					|| requestCode == CHOOSE_VIDEO_REQUESt) {
				// Choose photo or video from gallery or recent(android 4.4)
				if (data == null) {
					// Show the user an error message
					Toast.makeText(MainActivity.this,
							"Sorry,there was an error", Toast.LENGTH_LONG)
							.show();
				} else {
					// Data is not null
					mMediaUri = data.getData();
				}
				if (requestCode == CHOOSE_VIDEO_REQUESt) {
					// Ensure file size is less than 10 MB
					int fileSize = 0;
					// Declare an inputstream which coverts byte by byte
					// GetContentResolver resolves the Uri of the video
					// We must always close the inputstreams to avoid memory
					// leaks
					InputStream inputStream = null;
					try {
						inputStream = getContentResolver().openInputStream(
								mMediaUri);
						fileSize = inputStream.available();
					} catch (FileNotFoundException e) {
						// Catch FileNotFoundException
						// Show the error message to the user
						Toast.makeText(MainActivity.this,
								"There was a problem with the selected file",
								Toast.LENGTH_LONG).show();
						return;
					} catch (IOException e) {
						// Catch IOexception
						Toast.makeText(MainActivity.this,
								"There was a problem with the selected file",
								Toast.LENGTH_LONG).show();
						return;

					}
					// The following block of code always gets executed
					finally {
						try {
							inputStream.close();
						} catch (IOException e) {
							// Intentionally Blank
						}
					}
					// File size checking and warning system
					if (fileSize >= FILE_SIZE) {
						// Show an error message to the user
						Toast.makeText(MainActivity.this,
								"Selected file is too large.",
								Toast.LENGTH_LONG).show();
						// Return so as to not to proceed further
						return;

					}

				}

			} else {
				// Add it to the gallery
				// We will store files in the gallery using a new intent and
				// then
				// broadcasting it to the gallery
				Intent mediaScanIntent = new Intent(
						Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				// Set Uri of the intent
				mediaScanIntent.setData(mMediaUri);
				// Send Broadcast to the gallery application
				sendBroadcast(mediaScanIntent);
			}
			// Starts recipients activity
			Intent recipientsIntent = new Intent(this, RecipientsActivity.class);
			// Transfer uri as data to the recipients activity.
			recipientsIntent.setData(mMediaUri);
			// Determining the file type
			String fileType;
			if (requestCode == CHOOSE_PHOTO_REQUESt
					|| requestCode == TAKE_PHOTO_REQUESt) {
				fileType = ParseConstants.TYPE_IMAGE;
			} else {
				fileType = ParseConstants.TYPE_VIDEO;
			}
			// Add the fileType data to the intent
			recipientsIntent.putExtra(ParseConstants.KEY_FILE_TYPE, fileType);
			// Start the Activity
			startActivity(recipientsIntent);

		} else if (resultCode != RESULT_CANCELED) {
			// Show the user error message
			Toast.makeText(this, "Sorry there was an error", Toast.LENGTH_LONG)
					.show();

		}

	}

	// Elements in the overflowing menu

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// For items in action bar
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// Defining functionality of logout button in the action bar
		int id = item.getItemId();
		// switch case for selecting the code to bexecuted for the option
		// selected
		switch (id) {
		case R.id.action_logout:
			ParseUser.logOut();
			Intent intent = new Intent(this, LoginActivity.class);
			// To prevent back button going to mainactivity
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
			break;

		case R.id.action_edit_friends:
			// To start an activity in another activity we declare intents
			Intent intent1 = new Intent(this, EditFriendsActivity.class);
			startActivity(intent1);
			break;
		case R.id.menu_camera_button:
			// Camera button functionality
			// Display alert dialog to display various camera options
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setItems(cameraoptions, mDialogOptionsSelected);
			AlertDialog dialog = builder.create();
			dialog.show();
			break;

		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

}
