package com.example.contactsapp.ui.home

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsapp.MainActivity
import com.example.contactsapp.R
import com.example.contactsapp.databaseManager.ProfilManager
import com.example.contactsapp.databinding.ActivityListProfilBinding
import com.example.contactsapp.models.Contact
import com.example.projgl2.MyRecyclerProfileAdapter
import java.util.Locale

class HomeFragment : Fragment() {

    private lateinit var binding: ActivityListProfilBinding
    private lateinit var adapter: MyRecyclerProfileAdapter
    private var list = ArrayList<Contact>()
    private companion object {
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityListProfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editText = binding.editText
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let { searchTerm ->
                    if (searchTerm.isNotEmpty()) {
                        filterContacts(searchTerm.toString(), ArrayList(list))
                    } else {
                        val profilManager = ProfilManager(requireContext())
                        profilManager.open("gestion_profile.db")
                        list = profilManager.getContacts()
                        profilManager.mabase?.close()
                        adapter.setList(list)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        editText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                // Clear text when EditText gains focus
                editText.hint = ""
            }
        }

        val profilManager = ProfilManager(requireContext())
        profilManager.open("gestion_profile.db")
        list = profilManager.getContacts()
        profilManager.mabase?.close()
        adapter = MyRecyclerProfileAdapter(requireContext(), list)
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

        binding.layoutNoC.setOnClickListener {
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
            MainActivity.navController?.navigate(R.id.navigation_add_contact)
        }
    }

    private fun filterContacts(searchTerm: String, contactList: ArrayList<Contact>) {
        val filteredContacts = ArrayList<Contact>()
        for (contact in contactList) {
            if (contact.last_name?.lowercase(Locale.ROOT)?.contains(searchTerm.lowercase(Locale.ROOT)) == true ||
                contact.first_name?.lowercase(Locale.ROOT)?.contains(searchTerm.lowercase(Locale.ROOT)) == true ||
                contact.number?.contains(searchTerm) == true
            ) {
                filteredContacts.add(contact)
            }
        }

        adapter.setList(filteredContacts)
    }
}
