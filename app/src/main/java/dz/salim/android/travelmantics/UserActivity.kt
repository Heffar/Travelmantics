package dz.salim.android.travelmantics

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        FirebaseUtil.mDeals = ArrayList()
        val adapter = DealAdapter()
        val dealsLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv_traveldeals.layoutManager = dealsLayoutManager
        rv_traveldeals.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.user_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.new_deal -> {
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return false
    }
}
