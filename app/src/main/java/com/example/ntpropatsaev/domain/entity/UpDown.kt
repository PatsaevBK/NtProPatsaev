package com.example.ntpropatsaev.domain.entity

sealed class UpDown {
    data object Up: UpDown()

    data object Down: UpDown()
}