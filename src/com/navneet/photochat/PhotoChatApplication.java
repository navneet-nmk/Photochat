package com.navneet.photochat;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.PushService;

public class PhotoChatApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// For creating the parse backend
		// Refer to parse docs
		Parse.initialize(this, "oQd6qUkCFtmFJdARTufcyctRAACDrhoSbSRFn2XK",
				"fH0mDVS5iIBRvNmz8CBCfgXTJD6JSPCjGOL9AXdj");
		// For Push Notification
		PushService.setDefaultPushCallback(this, MainActivity.class);
	}

	// Declaring the updateParseInstallation method
	public static void updateParseInstallation(ParseUser user) {
		// Declare a parse installation variable
		ParseInstallation intst = ParseInstallation.getCurrentInstallation();
		intst.put(ParseConstants.KEY_USER_ID, user.getObjectId());
		// save this installation to parse backend
		intst.saveInBackground();
	}
}
