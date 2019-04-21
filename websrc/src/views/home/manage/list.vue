<template>
    <section>
        <!--工具条-->
        <el-input placeholder="输入清单名称查找" v-model="search.key" class="input-with-select px10_divider">
            <el-button slot="append" icon="el-icon-search" v-on:click="handleSearch"></el-button>
        </el-input>

        <div class="px10_divider">
            <el-card v-for="(album,index) in albumList" style="margin-bottom: 10px">
                <div slot="header" class="clearfix" >
                    <span style="color: #fff;font-weight:bold;">{{album.name}}</span>
                </div>
                <el-row>
                    <el-col :md="8" :xs="24" v-for="(img,fileIndex) in album.list" :key="img.desc">
                        <el-card style="padding: 2px;margin: 5px">
                            <img :src="img.url"
                                 style="width: 100%; height: 150px"
                                 :preview="album.name" :preview-text="img.desc">
                            <div style="height: 20px;margin-top: 10px">
                                <el-tooltip class="item" effect="dark" :content="img.desc" placement="bottom">
                                    <span class="svg_name">{{img.desc}}</span>
                                </el-tooltip>
                            </div>
                        </el-card>
                    </el-col>
                </el-row>
            </el-card>
        </div>
    </section>
</template>

<script>
    export default {
        name: "list",
        data(){
            return{
                search:{
                    key:''
                },
                albumList:[
                    {
                        name:"测试清单--1",
                        list:[
                            {
                                url:'./img/1.jpeg',
                                desc:'简介一，奥斯卡打卡说明的马拉松；的；；拉速度；拉速度；拉速度快；拉斯卡掉了；啊'
                            },
                            {
                                url:'./img/2.jpeg',
                                desc:'简发生科技的考拉上的卡死了达斯柯达加拉斯科技的离开；啊'
                            },
                            {
                                url:'./img/3.jpeg',
                                desc:'发生的快乐撒打开拉速度来看拉斯卡掉了；啊'
                            },
                            {
                                url:'./img/4.jpeg',
                                desc:'阿达斯柯达是肯定就卡了手机打开拉速度加快拉倒就爱可乐三季度阿达撒凉快圣诞节奥斯卡来得及案例可视对讲拉速度快；拉斯卡掉了；啊'
                            },
                            {
                                url:'./img/5.jpeg',
                                desc:'打开手机打开拉数据大可乐三季度卡拉斯酒店拉开圣诞节啊看来是的骄傲了肯德基拉克丝掉了；啊'
                            },
                            {
                                url:'./img/6.jpeg',
                                desc:'啊实打实大水库的拉伸扩大绿色的卡洛斯的卡洛斯的卡了；打卡；落地卡了；打卡啦；打卡啦；都卡死了；的卡洛斯；打卡收到了；卡死了打卡'
                            },
                            {
                                url:'./img/7.jpeg',
                                desc:'快乐大受打击阿卡丽圣诞节阿斯利康'
                            },
                            {
                                url:'./img/8.jpeg',
                                desc:'就爱上的黄金卡收到货卡的哈时间肯定会'
                            },
                            {
                                url:'./img/9.jpeg',
                                desc:'啊实打实的骄傲是打死都按时地哦啊欧式'
                            }
                        ]
                    },
                    {
                        name:"测试清单--2",
                        list:[
                            {
                                url:'./img/6.jpeg',
                                desc:'啊实打实大水库的拉伸扩大绿色的卡洛斯的卡洛斯的卡了；打卡；落地卡了；打卡啦；打卡啦；都卡死了；的卡洛斯；打卡收到了；卡死了打卡'
                            },
                            {
                                url:'./img/7.jpeg',
                                desc:'快乐大受打击阿卡丽圣诞节阿斯利康'
                            },
                            {
                                url:'./img/8.jpeg',
                                desc:'就爱上的黄金卡收到货卡的哈时间肯定会'
                            },
                            {
                                url:'./img/9.jpeg',
                                desc:'啊实打实的骄傲是打死都按时地哦啊欧式'
                            },
                            {
                                url:'./img/10.jpeg',
                                desc:'我偶滴欧萨U盾欧艾斯对哦啊是对傲丝度傲丝度奥斯'
                            },
                            {
                                url:'./img/1.jpeg',
                                desc:'简介一，奥斯卡打卡说明的马拉松；的；；拉速度；拉速度；拉速度快；拉斯卡掉了；啊'
                            },
                            {
                                url:'./img/3.jpeg',
                                desc:'发生的快乐撒打开拉速度来看拉斯卡掉了；啊'
                            },
                            {
                                url:'./img/4.jpeg',
                                desc:'阿达斯柯达是肯定就卡了手机打开拉速度加快拉倒就爱可乐三季度阿达撒凉快圣诞节奥斯卡来得及案例可视对讲拉速度快；拉斯卡掉了；啊'
                            },
                            {
                                url:'./img/5.jpeg',
                                desc:'打开手机打开拉数据大可乐三季度卡拉斯酒店拉开圣诞节啊看来是的骄傲了肯德基拉克丝掉了；啊'
                            },
                        ]
                    }
                ]
            }
        },
        methods:{
            handleSearch(){

            }
        }
    }
</script>

<style scoped  lang="scss">

    .px10_divider {
        margin-top: 10px;
    }

    #list {
        overflow-x: auto;
        list-style: none;
        white-space: nowrap;
        width: auto;
    }

    ::-webkit-scrollbar {
        width: 0 !important
    }

    .svg_name {
        white-space:normal;
        color: #606266;
        font-weight: 500;
        font-size: 14px;
    }

    .item {
        display: inline-block;
        padding-right: 20px;
    }

    ul {
        margin-block-start: 0;
        margin-block-end: 0;
        padding-inline-start: 20px;
    }

    .no_display {
        display: none;
    }

</style>