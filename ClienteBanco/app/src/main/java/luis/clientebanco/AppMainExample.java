/***
	Copyright (c) 2011-2012 WareNinja.com 
	http://www.WareNinja.com - https://github.com/WareNinja
	
	Author: yg@wareninja.com / twitter: @WareNinja

  Licensed under the Apache License, Version 2.0 (the "License"); you may
  not use this file except in compliance with the License. You may obtain
  a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  
  >> Summary of the license:
  	You are allowed to re-use this code as you like, no kittens should be harmed though! 
 */


package luis.clientebanco;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieSyncManager;

import luis.clientebanco.OAuth.AppContext;
import luis.clientebanco.OAuth.FacebookOAuthDialog;
import luis.clientebanco.OAuth.GenericDialogListener;
import luis.clientebanco.OAuth.LOGGING;
import luis.clientebanco.OAuth.WebService;

public class AppMainExample extends Activity {
    
	protected static final String TAG = "AppMainExample";
	
	public Context mContext;
	public Activity mActivity;
	public WebService webService;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mContext = this;
        mActivity = this;
        setContentView(R.layout.appmainexample);
    }
    

	public void onClick_facebookLogin(View v) {
		//NotifierHelper.displayToast(mContext, "TODO: onClick_facebookLogin", NotifierHelper.SHORT_TOAST);
		
		// https://developers.facebook.com/docs/reference/dialogs/oauth/
		
    	webService = new WebService();
    	
    	String authRequestRedirect = AppContext.FB_APP_OAUTH_BASEURL+AppContext.FB_APP_OAUTH_URL
		        + "?client_id="+AppContext.FB_APP_ID
		        + "&response_type=token" 
		        + "&display=touch"
		        + "&scope=" + TextUtils.join(",", AppContext.FB_PERMISSIONS)
		        + "&redirect_uri="+AppContext.FB_APP_CALLBACK_OAUTHCALLBACK
		        ;
		if(LOGGING.DEBUG)
			Log.d(TAG, "authRequestRedirect->"+authRequestRedirect);
		
		CookieSyncManager.createInstance(this);
		new FacebookOAuthDialog(mContext, authRequestRedirect
				, new GenericDialogListener() {
			public void onComplete(Bundle values) {
				if(LOGGING.DEBUG)
					Log.d(TAG, "onComplete->"+values);
				// https://YOUR_REGISTERED_REDIRECT_URI/?code=CODE
				// onComplete->Bundle[{expires_in=0, access_token=<ACCESS_TOKEN>}]
		/*
		if user ALLOWs your app -> Bundle[{expires_in=0, access_token=<ACCESS_TOKEN>}]
		if user doesNOT ALLOW -> Bundle[{error=access_denied, error_description=The+user+denied+your+request}]
 		*/
				// ensure any cookies set by the dialog are saved
                CookieSyncManager.getInstance().sync();
				
				String tokenResponse = "";
				try{
					
					tokenResponse = values.toString();
					
					broadcastLoginResult(AppContext.COMMUNITY.FACEBOOK, tokenResponse);
					//JSONObject tokenJson = new JSONObject(tokenResponse);
					//if(LOGGING.DEBUG)Log.d(TAG, "tokenJson->" + tokenJson);

				}
				catch (Exception ex1){
					Log.w(TAG, ex1.toString());
					tokenResponse = null;
				}
		    }
			public void onError(String e) {
				if(LOGGING.DEBUG)Log.d(TAG, "onError->"+e);
		    }
			public void onCancel() {

				if(LOGGING.DEBUG)Log.d(TAG, "onCancel()");
		    }
		}).show();
	}

	public void urbankLogin(View V){

		webService = new WebService();

		String authRequestRedirect = AppContext.FB_APP_OAUTH_BASEURL+AppContext.FB_APP_OAUTH_URL
				+ "?client_id="+AppContext.FB_APP_ID
				+ "&response_type=token"
				+ "&display=touch"
				+ "&scope=" + TextUtils.join(",", AppContext.FB_PERMISSIONS)
				+ "&redirect_uri="+AppContext.FB_APP_CALLBACK_OAUTHCALLBACK
				;

	}

	private void broadcastLoginResult(AppContext.COMMUNITY community, String payload) {
		
		String intentAction = "";
		String intentExtra = "";
		try {

			if (AppContext.COMMUNITY.FACEBOOK == community) {
				intentAction = AppContext.BCAST_USERLOGIN_FACEBOOK;
				intentExtra = AppContext.INTENT_EXTRA_USERLOGIN_FACEBOOK;
			}
			/*else if (AppContext.COMMUNITY.TWILIO == community) {
				intentAction = AppContext.BCAST_USERLOGIN_TWILIO;
				intentExtra = AppContext.INTENT_EXTRA_USERLOGIN_TWILIO;
			}*/
			
			if(LOGGING.DEBUG)Log.d(TAG, "sending Broadcast! " 
					+ "|intentAction->"+intentAction
					+ "|intentExtra->"+intentExtra
					+ "|payload->"+payload
					);
			
			Intent mIntent = new Intent();
        	mIntent.setAction(intentAction);
        	mIntent.putExtra(intentExtra, payload);
        	this.sendBroadcast(mIntent);
		}
    	catch (Exception ex) {
    		Log.w(TAG, ex.toString());
    	}
    	
	}


}

