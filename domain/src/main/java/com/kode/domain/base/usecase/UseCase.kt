package com.kode.domain.base.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
* Базовый юзкейс,
 * в котором необходимо переобпределить [UseCase.run].
 * [UseCase.run] будет завёрнута в Flow<Result>.
 * Подходит для one-shot задач (добавление записи в бд, удаление).
* */
abstract class UseCase<in Param, out Type> {

    // В переопределении run нужно вернуть Type
    abstract suspend fun run(param: Param): Type

    // "invoke" упакует результат run в flow и result
    suspend operator fun invoke(param: Param): Flow<Result<Type>> {
        return flow {
            val result = try {
                Result.success(run(param))
            } catch (e: Throwable) {
                e.printStackTrace()
                Result.failure(e)
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
}