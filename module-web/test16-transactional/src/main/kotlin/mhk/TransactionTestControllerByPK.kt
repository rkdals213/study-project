package mhk

import mhk.entity.TransactionTestEntity
import mhk.repository.TransactionTestEntityRepository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/pk")
class TransactionTestControllerByPK(
    private val repository: TransactionTestEntityRepository
) {

    @GetMapping("/select01")
    fun select01(): TransactionTestEntity {
        return repository.findById(1L).get()
    }

//    2025-01-25T06:28:02.825568Z	  500 Query	set session transaction read only
//    2025-01-25T06:28:02.826999Z	  500 Query	SET autocommit=0
//    2025-01-25T06:28:02.832260Z	  501 Query	SET autocommit=0
//    2025-01-25T06:28:02.834649Z	  501 Query	select tte1_0.id,tte1_0.data,tte1_0.transaction_id from transaction_test_entity tte1_0 where tte1_0.id=1
//    2025-01-25T06:28:02.836821Z	  500 Query	commit
//    2025-01-25T06:28:02.837593Z	  501 Query	commit
//    2025-01-25T06:28:02.838301Z	  501 Query	SET autocommit=1
//    2025-01-25T06:28:02.839105Z	  500 Query	SET autocommit=1
//    2025-01-25T06:28:02.839844Z	  500 Query	set session transaction read write

    @Transactional(readOnly = true)
    @GetMapping("/select02")
    fun select02(): TransactionTestEntity {
        return repository.findById(1L).get()
    }

//    2025-01-25T06:28:20.902369Z	  500 Query	set session transaction read only
//    2025-01-25T06:28:20.905005Z	  500 Query	SET autocommit=0
//    2025-01-25T06:28:20.906361Z	  500 Query	set session transaction read only
//    2025-01-25T06:28:20.908098Z	  500 Query	select tte1_0.id,tte1_0.data,tte1_0.transaction_id from transaction_test_entity tte1_0 where tte1_0.id=1
//    2025-01-25T06:28:20.910475Z	  500 Query	commit
//    2025-01-25T06:28:20.911196Z	  500 Query	set session transaction read write
//    2025-01-25T06:28:20.913042Z	  500 Query	commit
//    2025-01-25T06:28:20.913922Z	  500 Query	SET autocommit=1
//    2025-01-25T06:28:20.914694Z	  500 Query	set session transaction read write

    @Transactional(readOnly = false, propagation = Propagation.SUPPORTS)
    @GetMapping("/select03")
    fun select03(): TransactionTestEntity {
        return repository.findById(1L).get()
    }

//    2025-01-25T06:33:01.847665Z	  520 Query	set session transaction read only
//    2025-01-25T06:33:01.849997Z	  520 Query	SET autocommit=0
//    2025-01-25T06:33:01.855245Z	  521 Query	SET autocommit=0
//    2025-01-25T06:33:01.857810Z	  521 Query	select tte1_0.id,tte1_0.data,tte1_0.transaction_id from transaction_test_entity tte1_0 where tte1_0.id=1
//    2025-01-25T06:33:01.859553Z	  520 Query	commit
//    2025-01-25T06:33:01.860606Z	  521 Query	commit
//    2025-01-25T06:33:01.861639Z	  521 Query	SET autocommit=1
//    2025-01-25T06:33:01.862626Z	  520 Query	SET autocommit=1
//    2025-01-25T06:33:01.863429Z	  520 Query	set session transaction read write

    @Transactional(readOnly = false)
    @GetMapping("/select04")
    fun select04(): TransactionTestEntity {
        return repository.findById(1L).get()
    }

//    2025-01-25T06:28:40.757258Z	  500 Query	SET autocommit=0
//    2025-01-25T06:28:40.760151Z	  500 Query	set session transaction read only
//    2025-01-25T06:28:40.764483Z	  500 Query	select tte1_0.id,tte1_0.data,tte1_0.transaction_id from transaction_test_entity tte1_0 where tte1_0.id=1
//    2025-01-25T06:28:40.768835Z	  500 Query	commit
//    2025-01-25T06:28:40.769897Z	  500 Query	set session transaction read write
//    2025-01-25T06:28:40.784208Z	  500 Query	commit
//    2025-01-25T06:28:40.786183Z	  500 Query	SET autocommit=1
}
