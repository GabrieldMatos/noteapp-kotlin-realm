package com.example.mynoteapp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.Realm.getDefaultInstance
import io.realm.RealmResults
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.notes_rv_layout.view.*
import java.io.Serializable

class NotesAdapter(
    private val context: Context?,
    private val notesList: RealmResults<Notes>

)
    :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.notes_rv_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder.itemView.title_TextView.text = notesList[position]!!.title
        holder.itemView.description_TextView.text = notesList[position]!!.description
        holder.itemView.createAt_TextView.text = notesList[position]!!.createAt

        fun deleteFromRealm(){
            try {
                val realm:Realm = getDefaultInstance()
                realm.beginTransaction()
                val data = realm.where<Notes>().equalTo("id", notesList[position]!!.id).findFirst()
                data?.deleteFromRealm()
                realm.commitTransaction()
                Toast.makeText(context, "Nota deletada com sucesso!", Toast.LENGTH_SHORT).show()

            }catch (e:Exception){
                Toast.makeText(context, "Erro ao deletar nota $e", Toast.LENGTH_SHORT).show()
            }
        }

        fun showDialog(){
            lateinit var dialog: AlertDialog

            val builder = context?.let { AlertDialog.Builder(it) }
            builder?.setTitle("DELETE!")
            builder?.setMessage("Tem certeza que deseja deletar esta nota?")


            val dialogClickListener = DialogInterface.OnClickListener{ _, which ->
                when(which){
                    DialogInterface.BUTTON_POSITIVE -> {
                        deleteFromRealm()
                        notifyDataSetChanged()
                    }
                }
            }

            builder?.setPositiveButton("YES",dialogClickListener)
            builder?.setNegativeButton("NO",dialogClickListener)
            builder?.setNeutralButton("CANCEL",dialogClickListener)

            dialog = builder!!.create()
            dialog.show()
        }

        holder.itemView.findViewById<ImageButton>(R.id.delete_Btn).setOnClickListener {
            showDialog()
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, AddNotesActivity::class.java)
            intent.putExtra("id", notesList[position]!!.id)
            context?.startActivity(intent)
        }
    }

    class ViewHolder(v: View?): RecyclerView.ViewHolder(v!!){

    }
}