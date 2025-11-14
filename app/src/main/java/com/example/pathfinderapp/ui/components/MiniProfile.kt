package com.example.pathfinderapp.ui.components

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun MiniProfile(
    username: String,
    email: String,
    role: String,
    memberSince: String,
    photoUri: Uri?,
    onChangePhoto: () -> Unit,
    onClose: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("Mi Perfil") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // FOTO PERFIL
                Box(modifier = Modifier.size(90.dp)) {
                    if (photoUri != null) {
                        AsyncImage(
                            model = photoUri,
                            contentDescription = "Foto perfil",
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                        )
                    }

                    // Botón para cambiar foto
                    FloatingActionButton(
                        onClick = onChangePhoto,
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.BottomEnd),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }

                // Información de perfil
                ProfileInfoRow("Nombre", username)
                ProfileInfoRow("Email", email)
                ProfileInfoRow("Rol", role)
                ProfileInfoRow("Miembro desde", memberSince)
            }
        },
        confirmButton = {
            Button(onClick = onClose) { Text("Cerrar") }
        }
    )
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
