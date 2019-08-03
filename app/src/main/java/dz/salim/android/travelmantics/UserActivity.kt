package dz.salim.android.travelmantics

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
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
            R.id.logout -> {
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        Toast.makeText(this, "See you soon!", Toast.LENGTH_LONG).show()
                        FirebaseUtil.attachListener()
                    }
                FirebaseUtil.detachListener()
                return true
            }

        }
        return false
    }

    override fun onPause() {
        super.onPause()
        FirebaseUtil.detachListener()
    }

    override fun onResume() {
        super.onResume()
        FirebaseUtil.signIn(this)
        FirebaseUtil.mDeals = ArrayList()
        val adapter = DealAdapter()
        val dealsLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv_traveldeals.layoutManager = dealsLayoutManager
        rv_traveldeals.adapter = adapter
        FirebaseUtil.attachListener()
    }
}
