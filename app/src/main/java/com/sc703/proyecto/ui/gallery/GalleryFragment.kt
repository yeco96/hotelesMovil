package com.sc703.proyecto.ui.gallery

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.NonNull
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sc703.proyecto.R
import java.io.File
import java.io.IOException


class GalleryFragment : Fragment() {

    private lateinit var NameEdt: EditText
    private lateinit var PhoneEdt: EditText
    private lateinit var AddressEdt: EditText

    private lateinit var database: DatabaseReference
    private lateinit var usuario: Usuario


    private lateinit var imv_imagen: ImageView
    private lateinit var btn_Buscar: Button
    private lateinit var btn_Cargar: Button
    private lateinit var btn_Save: Button


    private lateinit var Almacenamiento: StorageReference
    private lateinit var RutaIMG: Uri
    private val ID_Proceso = 1234

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)

        imv_imagen = root.findViewById(R.id.imv_Imagen)
        btn_Buscar = root.findViewById(R.id.btn_Buscar)
        btn_Cargar = root.findViewById(R.id.btn_Cargar)
        btn_Save = root.findViewById(R.id.btn_Save)

        btn_Buscar.setOnClickListener { v -> BuscarArchivo(v) }
        btn_Cargar.setOnClickListener { v -> CargarArchivo(v) }
        btn_Save.setOnClickListener { Save() }

        NameEdt = root.findViewById(R.id.idEdtName)
        PhoneEdt = root.findViewById(R.id.idEdtPhoneNumber)
        AddressEdt = root.findViewById(R.id.idEdtAddress)

        database = Firebase.database.reference
        usuario = Usuario()

        val user = Firebase.auth.currentUser
        user?.let {
            usuario.id = user.uid
            AddressEdt.setText(user.email)
        }
        database.child("users").child(usuario.id).child("dataUser")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    usuario = snapshot.getValue<Usuario>()!!
                    NameEdt.setText(usuario.name.toString())
                    PhoneEdt.setText(usuario.telefono.toString())
                    AddressEdt.setText(usuario.correo.toString())
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        DescargaArchivo()

        return root
    }

    private fun addDatatoFirebase(name: String, phone: String, address: String) {
        usuario.name = name
        usuario.telefono = phone
        usuario.correo = address

        val child = database.child("users").child(usuario.id)

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(@NonNull snapshot: DataSnapshot) {
                child.child("dataUser").setValue(usuario)
                Toast.makeText(context, "data added", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(@NonNull error: DatabaseError) {
                Toast.makeText(context, "Fail to add data $error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun Save() {
        val name: String = NameEdt.text.toString()
        val phone: String = PhoneEdt.text.toString()
        val address: String = AddressEdt.text.toString()

        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(phone) && TextUtils.isEmpty(address)) {
            Toast.makeText(context, "Please add some data.", Toast.LENGTH_SHORT).show()
        } else {
            addDatatoFirebase(name, phone, address)
        }
    }

    //Funcion para abrir un explorador de archivos *** Imagenes
    private fun BuscarArchivo(view: View) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, R.string.Storage_Image_select.toString()), ID_Proceso
        )
    }

    //Funcion para obtener los datos de la imagen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ID_Proceso && resultCode == Activity.RESULT_OK && data != null)
            RutaIMG = data.data!!
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, RutaIMG)
            imv_imagen.setImageBitmap(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(
                context,
                R.string.Storage_Image_select_fail.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    //Funcion para Cargar las imagenes al Storage de Firebase
    private fun CargarArchivo(view: View) {
        Almacenamiento = FirebaseStorage.getInstance().reference.child("Imagenes")
        if (RutaIMG == null) {
            Toast.makeText(context, R.string.Storage_upload_notfound, Toast.LENGTH_SHORT).show()
        }

        val dialogo = ProgressDialog(context)
        dialogo.setTitle(R.string.Storage_upload)
        dialogo.show()


        val user = Firebase.auth.currentUser
        user?.let {
            usuario.id = user.uid
        }

        val ref = Almacenamiento.child(usuario.id.toString())
        ref.putFile(RutaIMG)
            .addOnSuccessListener {
                dialogo.dismiss()
                Toast.makeText(context, R.string.Storage_upload_succes, Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                dialogo.dismiss()
                Toast.makeText(context, R.string.Storage_upload_fail, Toast.LENGTH_SHORT).show()
            }
            .addOnProgressListener { snapshot ->
                val progreso = 100 * snapshot.bytesTransferred / snapshot.totalByteCount
                dialogo.setMessage(R.string.Storage_upload.toString() + progreso + "%")
            }

    }

    //Funcion para descargar las imagenes del Stotrage de Firebase
    private fun DescargaArchivo() {

        val user = Firebase.auth.currentUser
        user?.let {
            usuario.id = user.uid
        }

        //Asignamos la ruta de la imagen a descargar
        Almacenamiento = FirebaseStorage.getInstance().reference.child(
            "Imagenes/" + usuario.id.toString()
        )

        lateinit var temporal: File

        try {
            temporal = File.createTempFile("images", ".jpg")
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val archivoFinal = temporal
        Almacenamiento.getFile(temporal)
            .addOnSuccessListener {
                val imagen = archivoFinal.absolutePath
                imv_imagen.setImageBitmap(BitmapFactory.decodeFile(imagen))
            }
            .addOnFailureListener {
                Toast.makeText(context, R.string.Storage_download_fail, Toast.LENGTH_SHORT).show()
            }
    }
}