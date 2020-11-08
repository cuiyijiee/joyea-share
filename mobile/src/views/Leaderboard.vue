<template>
    <div class="leaderboard">
        <div class="date-picker">
            <van-dropdown-menu>
                <van-dropdown-item v-model="year" :options="option.year" @change="handleConditionChanged"/>
                <van-dropdown-item v-model="month" :options="option.month" @change="handleConditionChanged"/>
            </van-dropdown-menu>
        </div>
        <div class="type-selector">
            <div>
                <van-tabs v-model="leaderBoardType" @change="handleConditionChanged">
                    <van-tab title="素材采纳"/>
                    <van-tab title="素材引用"/>
                    <van-tab title="清单采纳"/>
                    <van-tab title="清单收藏"/>
                </van-tabs>
            </div>
        </div>
        <div class="obj-selector">
            <div>
                <van-tabs v-model="leaderObjType" @change="handleConditionChanged">
                    <van-tab title="个人排名"/>
                    <van-tab title="部门排行"/>
                </van-tabs>
            </div>
        </div>
        <div style="height: 120px;text-align: center;" v-if="leaderObjType === 0">
            <div style="font-size: 20px;color: #eb7808">我的排名</div>
            <div style="font-size: 50px;color: #cf9236">{{ myIndex }}</div>
            <div style="font-size: 20px;color: #eb7808">我的得分: {{ myScore }}</div>
        </div>
        <div v-if="leaderObjType === 0">
            <div v-for="(item,index) in leaderboardList">
                <van-card
                    :title="item.user.joyeaName"
                    :desc="item.user.department + ' | ' + item.user.position">
                    <template #thumb>
                        <avatars backgroundColor="#eb7808" color="#ffffff" :size="80"
                                 :username="item.user.joyeaName"></avatars>
                    </template>
                    <template #price>
                        积分: {{ item.value }}
                    </template>
                    <template #num>
                        名次: {{ index + 1 }}
                    </template>
                </van-card>
                <div style="height: 1px;background: #8c939d"/>
            </div>
        </div>
        <div v-else>
            <div v-for="(item,index) in leaderboardDepartmentList">
                <van-card
                    :title="item.department">
                    <template #thumb>
                        <avatars backgroundColor="#eb7808" color="#ffffff" :size="80"
                                 :username="item.department.substring(0,2)"></avatars>
                    </template>
                    <template #price>
                        积分: {{ item.value }}
                    </template>
                    <template #num>
                        名次: {{ index + 1 }}
                    </template>
                </van-card>
                <div style="height: 1px;background: #8c939d"/>
            </div>
        </div>
    </div>
</template>

<script>
import moment from 'moment';
import {uploadIntegralLeaderboard, srcQuoteLeaderboard, albumShareLeaderboard, albumQuoteLeaderboard} from '@/api';
import {mapGetters} from "vuex";
import avatars from 'vue-avatars'
import {Toast} from "vant";

export default {
    name: "Leaderboard",
    components: {
        avatars
    },
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

            leaderboardList: [],
            leaderboardDepartmentList: [],

            myScore: 0,
            myIndex: 0
        }
    },
    computed: {
        ...mapGetters([
            'userInfo'
        ])
    },
    methods: {
        handleConditionChanged() {
            console.log(`year: ${this.year},month: ${this.month},board: ${this.leaderBoardType},obj: ${this.leaderObjType}`);
            if (this.leaderBoardType === 0) {
                this.handleGetUploadIntegral();
            } else if (this.leaderBoardType === 1) {
                this.handleGetSrcQuote();
            } else if (this.leaderBoardType === 2) {
                this.handleGetAlbumShare();
            } else if (this.leaderBoardType === 3) {
                this.handleGetAlbumQuote();
            }
        },
        handleGenerateDateOption() {
            this.option.month.push({text: '全年', value: 0})
            for (let index = 1; index < 13; index++) {
                this.option.month.push({text: index + '月', value: index})
            }
            let currentYear = Number(moment(new Date()).format('YYYY'));
            for (let index = 2020; index <= currentYear; index++) {
                this.option.year.push({text: index + '年', value: index});
            }
        },
        handleGetAlbumShare() {
            const toast = Toast.loading({
                duration: 0, // 持续展示 toast
                forbidClick: true,
                message: '拼命加载中...',
            });
            albumShareLeaderboard(this.year, this.month).then(resp => {
                this.handleParseResp(resp);
            }).finally(() => {
                toast.clear();
            });
        },
        handleGetAlbumQuote() {
            const toast = Toast.loading({
                duration: 0, // 持续展示 toast
                forbidClick: true,
                message: '拼命加载中...',
            });
            albumShareLeaderboard(this.year, this.month).then(resp => {
                this.handleParseResp(resp);
            }).finally(() => {
                toast.clear();
            });
        },
        handleGetSrcQuote() {
            const toast = Toast.loading({
                duration: 0, // 持续展示 toast
                forbidClick: true,
                message: '拼命加载中...',
            });
            srcQuoteLeaderboard(this.year, this.month).then(resp => {
                this.handleParseResp(resp);
            }).finally(() => {
                toast.clear();
            });
        },
        handleGetUploadIntegral() {
            const toast = Toast.loading({
                duration: 0, // 持续展示 toast
                forbidClick: true,
                message: '拼命加载中...',
            });
            uploadIntegralLeaderboard(this.year, this.month).then(resp => {
                this.handleParseResp(resp);
            }).finally(() => {
                toast.clear();
            });
        },
        handleParseResp(resp) {
            if (this.leaderObjType === 0) {
                resp.data.sort(function (pre, next) {
                    return next.value - pre.value;
                });
                this.leaderboardList = resp.data;
                this.myIndex = resp.data.map(item => item.user.joyeaId).indexOf(this.userInfo.email) + 1;
                this.myScore = resp.data.filter(item => {
                    return item.user.joyeaId === this.userInfo.email;
                })[0].value;
            } else {
                let departmentList = {};
                resp.data.forEach(item => {
                    departmentList[item.user.department] = departmentList[item.user.department] || 0;
                    departmentList[item.user.department] += item.value;
                })
                let resultList = [];
                Object.keys(departmentList).forEach(key => {
                    resultList.push({department: key, value: departmentList[key]})
                });
                resultList.sort(function (pre, next) {
                    return next.value - pre.value;
                });
                this.leaderboardDepartmentList = resultList;
            }
        }
    },
    created() {
        this.handleGenerateDateOption();
        this.handleGetUploadIntegral();
    }
}
</script>

<style scoped>

</style>