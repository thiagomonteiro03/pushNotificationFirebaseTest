package br.com.alura.meetups.model

import android.os.Build

data class Device(
    val marca: String = Build.BRAND,
    val modelo: String = Build.MODEL,
    val token: String
)