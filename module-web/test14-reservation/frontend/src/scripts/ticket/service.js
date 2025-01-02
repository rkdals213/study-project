import {checkTicketAndLock} from "./repository.js";

export async function checkTicketAvailable(selectedItems) {
    let result = true

    for (const item of selectedItems) {
        const response = await checkTicketAndLock(item)

        if (response.ok) {

        } else {
            return false
        }
    }

    return result
}

