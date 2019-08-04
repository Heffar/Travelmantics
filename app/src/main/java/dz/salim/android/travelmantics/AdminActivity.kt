package dz.salim.android.travelmantics

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : AppCompatActivity() {

    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var deal: Traveldeal
    private val PICTURE_RESULT = 42

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase
        mDatabaseReference = mFirebaseDatabase.reference.child("traveldeals")

        deal = intent.getParcelableExtra("Deal") ?: Traveldeal()

        etTitle.setText(deal.title)
        etPrice.setText(deal.price)
        etDescription.setText(deal.description)
        showImage(deal.imageUrl)
        btn_upload.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/jpeg"
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(Intent.createChooser(intent, "Insert Picture"), PICTURE_RESULT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICTURE_RESULT && resultCode == RESULT_OK){
            val imageUri = data?.data
            val ref = FirebaseUtil.mStorageReference.child(imageUri!!.lastPathSegment!!)
            val uploadTask = ref.putFile(imageUri)

            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val url = task.result.toString()
                   // val pictureName = ref.downloadUrl.result?.path
                    val pictureName = task.result?.lastPathSegment
                    deal.imageUrl = url
                    deal.imageName = pictureName
                    showImage(url)
                } else {
                    Toast.makeText(this, "Failed to upload the image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.save_menu, menu)
        menu?.findItem(R.id.delete_menu)!!.isVisible = FirebaseUtil.isAdmin!!
        menu.findItem(R.id.save_menu)!!.isVisible = FirebaseUtil.isAdmin!!
        enableEditTexts(FirebaseUtil.isAdmin!!)
        btn_upload.isEnabled = FirebaseUtil.isAdmin!!
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
    }

    private fun deleteDeal(){
        if (deal.id == null){
            Toast.makeText(this, "Please, save the deal before deleting it", Toast.LENGTH_LONG).show()
        }
        if (deal.imageName != null && deal.imageName!!.isNotEmpty()){
            val ref = FirebaseUtil.mFirebaseStorage.reference.child(deal.imageName!!)
            ref.delete().addOnSuccessListener {
                Log.d("Delete Image", "Delete successfull")
            }.addOnFailureListener {
                Log.d("Delete Image", it.message)
            }
        }
        mDatabaseReference.child(deal.id!!).removeValue()
    }

    private fun backToList(){
        val intent = Intent(this, UserActivity::class.java)
        startActivity(intent)
    }

    private fun enableEditTexts(isEnabled: Boolean){
        etTitle.isEnabled = isEnabled
        etDescription.isEnabled = isEnabled
        etPrice.isEnabled = isEnabled
        FirebaseUtil.attachListener()
    }

    private fun showImage(url: String?){
        if (url != null && !url.isEmpty()){
           val width = Resources.getSystem().displayMetrics.widthPixels
            Picasso.get()
                .load(url)
                .resize(width, width *2/3)
                .centerCrop()
                .into(image_deal)
        }
    }
}
