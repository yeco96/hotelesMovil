package com.sc703.proyecto.ui.salir

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.sc703.proyecto.MainActivity
import com.sc703.proyecto.R

class SalirFragment : Fragment() {

    private lateinit var btn_Salir: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_salir, container, false)

        btn_Salir = root.findViewById(R.id.btn_Salir)

        btn_Salir.setOnClickListener(View.OnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(context,"Desconectando", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity,MainActivity::class.java)
            startActivity(intent)
        })

        return root
    }

}