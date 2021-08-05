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
        Hotel("Best Western Jaco Beach All Inclusive Resort", "Jacó, Costa Rica", "¢ 76.236", "https://cf.bstatic.com/xdata/images/hotel/max1024x768/108251013.jpg?k=18fa0a0484d18d6ee3ccf60f8c877b94fb458be403a590d2ddbc25537d18a692&o=&hp=1"),
        Hotel("Arenal Paraíso Resort & Spa", "Fortuna, Costa Rica", "¢ 64.494", "https://cf.bstatic.com/xdata/images/hotel/max1024x768/294465101.jpg?k=b4f88fb7b45b49d995c178784be5d0894a866923c58309107747ab41cddbbf6e&o=&hp=1"),
        Hotel("Hotel Manuel Antonio", "Manuel Antonio, Costa Rica", "CR 49.464", "https://cf.bstatic.com/xdata/images/hotel/max1024x768/151957467.jpg?k=6b5dc458311efe08f8bbdde0a411d206195ef35f6f7751f8c224377c6323e972&o=&hp=1"),
        Hotel("Occidental Tamarindo All Inclusive", "Tamarindo, Costa Rica", "¢ 68.823", "https://cf.bstatic.com/xdata/images/hotel/max1024x768/194573716.jpg?k=92622b4a766af788f1fea76611be700cea77303dc876079625a7106555f0287f&o=&hp=1"),
        Hotel("Los Sueños Marriott Ocean & Golf Resort", "Jacó, Costa Rica", "¢ 66.301", "https://cf.bstatic.com/xdata/images/hotel/max1024x768/65894916.jpg?k=17a8e467251c6e09a8b24b241b3adc39190620658c9266a7a0bb333e69d06292&o=&hp=1"),
        Hotel("Les Voiles Blanches", "Tamarindo, Costa Rica", "¢ 76,477", "https://cf.bstatic.com/xdata/images/hotel/max1024x768/272994896.jpg?k=cf9690197394e0bdb14b71b24e5e875951b09968f4ea707204b927767f90770b&o=&hp=1"),
        Hotel("Hotel Los Lagos Spa & Resort", "Fortuna, Costa Rica", "¢ 82.252", "https://cf.bstatic.com/xdata/images/hotel/max1024x768/285751963.jpg?k=d76f7eebd1ec2bba5dbc59ffbea8bb7b9548c0a4c1963b0502d0c2aaf5c4978b&o=&hp=1"),
        Hotel("Blue Banyan Inn", "Quepos, Costa Rica", "¢ 68.013", "https://cf.bstatic.com/xdata/images/hotel/max1024x768/16577688.jpg?k=703fc6a0cae47c7d2e1faaf577062d3f3d2579fad971315b3f4bd50c9ad948ec&o=&hp=1")

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