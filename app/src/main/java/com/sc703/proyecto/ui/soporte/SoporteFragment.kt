package com.sc703.proyecto.ui.soporte

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
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


    private lateinit var avatarImagellamar : ImageButton
    private lateinit var avatarImageViewCorreo : ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_soporte,container,false)
        txt_telefono = root.findViewById(R.id.txt_telefono)
        txt_correo = root.findViewById(R.id.txt_correo)
        txt_telefono.text = "cargando...";
        txt_correo.text = "cargando...";


        avatarImagellamar = root.findViewById(R.id.avatarImagellamar)
        avatarImageViewCorreo = root.findViewById(R.id.avatarImageViewCorreo)

        return root
    }

    override fun onStart() {
        super.onStart()

        txt_telefonoBD.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                txt_telefono.text = snapshot.value.toString()
                avatarImagellamar.setOnClickListener( { Llamar("tel:" + snapshot.value.toString()) })
            }
            override fun onCancelled(error: DatabaseError) {
                txt_telefono.text  = "Sin Datos"
            }
        })

        txt_correoBD.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                txt_correo.text = snapshot.value.toString()
                avatarImageViewCorreo.setOnClickListener(View.OnClickListener {
                    Correo(snapshot.value.toString(),"Hola","Hola, que hace") })
            }
            override fun onCancelled(error: DatabaseError) {
                txt_correo.text  = "Sin Datos"
            }
        })
    }


    private fun Llamar(Telefono:String){
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse(Telefono))
        startActivity(intent)
    }

    private fun Correo(Para: String, Asunto: String, Mensaje:String){
        val email = Intent(Intent.ACTION_SEND)
        email.data = Uri.parse("mailto:")
        email.type = "text/plain"
        email.putExtra(Intent.EXTRA_SUBJECT, Asunto)
        email.putExtra(Intent.EXTRA_EMAIL, arrayOf(Para))
        email.putExtra(Intent.EXTRA_TEXT, Mensaje)

        try {
            startActivity(Intent.createChooser(email,"Enviar correo"))
        }catch (e: ActivityNotFoundException){
            Toast.makeText(context,"No se encontró ninguna app de correo", Toast.LENGTH_SHORT)
                .show()
        }
    }



}