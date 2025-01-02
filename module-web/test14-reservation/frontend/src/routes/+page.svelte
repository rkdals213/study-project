<script>
    import {goto} from '$app/navigation';
    import {getEntranceToken} from "../scripts/reservation/repository.js"; // goto 함수 임포트

    let accountId = ''

    async function handleSubmit() {
        const response = await getEntranceToken(accountId)

        if (response.ok) {
            const result = await response.json();

            console.log("성공 : ", result)
            sessionStorage.setItem('token', JSON.stringify(result.data.token));

            await goto('/reservation-queue')
        } else {
            console.log("실패 : ", await response.json())
        }
    }
</script>

<h1>대기열 접속 페이지</h1>

<input type="text" bind:value={accountId} placeholder="Enter your ID"/>
<button on:click={handleSubmit()}>입장</button>
