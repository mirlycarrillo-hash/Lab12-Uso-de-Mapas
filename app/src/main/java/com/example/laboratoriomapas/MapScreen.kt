package com.example.laboratoriomapas

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

val ArequipaLocation = LatLng(-16.4090, -71.5375)

fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int,
    width: Int = 120,
    height: Int = 120
): BitmapDescriptor? {
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, width, height)
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

@Composable
fun MapScreen() {
    val context = LocalContext.current

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(ArequipaLocation, 12f)
    }

    // ---------- Ejercicio 1: tipo de mapa ----------
    var mapType by remember { mutableStateOf(MapType.NORMAL) }

    val mapTypes = listOf(
        "Normal" to MapType.NORMAL,
        "Satélite" to MapType.SATELLITE,
        "Híbrido" to MapType.HYBRID,
        "Terreno" to MapType.TERRAIN
    )

    val locations = listOf(
        LatLng(-16.433415, -71.5442652),  // JLByR
        LatLng(-16.4205151, -71.4945209), // Paucarpata
        LatLng(-16.3524187, -71.5675994)  // Zamacola
    )

    // ---------- Polígonos ----------
    val mallAventuraPolygon = listOf(
        LatLng(-16.432292, -71.509145),
        LatLng(-16.432757, -71.509626),
        LatLng(-16.433013, -71.509310),
        LatLng(-16.432566, -71.508853)
    )

    val parqueLambramaniPolygon = listOf(
        LatLng(-16.422704, -71.530830),
        LatLng(-16.422920, -71.531340),
        LatLng(-16.423264, -71.531110),
        LatLng(-16.423050, -71.530600)
    )

    val plazaDeArmasPolygon = listOf(
        LatLng(-16.398866, -71.536961),
        LatLng(-16.398744, -71.536529),
        LatLng(-16.399178, -71.536289),
        LatLng(-16.399299, -71.536721)
    )

    // ---------- Polilíneas ----------
    val rutaCentroPolyline = listOf(
        LatLng(-16.399000, -71.536600),
        LatLng(-16.410000, -71.533500),
        LatLng(-16.422900, -71.531000),
        LatLng(-16.428000, -71.520000),
        LatLng(-16.432600, -71.509300)
    )

    val rutaPuntosPolyline = listOf(
        LatLng(-16.433415, -71.5442652),
        ArequipaLocation,
        LatLng(-16.4205151, -71.4945209)
    )

    val rutaYuraPolyline = listOf(
        ArequipaLocation,
        LatLng(-16.3524187, -71.5675994),
        LatLng(-16.2520984, -71.6836503)
    )

    // Cámara: va a Yura y luego regresa al centro de Arequipa
    LaunchedEffect(Unit) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(
                LatLng(-16.2520984, -71.6836503), // Yura
                12f
            ),
            durationMs = 3000
        )
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(
                LatLng(-16.4150, -71.5200),
                13f
            ),
            durationMs = 3000
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(mapType = mapType)
        ) {
            // Marcador personalizado de Arequipa
            Marker(
                state = rememberMarkerState(position = ArequipaLocation),
                icon = bitmapDescriptorFromVector(context, R.drawable.montana),
                title = "Arequipa, Perú"
            )

            // Marcadores de la lista
            locations.forEach { location ->
                Marker(
                    state = rememberMarkerState(position = location),
                    title = "Ubicación",
                    snippet = "Punto de interés"
                )
            }

            // Polígonos
            Polygon(
                points = plazaDeArmasPolygon,
                strokeColor = Color.Red,
                fillColor = Color.Blue,
                strokeWidth = 5f
            )
            Polygon(
                points = parqueLambramaniPolygon,
                strokeColor = Color.Red,
                fillColor = Color.Blue,
                strokeWidth = 5f
            )
            Polygon(
                points = mallAventuraPolygon,
                strokeColor = Color.Red,
                fillColor = Color.Blue,
                strokeWidth = 5f
            )

            // Polilíneas
            Polyline(
                points = rutaCentroPolyline,
                color = Color.Green,
                width = 10f
            )
            Polyline(
                points = rutaPuntosPolyline,
                color = Color.Magenta,
                width = 8f
            )
            Polyline(
                points = rutaYuraPolyline,
                color = Color(0xFFFF9800),
                width = 8f
            )
        }

        // Selector del tipo de mapa (encima del mapa)
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .horizontalScroll(rememberScrollState())
        ) {
            mapTypes.forEach { (nombre, tipo) ->
                FilterChip(
                    selected = mapType == tipo,
                    onClick = { mapType = tipo },
                    label = { Text(nombre) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}