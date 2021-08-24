package com.sc703.proyecto

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sc703.proyecto.databinding.ActivityPrincipalBinding
import com.sc703.proyecto.ui.gallery.Usuario
import java.io.File
import java.io.IOException

class ActivityPrincipal : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityPrincipalBinding

    private lateinit var textViewCORREO: TextView
    private lateinit var imv_imagen: ImageView
    private lateinit var usuario: Usuario
    private lateinit var Almacenamiento: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarActivityPrincipal.toolbar)

//        val navigationView : NavigationView  = findViewById(R.id.nav_view)
//        val headerView : View = navigationView.rootView
//

//        binding.appBarActivityPrincipal.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_activity_principal)

        val headerView : View = navView.getHeaderView(0)
        textViewCORREO  = headerView.findViewById(R.id.textViewCORREO)
        imv_imagen  = headerView.findViewById(R.id.imageViewUSUARIO)

        usuario = Usuario()
        val user = Firebase.auth.currentUser
        user?.let {
            usuario.correo = user.email.toString()
        }
        DescargaArchivo()

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_hoteles, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_salir
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        textViewCORREO.text = usuario.correo
    }



    private fun DescargaArchivo() {
        usuario = Usuario()
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
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.activity_principal, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_activity_principal)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}