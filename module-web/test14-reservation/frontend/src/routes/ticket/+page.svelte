<script>
    import {goto} from "$app/navigation";
    import {checkTicketAvailable} from "../../scripts/ticket/service.js";

    export let data

    let tickets = data.data

    let selectedOptions = []

    async function handleSubmit() {
        if (selectedOptions.length === 0) {
            alert("선택된 옵션이 없습니다.")
            return
        }

        const result = await checkTicketAvailable(selectedOptions)

        if (!result) {
            alert("이미 선택된 티켓입니다")
            return
        }

        sessionStorage.setItem('ticketIds', JSON.stringify(selectedOptions))
        await goto("/ticket/purchase")
    }
</script>

<h1>티켓 페이지</h1>
{#each tickets as ticket}
  <div class="boxes">
    <label>
      {ticket.id} : {ticket.status}
      <input type="checkbox" id="checkbox-{ticket.id}" value="{ticket.id}" bind:group={selectedOptions}>
    </label>
  </div>
{/each}
<button on:click={handleSubmit}>선택 완료</button>
