package com.sc703.proyecto.ui.hoteles

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sc703.proyecto.R
import java.io.File
import java.io.IOException

class HotelesFragment : Fragment() {

    lateinit var txt_realtime: TextView
    private val FBDB = FirebaseDatabase.getInstance()
    private val DBref = FBDB.reference
    private val Noticias = DBref.child("Hotel1")

    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_hoteles,container,false)

        txt_realtime = root.findViewById(R.id.txt_realtime)

        return root
    }

    override fun onStart() {
        super.onStart()

        //Utilizamos el evento listener de nuestra noticia para ver si se modifica su valor
        Noticias.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                txt_realtime.text = snapshot.value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                txt_realtime.text = "Se produjo un error en la conexion a la BD"
            }

        })
    }

}