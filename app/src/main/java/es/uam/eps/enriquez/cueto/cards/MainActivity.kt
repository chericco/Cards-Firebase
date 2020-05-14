package es.uam.eps.enriquez.cueto.cards

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.*
import es.uam.eps.enriquez.cueto.cards.fragments.*
import es.uam.eps.enriquez.cueto.cards.viewModels.CardListViewModel



private const val TAG: String = "MainActivity"
private const  val MY_REQUEST_CODE: Int = 7117
open class MainActivity : AppCompatActivity(),FirebaseAuth.AuthStateListener, CardStudyFragment.onCardStudyFragmentInteractionListener,CardListFragment.onCardListFragmentInteractionListener, DeckListFragment.onDeckListFragmentInteractionListener, CardEditFragment.onCardEditFragmentInteractionListener {


    override fun onLogOut() {
        AuthUI.getInstance().signOut(this@MainActivity)
            .addOnCompleteListener {
                showSingInOptions()
            }
            .addOnFailureListener {
                e -> Toast.makeText(this@MainActivity, e.message,Toast.LENGTH_SHORT).show()
            }
    }
   private val cardListViewModel by lazy {
        ViewModelProviders.of(this).get(CardListViewModel::class.java)
    }
    override fun onSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }


    override fun onCardSelected() {

        var fragment = CardEditFragment.newInstance()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override  fun onDeckSelected(){
        var fragment = CardListFragment.newInstance()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackToDeckList() {
        var fragment = DeckListFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()

    }

    override fun onStuding() {
        var fragment = CardStudyFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackToList() {
        var fragment = CardListFragment.newInstance()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun message(msg: String) {
        Log.d(TAG, msg)
    }


    lateinit var providers: List<AuthUI.IdpConfig>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false)

        providers = listOf<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build()
        )




    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE){
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK){
                cardListViewModel!!.getAuth().currentUser

                Toast.makeText(this,""+cardListViewModel!!.getAuth().currentUser!!.email,Toast.LENGTH_SHORT).show()
                onBackToDeckList()

            }else {
                Toast.makeText(this,""+response!!.error!!.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

 private fun showSingInOptions(){
     startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
         .setAvailableProviders(providers)
         .setTheme(R.style.MyTheme)
         .build(),MY_REQUEST_CODE)
 }


    override fun onStart() {
        super.onStart()
     FirebaseAuth.getInstance().addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(this)
    }
    override fun onAuthStateChanged(p0: FirebaseAuth) {
        if(cardListViewModel.getAuth().currentUser == null){
            showSingInOptions()

        }else {
            cardListViewModel!!.getAuth().currentUser!!.getIdToken(true)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        onBackToDeckList()
                    }
                }

        }
    }


}
