<script lang="ts">
    import { getContext } from "svelte";
    import { put } from "./api";

    let name: string
    let workload: number = 120

    let show: boolean = false
    let uid: string

    const roles = [
        '教师', '实验员', '行政人员',
    ]

    const rolesUi = roles.concat(['无'])

    let primaryStr: string = roles[0]
    let secondaryStr: string = roles[1]

    function open() {
        name = ''
        show = true
    }

    function close() {
        show = false
    }

    const updateList = getContext('updateList')

    let nameErr = false
    async function add() {
        if (!name) {
            nameErr = true
            return
        }

        if (
            await put({
                uid: uid || crypto.randomUUID(),
                name, workload,
                primary: roles.indexOf(primaryStr),
                secondary: roles.indexOf(secondaryStr),
            })
        ) {
            (updateList as Function).call(undefined)
            close()
            nameErr = false
        }
    }
</script>

<style>
    .popup {
        background-color: #fafafa;
        padding: 20px;
        border-radius: 8px;
        border: solid 1px rgba(0, 0, 0, 0.3);
        width: 400px;
        display: none;
        position: fixed;
        z-index: 9999;
        top: 50vh;
        left: 50vw;
        transform: translate(-50%, -50%);
        box-shadow: 0 2px 40px rgba(0, 0, 0, 0.3);
    }

    .visible {
        display: block;
    }

    .line {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .error {
        border-color: red;
    }

    .button {
        margin: 12px 4px;
        width: fit-content;
    }

</style>

<!-- svelte-ignore a11y-click-events-have-key-events -->
<div class="button text" on:click={open}> + 添加 </div>

<div class="popup {show? 'visible': ''}">
    <div class="line">
        <div>名字</div>
        <input placeholder="输入姓名" class="{nameErr? 'error': ''}" type="text" autocomplete="off" spellcheck="false" bind:value={name}>
    </div>

    <div class="line">
        <div>主业</div>
        <div class="select-wrapper">
            <select bind:value={primaryStr}>
                {#each roles as role}
                    <option value={role}>{role}</option>
                {/each}
            </select>
        </div>
    </div>

    <div class="line">
        <div>副业</div>
        <div class="select-wrapper">
            <select bind:value={secondaryStr}>
                {#each rolesUi as role}
                    {#if role !== primaryStr}
                        <option value={role}>{role}</option>
                    {/if}
                {/each}
            </select>
        </div>
    </div>

    <div class="line">
        <div>课时</div>
        <input type="number" autocomplete="off" spellcheck="false" bind:value={workload}>
    </div>

    <div class="line">
        <div>UID</div>
        <input placeholder="缺省即使用随机字符串" type="text" autocomplete="off" spellcheck="false" bind:value={uid}>
    </div>

    <div class="line">
        <div></div>
        <div style="display: flex;">
            <!-- svelte-ignore a11y-click-events-have-key-events -->
            <div class="button text" on:click={close}>取消</div>
            <!-- svelte-ignore a11y-click-events-have-key-events -->
            <div class="button" on:click={add}>添加</div>
        </div>
    </div>
</div>