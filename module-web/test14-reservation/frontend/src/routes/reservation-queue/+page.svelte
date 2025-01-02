<script>
    import {onDestroy} from 'svelte';
    import {connectToOrderSubscriber} from "../../scripts/reservation/repository.js";
    import {goto} from "$app/navigation";

    $: order = 100000

    const token = JSON.parse(sessionStorage.getItem('token'))

    let orderSubscriber = connectToOrderSubscriber(token)

    orderSubscriber.onmessage = function (event) {
        order = event.data

        if (order < 2) {
            goto('/ticket')
        }
    }

    orderSubscriber.onerror = function (event) {
        console.error('SSE 오류 발생:', event);
    }

    onDestroy(() => {
        orderSubscriber.close();
        console.log('SSE 연결 종료');
    })
</script>

<h1>대기열</h1>
<div>대기 순번 : {order}</div>
