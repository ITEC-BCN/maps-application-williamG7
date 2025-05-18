package com.example.mapsapp.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.viewmodels.MyViewModel
import kotlinx.uuid.UUID
import kotlinx.uuid.generateUUID
import java.io.File
import java.time.LocalDateTime

// pantalla para crear un nuevo marcador en el mapa
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateMarkerScreen(
    navigateBack: () -> Unit,  // función para volver atrás
    latitud: Double,          // latitud del marcador
    longitud: Double           // longitud del marcador
) {
    // obtiene el contexto de la app
    val context = LocalContext.current
    // instancia el viewmodel
    val viewModel: MyViewModel = viewModel()

    // estado para el título del marcador
    var title by remember { mutableStateOf("") }
    // estado para la descripción
    var description by remember { mutableStateOf("") }
    // estado para la imagen (bitmap)
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    // uri de la imagen seleccionada
    val imageUri = remember { mutableStateOf<Uri?>(null) }

    // launcher para tomar foto con cámara
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && imageUri.value != null) {
            // convierte la uri a bitmap
            val stream = context.contentResolver.openInputStream(imageUri.value!!)
            bitmap = BitmapFactory.decodeStream(stream)
        }
    }

    // launcher para seleccionar imagen de galería
    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri.value = it
            // convierte la uri a bitmap
            val stream = context.contentResolver.openInputStream(it)
            bitmap = BitmapFactory.decodeStream(stream)
        }
    }

    // layout principal en columna
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // título de la pantalla
        Text("Agregar Marcador", fontSize = 28.sp)

        Spacer(Modifier.height(16.dp))

        // campo para el título
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // campo para la descripción
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // fila con botones para cámara y galería
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            // botón para abrir cámara
            Button(onClick = {
                val uri = createImageUri(context)
                imageUri.value = uri
                cameraLauncher.launch(uri!!)
            }) {
                Text("\uD83D\uDCF7 Cámara")
            }

            // botón para abrir galería
            Button(onClick = {
                pickImageLauncher.launch("image/*")
            }) {
                Text("Galería")
            }
        }

        Spacer(Modifier.height(16.dp))

        // muestra la imagen seleccionada
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Imagen seleccionada",
                modifier = Modifier
                    .size(180.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }

        Spacer(Modifier.height(24.dp))

        // botón para guardar el marcador
        Button(
            onClick = {
                viewModel.insertNewMarker(
                    title = title,
                    user_id = UUID.generateUUID(),
                    created_at = LocalDateTime.now().toString(),
                    description = description,
                    longitude = longitud,
                    latitude = latitud,
                    image = bitmap
                )
                navigateBack()
            },
            enabled = title.isNotBlank() && description.isNotBlank(),  // solo activo si hay título y descripción
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar")
        }

        Spacer(Modifier.height(16.dp))

        // botón para cancelar
        OutlinedButton(
            onClick = navigateBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar")
        }
    }
}

// función para crear un uri temporal para la imagen
fun createImageUri(context: Context): Uri {
    val file = File.createTempFile("temp_image_", ".jpg", context.cacheDir).apply {
        createNewFile()
        deleteOnExit()
    }

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",  // usa fileprovider para seguridad
        file
    )
}