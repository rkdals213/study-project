package mhk.flow

import arrow.core.Either

class FlowService {

    fun flow01(id: Long): Either<FlowFailureStatus.Flow01Fail, FlowSuccessStatus.Flow01Success> {
        return if (id <= 5L) {
            println("flow01 success : $id")
            Either.Right(FlowSuccessStatus.Flow01Success(id))
        } else {
            println("flow01 fail : $id")
            Either.Left(FlowFailureStatus.Flow01Fail(id))
        }
    }

    fun flow02(success: FlowSuccessStatus.Flow01Success): Either<FlowFailureStatus.Flow02Fail, FlowSuccessStatus.Flow02Success> {
        return if (success.id <= 3L) {
            println("flow02 success : $success")
            Either.Right(FlowSuccessStatus.Flow02Success(success.id))
        } else {
            println("flow02 fail : $success")
            Either.Left(FlowFailureStatus.Flow02Fail(success.id))
        }
    }

    fun flow03(success: FlowSuccessStatus.Flow02Success): Either<FlowFailureStatus.Flow03Fail, FlowSuccessStatus.Flow03Success> {
        return if (success.id == 1L) {
            println("flow03 success : $success")
            Either.Right(FlowSuccessStatus.Flow03Success(success.id))
        } else {
            println("flow03 fail : $success")
            Either.Left(FlowFailureStatus.Flow03Fail(success.id))
        }
    }
}
