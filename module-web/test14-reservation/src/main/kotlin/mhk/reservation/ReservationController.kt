package mhk.reservation

import mhk.common.ApiResponse
import mhk.common.apiResponse
import mhk.common.responseEntity
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
@RequestMapping("/reservation")
@CrossOrigin(origins = ["*"])
class ReservationController(
    private val reservationService: ReservationService
) {

    @PostMapping
    fun getReservationEntranceToken(@RequestBody getEntranceTokenRequest: GetEntranceTokenRequest): ResponseEntity<ApiResponse<GetEntranceTokenResponse>> {
        val entranceToken = reservationService.generateEntranceToken(getEntranceTokenRequest)
        return responseEntity {
            body = apiResponse {
                data = entranceToken
            }
        }
    }

    @GetMapping("/subscribe/entrance")
    fun subscribeEntrance(subscribeEntranceRequest: SubscribeEntranceRequest): ResponseEntity<SseEmitter> {
        val response = reservationService.subscribeReservationOrder(subscribeEntranceRequest.token)

        return responseEntity {
            body = response
            contentType = MediaType.TEXT_EVENT_STREAM
        }
    }
}


