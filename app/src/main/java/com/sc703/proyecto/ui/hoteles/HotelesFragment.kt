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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sc703.proyecto.R
import kotlinx.android.synthetic.main.fragment_hoteles.*
import kotlinx.android.synthetic.main.fragment_hoteles.view.*
import java.io.File
import java.io.IOException

class HotelesFragment : Fragment() {

    private lateinit var av_Banner: AdView

    val hoteles = listOf(
        Hotel("Best Western Jaco Beach All Inclusive Resort", "Jacó, Costa Rica", "¢ 76.236", "https://gocrein.com/wp-content/uploads/2020/10/94144093_3110521055635006_8476768203819712512_o.jpg"),
        Hotel("Hotel Los Lagos Spa & Resort", "Fortuna, Costa Rica", "¢ 82.252", "https://fastly.4sqi.net/img/general/200x200/64930688_Kw6lrWCD2Uy7asDWjG64bhBGpaT1pX6w6kXI4ppF-yY.jpg"),
        Hotel("Occidental Tamarindo All Inclusive", "Tamarindo, Costa Rica", "¢ 68.823", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQGxZGQkPK2N_qbbkb2XMG7mD1kdCbOcU1FZw&usqp=CAU"),
        Hotel("Los Sueños Marriott Ocean & Golf Resort", "Jacó, Costa Rica", "¢ 66.301", "https://foto.hrsstatic.com/fotos/3/2/1280/1280/100/FFFFFF/http%3A%2F%2Ffoto-origin.hrsstatic.com%2Ffoto%2F1%2F1%2F1%2F1%2F111165%2F111165_u_26639577.jpg/k5ls%2BYLuLltq3TblXOSWng%3D%3D/1024%2C683/6/Marriott_Vacation_Club_at_Los_Suenos-Los_Chiles-Info-27-111165.jpg"),
        Hotel("Les Voiles Blanches", "Tamarindo, Costa Rica", "¢ 76,477", "https://foto.hrsstatic.com/fotos/0/2/1280/1280/100/FFFFFF/http%3A%2F%2Ffoto-origin.hrsstatic.com%2Ffoto%2F9%2F4%2F2%2F5%2F942556%2F942556_um_19316871.jpg/qHKO%2BZ6cYYv%2BfbuCpWGOPA%3D%3D/500%2C331/6/Les_Voiles_Blanches_BB-Tamarindo-Surroundings-942556.jpg"),
        Hotel("Hotel Manuel Antonio", "Manuel Antonio, Costa Rica", "CR 49.464", "https://www.instantworldbooking.com/services/reservation/display_pic.php?im=hotellacolina_manuelantonio_outoceaside72.jpg&pth=https%3A%2F%2Fwww.instantworldbooking.com%2Fdirectory%2FCosta_Rica%2Fimg%2F&w=1200&h=900&req_w=1200&qu=85&crophr=1.10"),
        Hotel("Arenal Paraíso Resort & Spa", "Fortuna, Costa Rica", "¢ 64.494", "https://foto.hrsstatic.com/fotos/3/2/1280/1280/100/FFFFFF/http%3A%2F%2Ffoto-origin.hrsstatic.com%2Ffoto%2F4%2F1%2F7%2F5%2F417592%2F417592_b_7910785.jpg/HyT%2BzncsvSS%2BhQmrDN0DRA%3D%3D/1024%2C768/6/Arenal_Paraiso_Resort_and_Spa-Fortuna-Hotel_bar-417592.jpg"),
        Hotel("Blue Banyan Inn", "Quepos, Costa Rica", "¢ 68.013", "https://cdn.ostrovok.ru/t/240x240/content/30/ee/30ee34a8c94e70b7c53ac4744cfb222755863075.jpeg")

    )

    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_hoteles,container,false)

        view.rvHoteles.layoutManager = LinearLayoutManager(context)
        view.rvHoteles.adapter = HotelAdapter(hoteles)

        MobileAds.initialize(context){}
        val solicitud_Anuncio = AdRequest.Builder().build()
        av_Banner = view.findViewById(R.id.av_Banner)
        av_Banner.loadAd(solicitud_Anuncio)

        return view
    }

}