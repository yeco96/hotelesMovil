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
    lateinit var txt_realtime: TextView
    private val FBDB = FirebaseDatabase.getInstance()
    private val DBref = FBDB.reference
    private val Noticias = DBref.child("Hotel1")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_soporte,container,false)
        txt_realtime = root.findViewById(R.id.txt_realtime)
        return root
    }

    override fun onStart() {
        super.onStart()

        //Utilizamos el evento listener de nuestra noticia para ver si se modifica su valor
        Noticias.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                txt_realtime.text = "soprte" + snapshot.value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                txt_realtime.text  = "Se produjo un error en la conexion a la BD"
            }

        })
    }



}