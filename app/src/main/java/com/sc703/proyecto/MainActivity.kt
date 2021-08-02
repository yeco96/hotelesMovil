package com.sc703.proyecto

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    private lateinit var edt_correo: EditText
    private lateinit var edt_contrasena: EditText

    private lateinit var Autenticador: FirebaseAuth

    private val Codigo_Solicitud = 1234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edt_correo = findViewById(R.id.edt_correo)
        edt_contrasena = findViewById(R.id.edt_contrasena)

        Autenticador = FirebaseAuth.getInstance()

        SolicitarPermisos()

    }

    fun Registro(view: View?) {
        val correo = edt_correo.text.toString()
        val contrasena = edt_contrasena.text.toString()

        ValidarContrasena()
        ValidarCorreo()

        Autenticador.createUserWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val User = Autenticador.currentUser
                    Toast.makeText(applicationContext, "Creación de usuario exitosa", Toast.LENGTH_SHORT).show()
                    ActualizarInterfaz(User)
                } else {
                    Toast.makeText(applicationContext, "Creación de usuario fallida", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun Ingreso(view: View?) {
        val correo = edt_correo.text.toString()
        val contrasena = edt_contrasena.text.toString()

        ValidarContrasena()
        ValidarCorreo()

        Autenticador.signInWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val User = Autenticador.currentUser
                    Toast.makeText(applicationContext, "Inicio de sesion correcto", Toast.LENGTH_SHORT).show()
                    ActualizarInterfaz(User)
                } else {
                    Toast.makeText(applicationContext, "Inicio de sesion fallido", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        val User = Autenticador.currentUser
        Toast.makeText(applicationContext, "Inicio de sesion correcto", Toast.LENGTH_SHORT).show()
        ActualizarInterfaz(User)
    }

    private fun ActualizarInterfaz(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, ActivityPrincipal::class.java)
            startActivity(intent)
        }
    }

    private val PASSWORD_PATTERN = Pattern.compile(
        "(?=.*[0-9])" +
                "(?=.*[a-zA-Z])" +
                "(?=.*[!@#$%&*+=.])" +
                "(?=\\S+$)" +
                ".{6,18}" +
                "$")

    private fun ValidarCorreo(): Boolean {
        val Correo = edt_correo.text.toString().trim { it <= ' ' }
        return if (Correo.isEmpty()) {
            edt_correo.error = "Debe digitar un correo electronico"
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(Correo).matches()) {
            edt_correo.error = "El correo digitado no tiene el formato correcto. Utilice alguien@correo.com"
            false
        } else {
            edt_correo.error = null
            true
        }
    }

    private fun ValidarContrasena(): Boolean {
        val Contrasena = edt_contrasena.text.toString().trim { it <= ' ' }
        return if (Contrasena.isEmpty()) {
            edt_contrasena.error = "Debe digitar una contraseña"
            false
        } else if (!PASSWORD_PATTERN.matcher(Contrasena).matches()) {
            edt_contrasena.error = "La contraseña debe contener Mayusculas,minusculas, caracteres especiales y números"
            false
        } else {
            edt_contrasena.error = null
            true
        }
    }

    private fun SolicitarPermisos(){
        val GPS = ActivityCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)
        val Telefono = ActivityCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.CALL_PHONE)
        val Internet = ActivityCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.INTERNET)

        if(GPS != PackageManager.PERMISSION_GRANTED ||
            Telefono != PackageManager.PERMISSION_GRANTED ||
            Internet != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.CALL_PHONE,
                    android.Manifest.permission.INTERNET),Codigo_Solicitud)
            }
        }

    }

}