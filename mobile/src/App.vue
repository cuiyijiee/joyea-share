<template>
    <div id="app">
        <nut-navbar @on-click-back="back" @on-click-title="title"
                    @on-click-more="more" :leftShow="false" :right-show="false">仅一素材库
        </nut-navbar>
        <nut-searchbar placeText="输入你要查找素材名"/>
        <div style="margin-top: 10px">
            <nut-cell v-for="file in fileList" :key="file.name" :desc="file.desc"
                      :title="file.name" :sub-title="file.person + ' ' + file.time">
                <div slot="avatar">
                    <img :src="getIcon(file.type)" style="width: 30px;height: 30px">
                </div>
            </nut-cell>
        </div>
        <nut-tabbar @tab-switch="tabSwitch3" :tabbarList="tabList3" :bottom="true"/>
    </div>
</template>

<script>
    import {iconAssets} from "./util/IconUtil"

    export default {
        name: "App",
        data() {
            return {
                tabList3: [
                    {
                        tabTitle: "工作台",
                        curr: true,
                        icon: "http://img13.360buyimg.com/uba/jfs/t1/29316/38/1115/3203/5c0f3d61E35d0c7da/9e557f2cb5c9dab6.jpg",
                        activeIcon: "http://img20.360buyimg.com/uba/jfs/t1/9996/36/8646/4833/5c0f3d61E7c1b7e0f/c98ad61124172e93.jpg",
                        href: "###"
                    },
                    {
                        'tabTitle': '素材车',
                        'curr': false,
                        'num': 2,
                        'icon': 'http://img11.360buyimg.com/uba/jfs/t1/14848/18/1066/3723/5c0f41bdE9f2a38fe/e6ed6768717297fb.jpg',
                        'activeIcon': 'http://img30.360buyimg.com/uba/jfs/t1/17538/16/1070/6214/5c0f41bdE4bc9a1db/74cf978e5015454b.jpg',
                        'href': '###'
                    },
                    {
                        tabTitle: "我的",
                        curr: false,
                        icon: "http://img20.360buyimg.com/uba/jfs/t1/20004/20/1045/3620/5c0f3d61Eaaec1670/9e59db63983b7b9f.jpg",
                        activeIcon: "http://img14.360buyimg.com/uba/jfs/t1/23967/14/1072/6714/5c0f3d61E0ad8991e/8f741953f6e38f15.jpg",
                        href: "###"
                    }
                ],
                fileList: [
                    {
                        type: "dir",
                        name: "测试文件夹1",
                        person: "joyea",
                        time: "2020-03-20 19:20",
                    },
                    {
                        type: "dir",
                        name: "测试文件夹2",
                        person: "joyea",
                        time: "2020-03-20 19:20"
                    },
                    {
                        type: "img",
                        name: "一号园1.jpg",
                        person: "joyea",
                        time: "2020-03-20 19:20",
                        desc: "加清单"
                    },
                    {
                        type: "video",
                        name: "一号园视频.jpg",
                        person: "joyea",
                        time: "2020-03-20 19:20",
                        desc: "加清单"
                    }
                ],
            };
        },
        methods: {
            getIcon(type) {
                switch (type) {
                    case 'dir':
                        return iconAssets.dir;
                    case 'img':
                        return iconAssets.img;
                    case 'video':
                        return iconAssets.video;
                }
            },
            tabSwitch3() {
            },
            back() {
                alert("header头部， 点击返回");
            },
            title() {
                alert("header头部， 点击title");
            },
            more() {
                alert("header头部， 点击更多");
            }
        }
    };
</script>

<style>
    #app {
        font-family: Avenir, Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
    }
</style>
