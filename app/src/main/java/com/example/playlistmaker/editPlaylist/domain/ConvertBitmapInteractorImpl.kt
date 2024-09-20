package com.example.playlistmaker.editPlaylist.domain

class ConvertBitmapInteractorImpl (val convertBitmapRepository : ConvertBitmapRepository): ConvertBitmapInteractor {
    override suspend fun convertPathToBitmap(
        path: String,
        pathToBitmapConverterCallback: PathToBitmapConverterCallback
    ) {
        convertBitmapRepository.convertPathToBitmap(path,pathToBitmapConverterCallback)
    }
}