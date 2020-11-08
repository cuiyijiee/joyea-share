<template>
    <div class="leaderboard">
        <div class="date-picker">
            <van-dropdown-menu>
                <van-dropdown-item v-model="year" :options="option.year"/>
                <van-dropdown-item v-model="month" :options="option.month"/>
            </van-dropdown-menu>
        </div>
        <div class="type-selector">
            <div>
                <van-tabs v-model="leaderBoardType">
                    <van-tab title="素材采纳"/>
                    <van-tab title="素材引用"/>
                    <van-tab title="清单采纳"/>
                    <van-tab title="清单收藏"/>
                </van-tabs>
            </div>
        </div>
        <div class="obj-selector">
            <div>
                <van-tabs v-model="leaderObjType">
                    <van-tab title="个人排名"/>
                    <van-tab title="部门排行"/>
                </van-tabs>
            </div>
        </div>
    </div>
</template>

<script>
import moment from 'moment';

export default {
    name: "Leaderboard",
    data() {
        return {
            option: {
                year: [],
                month: []
            },
            year: 2020,
            month: 0,
            leaderBoardType: 0,
            leaderObjType: 0,
        }
    },
    methods: {
        handleGenerateDateOption() {
            this.option.month.push({text: '全年', value: 0})
            for (let index = 1; index < 13; index++) {
                this.option.month.push({text: index + '月', value: index})
            }
            let currentYear = Number(moment(new Date()).format('YYYY'));
            for (let index = 2020; index <= currentYear; index++) {
                this.option.year.push({text: index + '年', value: index});
            }
        }
    },
    created() {
        this.handleGenerateDateOption();
    }
}
</script>

<style scoped>

</style>