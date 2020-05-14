package es.uam.eps.enriquez.cueto.cards

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.settings_activity.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }


    override fun onResume() {
        super.onResume()
       bottomAppbar.setNavigationOnClickListener {
           startActivity(Intent(this, MainActivity::class.java))
       }
    }
    companion object {
        private const val MAXIMUM_KEY = "maximum_number"
        private const val MAXIMUM_DEFAULT = "20"
        private const val TOKENID_KEY = "tokenID"
        private const val TOKENID_KEY_DEFAULT = ""

        fun getMaximumNumberOfCards(context: Context): String? {
            return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(MAXIMUM_KEY, MAXIMUM_DEFAULT)
        }

        fun setMaximumNumberOfCards(context: Context, max: String) {
            val sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putString(MAXIMUM_KEY, max)
            editor.commit ()
        }


        fun getTokenId(context: Context): String? {
            return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(TOKENID_KEY, TOKENID_KEY_DEFAULT)
        }

        fun setTokenId(context: Context, tokenId: String){
            val sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putString(TOKENID_KEY, tokenId)
            editor.commit ()
        }

    }




}