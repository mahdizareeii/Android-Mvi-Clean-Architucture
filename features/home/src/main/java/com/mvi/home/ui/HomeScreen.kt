package com.mvi.home.ui

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvi.core.base.BaseScreen
import javax.inject.Inject

class HomeScreen @Inject constructor() : BaseScreen() {

    @Composable
    override fun onScreenCreated() {
        val homeViewModel = hiltViewModel<HomeViewModel>()
        homeViewModel.showToast.value?.let {
            Toast.makeText(getContext(), it, Toast.LENGTH_SHORT).show()
        }
        var contactList by remember {
            mutableStateOf<List<ContactsInfo>>(listOf())
        }

        val context = getContext() as ComponentActivity

        val getPermission = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                //get list
                contactList = getContacts(context)
            } else {
                //show message for permission
            }
        }
        SideEffect {
            getPermission.launch(Manifest.permission.READ_CONTACTS)
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(contactList) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = it.phoneNumber.toString() + " - " + it.displayedName.toString()
                )
            }
        }
    }

    private fun getContacts(context: Context): List<ContactsInfo> {
        val contentResolver: ContentResolver = context.contentResolver
        val contactsInfoList = ArrayList<ContactsInfo>()
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        if ((cursor?.count ?: 0) > 0) {
            while (cursor?.moveToNext() == true) {
                val hasPhoneNumber = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER) ?: 0
                ).toInt()
                if (hasPhoneNumber > 0) {
                    val contactsInfo = ContactsInfo()
                    contactsInfo.contactId = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts._ID) ?: 0
                    )
                    contactsInfo.displayedName = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME) ?: 0
                    )
                    val phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(contactsInfo.contactId),
                        null
                    )
                    if (phoneCursor?.moveToNext() == true) {
                        val phoneNumber = phoneCursor.getString(
                            phoneCursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                            ) ?: 0
                        )
                        contactsInfo.phoneNumber = phoneNumber
                    }
                    phoneCursor?.close()
                    contactsInfoList.add(contactsInfo)
                }
            }
        }
        cursor?.close()
        return contactsInfoList
    }
}