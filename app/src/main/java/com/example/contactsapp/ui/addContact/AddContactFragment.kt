package com.example.contactsapp.ui.addContact

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.contactsapp.MainActivity
import com.example.contactsapp.R
import com.example.contactsapp.databaseManager.ProfilManager
import com.example.contactsapp.databinding.ActivityAddProfilBinding
import com.example.contactsapp.models.Contact

class AddContactFragment : Fragment() {

    private var _binding: ActivityAddProfilBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityAddProfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnsaveAdd.setOnClickListener {
            val last_name = binding.ednameAdd.text.toString()
            val first_name = binding.edlastnameAdd.text.toString()
            val number = binding.edphoneAdd.text.toString()

            if (last_name.isNotEmpty() && first_name.isNotEmpty() && number.isNotEmpty()) {
                val p = Contact(MainActivity.data.size, last_name, first_name, number,if (binding.checkboxAdd.isChecked) 1 else 0)
                MainActivity.data.add(p)
                // Create an instance of ProfilManager
                val profilManager = ProfilManager(requireContext())
                profilManager.open("gestion_profile.db")
                p.isFav?.let { it1 -> profilManager.insert(p.last_name, p.first_name, p.number, it1) }
                profilManager.mabase?.close()

                //close keyboard
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.root.windowToken, 0)

                binding.btnsaveAdd.text = ""
                binding.progressBar.visibility=View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    Toast.makeText(requireContext(), "contact added", Toast.LENGTH_SHORT).show()
                    MainActivity.navController?.navigate(R.id.navigation_home)
                },2000)
            }else{
                Toast.makeText(requireContext(), "you need to add all your contact information's", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
