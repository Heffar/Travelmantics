package dz.salim.android.travelmantics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class DealAdapter: RecyclerView.Adapter<DealAdapter.DealViewHolder>() {

    var deals: ArrayList<Traveldeal> = ArrayList()
    private var mFirebaseDatabase: FirebaseDatabase
    private var mDatabaseReference: DatabaseReference
    private var mChildEventListener : ChildEventListener

    init {
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase
        mDatabaseReference = FirebaseUtil.mDatabaseReference.child("traveldeals")
        deals = FirebaseUtil.mDeals
        mChildEventListener = object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val deal: Traveldeal? = dataSnapshot.getValue(Traveldeal::class.java)
                deal?.id = dataSnapshot.key
                deals.add(deal!!)
                notifyItemInserted(deals.size-1)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
        mDatabaseReference.addChildEventListener(mChildEventListener)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealViewHolder {

        val context = parent.context
        val itemView = LayoutInflater.from(context).inflate(R.layout.traveldeal_list_item, parent, false)
        return DealViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return deals.size
    }

    override fun onBindViewHolder(holder: DealViewHolder, position: Int) {
        val deal = deals[position]
        holder.bind(deal)
    }

    class DealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var tvTitle: TextView
        var tvDescription: TextView
        var tvPrice: TextView
        init {
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tvDescription = itemView.findViewById(R.id.tvDescription)
            tvPrice = itemView.findViewById(R.id.tvPrice)
        }

        fun bind(deal: Traveldeal){
            tvTitle.text = deal.title
            tvDescription.text = deal.description
            tvPrice.text = deal.price
        }
    }
}