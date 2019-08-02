package dz.salim.android.travelmantics

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseUtil{
    var mFirebaseDatabase: FirebaseDatabase
    var mDatabaseReference: DatabaseReference
    var mDeals: ArrayList<Traveldeal>

    init {
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mFirebaseDatabase.reference
        mDeals = ArrayList()
    }
}