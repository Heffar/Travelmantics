package dz.salim.android.travelmantics

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

object FirebaseUtil{
    val RC_SIGN_IN = 123
    var mFirebaseDatabase: FirebaseDatabase
    var mDatabaseReference: DatabaseReference
    var mDeals: ArrayList<Traveldeal>
    var mFirebaseAuth: FirebaseAuth
    var mAuthListener: FirebaseAuth.AuthStateListener?
    var isAdmin: Boolean?
    init {
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mFirebaseDatabase.reference
        mDeals = ArrayList()
        mFirebaseAuth = FirebaseAuth.getInstance()
        mAuthListener = null
        isAdmin = false
    }

    fun signIn(activity: UserActivity){
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
                val userId = mFirebaseAuth.uid
                checkAdmin(activity, userId)
            }
        }
    }

    private fun checkAdmin(activity: UserActivity, userId: String?) {
        isAdmin = false
        val ref = mFirebaseDatabase.reference.child("administrators").child(userId!!)
        val listener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                isAdmin = true
                activity.showMenu()
            }

        }
        ref.addChildEventListener(listener)
    }

    fun attachListener() = mFirebaseAuth.addAuthStateListener(mAuthListener!!)
    fun detachListener() = mFirebaseAuth.removeAuthStateListener(mAuthListener!!)
}