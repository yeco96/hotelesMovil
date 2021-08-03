package com.sc703.proyecto.ui.soporte

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sc703.proyecto.R

class SoporteFragment : Fragment() {
    lateinit var txt_telefono: TextView
    lateinit var txt_correo: TextView
    private val FBDB = FirebaseDatabase.getInstance()
    private val DBref = FBDB.reference
    private val txt_telefonoBD = DBref.child("txt_telefono")
    private val txt_correoBD = DBref.child("txt_correo")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_soporte,container,false)
        txt_telefono = root.findViewById(R.id.txt_telefono)
        txt_correo = root.findViewById(R.id.txt_correo)
        return root
    }

    override fun onStart() {
        super.onStart()

        txt_telefonoBD.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                txt_telefono.text = snapshot.value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                txt_telefono.text  = "Sin Datos"
            }
        })

        txt_correoBD.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                txt_correo.text = snapshot.value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                txt_correo.text  = "Sin Datos"
            }
        })
    }



}