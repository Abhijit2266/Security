package com.example.security

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    lateinit var  inviteAdapter :InviteAdapter
    lateinit var mContext: Context
    private val listContacts:ArrayList<ContactModel> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("FetchContact22", "onViewCreated: ")
        val listMember = listOf(
            MemberModel("null"),
            MemberModel("null"),
            MemberModel("null"),
            MemberModel("null"),
        )
        val adapter = MemberAdapter(listMember)

        val recycler = requireView().findViewById<RecyclerView>(R.id.recycler_member)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        Log.d("FetchContact22", "fetchContacts: start")

        Log.d("FetchContact22", "fetchContacts: coroutine start ${listContacts.size}")
        inviteAdapter = InviteAdapter(listContacts)
        fetchDatabaseContacts()
        Log.d("FetchContact22", "fetchContacts: end")

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("FetchContact22", "fetchContacts: coroutine start")
            insertDatabaseContacts()

            Log.d("FetchContact22", "fetchContacts: coroutine end ${listContacts.size}")

        }
        val initeRecycler = requireView().findViewById<RecyclerView>(R.id.recycler_invite)
        initeRecycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        initeRecycler.adapter=inviteAdapter
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun fetchDatabaseContacts() {
        val database = SecurityDatabase.getDatabase(requireContext())

         database.contactDao().getAllContact().observe(viewLifecycleOwner){
             Log.d("FetchContact22","fetchDatabaseContacts:")
             listContacts.clear()
             listContacts.addAll(it)

             inviteAdapter.notifyDataSetChanged()

         }
    }
    private suspend fun insertDatabaseContacts() {
       val database = SecurityDatabase.getDatabase(requireContext())

        database.contactDao().insertAll(this.listContacts)
    }

    @SuppressLint("Range")
    private fun fetchContacts(): ArrayList<ContactModel> {
        Log.d("FetchContact22", "fetchContacts: start")
        val cr = requireActivity().contentResolver
        val cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null)
        val listContacts:ArrayList<ContactModel> = ArrayList()

        if (cursor!=null&& cursor.count>0){

            while (cursor.moveToNext()){
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhoneNumber = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                if (hasPhoneNumber>0){

                    val pCur = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?",
                        arrayOf(id),
                        ""
                    )
                    if (pCur !=null && pCur.count>0){
                        while (pCur.moveToNext()){
                            val phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                            listContacts.add(ContactModel(name,phone))
                        }
                        pCur.close()
                    }
                }
            }
            cursor.close()
        }
        Log.d("FetchContact22", "fetchContacts: end")
        return listContacts
    }
    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}