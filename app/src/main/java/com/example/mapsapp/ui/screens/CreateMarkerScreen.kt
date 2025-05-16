
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.mapsapp.R
import java.io.File

@Composable
fun CreateMarkerScreen(navigateBack: (String) -> Unit) {

    val contexto = LocalContext.current
    val imagenUri = remember { mutableStateOf<Uri?>(null) }
    val imagen: Painter = painterResource(id = R.drawable.camera_icon)
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }


    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && imagenUri.value != null) {
            val stream = contexto.contentResolver.openInputStream(imagenUri.value!!)
            bitmap.value = BitmapFactory.decodeStream(stream)
        }
    }

    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imagenUri.value = it
                val stream = contexto.contentResolver.openInputStream(it)
                bitmap.value = BitmapFactory.decodeStream(stream)
            }
        }

    val takePictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && imagenUri.value != null) {
                val stream = contexto.contentResolver.openInputStream(imagenUri.value!!)
                bitmap.value = BitmapFactory.decodeStream(stream)
            }
        }


    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Abrir Camara") },
            text = { Text("¿Quieres tomar una foto?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    val uri = createImageUri(contexto)
                    imagenUri.value = uri
                    launcher.launch(uri!!)
                }) {
                    Text("Tomar Foto")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 85.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Title",
            fontSize = 34.sp
        )

        Spacer(modifier = Modifier.height(15.dp))

        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("") }
        )

        Spacer(modifier = Modifier.height(34.dp))
        Text("Descripcion",
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(15.dp))

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = imagen,
            contentDescription = "Camara Icon",
            modifier = Modifier
            .size(48.dp)
            .clickable{
                val uri = createImageUri(contexto)
                imagenUri.value = uri
                launcher.launch(uri!!)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))
        bitmap.value?.let {
            Image(bitmap = it.asImageBitmap(), contentDescription = null,
                modifier = Modifier.size(150.dp).clip(RoundedCornerShape(12.dp)),
                )
        }

        Spacer(modifier = Modifier.height(24.dp))

        TextButton( onClick = {
            pickImageLauncher.launch("image/*")
        },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color.Blue
            ),
            modifier = Modifier
                .width(100.dp)
                .height(40.dp)
        ){
            Text(
                "ADD",
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = navigateBack as () -> Unit,
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color.Blue
            ),
            modifier = Modifier
                .width(100.dp)
        ) {
            Text("Go Back")
        }
    }
}

fun createImageUri(contexto: Context): Uri? {
    val file = File.createTempFile("temp_image_", ".jpg", contexto.cacheDir).apply {
        createNewFile()
        deleteOnExit()
    }

    return FileProvider.getUriForFile(
        contexto,
        "${contexto.packageName}.fileprovider",
        file
    )
}


//Column(
//            Modifier
//                .fillMaxWidth()
//                .weight(0.4f),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Text("Create new marker", fontSize = 28.sp, fontWeight = FontWeight.Bold)
//            TextField(value = markerTitle, onValueChange = { myViewModel.editMarkerTitle(it) })
//            TextField(value = userIdText, onValueChange = { myViewModel.editMarkerUserId(UUID(it)) })
//            TextField(value = markerCreatedAt, onValueChange = { myViewModel.editMarkerCreatedAt(it) })
//            TextField(value = markerCategory, onValueChange = { myViewModel.editMarkerCategory(it) })
//            TextField(value = markerLongitude.toString(), onValueChange = { myViewModel.editMarkerLongitude(it.toDouble()) })
//            TextField(value = markerLatitude.toString(), onValueChange = { myViewModel.editMarkerLatitude(it.toDouble()) })
//            TextField(value = markerImage.toString(), onValueChange = { myViewModel.editMarkerImage(it) })
//            Button(onClick = { myViewModel.insertNewMarker(markerTitle, markerUserId, markerCreatedAt, markerCategory, markerLongitude, markerLatitude,userImage) }) {
//                Text("Insert")
//            }
//        }


//  val markersList by myViewModel.markersList.observeAsState(emptyList<Marker>())
//    myViewModel.getAllMarkers()
//    val markerTitle: String by myViewModel.markerName.observeAsState("")
//    val markerUserId: UUID by myViewModel.markerUserId.observeAsState(UUID.generateUUID())
//    val markerCreatedAt: String by myViewModel.markerCreatedAt.observeAsState("")
//    val markerCategory: String by myViewModel.markerCategory.observeAsState("")
//    val markerLongitude: Double by myViewModel.markerLongitude.observeAsState(0.0)
//    val markerLatitude: Double by myViewModel.markerLatitude.observeAsState(0.0)
//    val markerImage: String by myViewModel.markerImage.observeAsState("")
//
//    val userImage by remember { mutableStateOf<Bitmap?>(null) }
//    var userIdText by remember { mutableStateOf(markerUserId.toString()) }



