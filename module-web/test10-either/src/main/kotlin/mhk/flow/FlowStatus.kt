package mhk.flow

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right

interface FlowStatus

sealed class FlowSuccessStatus : FlowStatus {
    data class Flow01Success(val id: Long) : FlowSuccessStatus()
    data class Flow02Success(val id: Long) : FlowSuccessStatus()
    data class Flow03Success(val id: Long) : FlowSuccessStatus()
}

sealed class FlowFailureStatus : FlowStatus {
    data class Flow01Fail(val id: Long) : FlowFailureStatus()
    data class Flow02Fail(val id: Long) : FlowFailureStatus()
    data class Flow03Fail(val id: Long) : FlowFailureStatus()
}


inline fun <reified T : FlowStatus, R : FlowStatus> Either<FlowStatus, FlowStatus>.nextFlow(
    action: (T) -> Either<T, R>
): Either<FlowStatus, FlowStatus> {
    return flatMap {
        when (it) {
            is T -> action(it)
            else -> it.right()
        }
    }
}
