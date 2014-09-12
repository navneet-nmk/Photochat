package com.navneet.photochat;

import java.util.Locale;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one
 * of the sections/tabs/pages.
 */
// fragment code
public class SectionsPagerAdapter extends FragmentPagerAdapter {

	protected Context mContext;

	public SectionsPagerAdapter(MainActivity context,
			android.support.v4.app.FragmentManager fragmentmanager) {
		// TODO Auto-generated constructor stub
		super(fragmentmanager);
		mContext = context;

	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a PlaceholderFragment (defined as a static inner class
		// below).
		// Create a switch to switch between the various tabs
		switch (position) {
		case 0:
			return new InboxFragment();
		case 1:
			return new FriendsFragment();

		}
		return null;
	}

	@Override
	public int getCount() {
		// Show 2 total pages.
		return 2;
	}

	// Setting the text on the various tabs
	@Override
	public CharSequence getPageTitle(int position) {
		Locale l = Locale.getDefault();
		switch (position) {
		case 0:
			return mContext.getString(R.string.title_section1).toUpperCase(l);
		case 1:
			return mContext.getString(R.string.title_section2).toUpperCase(l);

		}
		return null;
	}

	// Setting the icons on the various tabs
	public int getIcon(int position) {
		switch (position) {
		case 0:
			return R.drawable.ic_action_ic_tab_inbox;
		case 1:
			return R.drawable.ic_action_ic_tab_friends;
		}
		return R.drawable.ic_action_ic_tab_inbox;
	}

}
