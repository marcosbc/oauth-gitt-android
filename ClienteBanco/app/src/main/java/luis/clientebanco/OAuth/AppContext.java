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


	
	// REGISTER to these bcast identifiers and you will get login response together with its payload as json (INTENT_EXTRA_...)!!!
    public static final String INTENT_EXTRA_USERLOGIN_FACEBOOK = "USERLOGIN_FACEBOOK";
    public static final String BCAST_USERLOGIN_FACEBOOK = "com.wareninja.android.opensource.oauth2login.BCAST_USERLOGIN_FACEBOOK";


	// Urbank App Params

	public static final String UB_APP_OAUTH_URL = "http://oauth.urbank.bjorkelund.me/authorize";
	public static final String UB_APP_TOKEN_URL = "http://oauth.urbank.bjorkelund.me/access_token";
	public static final String UB_CLIENT_ID = "1";
	public static final String UB_CLIENT_SECRET = "zyxwvutsrqponmlkjihgfedcba987654321";
	public static final String UB_APP_REDIRECT = "moneyvault://success";

	public enum COMMUNITY {
		FACEBOOK, URBANK
	}

}
