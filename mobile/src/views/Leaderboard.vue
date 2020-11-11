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
        <div
            style="height: 120px;text-align: center;color: #ffffff;"
            :style="{background: 'url('+bg_leaderboard+')','background-size':'cover','background-repeat': 'no-repeat'}"
            v-if="leaderObjType === 0">
            <div style="font-size: 20px; padding-top: 20px">我的排名
                <van-icon style="margin-top: 2px" name="question" @click="tipVisible = true"/>
            </div>
            <div v-if="myScore > 0">
                <span style="font-size: 40px;">第{{ myIndex }}名</span>
                <span> ( 积分: {{ myScore }}分 )</span>
            </div>
            <div style="font-size: 50px;" v-else> -</div>
        </div>
        <div v-if="leaderObjType === 0">
            <van-row style="text-align: center;color: #8c939d;padding: 8px 0">
                <van-col span="6">排名</van-col>
                <van-col span="12">用户名</van-col>
                <van-col span="6">积分</van-col>
            </van-row>
            <div v-for="(item,index) in leaderboardList.slice(0,10)" style="margin: 10px 0">
                <van-row>
                    <van-col span="6" style="text-align: center">
                        <div style="margin-top: 10px" v-if="index < 3">
                            <van-image v-if="index === 0" width="30" height="30" :src="medal_gold"/>
                            <van-image v-if="index === 1" width="30" height="30" :src="medal_silver"/>
                            <van-image v-if="index === 2" width="30" height="30" :src="medal_bronze"/>
                        </div>
                        <div v-else style="margin-top: 10px">
                            {{ index + 1 }}
                        </div>
                    </van-col>
                    <van-col span="12" style="height: 50px">
                        <div v-if="item.value > 0">
                            <div>{{ item.user.joyeaName }}</div>
                            <div style="color: #8c939d">{{ item.user.department + ' | ' + item.user.position }}</div>
                        </div>
                        <div v-else style="margin-top: 10px;color: #99a9bf">
                            -- 虚位以待 --
                        </div>
                    </van-col>
                    <van-col span="6" style="text-align: center">
                        <div style="margin-top: 15px">{{ item.value }}</div>
                    </van-col>
                </van-row>
                <div style="height: 1px;background: #eeeeee"/>
            </div>
        </div>
        <div v-else>
            <van-row style="text-align: center;color: #8c939d;padding: 8px 0">
                <van-col span="6">排名</van-col>
                <van-col span="12">部门</van-col>
                <van-col span="6">积分</van-col>
            </van-row>
            <div v-for="(item,index) in leaderboardDepartmentList.slice(0,10)" style="margin: 10px 0">
                <van-row>
                    <van-col span="6" style="text-align: center">
                        <div style="margin-top: 10px" v-if="index < 3">
                            <van-image v-if="index === 0" width="30" height="30" :src="medal_gold"/>
                            <van-image v-if="index === 1" width="30" height="30" :src="medal_silver"/>
                            <van-image v-if="index === 2" width="30" height="30" :src="medal_bronze"/>
                        </div>
                        <div v-else style="margin-top: 10px">
                            {{ index + 1 }}
                        </div>
                    </van-col>
                    <van-col span="12" style="height: 50px">
                        <div v-if="item.value > 0" style="margin-top: 10px;">
                            <div>{{ item.department }}</div>
                        </div>
                        <div v-else style="margin-top: 10px;color: #99a9bf">
                            -- 虚位以待 --
                        </div>
                    </van-col>
                    <van-col span="6" style="text-align: center">
                        <div style="margin-top: 15px">{{ item.value }}</div>
                    </van-col>
                </van-row>
                <div style="height: 1px;background: #eeeeee"/>
            </div>
        </div>
        <van-dialog v-model="tipVisible" :title="tipTitle" show-cancel-button theme="round" :showCancelButton="false">
            <div class="content" style="padding: 0px 20px">
                <div v-if="leaderBoardType === 0">
                    <p>统计每个人被采纳的素材数量，但是会对被采纳的素材进行分类、采用积分的方式进行统计，照片、视频等未加工的通用素材积1分，友商对比表、细分市场介绍资料等经过整理分析形成的专用素材积5分。</p>
                    <p>如：友商报价书、合同等，积1分，如果进一步将此信息整理成友商与我司的对比表，则积5分。</p>
                </div>
                <div v-else-if="leaderBoardType === 1">
                    <p>统计每个人被采纳的素材被其他人引用或者下载的次数，这是对素材质量的排名。</p>
                </div>
                <div v-else-if="leaderBoardType === 2">
                    <p>统计每个人分享到清单广场的清单数量。</p>
                </div>
                <div v-else-if="leaderBoardType === 3">
                    <p>统计每个人分享到清单广场的清单被其他人收藏的次数，这是对清单质量的排名。</p>
                </div>
            </div>
        </van-dialog>
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
            medal_gold: require('../assets/medal-gold.png'),
            medal_silver: require('../assets/medal-silver.png'),
            medal_bronze: require('../assets/medal-bronze.png'),
            bg_leaderboard: require('../assets/bg-leaderboard.jpeg'),
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
            myIndex: 0,

            tipVisible: false
        }
    },
    computed: {
        tipTitle:function () {
            if (this.leaderBoardType === 0) {
                return "素材采纳排名规则";
            } else if (this.leaderBoardType === 1) {
                return "素材引用排名规则";
            } else if (this.leaderBoardType === 2) {
                return "清单采纳排名规则";
            } else {
                return "清单收藏排名规则";
            }
        },
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
                this.option.month.push({text: index + '月', value: index});
            }
            let currentYear = Number(moment(new Date()).format('YYYY'));
            for (let index = 2020; index <= currentYear; index++) {
                this.option.year.push({text: index + '年', value: index});
            }
            this.year = currentYear;
            this.month = Number(moment(new Date()).format('MM'));
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