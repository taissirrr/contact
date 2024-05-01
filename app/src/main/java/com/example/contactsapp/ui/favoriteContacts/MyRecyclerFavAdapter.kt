package com.example.projgl2

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsapp.R
import com.example.contactsapp.databaseManager.ProfilManager
import com.example.contactsapp.models.Contact

class MyRecyclerFavAdapter(
    var con: Context,
    data: ArrayList<Contact>,
) :
    RecyclerView.Adapter<MyRecyclerFavAdapter.ViewHolder>() {
    var data: ArrayList<Contact>

    init {
        this.data = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(con).inflate(R.layout.view_profil, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p: Contact = data[position]
        if (p.isFav==1){
            holder.imgFavorite.setImageResource(R.drawable.favorite_selected)
        }else holder.imgFavorite.setImageResource(R.drawable.favorite_unselected)

        holder.tvlastName.text = p.last_name
        holder.tvfirstName.text = p.first_name
        holder.tvnum.text = p.number
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvlastName: TextView
        var tvfirstName: TextView
        var tvnum: TextView
        var imgdelete: ImageView
        var imgedit: ImageView
        var imgcall: ImageView
        var imgFavorite: ImageView

        init {
            tvlastName = v.findViewById(R.id.tvnom_profil)
            tvfirstName = v.findViewById(R.id.tvprenom_profil)
            tvnum = v.findViewById(R.id.tvnumero_profil)
            imgdelete = v.findViewById(R.id.imageViewdelete_profil)
            imgedit = v.findViewById(R.id.imageViewedit_profil)
            imgcall = v.findViewById(R.id.imageViewcall_profil)
            imgFavorite=v.findViewById(R.id.fav)

            imgFavorite.setOnClickListener {
                val i = adapterPosition
                val profile: Contact = data[i]
                if (profile.isFav==0){
                    profile.isFav=1
                }else profile.isFav=0
                val p = Contact(profile.id, profile.last_name, profile.first_name, profile.number,profile.isFav)
                data[adapterPosition] = p
                if (p.isFav==0)
                    data.remove(p)
                notifyDataSetChanged()
                val profilManager = ProfilManager(con)
                profilManager.open("gestion_profile.db")
                p.id?.let { it1 -> p.isFav?.let { it2 ->
                    profilManager.modify(it1, p.last_name, p.first_name, p.number,
                        it2
                    )
                } }
                profilManager.mabase?.close()
            }
            // event
            imgdelete.setOnClickListener { // afficher une boite de dialog
                val alert = AlertDialog.Builder(con)
                alert.setTitle("Confirmation")
                alert.setMessage("You really want to delete your contact?")
                alert.setPositiveButton(
                    "supprimer"
                ) { dialogInterface, i ->
                    val indice = adapterPosition
                    val profilManager = ProfilManager(con)
                    profilManager.open("gestion_profile.db")
                    if (data.size > 0) {
                        Log.d("TAG", "delete: " + data[indice].id)
                        data[indice].id?.let { it1 -> profilManager.delete(it1) }
                        data.removeAt(indice)
                    }
                    profilManager.mabase?.close()
                    notifyDataSetChanged() // MAJ de l'affichage
                }
                alert.setNegativeButton(
                    "Cancel"
                ) { dialogInterface, i ->
                }
                alert.setNeutralButton("Exit", null)
                alert.show()
            }
            imgcall.setOnClickListener {
                val p: Contact? = data.find { it.id==data[adapterPosition].id}
                val  phoneNumber=p?.number
                if (phoneNumber?.isNotEmpty() == true) {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:$phoneNumber")
                    }
                    con.startActivity(intent)
                } else {
                    Toast.makeText(con, "Phone number is empty", Toast.LENGTH_SHORT).show()
                }
            }
            imgedit.setOnClickListener(object : View.OnClickListener {
                var alertDialog: AlertDialog? = null
                override fun onClick(view: View) {
                    val alert = AlertDialog.Builder(
                        con
                    )
                    alert.setTitle("Edition")
                    alert.setMessage("Update informations")
                    val inflater = LayoutInflater.from(con)
                    val vd: View = inflater.inflate(R.layout.view_dialog, null)
                    val edlast_name = vd.findViewById<EditText>(R.id.last_name)
                    val edfirst_name = vd.findViewById<EditText>(R.id.first_name)
                    val ednum = vd.findViewById<EditText>(R.id.num)
                    val btnSave = vd.findViewById<Button>(R.id.btn_save)
                    val btnCancel = vd.findViewById<Button>(R.id.btn_cancel)
                    val p: Contact? = data.find { it.id==data[adapterPosition].id}
                    edlast_name.setText(p?.last_name)
                    ednum.setText(p?.number)
                    edfirst_name.setText(p?.first_name)
                    btnCancel.setOnClickListener { alertDialog!!.dismiss() }
                    btnSave.setOnClickListener {
                        val last_name = edlast_name.text.toString()
                        val first_name = edfirst_name.text.toString()
                        val num = ednum.text.toString()
                        val pro = Contact(p?.id, last_name, first_name, num,p?.isFav)
                        data[adapterPosition] = pro
                        notifyDataSetChanged()
                        alertDialog!!.dismiss()
                        val profilManager = ProfilManager(con)
                        profilManager.open("gestion_profile.db")
                        pro.id?.let { it1 -> pro.isFav?.let { it2 ->
                            profilManager.modify(it1, pro.first_name, pro.last_name, pro.number,
                                it2
                            )
                        } }
                        profilManager.mabase?.close()
                    }
                    alert.setView(vd)
                    alertDialog = alert.create()
                    alertDialog?.show()
                }
            })
        }
    }
}