<script lang="ts">
    import { getContext } from "svelte";
    import { put, remove as rm } from "./api";


    export let uid: string
    export let name: string
    export let primary: number
    export let secondary: number
    export let salary: number
    export let workload: number
    export let isOdd: boolean

    const roles = [
        '教师', '实验员', '行政人员',
    ]

    const rolesUi = roles.concat(['无'])

    let primaryStr: string = roles[primary]
    let secondaryStr: string = secondary === null
        ? '无'
        : roles[secondary]

    async function edit() {
        if (
            await put({
                uid, name,
                primary: roles.indexOf(primaryStr),
                secondary: roles.indexOf(secondaryStr),
                workload,
            })
        ) {
            (updateList as Function).call(undefined)
            shouldUpdate = false
        }
    }

    let shouldUpdate = false
    function canUpdate() {
        shouldUpdate = true
    }

    const updateList = getContext('updateList')

    async function remove() {
        if (await rm(uid)) {
            (updateList as Function).call(undefined)
        }
    }
</script>

<style>
    .container {
        display: flex;
        padding: 8px 12px;
        width: calc(100% - 40px);
        height: 48px;
        border-radius: 8px;
        align-items: center;
        justify-content: space-between;
    }

    .container.border {
        padding: 7px 11px;
        border: solid 1px rgba(0, 0, 0, 0.2);
        background-color: rgba(255, 255, 255, 0.5);
    }

    .container:hover {
        background-color: #eee;
    }

    select, input {
        margin: 0;
    }

    .info {
        display: flex;
        align-items: center;
        width: fit-content;
    }

    .info > * {
        margin-right: 12px;
    }

    .remove {
        width: 36px;
        height: 36px;
        justify-content: center;
        align-items: center;
        cursor: pointer;
        border-radius: 50%;
        display: none;
        user-select: none;
    }

    .remove:hover {
        background-color: rgba(0, 0, 0, 0.2);
    }

    .remove:active {
        background-color: rgba(0, 0, 0, 0.4);
    }

    .container:hover > .btnGroup > .remove {
        display: flex;
    }

    .btnGroup {
        display: flex;
        align-items: center;
    }

    .btnGroup > * {
        margin-left: 8px;
    }

    .uid {
        visibility: hidden;
        color: #007ACC;
    }

    .container:hover > .uid {
        visibility: visible;
    }
</style>

<div class="container {isOdd? 'border': ''}">
    <div class="info">
        <input type="text" autocomplete="off" spellcheck="false" bind:value={name} on:change={canUpdate}>

        <div class="select-wrapper">
            <select bind:value={primaryStr} on:change={canUpdate}>
                {#each roles as role}
                    <option value={role}>{role}</option>
                {/each}
            </select>
        </div>

        <div class="select-wrapper">
            <select bind:value={secondaryStr} on:change={canUpdate}>
                {#each rolesUi as role}
                    {#if role !== primaryStr}
                        <option value={role}>{role}</option>
                    {/if}
                {/each}
            </select>
        </div>

        <input style="width: 100px;" type="number" autocomplete="off" spellcheck="false" bind:value={workload} on:change={canUpdate}>
        <div style="width: 100px;">{salary}</div>
    </div>

    <div class="uid">{uid}</div>

    <div class="btnGroup">
        <!-- svelte-ignore a11y-click-events-have-key-events -->
        <div class="remove" title="删除条目" on:click={remove}>⨉</div>
        {#if shouldUpdate}
        <!-- svelte-ignore a11y-click-events-have-key-events -->
        <div class="button" on:click={edit}>更新</div>
        {/if}
    </div>
</div>