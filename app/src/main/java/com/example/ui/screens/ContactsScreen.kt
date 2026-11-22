package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EmergencyAlertHelper
import com.example.data.entity.TrustedContact
import com.example.ui.AppLanguage
import com.example.ui.RakshaViewModel
import com.example.ui.components.RakshaTopAppBar
import com.example.ui.theme.KavachTeal
import com.example.ui.theme.Saffron
import com.example.ui.theme.SaffronDeep
import com.example.ui.theme.SaffronTint
import com.example.ui.theme.TealTint
import com.example.ui.tr

@Composable
fun ContactsScreen(
    viewModel: RakshaViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val contacts by viewModel.contacts.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var showTestDialog by remember { mutableStateOf(false) }
    var pendingDelete by remember { mutableStateOf<TrustedContact?>(null) }

    Scaffold(
        topBar = {
            RakshaTopAppBar(
                title = tr("Trusted contacts", "विश्वसनीय संपर्क"),
                onBackClick = onNavigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.testTag("add_contact_fab")
            ) {
                Icon(Icons.Outlined.Add, contentDescription = tr("Add contact", "संपर्क जोड़ें"))
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("contacts_screen"),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                NoticeCard(
                    icon = Icons.Outlined.Info,
                    text = tr(
                        "When you send an SOS, these contacts get an SMS with your message, your coordinates, and a live Google Maps link. Your primary contact is alerted first.",
                        "जब आप SOS भेजती हैं, तो इन संपर्कों को आपके संदेश, लोकेशन और लाइव Google Maps लिंक के साथ SMS मिलता है। आपके प्राथमिक संपर्क को सबसे पहले सूचित किया जाता है।"
                    ),
                    container = MaterialTheme.colorScheme.surfaceVariant,
                    content = MaterialTheme.colorScheme.onBackground
                )
            }

            if (contacts.isEmpty()) {
                item {
                    SurfaceCard(modifier = Modifier.padding(top = 12.dp)) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(TealTint),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.People,
                                    contentDescription = null,
                                    tint = KavachTeal,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = tr("No contacts yet", "अभी कोई संपर्क नहीं है"),
                                fontWeight = FontWeight.Medium,
                                fontSize = 17.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = tr(
                                    "Add family or friends who can respond quickly if you send an SOS.",
                                    "परिवार या दोस्तों को जोड़ें जो SOS भेजने पर तुरंत मदद कर सकें।"
                                ),
                                fontSize = 13.sp,
                                lineHeight = 19.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(18.dp))
                            Button(
                                onClick = { showAddDialog = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("add_first_contact_button")
                            ) {
                                Text(tr("Add a contact", "संपर्क जोड़ें"))
                            }
                        }
                    }
                }
            } else {
                items(contacts, key = { it.id }) { contact ->
                    ContactCardItem(
                        contact = contact,
                        onCall = { EmergencyAlertHelper.dialNumber(context, contact.phone) },
                        onTogglePrimary = {
                            viewModel.updateContact(contact.copy(isPrimary = !contact.isPrimary))
                        },
                        onDelete = { pendingDelete = contact }
                    )
                }

                item {
                    OutlinedButton(
                        onClick = { showTestDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .height(48.dp)
                            .testTag("test_broadcast_button")
                    ) {
                        Text(tr("Send a test alert", "टेस्ट अलर्ट भेजें"))
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddContactDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, phone, rel, isPrimary ->
                viewModel.addContact(name, phone, rel, isPrimary)
                showAddDialog = false
            }
        )
    }

    pendingDelete?.let { contact ->
        AlertDialog(
            onDismissRequest = { pendingDelete = null },
            title = { Text(tr("Remove ${contact.name}?", "${contact.name} को हटाएँ?")) },
            text = {
                Text(
                    tr(
                        "They will stop receiving your SOS alerts.",
                        "उन्हें अब आपके SOS अलर्ट नहीं मिलेंगे।"
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteContact(contact)
                        pendingDelete = null
                    }
                ) {
                    Text(tr("Remove", "हटाएँ"), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingDelete = null }) {
                    Text(tr("Cancel", "रद्द करें"))
                }
            }
        )
    }

    if (showTestDialog) {
        AlertDialog(
            onDismissRequest = { showTestDialog = false },
            title = { Text(tr("Send a test alert?", "टेस्ट अलर्ट भेजें?")) },
            text = {
                Text(
                    tr(
                        "This sends a real SMS with your location to all ${contacts.size} of your contacts. The message says it's a test.",
                        "इससे आपके सभी ${contacts.size} संपर्कों को आपकी लोकेशन के साथ एक असली SMS जाएगा। संदेश में लिखा होगा कि यह केवल एक टेस्ट है।"
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.triggerSosAlertImmediate("TEST: This is a test of the Rakshakavach alert. I'm safe, no action needed.")
                        showTestDialog = false
                    }
                ) {
                    Text(tr("Send test", "टेस्ट भेजें"))
                }
            },
            dismissButton = {
                TextButton(onClick = { showTestDialog = false }) {
                    Text(tr("Cancel", "रद्द करें"))
                }
            }
        )
    }
}

@Composable
fun ContactCardItem(
    contact: TrustedContact,
    onCall: () -> Unit,
    onTogglePrimary: () -> Unit,
    onDelete: () -> Unit
) {
    val shape = RoundedCornerShape(14.dp)
    val initials = contact.name
        .trim()
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifEmpty { "?" }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .border(0.5.dp, MaterialTheme.colorScheme.outline, shape)
            .padding(start = 14.dp, end = 4.dp, top = 10.dp, bottom = 10.dp)
            .testTag("contact_item_${contact.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(TealTint),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                color = KavachTeal,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = contact.name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                if (contact.isPrimary) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SaffronTint)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = tr("Primary", "प्राथमिक"),
                            color = SaffronDeep,
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.sp
                        )
                    }
                }
            }
            Text(
                text = "${contact.relationship} · ${contact.phone}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        IconButton(
            onClick = onTogglePrimary,
            modifier = Modifier
                .size(40.dp)
                .testTag("toggle_primary_${contact.id}")
        ) {
            Icon(
                imageVector = if (contact.isPrimary) Icons.Filled.Star else Icons.Outlined.StarBorder,
                contentDescription = if (contact.isPrimary) {
                    tr("Remove as primary contact", "प्राथमिक संपर्क से हटाएँ")
                } else {
                    tr("Make primary contact", "प्राथमिक संपर्क बनाएँ")
                },
                tint = if (contact.isPrimary) Saffron else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        IconButton(
            onClick = onCall,
            modifier = Modifier
                .size(40.dp)
                .testTag("call_contact_btn_${contact.id}")
        ) {
            Icon(
                imageVector = Icons.Outlined.Call,
                contentDescription = tr("Call ${contact.name}", "${contact.name} को कॉल करें"),
                tint = KavachTeal
            )
        }

        IconButton(
            onClick = onDelete,
            modifier = Modifier
                .size(40.dp)
                .testTag("delete_contact_btn_${contact.id}")
        ) {
            Icon(
                imageVector = Icons.Outlined.DeleteOutline,
                contentDescription = tr("Remove ${contact.name}", "${contact.name} को हटाएँ"),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, phone: String, relationship: String, isPrimary: Boolean) -> Unit
) {
    val relationships = listOf(
        "Mother" to "माता",
        "Father" to "पिता",
        "Sister" to "बहन",
        "Brother" to "भाई",
        "Guardian" to "अभिभावक",
        "Friend" to "दोस्त",
        "Partner" to "साथी",
        "Colleague" to "सहकर्मी",
        "Other" to "अन्य"
    )

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var selectedRelIndex by remember { mutableIntStateOf(5) } // Default: Friend
    var isPrimary by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }
    var expandedRel by remember { mutableStateOf(false) }

    val selectedRelDisplay = if (AppLanguage.isHindi) {
        relationships[selectedRelIndex].second
    } else {
        relationships[selectedRelIndex].first
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(tr("Add a trusted contact", "विश्वसनीय संपर्क जोड़ें"), fontWeight = FontWeight.Medium)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                FormField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = false
                    },
                    label = tr("Name", "नाम"),
                    error = if (nameError) tr("Enter a name", "नाम दर्ज करें") else null,
                    modifier = Modifier.testTag("add_contact_name_input")
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = {
                        phone = it
                        phoneError = false
                    },
                    label = { Text(tr("Mobile number", "मोबाइल नंबर")) },
                    placeholder = { Text("+91 98765 43210") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    isError = phoneError,
                    supportingText = {
                        if (phoneError) Text(tr("Enter a valid mobile number", "मान्य मोबाइल नंबर दर्ज करें"))
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_contact_phone_input")
                )

                ExposedDropdownMenuBox(
                    expanded = expandedRel,
                    onExpandedChange = { expandedRel = it }
                ) {
                    OutlinedTextField(
                        value = selectedRelDisplay,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(tr("Relationship", "रिश्ता")) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRel) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedRel,
                        onDismissRequest = { expandedRel = false }
                    ) {
                        relationships.forEachIndexed { index, (en, hi) ->
                            DropdownMenuItem(
                                text = { Text(if (AppLanguage.isHindi) hi else en) },
                                onClick = {
                                    selectedRelIndex = index
                                    expandedRel = false
                                }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(role = Role.Checkbox) { isPrimary = !isPrimary },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isPrimary,
                        onCheckedChange = null,
                        modifier = Modifier
                            .padding(12.dp)
                            .testTag("add_contact_primary_checkbox")
                    )
                    Text(
                        text = tr("Make this my primary contact", "इसे मेरा प्राथमिक संपर्क बनाएँ"),
                        fontSize = 14.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    nameError = name.isBlank()
                    phoneError = phone.count { it.isDigit() } < 10
                    if (!nameError && !phoneError) {
                        onConfirm(
                            name.trim(),
                            phone.trim(),
                            relationships[selectedRelIndex].first,
                            isPrimary
                        )
                    }
                },
                modifier = Modifier.testTag("save_contact_dialog_button")
            ) {
                Text(tr("Save contact", "संपर्क सहेजें"))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(tr("Cancel", "रद्द करें"))
            }
        }
    )
}