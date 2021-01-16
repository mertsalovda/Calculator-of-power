package ru.mertsalovda.core_api.interfaces

import retrofit2.Response

interface Repository {

    fun execute(
        response: Response<Any?>,
        onSuccess: (Any?) -> Unit,
        onError: (Response<Any?>) -> Unit
    )
}