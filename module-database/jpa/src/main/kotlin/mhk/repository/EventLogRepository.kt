package mhk.repository

import mhk.entity.EventLog
import org.springframework.data.jpa.repository.JpaRepository

interface EventLogRepository: JpaRepository<EventLog, Long> {
    fun findByEventId(eventId: String): EventLog?
}
