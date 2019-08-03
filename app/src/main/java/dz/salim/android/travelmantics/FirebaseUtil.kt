package dz.salim.android.travelmantics

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseUtil{
    val RC_SIGN_IN = 123
    var mFirebaseDatabase: FirebaseDatabase
    var mDatabaseReference: DatabaseReference
    var mDeals: ArrayList<Traveldeal>
    var mFirebaseAuth: FirebaseAuth
    var mAuthListener: FirebaseAuth.AuthStateListener?

    init {
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mFirebaseDatabase.reference
        mDeals = ArrayList()
        mFirebaseAuth = FirebaseAuth.getInstance()
        mAuthListener = null
    }

    fun signIn(activity: Activity){
        mAuthListener = FirebaseAuth.AuthStateListener {
            if (it.currentUser == null) {
                val providers = arrayListOf(
                    AuthUI.IdpConfig.EmailBuilder().build(),
                    AuthUI.IdpConfig.GoogleBuilder().build()
                )

                activity.startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                    RC_SIGN_IN
                )
            } else {
                Toast.makeText(activity, "Welcome Back!", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun attachListener() = mFirebaseAuth.addAuthStateListener(mAuthListener!!)
    fun detachListener() = mFirebaseAuth.removeAuthStateListener(mAuthListener!!)
}