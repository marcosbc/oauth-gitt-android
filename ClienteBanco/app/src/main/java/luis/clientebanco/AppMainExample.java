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
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import luis.clientebanco.OAuth.AppContext;
import luis.clientebanco.OAuth.GenericDialogListener;
import luis.clientebanco.OAuth.LOGGING;
import luis.clientebanco.OAuth.UrbankOAuthDialog;
import luis.clientebanco.OAuth.WebService;

public class AppMainExample extends Activity {
    
	protected static final String TAG = "AppMainExample";
	
	public Context mContext;
	public Activity mActivity;
	public WebService webService;

	private String accessCode;
	private String accessToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mContext = this;
        mActivity = this;
        setContentView(R.layout.appmainexample);
    }

	public void urbankLogin(View V){

		urbankAccessCode();

	}

	private void urbankAccessCode(){

		webService = new WebService();

		String authRequestRedirect = AppContext.UB_APP_OAUTH_URL
				+ "?client_id="+AppContext.UB_CLIENT_ID
				+ "&redirect_uri="+AppContext.UB_APP_REDIRECT
				+ "&response_type=code"
				+ "&scope=permission_read_transaction"
				;

		if(LOGGING.DEBUG)
			Log.d(TAG, "authRequestRedirect->"+authRequestRedirect);

		new  UrbankOAuthDialog(mContext, authRequestRedirect, new GenericDialogListener() {

			public void onComplete(Bundle values) {
				if(LOGGING.DEBUG)
					Log.d(TAG, "onComplete->"+values);

				accessCode = "";

				try{

					accessCode = values.getString("code");

					urbankAccessToken();
					//JSONObject tokenJson = new JSONObject(tokenResponse);
					//if(LOGGING.DEBUG)Log.d(TAG, "tokenJson->" + tokenJson);

				}
				catch (Exception ex1){
					Log.w(TAG, ex1.toString());
					accessCode = null;
				}
				Log.v(TAG, "prueba");
			}
			public void onError(String e) {
				if(LOGGING.DEBUG)Log.d(TAG, "onError->"+e);
			}
			public void onCancel() {

				if(LOGGING.DEBUG)Log.d(TAG, "onCancel()");
			}
		}).show();

	}

	public void urbankAccessToken(){

		accessToken = "";

		String authRequestRedirect = AppContext.UB_APP_TOKEN_URL
				+ "?client_id="+AppContext.UB_CLIENT_ID
				+ "&redirect_uri="+AppContext.UB_APP_REDIRECT
				+ "&grant_type=authorization_code"
				+ "&client_secret=" + AppContext.UB_CLIENT_SECRET
				+ "&code=" + accessCode
		;

		if(LOGGING.DEBUG)
			Log.d(TAG, "authRequestRedirect->"+authRequestRedirect);

		new  UrbankOAuthDialog(mContext, authRequestRedirect, new GenericDialogListener() {

			public void onComplete(Bundle values) {
				if(LOGGING.DEBUG)
					Log.d(TAG, "onComplete->"+values);

				accessToken = "";

				try{

					accessToken = values.toString();

					//JSONObject tokenJson = new JSONObject(tokenResponse);
					//if(LOGGING.DEBUG)Log.d(TAG, "tokenJson->" + tokenJson);

				}
				catch (Exception ex1){
					Log.w(TAG, ex1.toString());
					accessToken = null;
				}
				Log.v(TAG, "prueba");
			}
			public void onError(String e) {
				if(LOGGING.DEBUG)Log.d(TAG, "onError->"+e);
			}
			public void onCancel() {

				if(LOGGING.DEBUG)Log.d(TAG, "onCancel()");
			}
		}).show();

	}



}

