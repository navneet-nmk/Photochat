package com.navneet.photochat;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class FriendsFragment extends Fragment {

	public static final String TAG = FriendsFragment.class.getSimpleName();
	protected List<ParseUser> mFriends;
	protected ParseRelation<ParseUser> mFriendrelation;
	protected ParseUser mCurrentUser;
	protected GridView mFriendsGridView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.user_grid, container,
				false);
		mFriendsGridView = (GridView) rootView.findViewById(R.id.friendsGrid);
		// set empty text
		TextView emptyTextView = (TextView) rootView
				.findViewById(android.R.id.empty);
		mFriendsGridView.setEmptyView(emptyTextView);
		return rootView;
	}

	// Setting the onResume activity
	public void onResume() {
		super.onResume();
		// To define relationships
		mCurrentUser = ParseUser.getCurrentUser();
		mFriendrelation = mCurrentUser
				.getRelation(ParseConstants.KEY_FRIEND_RELATION);
		getActivity().setProgressBarIndeterminateVisibility(true);
		// Declaring variable
		ParseQuery<ParseUser> query = mFriendrelation.getQuery();
		// Arrange in ascending order
		query.addAscendingOrder(ParseConstants.KEY_USERNAME);
		query.findInBackground(new FindCallback<ParseUser>() {

			@Override
			public void done(List<ParseUser> friends, ParseException e) {

				getActivity().setProgressBarIndeterminateVisibility(false);
				if (e == null) {
					mFriends = friends;
					String[] usernames = new String[mFriends.size()];
					// Set a loop to set the different elements of the
					// string
					// array
					int i = 0;
					for (ParseUser user : mFriends) {
						usernames[i] = user.getUsername();
						i++;
					}
					// check whether gridView is empty or not
					if (mFriendsGridView.getAdapter() == null) {
						// Setting the custom grid adpater to display the
						// results
						UserAdpater adapter = new UserAdpater(getActivity(),
								mFriends);
						mFriendsGridView.setAdapter(adapter);
					} else {
						// refill the grid view if already created
						((UserAdpater) mFriendsGridView.getAdapter())
								.refill(mFriends);
					}
				} else {
					// Logging exception
					Log.e(TAG, e.getMessage());

				}
			}

		});
	}

}
