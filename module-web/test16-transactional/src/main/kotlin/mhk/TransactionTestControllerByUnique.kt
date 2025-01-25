package mhk

import mhk.entity.TransactionTestEntity
import mhk.repository.TransactionTestEntityRepository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/unique")
class TransactionTestControllerByUnique(
    private val repository: TransactionTestEntityRepository
) {

    @GetMapping("/select01")
    fun select01(): TransactionTestEntity {
        return repository.findByTransactionId(1L) ?: throw NoSuchElementException()
    }

//    2025-01-25T06:29:55.420058Z	  500 Query	select tte1_0.id,tte1_0.data,tte1_0.transaction_id from transaction_test_entity tte1_0 where tte1_0.transaction_id=1

    @Transactional(readOnly = true)
    @GetMapping("/select02")
    fun select02(): TransactionTestEntity {
        return repository.findByTransactionId(1L) ?: throw NoSuchElementException()
    }

//    2025-01-25T06:30:10.429386Z	  500 Query	set session transaction read only
//    2025-01-25T06:30:10.431361Z	  500 Query	SET autocommit=0
//    2025-01-25T06:30:10.440166Z	  500 Query	select tte1_0.id,tte1_0.data,tte1_0.transaction_id from transaction_test_entity tte1_0 where tte1_0.transaction_id=1
//    2025-01-25T06:30:10.443068Z	  500 Query	commit
//    2025-01-25T06:30:10.445392Z	  500 Query	SET autocommit=1
//    2025-01-25T06:30:10.447711Z	  500 Query	set session transaction read write

    @Transactional(readOnly = false, propagation = Propagation.SUPPORTS)
    @GetMapping("/select03")
    fun select03(): TransactionTestEntity {
        return repository.findByTransactionId(1L) ?: throw NoSuchElementException()
    }

//    2025-01-25T06:31:49.796159Z	  510 Query	select tte1_0.id,tte1_0.data,tte1_0.transaction_id from transaction_test_entity tte1_0 where tte1_0.transaction_id=1

    @Transactional(readOnly = false)
    @GetMapping("/select04")
    fun select04(): TransactionTestEntity {
        return repository.findByTransactionId(1L) ?: throw NoSuchElementException()
    }

//    2025-01-25T06:30:28.672517Z	  501 Query	SET autocommit=0
//    2025-01-25T06:30:28.679924Z	  501 Query	select tte1_0.id,tte1_0.data,tte1_0.transaction_id from transaction_test_entity tte1_0 where tte1_0.transaction_id=1
//    2025-01-25T06:30:28.683799Z	  501 Query	commit
//    2025-01-25T06:30:28.684811Z	  501 Query	SET autocommit=1
}
