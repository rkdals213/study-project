package mhk

import mhk.entity.TransactionTestEntity
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/query")
class TransactionTestControllerByQuery(
    private val repository: TransactionTestQueryRepository
) {

    @GetMapping("/select01")
    fun select01(): TransactionTestEntity {
        return repository.findById(1L) ?: throw NoSuchElementException()
    }

//    2025-01-25T06:35:25.268888Z	  530 Query	select tte1_0.id,tte1_0.data,tte1_0.transaction_id from transaction_test_entity tte1_0 where tte1_0.id=1

    @Transactional(readOnly = true)
    @GetMapping("/select02")
    fun select02(): TransactionTestEntity {
        return repository.findById(1L) ?: throw NoSuchElementException()
    }

//    2025-01-25T06:35:40.723047Z	  530 Query	set session transaction read only
//    2025-01-25T06:35:40.724728Z	  530 Query	SET autocommit=0
//    2025-01-25T06:35:40.727900Z	  530 Query	select tte1_0.id,tte1_0.data,tte1_0.transaction_id from transaction_test_entity tte1_0 where tte1_0.id=1
//    2025-01-25T06:35:40.729515Z	  530 Query	commit
//    2025-01-25T06:35:40.730416Z	  530 Query	SET autocommit=1
//    2025-01-25T06:35:40.731180Z	  530 Query	set session transaction read write

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @GetMapping("/select03")
    fun select03(): TransactionTestEntity {
        return repository.findById(1L) ?: throw NoSuchElementException()
    }

//    2025-01-25T06:35:57.677828Z	  530 Query	select tte1_0.id,tte1_0.data,tte1_0.transaction_id from transaction_test_entity tte1_0 where tte1_0.id=1

    @Transactional(readOnly = false)
    @GetMapping("/select04")
    fun select04(): TransactionTestEntity {
        return repository.findById(1L) ?: throw NoSuchElementException()
    }

//    2025-01-25T06:36:10.694506Z	  530 Query	SET autocommit=0
//    2025-01-25T06:36:10.702320Z	  530 Query	select tte1_0.id,tte1_0.data,tte1_0.transaction_id from transaction_test_entity tte1_0 where tte1_0.id=1
//    2025-01-25T06:36:10.716516Z	  530 Query	commit
//    2025-01-25T06:36:10.717517Z	  530 Query	SET autocommit=1
}
