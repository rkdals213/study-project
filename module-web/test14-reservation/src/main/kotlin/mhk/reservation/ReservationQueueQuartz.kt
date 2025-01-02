package mhk.reservation

import org.quartz.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Configuration
class ReservationQueueQuartz {

    @Bean
    fun jobDetail(): JobDetail {
        return JobBuilder.newJob(ReservationQueueQuartzJob::class.java)
            .withIdentity("reservation queue order notification job")
            .storeDurably()
            .build()
    }

    @Bean
    fun trigger(): Trigger {
        return TriggerBuilder.newTrigger()
            .withIdentity("reservation queue order notification trigger")
            .forJob(jobDetail())
            .withSchedule(
                SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInSeconds(3)
                    .repeatForever()
            )
            .build()
    }
}

@Component
class ReservationQueueQuartzJob(
    private val reservationService: ReservationService
) : Job {
    override fun execute(context: JobExecutionContext?) {
        reservationService.sendNotification()
    }
}
