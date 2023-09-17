package com.example.security

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
    private val listContacts:ArrayList<ContactModel> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listMember = listOf<MemberModel>(
            MemberModel("Abhijit"),
            MemberModel("Omkar"),
            MemberModel("Abhishek"),
            MemberModel("Aaku"),

        )
        val adapter = MemberAdapter(listMember)

        val recycler = requireView().findViewById<RecyclerView>(R.id.recycler_member)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter



        Log.d("FetchContact22", "fetchContacts: start")

        Log.d("FetchContact22", "fetchContacts: coroutine start ${listContacts.size}")
        val inviteAdapter = InviteAdapter(listContacts)
        Log.d("FetchContact22", "fetchContacts: end")

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("FetchContact22", "fetchContacts: coroutine start")
            listContacts.addAll(fetchContacts())

            insertDatabaseContacts(listContacts)

            withContext(Dispatchers.Main){
                inviteAdapter.notifyDataSetChanged()
            }
            Log.d("FetchContact22", "fetchContacts: coroutine end ${listContacts.size}")



        }


        val initeRecycler = requireView().findViewById<RecyclerView>(R.id.recycler_invite)
        initeRecycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        initeRecycler.adapter=inviteAdapter


    }

    private fun insertDatabaseContacts(listContacts: java.util.ArrayList<ContactModel>) {
        TODO("Not yet implemented")
    }

    private suspend fun insertDatabaseContacts() {
       val database = SecurityDatabase.getDatabase(requireContext())

        database.contactDao().insertAll(listContacts)

    }



    private fun fetchContacts(): ArrayList<ContactModel> {
        Log.d("FetchContact22", "fetchContacts: start")
        val cr = requireActivity().contentResolver
        val cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null)
        val listContacts:ArrayList<ContactModel> = ArrayList()

        if (cursor!=null&& cursor.count>0){

            while (cursor!=null && cursor.moveToNext()){
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
                        while (pCur!=null && pCur.moveToNext()){
                            val phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                            listContacts.add(ContactModel(name,phone))
                        }
                        pCur.close()

                    }
                }
            }
            if (cursor!=null){
                cursor.close()

            }
        }
        Log.d("FetchContact22", "fetchContacts: end")
        return listContacts
    }

    companion object {


        @JvmStatic
        fun newInstance() = HomeFragment()


    }
}