package dz.salim.android.travelmantics

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class DealAdapter: RecyclerView.Adapter<DealAdapter.DealViewHolder>() {

    var deals: ArrayList<Traveldeal> = FirebaseUtil.mDeals
    private var mFirebaseDatabase: FirebaseDatabase
    private var mDatabaseReference: DatabaseReference
    private var mChildEventListener : ChildEventListener

    init {
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase
        mDatabaseReference = FirebaseUtil.mDatabaseReference.child("traveldeals")
        mChildEventListener = object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val newDeal: Traveldeal? = dataSnapshot.getValue(Traveldeal::class.java)
                newDeal?.id = dataSnapshot.key
                deals.add(newDeal!!)
                notifyItemInserted(deals.size-1)
                Log.d("DEALADAPTER", "${FirebaseUtil.mDeals.size}")
            }

            override fun onChildRemoved(p0: DataSnapshot) {

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

    class DealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

        var tvTitle: TextView
        var tvDescription: TextView
        var tvPrice: TextView
        init {
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tvDescription = itemView.findViewById(R.id.tvDescription)
            tvPrice = itemView.findViewById(R.id.tvPrice)
            itemView.setOnClickListener(this)
        }

        fun bind(deal: Traveldeal){
            tvTitle.text = deal.title
            tvDescription.text = deal.description
            tvPrice.text = deal.price
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            val selectedDeal = FirebaseUtil.mDeals[position]
            val intent = Intent(v?.context, AdminActivity::class.java)
            intent.putExtra("Deal", selectedDeal)
            v?.context?.startActivity(intent)
        }

    }
}