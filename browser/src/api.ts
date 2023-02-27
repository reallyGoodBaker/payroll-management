const rootUrl = 'http://localhost:8888/api/'

export type Staff = {
    name: string
    primary: number
    secondary: number
    workload: number
    uid: string
}

export type StaffResult = Staff & { salary: number }

export async function all() {
    return await (await fetch(rootUrl + 'all')).json()
}

export async function put(staff: Staff) {
    if (staff.secondary === -1) {
        staff.secondary = 'null' as any
    }

    let str: string[] = []
    for (const k of Object.keys(staff)) {
        str.push(`${k}=${staff[k]}`)
    }
    
    const encodedBody = encodeURI(str.join('&'))
    const fetchRes = await fetch(rootUrl + 'put', {
        method: 'post',
        body: encodedBody
    })

    return fetchRes.ok
}

export async function remove(uid: string) {
    return (await fetch(rootUrl + 'delete', {
        method: 'post',
        body: encodeURI(`uid=${uid}`)
    })).ok
}

interface QueryResult {
    uid: StaffResult[]
    name: StaffResult[]
}

export async function query(s: string): Promise<QueryResult> {
    return await (await fetch(rootUrl + 'query', {
        method: 'post',
        body: encodeURI(`s=${s}`)
    })).json()
}