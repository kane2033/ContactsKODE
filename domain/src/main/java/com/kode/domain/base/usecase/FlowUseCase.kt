package com.kode.domain.base.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

/**
 * Базовый юзкейс, в котором нужно переопределить [FlowUseCase.run].
 * В отличие от [UseCase], в run нужно вернуть поток Flow<Type>.
 * Полезно, когда data source возвращает поток (БД Room).
 * */
abstract class FlowUseCase<in Param, out Type> {

    // В переопределении run нужно вернуть поток
    abstract fun run(param: Param): Flow<Type>

    // "invoke" упакует результат run в result
    operator fun invoke(param: Param): Flow<Result<Type>> {
        return run(param)
            .map { Result.success(it) }
            .catch { e ->
                e.printStackTrace()
                emit(Result.failure(e))
            }.flowOn(Dispatchers.IO)
    }

}