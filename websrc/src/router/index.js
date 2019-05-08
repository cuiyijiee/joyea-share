import Vue from 'vue'
import Router from 'vue-router'
import Home from '../views/home/index'
import Login from '../views/login/index'

//import Manage from '../views/home/manage'
import Build from '../views/home/build'
import Collection from '../views/home/manage/collection'
import List from '../views/home/manage/list'
import Share from '../views/home/share'

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
            name: '我的收藏',
            component: Home,
            icon: 'el-icon-star-off',
            noChild: true,
            children: [
                {path: '/manage/collection', component: Collection, name: 'collection', desc: '收藏'},
            ]
        }
    ]
})
