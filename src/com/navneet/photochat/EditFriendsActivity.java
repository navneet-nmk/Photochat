package com.navneet.photochat;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class EditFriendsActivity extends Activity {

	// Variable Declaration
	protected static final String TAG = EditFriendsActivity.class
			.getSimpleName();
	protected List<ParseUser> mUsers = new ArrayList<ParseUser>();
	protected ParseRelation<ParseUser> mFriendrelation;
	protected ParseUser mCurrentUser;
	// Member variable for the grid view
	protected GridView mUserGridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.user_grid);
		// Set the grid view
		mUserGridView = (GridView) findViewById(R.id.friendsGrid);
		// To check or un-check friends
		mUserGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
		// Set on item click listener
		mUserGridView.setOnItemClickListener(mOnItemClickListener);
		// set empty text
		TextView emptyTextView = (TextView) findViewById(android.R.id.empty);
		mUserGridView.setEmptyView(emptyTextView);

	}

	@Override
	protected void onResume() {
		super.onResume();
		// To define relationships
		mCurrentUser = ParseUser.getCurrentUser();
		mFriendrelation = mCurrentUser
				.getRelation(ParseConstants.KEY_FRIEND_RELATION);
		// Setting up the progress bar
		setProgressBarIndeterminateVisibility(true);
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		// To order the usernames in ascending order
		query.orderByAscending(ParseConstants.KEY_USERNAME);
		// To set linit of number of queries to avoid crashing of application
		query.setLimit(1000);
		// To search in background
		query.findInBackground(new FindCallback<ParseUser>() {

			@Override
			public void done(List<ParseUser> users, ParseException e) {
				// TODO Auto-generated method stub
				// Make the visibilty of the progress bar false
				setProgressBarIndeterminateVisibility(false);
				if (e == null) {
					// SUccess
					mUsers = users;
					// Declare an array of strings to show the names
					String[] usernames = new String[mUsers.size()];
					// Set a loop to set the different elements of the string
					// array
					int i = 0;
					for (ParseUser user : mUsers) {
						usernames[i] = user.getUsername();
						i++;
					}
					// check whether gridView is empty or not
					if (mUserGridView.getAdapter() == null) {
						// Setting the custom grid adpater to display the
						// results
						UserAdpater adapter = new UserAdpater(
								EditFriendsActivity.this, mUsers);
						mUserGridView.setAdapter(adapter);
					} else {
						// refill the grid view if already created
						((UserAdpater) mUserGridView.getAdapter())
								.refill(mUsers);
					}

					addFriendCheckMarks();
				} else {
					Log.e(TAG, e.getMessage());

				}

			}
		});

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

	private void addFriendCheckMarks() {
		mFriendrelation.getQuery().findInBackground(
				new FindCallback<ParseUser>() {

					@Override
					public void done(List<ParseUser> friends, ParseException e) {
						if (e == null) {
							// list returned-look for a match
							// Use for loop for searching the entries
							for (int i = 0; i < mUsers.size(); i++) {
								ParseUser user = mUsers.get(i);

								// Searching in the list returned in the done
								// method
								for (ParseUser friend : friends) {
									// Checking the object id's of friends list
									// and user list
									if (friend.getObjectId().equals(
											user.getObjectId())) {
										// Set check mark true
										mUserGridView.setItemChecked(i, true);
										// Set imageView

									}
								}
							}
						} else {
							// Exception
							// Logging the exception
							Log.e(TAG, e.getMessage());
						}

					}
				});
	}

	protected OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
			// Take view from the Image View checked
			ImageView checkedImageView = (ImageView) view
					.findViewById(R.id.selectedUserIcon);
			// Actual code to select friends in the backend
			// To check whether the item is clicked or not
			if (mUserGridView.isItemChecked(position)) {
				// add friend
				// getting the position of the user tapped
				mFriendrelation.add(mUsers.get(position));
				// Show checked mark to notify users
				checkedImageView.setVisibility(View.VISIBLE);

			} else { // remove a friend
				mFriendrelation.remove(mUsers.get(position));
				checkedImageView.setVisibility(View.INVISIBLE);

			} // Save in backend
				// For saving in the backend
			mCurrentUser.saveInBackground(new SaveCallback() {

				@Override
				public void done(ParseException e) {
					// If the method fails ,log the exception
					if (e != null) {
						Log.e(TAG, e.getMessage());
					}
				}
			});

		}

	};
}
