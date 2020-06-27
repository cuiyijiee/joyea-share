import Vue from 'vue';
import Router from 'vue-router';

import HomeContainer from "../components/tabbar/HomeContainer";
import OrderContainer from "../components/tabbar/OrderContainer";
import MineContainer from "../components/tabbar/MineContainer";
import SearchResult from "../components/tabbar/SearchContainer";
import UploadIndex from "../views/upload/Index"

import Main from "../views/Main";
import Login from "../views/Login"
import AlbumList from "../views/AlbumList";

import AlbumDetail from "../views/AlbumDetail";

Vue.use(Router);

export default new Router({
    base: "mobile",
    routes: [
        {
            path: '/login',
            name: '仅一素材库系统',
            component: Login,
        },
        {
            path: '/',
            redirect: '/home',
            name: "/",
            component: Main,
            children: [
                {
                    path: 'home',
                    name: '仅一素材库系统',
                    component: HomeContainer,
                    meta: {
                        allowBack: false,
                        allowHome: false
                    }
                },
                {
                    path: 'order',
                    name: '素材车',
                    component: OrderContainer,
                    meta: {
                        allowHome: false
                    }
                },
                {
                    path: 'mine',
                    name: '我的',
                    component: MineContainer,
                    meta: {
                        allowHome: false
                    }
                },
                {
                    path: '/search',
                    name: '搜索结果',
                    component: SearchResult,
                    meta: {
                        allowBack: true,
                        allowHome: false
                    }
                }
            ]
        },
        {
            path: '/album',
            name: '我的清单',
            component: AlbumList,
            meta: {
                allowBack: true,
                allowHome: true
            }
        },
        {
            path: '/albumDetail',
            name: '清单详情',
            component: AlbumDetail,
            meta: {
                allowBack: true,
                allowHome: true
            }
        },
        {
            path: '/upload/index',
            name: '素材上传',
            component: UploadIndex,
            meta: {
                allowBack: true,
                allowHome: true
            }
        }
    ],
    linkActiveClass: 'mui-active'   //覆盖默认的路由的高亮的类，默认的类叫 router-link-active
});