package dz.salim.android.travelmantics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        val adapter = DealAdapter()
        val dealsLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv_traveldeals.adapter = adapter
        rv_traveldeals.layoutManager = dealsLayoutManager
    }
}
