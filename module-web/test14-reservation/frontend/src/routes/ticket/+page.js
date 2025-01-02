import {getTickets} from "../../scripts/ticket/repository.js";

export async function load() {
    const res = await getTickets()
    return res.json()
}

