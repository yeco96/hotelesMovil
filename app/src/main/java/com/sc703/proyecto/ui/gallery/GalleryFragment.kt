package com.sc703.proyecto.ui.gallery

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sc703.proyecto.R
import com.sc703.proyecto.databinding.FragmentGalleryBinding
import java.io.File
import java.io.IOException

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var imv_imagen: ImageView
    private lateinit var edt_NombreImag: EditText
    private lateinit var btn_Buscar: Button
    private lateinit var btn_Cargar: Button
    private lateinit var btn_Descargar: Button

    //Creamos la referencia al servicio de Storage
    private lateinit var Almacenamiento: StorageReference

    //Ruta par la imagen
    private lateinit var RutaIMG: Uri

    //Asignamos un ID al proceso de busqueda
    private val ID_Proceso = 1234


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)

        imv_imagen = root.findViewById(R.id.imv_Imagen)
        edt_NombreImag = root.findViewById(R.id.edt_NombreImg)
        btn_Buscar = root.findViewById(R.id.btn_Buscar)
        btn_Cargar = root.findViewById(R.id.btn_Cargar)
        btn_Descargar = root.findViewById(R.id.btn_Descargar)

        btn_Buscar.setOnClickListener(View.OnClickListener { v -> BuscarArchivo(v) })
        btn_Cargar.setOnClickListener(View.OnClickListener { v -> CargarArchivo(v) })
        btn_Descargar.setOnClickListener(View.OnClickListener { DescargaArchivo() })

        return root
    }

    //Funcion para abrir un explorador de archivos *** Imagenes
    private fun BuscarArchivo(view: View){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Seleccione una imagen"),ID_Proceso)
    }

    //Funcion para obtener los datos de la imagen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ID_Proceso && resultCode == Activity.RESULT_OK && data != null)
            RutaIMG = data.data!!
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver,RutaIMG)
            imv_imagen.setImageBitmap(bitmap)
        }catch (e: IOException){
            e.printStackTrace()
            Toast.makeText(context,"No se pudo cargar la imagen seleccionada",
                Toast.LENGTH_SHORT).show()
        }
    }

    //Funcion para Cargar las imagenes al Storage de Firebase
    private fun CargarArchivo (view: View){
        Almacenamiento = FirebaseStorage.getInstance().reference.child("Imagenes")
        if (RutaIMG != null){
            val dialogo = ProgressDialog(context)
            dialogo.setTitle("Progreso de Carga")
            dialogo.show()

            val ref = Almacenamiento.child(edt_NombreImag.text.toString())
            ref.putFile(RutaIMG)
                .addOnSuccessListener {
                    dialogo.dismiss()
                    Toast.makeText(context,"Imagen cargada correctamente", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    dialogo.dismiss()
                    Toast.makeText(context, "No se pudo cargar la imagen", Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnProgressListener { snapshot ->
                    val progreso = 100 * snapshot.bytesTransferred / snapshot.totalByteCount
                    dialogo.setMessage("Progreso de carga " + progreso + "%")
                }
        }else{
            Toast.makeText(context,"No se encontro una imagen para cargar", Toast.LENGTH_SHORT)
                .show()
        }
    }

    //Funcion para descargar las imagenes del Stotrage de Firebase
    private fun DescargaArchivo(){
        //Asignamos la ruta de la imagen a descargar
        Almacenamiento = FirebaseStorage.getInstance().reference.child("Imagenes/" +
                edt_NombreImag.text.toString())

        lateinit var temporal: File

        try {
            temporal = File.createTempFile("images",".jpg")
        }catch (e: IOException){
            e.printStackTrace()
        }
        val archivoFinal = temporal
        Almacenamiento.getFile(temporal)
            .addOnSuccessListener {
                val imagen = archivoFinal.absolutePath
                imv_imagen.setImageBitmap(BitmapFactory.decodeFile(imagen))
            }
            .addOnFailureListener {
                Toast.makeText(context,"La descarga del archivo ha fallado", Toast.LENGTH_SHORT)
                    .show()
            }
    }
}