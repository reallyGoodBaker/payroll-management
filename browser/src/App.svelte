<script lang="ts">
    import { setContext } from "svelte";
    import AddElement from "./AddElement.svelte"
    import ListTile from "./ListTile.svelte"
    import {all, query, StaffResult} from './api'

    let list: Array<StaffResult> = []

    document.title = '高校工资管理系统'

    function renderList() {
        requestAnimationFrame(async () => {
            const data = await all()
            list = data
        })
    }

    setContext('updateList', () => {
        setTimeout(() => {
            if (!value) {
                return renderList()
            }

            renderSearchList()
        })
    })

    renderList()

    let value: string
    let uidQueryResult: Array<StaffResult> = []
    let nameQueryResult: Array<StaffResult> = []

    const renderSearchList = (() => {
        let timer: any
        let func: Function = async () => {
            if (!value) {
                return
            }

            const {uid, name} = await query(value)
            uidQueryResult = uid
            nameQueryResult = name
            timer = null
        }

        return () => {
            if (timer) {
                clearTimeout(timer)
            }

            timer = setTimeout(func, 500);
        }
    })()
</script>

<style>

    :global(body) {
        margin: 0;
        padding: 0;
        width: 100vw;
        height: 100vh;
        background-color: #f5f5f5;
    }

    :global(.button) {
        margin: 4px;
        border-radius: 4px;
        user-select: none;
        cursor: pointer;
        background-color: #007ACC;
        color: #fff;
        padding: 8px 12px;
    }

    :global(.button.text) {
        background-color: transparent;
        color: #222;
    }

    :global(.button:hover) {
        filter: brightness(120%);
    }

    :global(.button:active) {
        filter: brightness(90%);
    }

    :global(.button.text:hover) {
        filter: brightness(1);
        background-color: rgba(0, 0, 0, 0.2);
    }

    :global(.button.text:active) {
        filter: brightness(1);
        background-color: rgba(0, 0, 0, 0.4);
    }

    :global(input, select) {
        appearance: none;
        outline: none;
        border: none;
        background-color: transparent;
        border-radius: 8px 8px 0 0;
        border-bottom: solid 1px rgba(0, 0, 0, 0.2);
    }

    :global(select) {
        width: 100px;
    }

    :global(input:hover, select:hover) {
        background-color: rgba(0, 0, 0, 0.04);
    }

    :global(input:focus, select:focus) {
        background-color: rgba(0, 0, 0, 0.08);
        border-bottom: solid 1px #007ACC;
    }

    :global(.select-wrapper) {
        position: relative;
    }

    :global(.select-wrapper::after) {
        content: '▼';
        pointer-events: none;
        color: rgba(0, 0, 0, 0.4);
        font-size: xx-small;
        position: absolute;
        right: 8px;
        top: 50%;
        transform: translateY(-50%);
    }

    .search {
        width: 300px;
        margin: 8px calc(4% + 8px);
        background-color: rgba(255, 255, 255, 0.6);
        border-radius: 4px;
    }

    .search-bar {
        width: 100%;
        border-bottom: solid 1px rgba(0, 0, 0, 0.08);
    }

    .title {
        position: sticky;
        font-weight: bold;
        color: #007ACC;
        margin: 8px;
        margin-top: 56px;
    }

    .win {
        width: 100vw;
        height: 100vh;
        overflow: hidden;
    }

    .list {
        width: 92%;
        margin-left: 4%;
        height: calc(100vh - 89px);
        overflow: auto;
        position: relative;
    }

    .nmt {
        margin-top: 8px;
    }

</style>

<div class="win">
    <div class="search-bar">
        <input placeholder="搜索" class="search" type="text"
            autocomplete="off" spellcheck="false"
            bind:value on:input={renderSearchList}>
        <div style="display: flex;">
            <div class="title nmt" style="margin-left: calc(4% + 16px); width: 192px;">名字</div>
            <div class="title nmt" style="width: 96px">主业</div>
            <div class="title nmt" style="width: 96px">副业</div>
            <div class="title nmt" style="width: 90px">课时</div>
            <div class="title nmt" style="">工资</div>
        </div>
    </div>
    <div class="list">
        {#if !value}
            {#each list as {name, primary, salary, secondary, workload, uid}, i}
                <ListTile {uid} {name} {primary} {salary} {secondary} {workload} isOdd={!!(i%2)}/>
            {/each}
            <AddElement/>
        {:else}
            <div class="title">UID</div>
            {#each uidQueryResult as {name, primary, salary, secondary, workload, uid}, i}
                <ListTile {uid} {name} {primary} {salary} {secondary} {workload} isOdd={!!(i%2)}/>
            {:else}
                <p style="margin-left: 8px;">无结果</p>
            {/each}
            <div class="title">名字</div>
            {#each nameQueryResult as {name, primary, salary, secondary, workload, uid}, i}
                <ListTile {uid} {name} {primary} {salary} {secondary} {workload} isOdd={!!(i%2)}/>
            {:else}
                <p style="margin-left: 8px;">无结果</p>
            {/each}
        {/if}
    </div>
</div>