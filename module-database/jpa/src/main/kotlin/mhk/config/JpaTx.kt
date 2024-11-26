package mhk.config

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class JpaTx(
    _txAdvice: TxAdvice
) {

    init {
        txAdvice = _txAdvice
    }

    companion object {
        private lateinit var txAdvice: TxAdvice

        fun <T> readable(function: () -> T): T {
            return txAdvice.readable(function)
        }

        fun <T> writable(function: () -> T): T {
            return txAdvice.writable(function)
        }
    }

    @Component
    class TxAdvice {

        @Transactional(readOnly = true, transactionManager = "jpaTransactionManager")
        fun <T> readable(function: () -> T): T {
            return function.invoke()
        }

        @Transactional(readOnly = false, transactionManager = "jpaTransactionManager")
        fun <T> writable(function: () -> T): T {
            return function.invoke()
        }
    }
}
