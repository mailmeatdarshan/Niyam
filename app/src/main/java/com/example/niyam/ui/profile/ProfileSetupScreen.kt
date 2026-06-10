package com.example.niyam.ui.profile

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.niyam.ui.theme.SaffronPrimary
import com.example.niyam.ui.theme.SaffronSecondary
import com.example.niyam.ui.theme.SaffronTertiary
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
    viewModel: ProfileViewModel,
    isEditing: Boolean = false,
    onSaveSuccess: () -> Unit,
    onBackClick: (() -> Unit)? = null
) {
    val context = LocalContext.current
    
    val currentName by viewModel.userName.collectAsState()
    val currentHeight by viewModel.userHeight.collectAsState()
    val currentWeight by viewModel.userWeight.collectAsState()
    val currentGender by viewModel.userGender.collectAsState()
    val currentPfpPath by viewModel.userPfpPath.collectAsState()

    var name by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    
    // Set initial values if editing or loading existing profile
    LaunchedEffect(currentName, currentHeight, currentWeight, currentGender) {
        if (currentName.isNotEmpty()) {
            name = currentName
            height = if (currentHeight > 0f) currentHeight.toString() else ""
            weight = if (currentWeight > 0f) currentWeight.toString() else ""
            gender = currentGender
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
        }
    }

    val imageBitmap: ImageBitmap? = remember(selectedImageUri, currentPfpPath) {
        try {
            if (selectedImageUri != null) {
                context.contentResolver.openInputStream(selectedImageUri!!)?.use { input ->
                    BitmapFactory.decodeStream(input)?.asImageBitmap()
                }
            } else if (!currentPfpPath.isNullOrEmpty()) {
                val file = File(currentPfpPath!!)
                if (file.exists()) {
                    BitmapFactory.decodeFile(file.absolutePath)?.asImageBitmap()
                } else null
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (isEditing) "EDIT PROFILE" else "NIYAM SETUP",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = SaffronPrimary
                        )
                    )
                },
                navigationIcon = {
                    if (isEditing && onBackClick != null) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = SaffronPrimary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background.copy(alpha = 0.95f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                if (!isEditing) {
                    Text(
                        text = "Begin Your Niyam Journey",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Let's set up your profile to personalize your daily spiritual, health, and focus routines.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }

                // Profile Picture Selector
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(BorderStroke(2.dp, SaffronPrimary), CircleShape)
                        .clickable { launcher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (imageBitmap != null) {
                        Image(
                            bitmap = imageBitmap,
                            contentDescription = "Profile Picture",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Default Avatar",
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                    
                    // Camera icon overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Change Photo",
                            tint = Color.White,
                            modifier = Modifier
                                .padding(bottom = 6.dp)
                                .size(20.dp)
                        )
                    }
                }

                // Form Fields Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.15f))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Name Input
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Your Name") },
                            placeholder = { Text("Enter your name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SaffronPrimary,
                                focusedLabelColor = SaffronPrimary
                            )
                        )

                        // Height Input
                        OutlinedTextField(
                            value = height,
                            onValueChange = { height = it },
                            label = { Text("Height (cm)") },
                            placeholder = { Text("e.g. 175") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SaffronPrimary,
                                focusedLabelColor = SaffronPrimary
                            )
                        )

                        // Weight Input
                        OutlinedTextField(
                            value = weight,
                            onValueChange = { weight = it },
                            label = { Text("Weight (kg)") },
                            placeholder = { Text("e.g. 70") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SaffronPrimary,
                                focusedLabelColor = SaffronPrimary
                            )
                        )

                        // Gender Row Label
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Gender",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val options = listOf("Male", "Female", "Other")
                                options.forEach { option ->
                                    val isSelected = gender == option
                                    val chipBg = if (isSelected) SaffronPrimary else MaterialTheme.colorScheme.surfaceVariant
                                    val chipText = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                    
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(chipBg)
                                            .clickable { gender = option }
                                            .padding(vertical = 12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = option,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = chipText
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                if (showError) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Save Button
                Button(
                    onClick = {
                        if (name.isBlank()) {
                            errorMessage = "Please enter your name"
                            showError = true
                        } else if (height.toFloatOrNull() == null) {
                            errorMessage = "Please enter a valid height"
                            showError = true
                        } else if (weight.toFloatOrNull() == null) {
                            errorMessage = "Please enter a valid weight"
                            showError = true
                        } else if (gender.isBlank()) {
                            errorMessage = "Please select your gender"
                            showError = true
                        } else {
                            showError = false
                            val success = viewModel.saveProfile(
                                name = name,
                                heightStr = height,
                                weightStr = weight,
                                gender = gender,
                                pfpUri = selectedImageUri
                            )
                            if (success) {
                                onSaveSuccess()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(SaffronPrimary, SaffronSecondary)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isEditing) "SAVE CHANGES" else "GET STARTED",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp,
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }
    }
}
