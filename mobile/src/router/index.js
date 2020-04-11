import Vue from 'vue';
import Router from 'vue-router';

import HomeContainer from "../components/tabbar/HomeContainer";
import OrderContainer from "../components/tabbar/OrderContainer";
import MineContainer from "../components/tabbar/MineContainer";

import Main from "../views/Main";
import Login from "../views/Login"

Vue.use(Router);


export default new Router({
    routes: [
        {
            path: '/login',
            name: 'login',
            component: Login
        },
        {
            path: '/',
            redirect: '/home',
            name:"/",
            component: Main,
            children: [
                {
                    path: 'home',
                    name: 'HomeContainer',
                    component: HomeContainer
                },
                {
                    path: 'order',
                    name: 'OrderContainer',
                    component: OrderContainer
                },
                {
                    path: 'mine',
                    name: 'MineContainer',
                    component: MineContainer
                }
            ]
        }
    ],
    linkActiveClass: 'mui-active'   //覆盖默认的路由的高亮的类，默认的类叫 router-link-active
});