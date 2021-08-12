package com.sc703.proyecto.ui.hoteles

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.sc703.proyecto.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_hotel.view.*


class HotelAdapter (val hotel:List<Hotel>): RecyclerView.Adapter<HotelAdapter.HotelHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return HotelHolder(layoutInflater.inflate(R.layout.item_hotel, parent, false))
    }

    override fun getItemCount(): Int = hotel.size

    override fun onBindViewHolder(holder: HotelHolder, position: Int) {
        holder.render(hotel[position])
    }

    class HotelHolder(val view: View): RecyclerView.ViewHolder(view){
        fun render(hotel: Hotel){
            view.tvNombre.text = hotel.nombre
            view.tvUbicacion.text = hotel.ubicacion
            view.tvPrecio.text = hotel.precio
            Picasso.get().load(hotel.imagen).into(view.ivHotel)
            //view.setOnClickListener { Toast.makeText(view.context, "Has seleccionado a ${hotel.nombre}", Toast.LENGTH_SHORT).show() }

            view.setOnClickListener {
                val uri: Uri = Uri.parse(hotel.imagen)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                view.context.startActivity(intent)
            }
        }
    }



}