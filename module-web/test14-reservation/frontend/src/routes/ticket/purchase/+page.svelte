<script>
    import {goto} from "$app/navigation";
    import {purchaseTicket} from "../../../scripts/ticket/repository.js";

    const token = JSON.parse(sessionStorage.getItem('token'))
    const ticketIds = JSON.parse(sessionStorage.getItem('ticketIds'))

    async function handleSubmit() {
        for (let ticketId of ticketIds) {
            const response = await purchaseTicket(ticketId, token)

            if (response.ok) {
                await goto('/')
            } else {
                console.log("실패 : ", await response.json())
            }
        }
    }
</script>

<h1>예약 화면</h1>
<div>
  선택한 티켓 : {ticketIds}
</div>
<button on:click={handleSubmit}>예약</button>
