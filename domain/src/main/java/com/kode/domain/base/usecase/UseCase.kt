package com.kode.domain.base.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

abstract class UseCase<out Type, in Param> {

    // В переопределении run нужно только вернуть Type
    abstract fun run(param: Param): Type

    // "invoke" упакует результат run в flow и result
    operator fun invoke(param: Param): Flow<Result<Type>> {
        return flowOf(Result.success(run(param))).catch { e ->
            e.printStackTrace()
            emit(Result.failure(e))
        }.flowOn(Dispatchers.IO)
    }

}