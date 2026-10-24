package com.example.appsemestral.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

object FilePickerUtils {

    /**
     * Obtiene el nombre del archivo a partir de su Content Uri.
     */
    fun obtenerNombreArchivo(context: Context, uri: Uri?): String? {
        if (uri == null) return null
        var nombre: String? = null

        if (uri.scheme == "content") {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val indexNombre = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (indexNombre != -1) {
                        nombre = cursor.getString(indexNombre)
                    }
                }
            }
        }

        if (nombre == null) {
            nombre = uri.path?.substringAfterLast('/')
        }

        return nombre
    }
}
