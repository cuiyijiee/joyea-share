import Vue from 'vue'
import Router from 'vue-router'
import Home from '../views/home/index'
import Login from '../views/login/index'

import Build from '../views/home/build'
import Redirect from '../views/home/redirect'
import List from '../views/home/manage/list'
import Share from '../views/home/share'
import UploadManage from "../views/home/UploadManage";
import UploadIndex from "../views/home/upload/index";
import Transcode from  "../views/home/tanscode";

Vue.use(Router);

export default new Router({
    base: process.env.BASE_URL,
    routes: [
        {
            path: '/login',
            name: 'login',
            desc: '登陆',
            component: Login,
            hidden: true
        },
        {
            path: '/redirect',
            name: 'redirect',
            desc: '跳转',
            component: Redirect,
            hidden: true
        },
        {
            path: '/share',
            name: 'share',
            desc: '分享',
            component: Share,
            hidden: true
        },
        {
            path: '/',
            name: '工作台',
            desc: '工作台',
            component: Home,
            icon: 'el-icon-edit',
            noChild: true,
            redirect: '/build',
            children: [
                {path: '/build', component: Build, name: 'build', desc: '制作清单',}
            ]
        },
        {
            path: '/',
            name: '我的清单',
            component: Home,
            icon: 'el-icon-tickets',
            noChild: true,
            children: [
                {path: '/manage/list', component: List, name: 'list', desc: '清单'},
            ]
        },
        {
            path: '/',
            name: '素材审核',
            component: Home,
            icon: 'el-icon-paperclip',
            noChild: true,
            children: [
                {path: '/upload/manage', component: UploadManage, name: 'uploadManage', desc: '素材审核'},
            ]
        },
        {
            path: '/',
            name: '素材上传',
            component: Home,
            icon: 'el-icon-paperclip',
            noChild: true,
            children: [
                {path: '/upload/index', component: UploadIndex, name: 'uploadIndex', desc: '素材上传'},
            ]
        },
        {
            path: '/',
            name: '转码视频管理',
            component: Home,
            icon: 'el-icon-paperclip',
            noChild: true,
            children: [
                {path: '/transcode/index', component: Transcode, name: 'transcode', desc: '转码视频管理'},
            ]
        }
        // {
        //     path: '/',
        //     name: '我的收藏',
        //     component: Home,
        //     icon: 'el-icon-star-off',
        //     noChild: true,
        //     children: [
        //         {path: '/manage/collection', component: Collection, name: 'collection', desc: '收藏'},
        //     ]
        // }
    ]
})
