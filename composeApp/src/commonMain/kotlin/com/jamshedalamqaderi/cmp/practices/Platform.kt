package com.jamshedalamqaderi.cmp.practices

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform