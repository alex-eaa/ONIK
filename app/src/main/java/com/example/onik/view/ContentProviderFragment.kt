package com.example.onik.view

import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.example.onik.databinding.FragmentContactsBinding

class ContentProviderFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getContacts()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun getContacts() {

        val cursor: Cursor? = activity?.contentResolver?.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )

        cursor?.let {
            for (i in 0..cursor.count) {
                if (cursor.moveToPosition(i)) {
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    addTextViewWithContact(name)
                }
            }
            it.close()
        }
    }


    private fun addTextViewWithContact(name: String?) {
        val scale = resources.displayMetrics.density
        val paddingInDP = 16
        val padding = (paddingInDP * scale + 0.5f).toInt()

        TextView(activity).apply {
            text = name
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
            updatePadding(bottom = padding)
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            binding.containerForContacts.addView(this)
        }
    }


}