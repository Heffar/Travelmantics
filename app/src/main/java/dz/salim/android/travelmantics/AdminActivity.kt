package dz.salim.android.travelmantics

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : AppCompatActivity() {

    lateinit var mFirebaseDatabase: FirebaseDatabase
    lateinit var mDatabaseReference: DatabaseReference
    lateinit var deal: Traveldeal
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase
        mDatabaseReference = mFirebaseDatabase.reference.child("traveldeals")

        deal = intent.getParcelableExtra("Deal") ?: Traveldeal()

        etTitle.setText(deal.title)
        etPrice.setText(deal.price)
        etDescription.setText(deal.description)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.save_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.save_menu -> {
                saveDeal()
                Toast.makeText(this, "Deal Saved!", Toast.LENGTH_LONG).show()
                backToList()
            }
            R.id.delete_menu -> {
                deleteDeal()
                Toast.makeText(this, "Deal Deleted!", Toast.LENGTH_LONG).show()
                backToList()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveDeal(){
        deal.title= etTitle.text.toString()
        deal.description = etDescription.text.toString()
        deal.price = etPrice.text.toString()

        if (deal.id == null){
            mDatabaseReference.push().setValue(deal)
        } else {
            mDatabaseReference.child(deal.id!!).setValue(deal)
        }
        Log.d("DEALS", "${FirebaseUtil.mDeals.size}")
    }

    private fun deleteDeal(){
        if (deal.id == null){
            Toast.makeText(this, "Please, save the deal before deleting it", Toast.LENGTH_LONG).show()
        } else {
            mDatabaseReference.child(deal.id!!).removeValue()
        }
    }

    private fun backToList(){
        val intent = Intent(this, UserActivity::class.java)
        startActivity(intent)
    }
}
