package com.kode.domain.contacts.entity.image

import android.net.Uri

/**
 * Обёртка двух uri одного файла.
 * @param fileUri - uri файла, через который можно получить сам файл непосредственно
 * (в данном случае, [fileUri] сохраняется в БД).
 * @param sharedByProviderUri - uri этого же файла,
 * полученный через FileProvider. По данному uri сторонние приложения могут получить доступ к этому файлу.
 * Необходим приложению камеры.
 * */
data class SharedFileUri(
    val fileUri: Uri,
    val sharedByProviderUri: Uri
)