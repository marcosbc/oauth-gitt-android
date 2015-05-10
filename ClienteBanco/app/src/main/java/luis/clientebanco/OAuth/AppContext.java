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


package luis.clientebanco.OAuth;


public class AppContext {

	public static final boolean DEBUG = LOGGING.DEBUG;// enable/disable logging

	
	public static final String GRAPH_BASE_URL = "http://graph.facebook.com/";
	public static final String GRAPH_BASE_URL_SSL = "https://graph.facebook.com/";
	
	// REGISTER to these bcast identifiers and you will get login response together with its payload as json (INTENT_EXTRA_...)!!!
    public static final String INTENT_EXTRA_USERLOGIN_FACEBOOK = "USERLOGIN_FACEBOOK";
    public static final String BCAST_USERLOGIN_FACEBOOK = "com.wareninja.android.opensource.oauth2login.BCAST_USERLOGIN_FACEBOOK";


	
	//public static final String APP_CACHEDIR = ".WareNinja_OpenSource_appCache";
	
	// -->> REPLACE THESE VALUES WITH YOUR OWN APP!!!! <<--
	// NOTE: below values are using Test App for WareNinja.net ONLY!

	
	// Facebook App Params
	public static final String FB_APP_ID = "425922710921175";
	public static final String[] FB_PERMISSIONS = new String[] {
		// NOTE: remember to extend these permissions as per your need!!!!
		"email"
		}; 
	public static final String FB_APP_CALLBACK_OAUTHCALLBACK = "fbconnect://success";// YOURAPP_REDIRECT_URI
	public static final String FB_APP_REDIRECT_SIGNIN = "https://m.facebook.com";
	public static final String FB_APP_OAUTH_BASEURL = "https://m.facebook.com";
	public static final String FB_APP_OAUTH_URL = "/dialog/oauth/";
	
	// Urbank App Params

	public static final String UB_APP_OAUTH_URL = "http://oauth.urbank.bjorkelund.me/authorize";
	public static final String UB_APP_TOKEN_URL = "http://oauth.urbank.bjorkelund.me/access_token";
	public static final String UB_CLIENT_ID = "1";
	public static final String UB_CLIENT_SECRET = "zyxwvutsrqponmlkjihgfedcba987654321";

	public enum COMMUNITY {
		FACEBOOK, URBANK
	}

}
