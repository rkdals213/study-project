package mhk.ticket

import mhk.common.ApiResponse
import mhk.common.EmptyBody
import mhk.common.apiResponse
import mhk.common.responseEntity
import mhk.reservation.PurchaseRequest
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/tickets")
@CrossOrigin(origins = ["*"])
class TicketController(
    private val ticketService: TicketService
) {

    @GetMapping
    fun getTickets(): ResponseEntity<ApiResponse<List<Ticket>>> {
        val response = ticketService.getTickets()

        return responseEntity {
            body = apiResponse {
                data = response
            }
        }
    }

    @PostMapping("/checkTicketAndLock/{id}")
    fun checkTicketAndLock(@PathVariable id: Long): ResponseEntity<ApiResponse<EmptyBody>> {
        ticketService.checkTicketAndLock(id)

        return responseEntity {
            body = apiResponse {
                data = EmptyBody
            }
        }
    }

    @PostMapping("/purchase/{id}")
    fun purchaseTicket(@PathVariable id: Long, @RequestBody purchaseRequest: PurchaseRequest): ResponseEntity<ApiResponse<EmptyBody>> {
        ticketService.purchaseTicket(id, purchaseRequest.token)

        return responseEntity {
            body = apiResponse {
                message = "구매 완료"
                data = EmptyBody
            }
        }
    }
}
