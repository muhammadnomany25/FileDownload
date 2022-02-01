package com.nagwa.files.shared_prefs

import android.content.Context
import android.content.SharedPreferences

/**
 * @usage: The shared preferences class handler for the app
 * <p>
 * Created by Muhammad Noamany
 * Email: muhammadnoamany@gmail.com
 */
class UserSaver(
    private var preferences: SharedPreferences
) {
    /**
     * contains constant values of the shared prefs value
     * contains static fun to return app locale to make use of it in OnAttachBaseContext of the activity
     */
    companion object {
        private const val Preferences_LANGUAGE = "lang"
        private const val DEFAULT_LOCALE = "en"
        fun getAppLang(context: Context): String {
            val prefs =
                context.getSharedPreferences(context.applicationInfo.name, Context.MODE_PRIVATE)
            return prefs.getString(Preferences_LANGUAGE, DEFAULT_LOCALE)!!
        }
    }

    /**
     * Return current locale of the app
     */
    fun getAppLang(): String {
        return preferences.getString(Preferences_LANGUAGE, DEFAULT_LOCALE)!!
    }

}