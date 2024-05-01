package com.example.contactsapp.ui.favoriteContacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsapp.databaseManager.ProfilManager
import com.example.contactsapp.databinding.FragmentFavoritesBinding
import com.example.contactsapp.models.Contact
import com.example.projgl2.MyRecyclerFavAdapter

class FavoriteContactsFragment: Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var adapter: MyRecyclerFavAdapter
    private var list = ArrayList<Contact>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val profilManager = ProfilManager(requireContext())
        profilManager.open("gestion_profile.db")
        list = profilManager.getFavs()
        profilManager.mabase?.close()
        adapter = MyRecyclerFavAdapter(requireContext(), list)
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
        binding.lvList.adapter = adapter
        binding.lvList.layoutManager = layoutManager

        if (list.isEmpty()){
            binding.layoutNoC.visibility=View.VISIBLE
            binding.lvList.visibility=View.GONE
        }else{
            binding.layoutNoC.visibility=View.GONE
            binding.lvList.visibility=View.VISIBLE
        }
    }
}
